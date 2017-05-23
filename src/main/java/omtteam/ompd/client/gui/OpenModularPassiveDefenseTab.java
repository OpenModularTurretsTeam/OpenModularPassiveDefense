package omtteam.ompd.client.gui;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import omtteam.ompd.init.ModBlocks;
import omtteam.ompd.reference.Reference;

import javax.annotation.Nonnull;

import static omtteam.ompd.init.ModBlocks.wallItem;

public class OpenModularPassiveDefenseTab extends CreativeTabs {
    private static OpenModularPassiveDefenseTab instance;

    private OpenModularPassiveDefenseTab(String label) {
        super(label);
    }

    public static OpenModularPassiveDefenseTab getInstance() {
        if (instance == null) {
            instance = new OpenModularPassiveDefenseTab(Reference.NAME);
        }
        return instance;
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
