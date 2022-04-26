package neoroutine.minetd.common.blocks.container;

import neoroutine.minetd.common.setup.Registration;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.registries.RegistryObject;

public class BaseContainer<B extends Block, C extends AbstractContainerMenu> extends AbstractContainerMenu
{
    protected BlockEntity blockEntity;
    protected Player playerEntity;
    protected IItemHandler playerInventory;

    private RegistryObject<B> block;

    public BaseContainer(RegistryObject<MenuType<C>> container, RegistryObject<B> block, int windowId, BlockPos position, Inventory playerInventory, Player player)
    {
        super(container.get(), windowId);

        blockEntity = player.getCommandSenderWorld().getBlockEntity(position);

        this.playerEntity = player;
        this.playerInventory = new InvWrapper(playerInventory);
        this.block = block;

        if (blockEntity != null)
        {
            blockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler ->
            {
                addSlot(new SlotItemHandler(handler, 0, 77, 24));
            });
        }

        layoutPlayerInventorySlots(10, 70);
        trackPower();

    }

    @Override
    public boolean stillValid(Player player)
    {
        return stillValid(ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos()), playerEntity, block.get());
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;

        Slot slot = this.slots.get(index);
        if (slot != null  && slot.hasItem())
        {
            ItemStack slotStack = slot.getItem();
            itemStack = slotStack.copy();

            if (index == 0)
            {
                if (!this.moveItemStackTo(slotStack, 1,37, true))
                {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(slotStack, itemStack);
            }
            else
            {
                if (ForgeHooks.getBurnTime(slotStack, RecipeType.SMELTING) > 0)
                {
                    if (!this.moveItemStackTo(slotStack, 0, 1, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (index < 28)
                {
                    if (!this.moveItemStackTo(slotStack, 28, 37, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (index < 37 && !this.moveItemStackTo(slotStack, 1, 28, false))
                {
                    return ItemStack.EMPTY;
                }
            }

            if (slotStack.isEmpty())
            {
                slot.set(ItemStack.EMPTY);
            }
            else
            {
                slot.setChanged();
            }

            if (slotStack.getCount() == itemStack.getCount())
            {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, slotStack);
        }

        return itemStack;
    }

    private int addSlotRange(IItemHandler itemHandler, int index, int x, int y, int amount, int dx)
    {
        for (int i = 0 ; i < amount ; i++)
        {
            addSlot(new SlotItemHandler(itemHandler, index, x, y));
            x += dx;
            index++;
        }
        return index;
    }

    private int addSlotBox(IItemHandler itemHandler, int index, int x, int y, int horAmount, int dx, int verAmount, int dy)
    {
        for (int j = 0 ; j < verAmount ; j++)
        {
            index = addSlotRange(itemHandler, index, x, y, horAmount, dx);
            y += dy;
        }
        return index;
    }

    private void layoutPlayerInventorySlots(int leftCol, int topRow)
    {
        // Player inventory
        addSlotBox(playerInventory, 9, leftCol, topRow, 9, 18, 3, 18);

        // Hotbar
        topRow += 58;
        addSlotRange(playerInventory, 0, leftCol, topRow, 9, 18);
    }


    protected void trackPower(){};
}
