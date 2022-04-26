package neoroutine.minetd.common.blocks.generators.softgenerator;

import neoroutine.minetd.common.blocks.generators.GeneratorBlock;
import neoroutine.minetd.common.blocks.generators.GeneratorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class SoftGenerator extends GeneratorBlock
{
    public SoftGenerator()
    {
        super();

        setBlockMessage("Well, that's a start (T1 Generator)");
        setScreenMessage("T1 Generator");
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos position, BlockState state) {
        return new SoftGeneratorBlockEntity(position, state);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos position, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!level.isClientSide())
        {
            BlockEntity currentBE = level.getBlockEntity(position);

            if (currentBE instanceof GeneratorBlockEntity)
            {
                MenuProvider containerProvider = new MenuProvider()
                {
                    @Override
                    public Component getDisplayName()
                    {
                        return new TranslatableComponent(getScreenMessage());
                    }

                    @Nullable
                    @Override
                    public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player player)
                    {
                        return new SoftGeneratorContainer(windowId, position, playerInventory, player);
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
