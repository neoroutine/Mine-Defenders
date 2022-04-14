package neoroutine.minetd.common.blocks.king;

import neoroutine.minetd.common.capabilities.CapabilityBurningItem;
import neoroutine.minetd.common.capabilities.CapabilityEnergyProperties;
import neoroutine.minetd.common.capabilities.CapabilityGrandmaster;
import neoroutine.minetd.common.capabilities.CapabilityKingHealth;
import neoroutine.minetd.common.energy.BurningItem;
import neoroutine.minetd.common.entities.antiking.AntikingEntity;
import neoroutine.minetd.common.grandmaster.EloRatingProvider;
import neoroutine.minetd.common.grandmaster.Grandmaster;
import neoroutine.minetd.common.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class KingBlockEntity extends BlockEntity
{
    private int spawnCounter = 0;
    private int healthCounter = 0;

    private int startingCount = 0;
    private int startingTimer = 100;

    private boolean duplicationStarted = false;

    private AABB boundingBox = null;

    List<Entity> entities = null;

    //Handle items
    private final ItemStackHandler itemStackHandler = createItemStackHandler();
    private final LazyOptional<IItemHandler> itemHandler = LazyOptional.of(() -> itemStackHandler);

    private BurningItem duplicatedItem = createDuplicatedItem();
    private final LazyOptional<BurningItem> duplicatedItemHandler = LazyOptional.of(() -> duplicatedItem);

    public final Grandmaster grandmaster = createGrandmaster();
    private final LazyOptional<Grandmaster> grandmasterHandler = LazyOptional.of(() -> grandmaster);

    public final KingHealth health = createHealth();
    private final LazyOptional<KingHealth> healthHandler = LazyOptional.of(() -> health);

    public KingBlockEntity(BlockPos position, BlockState state)
    {
        super(Registration.KING_BE.get(), position, state);
        boundingBox = new AABB(position).inflate(1);
    }

    @Override
    public void setRemoved()
    {
        super.setRemoved();
        itemHandler.invalidate();
        duplicatedItemHandler.invalidate();
    }

    public void tickServer()
    {
        BlockPos pos = getBlockPos();
        pos = new BlockPos(pos.getX(), pos.getY(), pos.getZ()+30);

        Runnable task = () ->
        {
            //Maybe optimize by querying Monster.class and entities.size() > 0 -> attacked=true
            entities = level.getEntitiesOfClass(Entity.class, boundingBox);
            boolean attacked = isAttacked();
            if (attacked) health.setHealth(health.getHealth() - 10);
            if (health.getHealth() <= 0)
            {
                //
            }
        };

        delayedPredicateHealth(task, 40);


        if (duplicatedItem.getBurningCounter() > 0)
        {
            duplicationStarted = true;
            ItemStack stack = itemStackHandler.getStackInSlot(0);
            if (stack.isEmpty())
            {
                duplicatedItem.setBurningCounter(0);
                duplicatedItem.setBurningItem(null);
                duplicationStarted = false;
            }
            else
            {
                duplicatedItem.setBurningCounter(duplicatedItem.getBurningCounter()-1);
            }

            setChanged();
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
        }

        if (duplicatedItem.getBurningCounter() <= 0)
        {
            duplicatedItem.setBurningItem(null);
            ItemStack stack = itemStackHandler.getStackInSlot(0);

            int burnTime = 0;

            if (stack != null && EloPointsCost.isDuplicable(stack.getItem()))
            {
                if (duplicationStarted)
                {
                    stack.setCount(stack.getCount()+1);
                    itemStackHandler.setStackInSlot(0, stack);
                    duplicationStarted = false;

                    this.grandmaster.updatePlayer(this.level);
                    this.grandmaster.getPlayer().getCapability(EloRatingProvider.PLAYER_ELO_POINTS).ifPresent(capability ->
                    {
                        int cost = EloPointsCost.getPointCost(stack.getItem());
                        capability.subtractPoints(this.grandmaster.getPlayer(), cost);
                        int points = capability.getPoints();
                        String message = String.format("You duplicated an item. Points -%d (%d)", cost, points);
                        this.grandmaster.getPlayer().displayClientMessage(new TranslatableComponent(message), true);
                    });
                }

                burnTime = EloPointsCost.getTimeCost(stack.getItem());
            }

            if (burnTime > 0)
            {
                duplicatedItem.setBurningCounter(burnTime);
                duplicatedItem.setBurningItem(stack.getItem());
                level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
                setChanged();
            }
        }


        if (startingCount >= startingTimer)
        {
            spawnAntiking(pos);
        }
        else
        {
            startingCount++;
        }
        level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
    }


    private void delayedPredicateSpawn(Runnable task, int delay)
    {
        if (spawnCounter >= delay)
        {
            task.run();
            spawnCounter = 0;
        }

        spawnCounter++;
    }

    private void delayedPredicateHealth(Runnable task, int delay)
    {
        if (healthCounter >= delay)
        {
            task.run();
            healthCounter = 0;
        }

        healthCounter++;
    }

    private boolean isAttacked()
    {
        if (entities.size() == 0) { return false;}

        for (Entity entity: entities)
        {
            if (entity instanceof Monster)
            {
                return true;
            }
        }

        return false;
    }

    public void updatePlacer(Player player)
    {
        this.grandmaster.setPlayer(player);
        this.grandmaster.updatePlayer(this.level);
        level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
        setChanged();
    }

    private void spawnAntiking(BlockPos pos)
    {
        EntityType<AntikingEntity> mob = Registration.ANTIKING.get();

        Runnable task = () ->
        {
            mob.spawn((ServerLevel) level, null, ((ServerLevel) level).getRandomPlayer(), pos, MobSpawnType.EVENT, false, false);
        };

        delayedPredicateSpawn(task,60);
    }

    @Override
    public void load(CompoundTag tag)
    {
        super.load(tag);
        loadClientData(tag);
        if (tag.contains("Inventory"))
        {
            itemStackHandler.deserializeNBT(tag.getCompound("Inventory"));
        }
        if (tag.contains("Info"))
        {
            duplicatedItem.deserializeNBT(tag.getCompound("Info"));
        }
        if (tag.contains("Grandmaster"))
        {
            grandmaster.deserializeNBT(tag.getCompound("Grandmaster"));
            grandmaster.updatePlayer(this.level);
        }
        if (tag.contains("Health"))
        {
            health.deserializeNBT(tag.getCompound("Health"));
        }
    }

    @Override
    public void saveAdditional(CompoundTag tag)
    {
        super.saveAdditional(tag);
        saveClientData(tag);
        tag.put("Inventory", itemStackHandler.serializeNBT());
        tag.put("Info", duplicatedItem.serializeNBT());

        grandmaster.updatePlayer(this.level);
        tag.put("Grandmaster", grandmaster.serializeNBT());

        tag.put("Health", health.serializeNBT());

    }

    private void saveClientData(CompoundTag tag)
    {
        tag.put("Duplicated", duplicatedItem.serializeNBT());
        grandmaster.updatePlayer(this.level);
        tag.put("GrandmasterClient", grandmaster.serializeNBT());
        tag.put("HealthClient", health.serializeNBT());

    }

    private void loadClientData(CompoundTag tag)
    {
        if (tag.contains("Duplicated"))
        {
            duplicatedItem.deserializeNBT(tag.getCompound("Duplicated"));
        }

        if (tag.contains("GrandmasterClient"))
        {
            grandmaster.deserializeNBT(tag.getCompound("GrandmasterClient"));
            grandmaster.updatePlayer(this.level);
        }

        if (tag.contains("HealthClient"))
        {
            health.deserializeNBT(tag.getCompound("HealthClient"));
        }
    }



    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket packet) {
        // This is called client side: remember the current state of the values that we're interested in

        CompoundTag tag = packet.getTag();
        // This will call loadClientData()
        handleUpdateTag(tag);

        // If any of the values was changed we request a refresh of our model data and send a block update (this will cause
        // the baked model to be recreated)

        level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
    }

    @Override
    public CompoundTag getUpdateTag()
    {
        CompoundTag tag = super.getUpdateTag();
        saveClientData(tag);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag)
    {
        if (tag != null) {
            loadClientData(tag);
        }
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction side)
    {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
        {
            return itemHandler.cast();
        }

        if (capability == CapabilityBurningItem.BURNING_ITEM_CAPABILITY)
        {
            return duplicatedItemHandler.cast();
        }

        if (capability == CapabilityGrandmaster.GRANDMASTER_CAPABILITY)
        {
            return grandmasterHandler.cast();
        }

        if (capability == CapabilityKingHealth.KING_HEALTH_CAPABILITY)
        {
            return healthHandler.cast();
        }


        return super.getCapability(capability, side);
    }

    //Helper functions
    private @NotNull ItemStackHandler createItemStackHandler()
    {
        return new ItemStackHandler(1)
        {
            @Override
            protected void onContentsChanged(int slot)
            {
                setChanged();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack)
            {
                return EloPointsCost.isDuplicable(stack.getItem());
            }

            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate)
            {
                if (!EloPointsCost.isDuplicable(stack.getItem()))
                {
                    return stack;
                }

                return super.insertItem(slot, stack, simulate);
            }
        };
    }

    private BurningItem createDuplicatedItem()
    {
        return new BurningItem()
        {
            @Override
            protected void onUpdate()
            {
                setChanged();
            }
        };
    }

    private Grandmaster createGrandmaster()
    {
        return new Grandmaster()
        {
            @Override
            protected void onUpdate() { setChanged();}
        };
    }

    private KingHealth createHealth()
    {
        return new KingHealth()
        {
            @Override
            protected void onUpdate() { setChanged();}
        };
    }

}
