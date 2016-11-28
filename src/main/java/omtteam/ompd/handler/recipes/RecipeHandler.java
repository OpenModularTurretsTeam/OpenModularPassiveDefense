package omtteam.ompd.handler.recipes;


import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import omtteam.ompd.init.ModBlocks;

public class RecipeHandler {
    public static ItemStack hardWallTierOne;
    public static ItemStack hardWallTierTwo;
    public static ItemStack hardWallTierThree;
    public static ItemStack hardWallTierFour;
    public static ItemStack hardWallTierFive;
    public static ItemStack fenceTierOne;
    public static ItemStack fenceTierTwo;
    public static ItemStack fenceTierThree;
    public static ItemStack fenceTierFour;
    public static ItemStack fenceTierFive;



    public static void initRecipes() {
        hardWallTierOne = new ItemStack(ModBlocks.hardened, 16, 0);
        hardWallTierTwo = new ItemStack(ModBlocks.hardened, 16, 1);
        hardWallTierThree = new ItemStack(ModBlocks.hardened, 16, 2);
        hardWallTierFour = new ItemStack(ModBlocks.hardened, 16, 3);
        hardWallTierFive = new ItemStack(ModBlocks.hardened, 16, 4);
        fenceTierOne = new ItemStack(ModBlocks.fence, 16, 5);
        fenceTierTwo = new ItemStack(ModBlocks.fence, 16, 6);
        fenceTierThree = new ItemStack(ModBlocks.fence, 16, 7);
        fenceTierFour = new ItemStack(ModBlocks.fence, 16, 8);
        fenceTierFive = new ItemStack(ModBlocks.fence, 16, 9);

        // Recipes



        //Fences
        GameRegistry.addRecipe(new ShapedOreRecipe(fenceTierOne, "ABA", "BAB", "ABA", 'A',
                Blocks.IRON_BARS, 'B',
                Blocks.COBBLESTONE));

        GameRegistry.addRecipe(new ShapedOreRecipe(fenceTierTwo, "ABA", "BAB", "ABA", 'A',
                Blocks.IRON_BARS, 'B', Items.IRON_INGOT));

        GameRegistry.addRecipe(new ShapedOreRecipe(fenceTierThree ,"ABA", "BAB", "ABA", 'A',
                Blocks.IRON_BARS, 'B', "ingotGold"));

        GameRegistry.addRecipe(new ShapedOreRecipe(fenceTierFour, "ABA", "BAB", "ABA", 'A',
                Blocks.IRON_BARS, 'B',
                Items.DIAMOND));

        GameRegistry.addRecipe(new ShapedOreRecipe(fenceTierFive, "ABA", "BAB", "ABA", 'A',
                Blocks.IRON_BARS, 'B',
                Blocks.OBSIDIAN));

        //Hard Walls
        GameRegistry.addRecipe(new ShapedOreRecipe(hardWallTierOne, "ABA", "BCB", "ABA", 'A',
                Blocks.GRAVEL, 'B',
                Blocks.COBBLESTONE, 'C',
                Blocks.SAND));

        GameRegistry.addRecipe(new ShapedOreRecipe(hardWallTierTwo, "ABA", "BAB", "ABA", 'A',
                hardWallTierOne, 'B', Blocks.STONE));

        GameRegistry.addRecipe(
                new ShapedOreRecipe(hardWallTierThree, "ABA", "BAB", "ABA", 'A',
                        hardWallTierTwo, 'B', Blocks.BRICK_BLOCK));

        GameRegistry.addRecipe(new ShapedOreRecipe(hardWallTierFour, "ABA", "BAB", "ABA", 'A',
                hardWallTierThree, 'B',
                Blocks.NETHER_BRICK));

        GameRegistry.addRecipe(new ShapedOreRecipe(hardWallTierFive, "ABA", "BAB", "ABA", 'A',
                hardWallTierFour, 'B', Blocks.OBSIDIAN));

    }
}
