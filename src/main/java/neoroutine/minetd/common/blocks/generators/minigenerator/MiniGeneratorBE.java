package neoroutine.minetd.common.blocks.generators.minigenerator;

import neoroutine.minetd.common.blocks.blockentity.BaseBlockEntity;
import neoroutine.minetd.common.blocks.generators.GeneratorBlockEntity;
import neoroutine.minetd.common.blocks.generators.GeneratorCapacity;
import neoroutine.minetd.common.blocks.generators.GeneratorGeneration;
import neoroutine.minetd.common.blocks.generators.GeneratorPush;
import neoroutine.minetd.common.blocks.towerbase.TowerBase;
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

public class MiniGeneratorBE extends GeneratorBlockEntity
{

    public MiniGeneratorBE(BlockPos position, BlockState state)
    {
        super(Registration.MINIGERATOR_BE.get(), position, state);

    }

}