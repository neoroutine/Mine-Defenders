package neoroutine.minetd.common.blocks.blockentity.king;

import neoroutine.minetd.common.blocks.container.BaseContainer;
import neoroutine.minetd.common.capabilities.CapabilityEnergyProperties;
import neoroutine.minetd.common.energy.BaseEnergyProperties;
import neoroutine.minetd.common.energy.BaseEnergyStorage;
import neoroutine.minetd.common.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.DataSlot;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class KingContainer extends BaseContainer
{
    public KingContainer(int windowId, BlockPos position, Inventory playerInventory, Player player)
    {
        super(Registration.KING_CONTAINER, Registration.KING, windowId, position, playerInventory, player);
    }

}
