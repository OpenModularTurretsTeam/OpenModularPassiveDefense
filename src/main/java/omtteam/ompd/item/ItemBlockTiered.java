package omtteam.ompd.item;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import omtteam.ompd.config.DefenseTier;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * The item of a block with the tier in its metadata (see {@link DefenseTier}); translation keys
 * {@code tile.<name>_tier_one} to {@code tile.<name>_tier_five}.
 */
public class ItemBlockTiered extends ItemBlock {
    private static final String[] TIER_NAMES = ItemTiered.TIER_NAMES;

    private final String name;

    public ItemBlockTiered(Block block, String name) {
        super(block);
        this.name = name;
        this.setHasSubtypes(true);
        this.setRegistryName(name);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void getSubItems(CreativeTabs itemIn, NonNullList<ItemStack> subItems) {
        if (isInCreativeTab(itemIn)) {
            for (int i = 0; i < TIER_NAMES.length; i++) {
                subItems.add(new ItemStack(this, 1, i));
            }
        }
    }

    @Override
    @Nonnull
    public String getTranslationKey(ItemStack itemStack) {
        return "tile." + name + "_tier_" + TIER_NAMES[DefenseTier.clampMeta(itemStack.getItemDamage())];
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }
}
