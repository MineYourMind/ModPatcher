package me.nallar.modpatcher.patches.debug.thermalExpansion;

import cpw.mods.fml.common.FMLLog;
import net.minecraft.server.MinecraftServer;

import java.util.*;

/**
 * Created by Slind on 11.09.2014.
 */
public class ConduitItem {

    private static HashMap<String, Integer> counter = new HashMap<String, Integer>();
    private static Integer lastPrint = null;

    public static void logTE(String c) {
        int tick = MinecraftServer.getServer().getTickCounter();
        int count = counter.containsKey(c) ? counter.get(c) : 0;
        counter.put(c, count + 1);
        if(lastPrint == null || (tick - lastPrint) > (5 * 60 * 1000 / 50 )) {
            List<Map.Entry<String, Integer>> sCounter = sortByComparator(counter, false);
            FMLLog.warning("Heavy ItemDucts:");
            for (Map.Entry<String, Integer> e : sCounter)
            {
                if (e.getValue() > 4600)
                    FMLLog.info(" -  " + e.getValue() + "   " + e.getKey());
            }
            counter.clear();
            sCounter.clear();
            lastPrint = tick;
        }
    }

    private static List<Map.Entry<String, Integer>> sortByComparator(Map<String, Integer> unsortMap, final boolean order)
    {
        List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String, Integer>>(unsortMap.entrySet());
        // Sorting the list based on values
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2) {
                if (order) {
                    return o1.getValue().compareTo(o2.getValue());
                } else {
                    return o2.getValue().compareTo(o1.getValue());
                }
            }
        });

        return list;
    }
}
