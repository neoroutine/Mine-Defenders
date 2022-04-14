package neoroutine.minetd.common.blocks.king;

import neoroutine.minetd.common.blocks.container.BaseContainer;
import neoroutine.minetd.common.capabilities.CapabilityBurningItem;
import neoroutine.minetd.common.capabilities.CapabilityEnergyProperties;
import neoroutine.minetd.common.capabilities.CapabilityGrandmaster;
import neoroutine.minetd.common.capabilities.CapabilityKingHealth;
import neoroutine.minetd.common.energy.BaseEnergyProperties;
import neoroutine.minetd.common.energy.BaseEnergyStorage;
import neoroutine.minetd.common.energy.BurningItem;
import neoroutine.minetd.common.grandmaster.Grandmaster;
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

    public int getDuplicatedItemCounter()
    {
        int result = blockEntity.getCapability(CapabilityBurningItem.BURNING_ITEM_CAPABILITY).map(BurningItem::getBurningCounter).orElse(0);
        return result;
    }

    public String getDuplicatedItemName()
    {
        return blockEntity.getCapability(CapabilityBurningItem.BURNING_ITEM_CAPABILITY).map(BurningItem::getBurningItemName).orElse("None");
    }

    public String getGrandmasterName()
    {
        return blockEntity.getCapability(CapabilityGrandmaster.GRANDMASTER_CAPABILITY).map(Grandmaster::getGrandmasterName).orElse("Unknown");
    }

    public String getGrandmasterUUID()
    {
        return blockEntity.getCapability(CapabilityGrandmaster.GRANDMASTER_CAPABILITY).map(Grandmaster::getGrandMasterUUID).orElse("Unknown");
    }

    public int getKingHealth()
    {
        return blockEntity.getCapability(CapabilityKingHealth.KING_HEALTH_CAPABILITY).map(KingHealth::getHealth).orElse(0);
    }

    public int getKingMaxHealth()
    {
        return blockEntity.getCapability(CapabilityKingHealth.KING_HEALTH_CAPABILITY).map(KingHealth::getMaxHealth).orElse(0);
    }
}
