package neoroutine.minetd.common.blocks.towers.pawn;

import neoroutine.minetd.common.blocks.towers.*;
import neoroutine.minetd.common.capabilities.CapabilityEnergyProperties;
import neoroutine.minetd.common.energy.BaseEnergyProperties;
import neoroutine.minetd.common.energy.BaseEnergyStorage;
import neoroutine.minetd.common.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;

import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
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

public class PawnBE extends TowerBlockEntity
{

    public PawnBE(BlockPos position, BlockState state)
    {
        super(Registration.PAWN_BE.get(), position, state);

        towerProperties.setReach(TowerReach.PAWN);
        towerProperties.setDelay(TowerDelay.PAWN);
        towerProperties.setDamage(TowerDamage.PAWN);
        towerProperties.setConsumption(TowerConsumption.PAWN);

        Direction facing = state.getValue(HorizontalDirectionalBlock.FACING);
        reachBox = new AABB(relativeCounter(worldPosition, facing, towerProperties.getReach()+1)).inflate(towerProperties.getReach());
    }

}
