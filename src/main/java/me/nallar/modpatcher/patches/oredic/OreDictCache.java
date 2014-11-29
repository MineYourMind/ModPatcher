package me.nallar.modpatcher.patches.oredic;

import net.minecraft.item.ItemStack;
import net.minecraft.util.IntHashMap;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Caches ore dictionary reverse lookups
 *
 * @author Xfel
 *
 * @see OreDictionary
 */
public class OreDictCache {

    static int getItemKey(ItemStack stack) {
        int itemDamage = stack.getItemDamage();
        if (itemDamage <= 0) {
            return stack.itemID << 16;
        }
        return itemDamage | (stack.itemID << 16);
    }

    static final Object VALID_MARKER = new Object();

    // a bit more effective in memory... I hope it can handle the growth factor
    private static IntHashMap itemstackToIdCache = new IntHashMap();

    /**
     * Gets the integer ID for the specified item stack. If the item stack is
     * not linked to any ore, this will return -1 and no new entry will be
     * created.
     *
     * @param itemStack
     *            The item stack of the ore.
     * @return A number representing the ID for this ore type, or -1 if couldn't
     *         find it.
     */
    public static int getOreID(ItemStack itemStack) {
        ItemStackInfo info = getEntry(itemStack);
        return info.oreId;
    }

    public static boolean doOreIDsMatch(ItemStack stackA, ItemStack stackB) {
        ItemStackInfo info = getEntry(stackA);

        return info.matches(stackB);
    }

    public static String getOreName(ItemStack stack) {
        ItemStackInfo info = getEntry(stack);

        return info.oreName;
    }

    public static ItemStackInfo getEntry(ItemStack itemStack) {
        int itemKey = getItemKey(itemStack);

        ItemStackInfo info = (ItemStackInfo) itemstackToIdCache.lookup(itemKey);

        if (info == null) {
            // do the real lookup - sorry, it's going to be slow, but I can't do
            // anything about that, really!
            int oreId = OreDictionary.getOreID(itemStack);

            info = new ItemStackInfo(oreId);
            itemstackToIdCache.addKey(itemKey, info);
        }
        return info;
    }


}
