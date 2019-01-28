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

public class ItemBlockWall extends ItemBlock {
    public ItemBlockWall(Block block) {
        super(block);
        this.setHasSubtypes(true);
        this.setRegistryName(OMPDNames.Blocks.wall);
    }

    private final static String[] subNames = {
            OMPDNames.Blocks.wallTierOne, OMPDNames.Blocks.wallTierTwo, OMPDNames.Blocks.wallTierThree,
            OMPDNames.Blocks.wallTierFour, OMPDNames.Blocks.wallTierFive
    };

    @Override
    @ParametersAreNonnullByDefault
    public void getSubItems(CreativeTabs itemIn, NonNullList<ItemStack> subItems) {
        if (isInCreativeTab(itemIn)) {
            for (int i = 0; i < 5; i++) {
                subItems.add(new ItemStack(ModBlocks.wall, 1, i));
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
    public void addInformation(ItemStack stack, @Nullable World playerIn, List<String> tooltip, ITooltipFlag advanced) {
        if (stack != null) {
            switch (stack.getMetadata()) {
            }
        }
    }
}
