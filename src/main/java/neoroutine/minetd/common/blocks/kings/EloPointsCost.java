package neoroutine.minetd.common.blocks.kings;


import com.mojang.datafixers.util.Pair;
import net.minecraft.world.item.Item;

import java.util.HashMap;
import java.util.Map;

public class EloPointsCost {

    //ItemName -> [PointCost in EloPoints, TimeCost in ticks]
    private static final Map<String, Pair<Integer, Integer>> itemCost = new HashMap(Map.ofEntries
            (
                    Map.entry("minecraft:oak_log", new Pair<>(4, 40)),
                    Map.entry("minecraft:redstone", new Pair<>(10, 80)),
                    Map.entry("minecraft:gold_ingot", new Pair<>(50, 100)),
                    Map.entry("minecraft:diamond", new Pair<>(100, 200)),

                    Map.entry("minetd:pawn_move", new Pair<>(100, 200)),
                    Map.entry("minetd:knight_move", new Pair<>(200, 200)),
                    Map.entry("minetd:bishop_move", new Pair<>(200, 200)),
                    Map.entry("minetd:rook_move", new Pair<>(400, 200)),
                    Map.entry("minetd:queen_move", new Pair<>(800, 200))
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
