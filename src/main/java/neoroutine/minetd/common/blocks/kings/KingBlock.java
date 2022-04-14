package neoroutine.minetd.common.blocks.kings;

import neoroutine.minetd.common.blocks.kings.white.WhiteKingContainer;
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
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
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

public class KingBlock extends Block implements EntityBlock
{
    private String BLOCK_MESSAGE   = "message.king";
    private String SCREEN_MESSAGE  = "screen.minetd.king";
    protected String towerName = "King";

    public static final DirectionProperty FACING_PROPERTY = HorizontalDirectionalBlock.FACING;

    private static final VoxelShape RENDER_SHAPE = Shapes.box(0.1, 0.1, 0.1, 0.9, 0.9, 0.9);

    public KingBlock()
    {
        super(Properties.of(Material.METAL)
                .sound(SoundType.METAL)
                .strength(2.0f)
                .lightLevel(state -> 14)
                .requiresCorrectToolForDrops());

    }

    //Implement safety checks later on (size of string))==
    public String getBlockMessage() { return this.BLOCK_MESSAGE; }
    protected void setBlockMessage(String value){ this.BLOCK_MESSAGE = value; }

    public String getScreenMessage() { return this.SCREEN_MESSAGE; }
    protected void setScreenMessage(String value){ this.SCREEN_MESSAGE = value;}

    public String getTowerName() { return this.towerName; }
    protected void setTowerName(String value){ this.towerName = value;}

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos position, BlockState state)
    {
        return null;
    }


    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type)
    {
        if (level.isClientSide()) { return null;}

        return (lvl, pos, blockState, t) ->
        {
            if (t instanceof KingBlockEntity tile) {
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
        components.add(new TranslatableComponent(BLOCK_MESSAGE).withStyle(ChatFormatting.BLUE));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder);
        builder.add(FACING_PROPERTY);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        return this.defaultBlockState().setValue(FACING_PROPERTY, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState p_51552_, Rotation p_51553_)
    {
        return p_51552_.setValue(FACING_PROPERTY, p_51553_.rotate(p_51552_.getValue(FACING_PROPERTY)));
    }

    @Override
    public BlockState mirror(BlockState p_48719_, Mirror p_48720_)
    {
        return p_48719_.rotate(p_48720_.getRotation(p_48719_.getValue(FACING_PROPERTY)));
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos position, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!level.isClientSide())
        {
            BlockEntity currentBE = level.getBlockEntity(position);

            if (currentBE instanceof KingBlockEntity)
            {
                MenuProvider containerProvider = new MenuProvider()
                {
                    @Override
                    public Component getDisplayName()
                    {
                        return new TranslatableComponent(SCREEN_MESSAGE);
                    }

                    @Nullable
                    @Override
                    public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player player)
                    {
                        return new WhiteKingContainer(windowId, position, playerInventory, player);
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
