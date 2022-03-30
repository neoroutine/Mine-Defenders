package neoroutine.minetd.common.blocks.towers.pawn;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Pawn extends Block implements EntityBlock
{
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    private static final String MESSAGE_TOWER_T1_BASE        = "Tiny tower\nLast of their kind in performance";
    private static final String SCREEN_MINETD_TOWER_T1 = "screen.minetd.tower_t1";

    private static final VoxelShape RENDER_SHAPE = Shapes.box(0.1, 0.1, 0.1, 0.9, 0.9, 0.9);

    public Pawn()
    {
        super(Properties.of(Material.METAL)
                .sound(SoundType.METAL)
                .strength(2.0f)
                .lightLevel(state->state.getValue(BlockStateProperties.POWERED) ? 14 : 0)
                .requiresCorrectToolForDrops());

        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.SOUTH));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos position, BlockState state)
    {
        return new PawnBE(position, state);
    }


    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type)
    {
        if (level.isClientSide()) { return null;}

        return (lvl, pos, blockState, t) ->
        {
            if (t instanceof PawnBE tile) {
                tile.tickServer();
            }
        };
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState state, BlockGetter reader, BlockPos position)
    {
        return RENDER_SHAPE;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter reader, List<Component> components, TooltipFlag flags)
    {
        components.add(new TranslatableComponent(MESSAGE_TOWER_T1_BASE, Integer.toString(PawnBE.energyProperties.getMaxPowerCapacity()))
                .withStyle(ChatFormatting.BLUE));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder);
        builder.add(BlockStateProperties.POWERED);
        builder.add(FACING);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection());

    }

    @Override
    public BlockState rotate(BlockState p_51552_, Rotation p_51553_) {
        return p_51552_.setValue(FACING, p_51553_.rotate(p_51552_.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState p_48719_, Mirror p_48720_) {
        return p_48719_.rotate(p_48720_.getRotation(p_48719_.getValue(FACING)));
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos position, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!level.isClientSide())
        {
            BlockEntity currentBE = level.getBlockEntity(position);

            if (currentBE instanceof PawnBE)
            {
                MenuProvider containerProvider = new MenuProvider()
                {
                    @Override
                    public Component getDisplayName()
                    {
                        return new TranslatableComponent(SCREEN_MINETD_TOWER_T1);
                    }

                    @Nullable
                    @Override
                    public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player player)
                    {
                        return new PawnContainer(windowId, position, playerInventory, player);
                    }
                };

                NetworkHooks.openGui((ServerPlayer) player, containerProvider, currentBE.getBlockPos());
            }
            else
            {
                throw new IllegalStateException("Named container provider is missing");
            }
        }

        return InteractionResult.SUCCESS;
    }
}
