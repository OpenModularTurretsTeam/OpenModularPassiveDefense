package omtteam.ompd.registry;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

import static omtteam.ompd.registry.ModBlocks.wallItem;

public class OpenModularPassiveDefenseTab extends CreativeTabs {
    private static OpenModularPassiveDefenseTab instance;

    private OpenModularPassiveDefenseTab(String label) {
        super(label);
    }

    public static OpenModularPassiveDefenseTab getInstance() {
        if (instance == null) {
            // the lang files name the tab itemGroup.openmodularpassivedefense
            instance = new OpenModularPassiveDefenseTab("openmodularpassivedefense");
        }
        return instance;
    }

    @Override
    @Nonnull
    public ItemStack getIcon() {
        return new ItemStack(ModBlocks.hardened);
    }

    @Override
    @Nonnull
    public ItemStack createIcon() {
        return new ItemStack(wallItem);
    }
}
