package omtteam.ompd.client.gui;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import omtteam.ompd.init.ModBlocks;

import javax.annotation.Nonnull;

import static omtteam.ompd.init.ModBlocks.wallItem;

public class OpenModularPassiveDefenseTab extends CreativeTabs {
    public OpenModularPassiveDefenseTab(String label) {
        super(label);
    }

    @Override
    @Nonnull
    public ItemStack getIconItemStack() {
        return new ItemStack(ModBlocks.hardened);
    }

    @Override
    @Nonnull
    public Item getTabIconItem() {
        return wallItem;
    }
}
