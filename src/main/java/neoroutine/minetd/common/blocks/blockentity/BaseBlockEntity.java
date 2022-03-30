package neoroutine.minetd.common.blocks.blockentity;

import neoroutine.minetd.common.capabilities.CapabilityBurningItem;
import neoroutine.minetd.common.capabilities.CapabilityEnergyProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.RegistryObject;
import javax.annotation.Nullable;

//TODO:getCapability() name clash, same exposure yet neither overrides the other (MiniGenerator getCapability() and CapabilityProvider's one)
public class BaseBlockEntity<BE extends BaseBlockEntity> extends BlockEntity
{
    public BaseBlockEntity(RegistryObject<BlockEntityType<BE>> be, BlockPos position, BlockState state)
    {
        super(be.get(), position, state);
    }

    public void tickServer() {}

    protected void pushOutPower() {}

    @Override
    public void load(CompoundTag tag)
    {
        super.load(tag);
        loadClientData(tag);
    }

    @Override
    public void saveAdditional(CompoundTag tag)
    {
        super.saveAdditional(tag);
        saveClientData(tag);
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


}