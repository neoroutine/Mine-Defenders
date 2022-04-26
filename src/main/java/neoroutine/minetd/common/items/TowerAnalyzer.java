package neoroutine.minetd.common.items;

import neoroutine.minetd.common.blocks.towers.TowerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.entity.BlockEntity;

import static neoroutine.minetd.common.setup.Registration.ITEM_PROPERTIES;

public class TowerAnalyzer extends Item
{
    public static BlockPos cachedBlockPos = null;

    public TowerAnalyzer()
    {
        super(ITEM_PROPERTIES);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        BlockPos clickedPos = context.getClickedPos();
        BlockEntity clickedBE = context.getLevel().getBlockEntity(clickedPos);

        cachedBlockPos = null;

        if (clickedBE instanceof TowerBlockEntity tower)
        {
            cachedBlockPos = clickedPos;
        }

        return super.useOn(context);
    }
}
