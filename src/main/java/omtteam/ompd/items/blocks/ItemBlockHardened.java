package omtteam.ompd.items.blocks;

import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import omtteam.ompd.init.ModBlocks;
import omtteam.ompd.reference.OMPDNames;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.ParametersAreNullableByDefault;
import java.util.List;

public class ItemBlockHardened extends ItemBlock {
    public ItemBlockHardened(Block block) {
        super(block);
        this.setHasSubtypes(true);
        this.setRegistryName(OMPDNames.Blocks.hardened);
    }

    private final static String[] subNames = {
            OMPDNames.Blocks.hardenedTierOne, OMPDNames.Blocks.hardenedTierTwo, OMPDNames.Blocks.hardenedTierThree,
            OMPDNames.Blocks.hardenedTierFour, OMPDNames.Blocks.hardenedTierFive
    };

    @Override
    @ParametersAreNonnullByDefault
    public void getSubItems(CreativeTabs itemIn, NonNullList<ItemStack> subItems) {
        if(isInCreativeTab(itemIn)) {
            for (int i = 0; i < 5; i++) {
                subItems.add(new ItemStack(ModBlocks.hardened, 1, i));
            }
        }
    }

    @Override
    @Nonnull
    public String getUnlocalizedName(ItemStack itemStack) {
        return "tile." + subNames[itemStack.getItemDamage()];
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }

    @Override
    @ParametersAreNullableByDefault
    public void addInformation(ItemStack stack, @Nullable World playerIn, List<String> tooltip, ITooltipFlag advanced) {        if (stack != null) {
            switch (stack.getMetadata()) {
            }
        }
    }
}
