package me.nallar.modpatcher.patches.oredic;

import net.minecraft.item.ItemStack;
import net.minecraft.util.IntHashMap;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;

/**
 * A cache entry
 *
 * @author Xfel
 *
 */
public class ItemStackInfo {

    public final int oreId;

    public final String oreName;

    private boolean initItems;

    private IntHashMap wildcardItems;

    private IntHashMap damageItems;

    public ItemStackInfo(int oreId) {
        this.oreId = oreId;

        if (oreId != -1) {
            oreName = OreDictionary.getOreName(oreId);
            initItems = true;
        } else {
            oreName = "Unknown";
            initItems = false;
        }
    }

    public ItemStackInfo(int oreId, String oreName, IntHashMap wildcardItems,
                         IntHashMap damageItems) {
        this.oreId = oreId;
        this.oreName = oreName;
        this.wildcardItems = wildcardItems;
        this.damageItems = damageItems;
    }


    public boolean matches(ItemStack stack) {

        if (oreId == -1) {
            return false;
        }

        if (initItems) {
            initItems = false;
            ArrayList<ItemStack> ores = OreDictionary.getOres(oreId);

            // we don't really know how big we need these sets, so let's
            // just let them grow...
            wildcardItems = new IntHashMap();
            damageItems = new IntHashMap();

            for (ItemStack ore : ores) {
                int itemDamage = stack.getItemDamage();
                if (itemDamage == OreDictionary.WILDCARD_VALUE) {
                    wildcardItems.addKey(ore.itemID, OreDictCache.VALID_MARKER);
                } else {
                    damageItems.addKey(OreDictCache.getItemKey(stack),
                            OreDictCache.VALID_MARKER);
                }
            }
        }

        // now lookup
        return wildcardItems.containsItem(stack.itemID)
                || damageItems.containsItem(OreDictCache.getItemKey(stack));
    }
}