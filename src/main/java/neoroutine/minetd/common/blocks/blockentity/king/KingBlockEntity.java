package neoroutine.minetd.common.blocks.blockentity.king;

import neoroutine.minetd.common.entities.antiking.AntikingEntity;
import neoroutine.minetd.common.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class KingBlockEntity extends BlockEntity
{
    private int counter = 0;

    public KingBlockEntity(BlockPos position, BlockState state)
    {
        super(Registration.KING_BE.get(), position, state);
    }

    @Override
    public void setRemoved()
    {
        super.setRemoved();
    }

    public void tickServer()
    {
        BlockPos pos = getBlockPos();
        EntityType<AntikingEntity> mob = Registration.ANTIKING.get();

        Runnable task = () ->
        {
            mob.spawn((ServerLevel) level, null, ((ServerLevel) level).getRandomPlayer(), new BlockPos(pos.getX(), pos.getY(), pos.getZ()+20), MobSpawnType.EVENT, false, false);
        };

        delayedPredicate(task);

    }

    private void delayedPredicate(Runnable task)
    {
        if (counter >= 40)
        {
            task.run();
            counter = 0;
        }

        counter++;
    }

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


    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction side)
    {
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

    private void saveClientData(CompoundTag tag)
    {

    }

    private void loadClientData(CompoundTag tag)
    {

    }

}
