package neoroutine.minetd.common.blocks.towers;

import neoroutine.minetd.common.capabilities.CapabilityEnergyProperties;
import neoroutine.minetd.common.capabilities.CapabilityGrandmaster;
import neoroutine.minetd.common.energy.BaseEnergyProperties;
import neoroutine.minetd.common.energy.BaseEnergyStorage;
import neoroutine.minetd.common.grandmaster.EloRatingProvider;
import neoroutine.minetd.common.grandmaster.Grandmaster;
import neoroutine.minetd.common.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class TowerBlockEntity extends BlockEntity
{
    public final BaseEnergyProperties energyProperties = new BaseEnergyProperties(20_000, 0, 0);
    private final LazyOptional<BaseEnergyProperties> energyPropertiesHandler = LazyOptional.of(() -> energyProperties);

    //Handle energy
    private final BaseEnergyStorage energyStorage = createEnergyStorage();
    private final LazyOptional<IEnergyStorage> energyHandler = LazyOptional.of(() -> energyStorage);

    public final Grandmaster grandmaster = createGrandmaster();
    private final LazyOptional<Grandmaster> grandmasterHandler = LazyOptional.of(() -> grandmaster);

    public final TowerProperties towerProperties = new TowerProperties(TowerDelay.PAWN, TowerReach.PAWN, TowerDamage.PAWN, TowerConsumption.PAWN);
    private int counter = 0;

    protected Item moveItem = Registration.PAWN_MOVE.get();


    protected AABB reachBox = null;
    private List<Entity> entities = null;

    public TowerBlockEntity(BlockEntityType<?> be, BlockPos position, BlockState state)
    {
        super(be, position, state);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        energyPropertiesHandler.invalidate();
        energyHandler.invalidate();
        grandmasterHandler.invalidate();
    }

    public BlockPos relativeCounter(BlockPos pos, Direction facing, int count)
    {
        if (count == 0) { return pos;}

        return relativeCounter(pos.relative(facing), facing, count-1);
    }

    //TODO: Later cache entity list somewhere to avoid fetching for it every time
    public void tickServer()
    {

        Runnable task = () ->
        {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
            grandmaster.updatePlayer(this.level);

            entities = level.getEntitiesOfClass(Entity.class, reachBox);
            boolean attacked = attackRandomEnemy();
            if (attacked)
            {
                energyStorage.consumeEnergy(towerProperties.getConsumption());
                Random rand = new Random();
                if (rand.nextInt(0, 21) == 20)
                {
                    this.grandmaster.getPlayer().addItem(new ItemStack(moveItem));
                }
            }

            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
            setChanged();
        };


        delayedPredicate(task);
    }



    private void delayedPredicate(Runnable task)
    {
        if (counter >= towerProperties.getDelay())
        {
            task.run();
            counter = 0;
        }

        counter++;
    }

    public void updatePlacer(Player player)
    {
        this.grandmaster.setPlayer(player);
        this.grandmaster.updatePlayer(this.level);
        level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
        setChanged();
    }

    private boolean enemyAvailable()
    {
        if (entities == null) { return false;}
        boolean available = false;
        for (Entity entity: entities)
        {
            if (entity instanceof Monster)
            {
                available = true;
            }
        }

        return available;
    }

    private boolean canAttack()
    {
        return energyStorage.getEnergyStored() > towerProperties.getConsumption() && enemyAvailable();
    }

    private boolean attackRandomEnemy()
    {
        if (!canAttack()) { return false;}

        Random rand = new Random();
        int randomHostileIndex = rand.nextInt(entities.size());

        Entity randomHostile = entities.get(randomHostileIndex);
        while (!(randomHostile instanceof Monster))
        {
            randomHostileIndex = rand.nextInt(entities.size());
            randomHostile = entities.get(randomHostileIndex);
        }

        String randomHostileName = randomHostile.getScoreboardName();
        randomHostile.hurt(DamageSource.MAGIC, towerProperties.getDamage());

        if (!randomHostile.isAlive())
        {
            this.grandmaster.updatePlayer(this.level);
            this.grandmaster.getPlayer().getCapability(EloRatingProvider.PLAYER_ELO_POINTS).ifPresent(capability ->
            {
                capability.addPoints(this.grandmaster.getPlayer(), 5);
                int points = capability.getPoints();
                String message = String.format("Your tower killed an enemy. Points +%d (%d)", 5, points);
                this.grandmaster.getPlayer().displayClientMessage(new TranslatableComponent(message), true);
            });
        }

        return true;
    }

    protected void pushOutPower() {}

    @Override
    public void load(CompoundTag tag)
    {
        super.load(tag);
        loadClientData(tag);
        if (tag.contains("Grandmaster"))
        {
            grandmaster.deserializeNBT(tag.getCompound("Grandmaster"));
            grandmaster.updatePlayer(this.level);
        }
    }

    @Override
    public void saveAdditional(CompoundTag tag)
    {
        super.saveAdditional(tag);
        saveClientData(tag);
        grandmaster.updatePlayer(this.level);
        tag.put("Grandmaster", grandmaster.serializeNBT());
    }

    private void saveClientData(CompoundTag tag)
    {
        grandmaster.updatePlayer(this.level);
        tag.put("GrandmasterClient", grandmaster.serializeNBT());
    }

    private void loadClientData(CompoundTag tag)
    {
        if (tag.contains("GrandmasterClient"))
        {
            grandmaster.deserializeNBT(tag.getCompound("GrandmasterClient"));
            grandmaster.updatePlayer(this.level);
        }
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket()
    {
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
        if (capability == CapabilityEnergy.ENERGY)
        {
            return energyHandler.cast();
        }

        if (capability == CapabilityEnergyProperties.ENERGY_PROPERTIES_CAPABILITY)
        {
            return energyPropertiesHandler.cast();
        }

        if (capability == CapabilityGrandmaster.GRANDMASTER_CAPABILITY)
        {
            return grandmasterHandler.cast();
        }

        return super.getCapability(capability, side);
    }

    private BaseEnergyStorage createEnergyStorage()
    {
        return new BaseEnergyStorage(energyProperties.getMaxPowerCapacity(), 200, 0)
        {
            @Override
            protected void onEnergyChanged()
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

    public AABB getReachBox()
    {
        return reachBox;
    }
}
