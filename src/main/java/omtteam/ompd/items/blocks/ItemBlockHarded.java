package omtteam.ompd.items.blocks;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import omtteam.ompd.init.ModBlocks;
import omtteam.ompd.reference.Names;

import java.util.List;

public class ItemBlockHarded extends ItemBlock {
    public ItemBlockHarded(Block block) {
        super(block);
        setHasSubtypes(true);
    }

    public final static String[] subNames = {
            Names.Blocks.hardenedTierOne, Names.Blocks.hardenedTierTwo,Names.Blocks.hardenedTierThree,
            Names.Blocks.hardenedTierFour,Names.Blocks.hardenedTierFive
    };

    @Override
    public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
        for (int i = 0; i < 5; i++) {
            subItems.add(new ItemStack(ModBlocks.hardened, 1, i));
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        return "tile." + subNames[itemStack.getItemDamage()];
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List tooltip, boolean advanced) {
        switch (stack.getMetadata()) {

        }
    }
}
