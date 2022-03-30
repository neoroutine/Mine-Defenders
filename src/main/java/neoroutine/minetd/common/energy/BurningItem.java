package neoroutine.minetd.common.energy;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.registries.ForgeRegistries;

public class BurningItem implements INBTSerializable<Tag>
{
    private int burningCounter;
    private Item burningItem;

    public BurningItem()
    {
        burningCounter = 0;
        burningItem = null;
    }

    public int getBurningCounter()
    {
        return burningCounter;
    }

    public void setBurningCounter(int value)
    {
        burningCounter = value;
        onUpdate();
    }

    public Item getBurningItem() {return burningItem;}

    public String getBurningItemName() { if (burningItem == null) { return "None";} return burningItem.toString();}

    public void setBurningItem(Item item)
    {
        burningItem = item;
        onUpdate();
    }

    protected void onUpdate() {}

    @Override
    public Tag serializeNBT()
    {
        CompoundTag burningItem = new CompoundTag();
        burningItem.putInt("Counter", getBurningCounter());
        burningItem.putString("Item", getBurningItemName());

        return burningItem;
    }

    @Override
    public void deserializeNBT(Tag nbt)
    {
        burningCounter = ((CompoundTag)nbt).getInt("Counter");
        String itemName = ((CompoundTag)nbt).getString("Item");
        if (itemName.equals("None")) { burningItem = null;}
        else
        {
            burningItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation("minecraft", itemName));
        }
        onUpdate();
    }
}
