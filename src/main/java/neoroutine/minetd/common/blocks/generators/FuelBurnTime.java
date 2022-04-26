package neoroutine.minetd.common.blocks.generators;

import com.mojang.datafixers.util.Pair;
import net.minecraft.world.item.Item;

import java.util.HashMap;
import java.util.Map;

public class FuelBurnTime
{
    //ItemName -> [FuelTime in ticks]
    private static final Map<String, Integer> itemFuelTime = new HashMap(Map.ofEntries
            (
                    Map.entry("minetd:soft_coal", 100),
                    Map.entry("minetd:dense_coal", 400),
                    Map.entry("minetd:neutron_coal", 1600)
            ));

    public static boolean isFuel(Item item)
    {
        return itemFuelTime.containsKey(item.getRegistryName().toString());
    }

    public static int getFuelTime(Item item)
    {
        if (!isFuel(item)) { return 0; }
        return itemFuelTime.get(item.getRegistryName().toString());
    }
}
