package neoroutine.minetd.common.blocks.kings.white;

import neoroutine.minetd.common.blocks.kings.KingBlock;
import neoroutine.minetd.common.blocks.kings.KingBlockEntity;
import neoroutine.minetd.common.blocks.towers.TowerBlockEntity;
import neoroutine.minetd.common.blocks.towers.pawn.PawnBE;
import neoroutine.minetd.common.blocks.towers.pawn.PawnContainer;
import neoroutine.minetd.common.setup.Registration;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class WhiteKingBlock extends KingBlock {
    public WhiteKingBlock() {
        super();

        this.registerDefaultState(this.stateDefinition.any().setValue(FACING_PROPERTY, Direction.SOUTH));

        setBlockMessage("A White King, ready to lose ?");
        setScreenMessage("White King");
        setTowerName("White King");
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos position, BlockState state) {
        return new WhiteKingBlockEntity(position, state);
    }


    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos position, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!level.isClientSide()) {
            BlockEntity currentBE = level.getBlockEntity(position);
            if (currentBE instanceof KingBlockEntity) {
                MenuProvider containerProvider = new MenuProvider() {
                    @Override
                    public Component getDisplayName() {
                        return new TranslatableComponent(getScreenMessage());
                    }

                    @Nullable
                    @Override
                    public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player player) {
                        return new WhiteKingContainer(windowId, position, playerInventory, player);
                    }
                };
                NetworkHooks.openGui((ServerPlayer) player, containerProvider, currentBE.getBlockPos());
            } else {
                throw new IllegalStateException("Named container provider is missing");
            }
        }
        return InteractionResult.SUCCESS;
    }
}

