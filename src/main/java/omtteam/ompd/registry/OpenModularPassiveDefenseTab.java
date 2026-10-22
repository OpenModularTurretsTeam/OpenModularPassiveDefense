package omtteam.ompd.registry;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import omtteam.ompd.reference.Reference;

import javax.annotation.Nonnull;

import static omtteam.ompd.registry.ModBlocks.wallItem;

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
    public ItemStack getTabIconItem() {
        return new ItemStack(wallItem);
    }
}
