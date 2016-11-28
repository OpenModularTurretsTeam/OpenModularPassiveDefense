package omtteam.ompd.client.gui;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import omtteam.ompd.init.ModBlocks;

public class OpenModularPassiveDefenseTab extends CreativeTabs {
    public OpenModularPassiveDefenseTab(String label) {
        super(label);
    }

    @Override
    public ItemStack getIconItemStack() {
        return new ItemStack(ModBlocks.hardened);
    }

    @Override
    public Item getTabIconItem() {
        return null;
    }
}
