package neoroutine.minetd.common.blocks.towerbase;

import neoroutine.minetd.common.blocks.towers.TowerBlockEntity;
import neoroutine.minetd.common.blocks.towers.pawn.PawnBE;
import neoroutine.minetd.common.capabilities.CapabilityEnergyProperties;
import neoroutine.minetd.common.energy.BaseEnergyProperties;
import neoroutine.minetd.common.energy.BaseEnergyStorage;
import neoroutine.minetd.common.setup.Registration;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;


import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicInteger;

public class TowerBaseBE extends BlockEntity
{
    /**
     * Check the {@link #pushOutPower()} and simulatedMaxEnergyReceived for the reason behind push 0,
     * tower base adjuts its output according to nearby blocks
     */
    public static final BaseEnergyProperties energyProperties = new BaseEnergyProperties(20_000, 0, 0);
    private final LazyOptional<BaseEnergyProperties> energyPropertiesHandler = LazyOptional.of(() -> energyProperties);

    //Handle energy
    private final BaseEnergyStorage energyStorage = createEnergyStorage();
    private final LazyOptional<IEnergyStorage> energyHandler = LazyOptional.of(() -> energyStorage);

    public TowerBaseBE(BlockPos position, BlockState state)
    {
        super(Registration.TOWER_BASE_BE.get(), position, state);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        energyPropertiesHandler.invalidate();
        energyHandler.invalidate();
    }

    public void tickServer()
    {
        BlockState blockState = level.getBlockState(worldPosition);
        setChanged();
        level.sendBlockUpdated(worldPosition, blockState, blockState, Block.UPDATE_ALL);

        pushOutPower();
    }

    /**
     *
     * 0 Push because the Tower Base adapts itself to blocks around it, see simulatedMaxEnergyReceived <ul><li>-blablablu</li></ul>
     */
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
                        if (handler.canReceive() && neighbourBE instanceof TowerBlockEntity)
                        {
                            int simulatedMaxEnergyReceived = handler.receiveEnergy(Integer.MAX_VALUE, true);
                            int energyReceived = handler.receiveEnergy(Math.min(storedEnergy.get(), simulatedMaxEnergyReceived), false);
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

        if (tag.contains("Energy"))
        {
            energyStorage.deserializeNBT(tag.get("Energy"));
        }

    }

    @Override
    public void saveAdditional(CompoundTag tag)
    {
        super.saveAdditional(tag);
        saveClientData(tag);
        tag.put("Energy", energyStorage.serializeNBT());
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

        return super.getCapability(capability, side);
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

    private void saveClientData(CompoundTag tag) {}

    private void loadClientData(CompoundTag tag) {}

    private BaseEnergyStorage createEnergyStorage()
    {
        return new BaseEnergyStorage(energyProperties.getMaxPowerCapacity(), 200, 100)
        {
            @Override
            protected void onEnergyChanged()
            {
                setChanged();
            }

        };
    }
}
