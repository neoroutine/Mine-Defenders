package neoroutine.minetd.common.blocks.towers;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.List;

//TODO Implement common methods/variables among towers, for now blocks are invisible

public class TowerBlock extends Block implements EntityBlock
{
    private String BLOCK_MESSAGE   = "message.tower";
    private String SCREEN_MESSAGE  = "screen.minetd.tower";
    protected String towerName = "Tower";

    public static final DirectionProperty FACING_PROPERTY = HorizontalDirectionalBlock.FACING;

    public static final VoxelShape RENDER_SHAPE = Shapes.box(0.1, 0.1, 0.1, 0.9, 0.9, 0.9);

    public TowerBlock()
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
    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return null;
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState state, BlockGetter reader, BlockPos position)
    {
        return RENDER_SHAPE;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide()) { return null;}

        return (lvl, pos, blockState, t) ->
        {
            if (t instanceof TowerBlockEntity tile)
            {
                tile.tickServer();
            }
        };
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter reader, List<Component> components, TooltipFlag flags)
    {
        components.add(new TranslatableComponent(getBlockMessage()).withStyle(ChatFormatting.BLUE));
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
}
