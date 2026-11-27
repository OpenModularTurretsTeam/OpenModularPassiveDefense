package omtteam.ompd.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import omtteam.ompd.OpenModularPassiveDefense;
import omtteam.ompd.config.DefenseTier;
import omtteam.ompd.reference.Reference;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * A crafting item with the tier in its metadata (see {@link DefenseTier}); translation keys
 * {@code item.<name>_tier_one} to {@code item.<name>_tier_five}.
 */
public class ItemTiered extends Item {
    public static final String[] TIER_NAMES = {"one", "two", "three", "four", "five"};

    private final String name;

    public ItemTiered(String name) {
        this.name = name;
        this.setHasSubtypes(true);
        this.setCreativeTab(OpenModularPassiveDefense.modularPassiveDefenseTab);
        this.setRegistryName(Reference.MOD_ID, name);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
        if (isInCreativeTab(tab)) {
            for (int i = 0; i < TIER_NAMES.length; i++) {
                subItems.add(new ItemStack(this, 1, i));
            }
        }
    }

    @Override
    @Nonnull
    public String getTranslationKey(ItemStack itemStack) {
        return "item." + name + "_tier_" + TIER_NAMES[DefenseTier.clampMeta(itemStack.getItemDamage())];
    }
}
