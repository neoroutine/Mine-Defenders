package neoroutine.minetd.common.blocks.king;


import com.mojang.datafixers.util.Pair;
import net.minecraft.world.item.Item;

import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;

public class EloPointsCost {

    //ItemName -> [PointCost in EloPoints, TimeCost in ticks]
    private static final Map<String, Pair<Integer, Integer>> itemCost = new HashMap(Map.ofEntries
            (
                    Map.entry("minecraft:oak_planks", new Pair<>(1, 10)),
                    Map.entry("minecraft:oak_log", new Pair<>(4, 40)),
                    Map.entry("minecraft:gold_ingot", new Pair<>(50, 100)),
                    Map.entry("minecraft:diamond", new Pair<>(100, 200))
            ));

    public static boolean isDuplicable(Item item)
    {
        return itemCost.containsKey(item.getRegistryName().toString());
    }

    public static int getPointCost(Item item)
    {
        return itemCost.get(item.getRegistryName().toString()).getFirst();
    }

    public static int getTimeCost(Item item)
    {
        return itemCost.get(item.getRegistryName().toString()).getSecond();
    }
}
