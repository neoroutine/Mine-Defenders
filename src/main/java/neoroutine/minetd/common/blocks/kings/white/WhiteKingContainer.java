package neoroutine.minetd.common.blocks.kings.white;

import neoroutine.minetd.common.blocks.container.BaseContainer;
import neoroutine.minetd.common.blocks.kings.KingHealth;
import neoroutine.minetd.common.capabilities.CapabilityBurningItem;
import neoroutine.minetd.common.capabilities.CapabilityGrandmaster;
import neoroutine.minetd.common.capabilities.CapabilityKingHealth;
import neoroutine.minetd.common.energy.BurningItem;
import neoroutine.minetd.common.grandmaster.Grandmaster;
import neoroutine.minetd.common.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;

public class WhiteKingContainer extends BaseContainer
{
    public WhiteKingContainer(int windowId, BlockPos position, Inventory playerInventory, Player player)
    {
        super(Registration.WHITE_KING_CONTAINER, Registration.WHITE_KING, windowId, position, playerInventory, player);
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

    public int getGrandmasterEloPoints()
    {
        return blockEntity.getCapability(CapabilityGrandmaster.GRANDMASTER_CAPABILITY).map(Grandmaster::getGrandmasterEloPoints).orElse(0);
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