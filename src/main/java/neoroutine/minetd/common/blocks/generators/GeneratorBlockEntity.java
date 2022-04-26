package neoroutine.minetd.common.blocks.generators;

import neoroutine.minetd.common.blocks.towerbase.TowerBaseBE;
import neoroutine.minetd.common.capabilities.CapabilityBurningItem;
import neoroutine.minetd.common.capabilities.CapabilityEnergyProperties;
import neoroutine.minetd.common.energy.BaseEnergyProperties;
import neoroutine.minetd.common.energy.BaseEnergyStorage;
import neoroutine.minetd.common.energy.BurningItem;
import neoroutine.minetd.common.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicInteger;

public class GeneratorBlockEntity extends BlockEntity
{
    public static final BaseEnergyProperties energyProperties = new BaseEnergyProperties(GeneratorCapacity.SMALL, GeneratorGeneration.SMALL, GeneratorPush.SMALL);
    private final LazyOptional<BaseEnergyProperties> energyPropertiesHandler = LazyOptional.of(() -> energyProperties);

    //Handle items
    private final ItemStackHandler itemStackHandler = createItemStackHandler();
    private final LazyOptional<IItemHandler> itemHandler = LazyOptional.of(() -> itemStackHandler);

    //Handle energy
    private final BaseEnergyStorage energyStorage = createEnergyStorage();
    private final LazyOptional<IEnergyStorage> energyHandler = LazyOptional.of(() -> energyStorage);

    private BurningItem burningItem = createBurningItem();
    private final LazyOptional<BurningItem> burningCounterHandler = LazyOptional.of(() -> burningItem);


    public GeneratorBlockEntity(BlockEntityType<?> be, BlockPos position, BlockState state)
    {
        super(be, position, state);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        energyPropertiesHandler.invalidate();
        itemHandler.invalidate();
        energyHandler.invalidate();
        burningCounterHandler.invalidate();
    }

    public void tickServer()
    {
        BlockState blockState = level.getBlockState(worldPosition);

        if (burningItem.getBurningCounter() > 0 && energyStorage.canReceive())
        {
            energyStorage.addEnergy(energyProperties.getPowerGeneration());
            burningItem.setBurningCounter(burningItem.getBurningCounter()-1);
            setChanged();
            level.sendBlockUpdated(worldPosition, blockState, blockState, Block.UPDATE_ALL);
        }

        if (burningItem.getBurningCounter() <= 0)
        {
            burningItem.setBurningItem(null);
            ItemStack stack = itemStackHandler.getStackInSlot(0);

            int burnTime = FuelBurnTime.getFuelTime(stack.getItem());
            if (burnTime > 0)
            {
                itemStackHandler.extractItem(0, 1, false);
                burningItem.setBurningCounter(burnTime);
                burningItem.setBurningItem(stack.getItem());
                setChanged();
            }
        }

        if (blockState.getValue(BlockStateProperties.POWERED) != burningItem.getBurningCounter() > 0)
        {
            level.setBlock(worldPosition, blockState.setValue(BlockStateProperties.POWERED, burningItem.getBurningCounter() > 0), Block.UPDATE_ALL);
        }

        pushOutPower();
    }

    protected void pushOutPower()
    {
        AtomicInteger storedEnergy = new AtomicInteger(energyStorage.getEnergyStored());
        if (storedEnergy.get() > 0)
        {
            for (Direction direction: Direction.values())
            {
                BlockEntity neighbourBE = level.getBlockEntity(worldPosition.relative(direction));
                if (neighbourBE != null)
                {
                    boolean doAgain = neighbourBE.getCapability(CapabilityEnergy.ENERGY, direction.getOpposite()).map(handler ->
                    {
                        if (handler.canReceive() && neighbourBE instanceof TowerBaseBE)
                        {
                            int energyReceived = handler.receiveEnergy(Math.min(storedEnergy.get(), energyProperties.getPowerPush()), false);
                            storedEnergy.addAndGet(-energyReceived);
                            energyStorage.consumeEnergy(energyReceived);
                            setChanged();

                            return storedEnergy.get() > 0;
                        }
                        else
                        {
                            return true;
                        }
                    }).orElse(true);

                    if (!doAgain)
                    {
                        return;
                    }
                }
            }
        }
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

        if (tag.contains("Energy"))
        {
            energyStorage.deserializeNBT(tag.get("Energy"));
        }

        if (tag.contains("Info"))
        {
            burningItem.deserializeNBT(tag.getCompound("Info"));
        }
    }

    @Override
    public void saveAdditional(CompoundTag tag)
    {
        super.saveAdditional(tag);
        saveClientData(tag);
        tag.put("Inventory", itemStackHandler.serializeNBT());
        tag.put("Energy", energyStorage.serializeNBT());
        tag.put("Info", burningItem.serializeNBT());
    }


    private void saveClientData(CompoundTag tag)
    {
        tag.put("Burning", burningItem.serializeNBT());
    }

    private void loadClientData(CompoundTag tag)
    {
        if (tag.contains("Burning"))
        {
            burningItem.deserializeNBT(tag.getCompound("Burning"));
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
                return FuelBurnTime.getFuelTime(stack.getItem()) > 0;
            }

            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate)
            {
                if (FuelBurnTime.getFuelTime(stack.getItem()) <= 0)
                {
                    return stack;
                }

                return super.insertItem(slot, stack, simulate);
            }
        };
    }

    private BaseEnergyStorage createEnergyStorage()
    {
        return new BaseEnergyStorage(energyProperties.getMaxPowerCapacity(), 0)
        {
            @Override
            protected void onEnergyChanged()
            {
                setChanged();
            }
        };
    }

    private BurningItem createBurningItem()
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

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction side)
    {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
        {
            return itemHandler.cast();
        }

        if (capability == CapabilityEnergy.ENERGY)
        {
            return energyHandler.cast();
        }

        if (capability == CapabilityBurningItem.BURNING_ITEM_CAPABILITY)
        {
            return burningCounterHandler.cast();
        }

        if (capability == CapabilityEnergyProperties.ENERGY_PROPERTIES_CAPABILITY)
        {
            return energyPropertiesHandler.cast();
        }

        return super.getCapability(capability, side);
    }
}
