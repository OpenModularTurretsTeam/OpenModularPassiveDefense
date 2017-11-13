package omtteam.ompd.handler.recipes;


import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import omtteam.omlib.util.JSONRecipeBuilder;
import omtteam.ompd.handler.ConfigHandler;
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
        fenceTierOne = new ItemStack(ModBlocks.fence, 16, 0);
        fenceTierTwo = new ItemStack(ModBlocks.fence, 16, 1);
        fenceTierThree = new ItemStack(ModBlocks.fence, 16, 2);
        fenceTierFour = new ItemStack(ModBlocks.fence, 16, 3);
        fenceTierFive = new ItemStack(ModBlocks.fence, 16, 4);

        JSONRecipeBuilder.setupDir(ConfigHandler.config);
        //Fences
        JSONRecipeBuilder.addShapedRecipe(fenceTierOne, "ABA", "BAB", "ABA", 'A',
                Blocks.IRON_BARS, 'B',
                Blocks.COBBLESTONE);

        JSONRecipeBuilder.addShapedRecipe(fenceTierTwo, "ABA", "BAB", "ABA", 'A',
                Blocks.IRON_BARS, 'B', Items.IRON_INGOT);

        JSONRecipeBuilder.addShapedRecipe(fenceTierThree ,"ABA", "BAB", "ABA", 'A',
                Blocks.IRON_BARS, 'B', "ingotGold");

        JSONRecipeBuilder.addShapedRecipe(fenceTierFour, "ABA", "BAB", "ABA", 'A',
                Blocks.IRON_BARS, 'B',
                Items.DIAMOND);

        JSONRecipeBuilder.addShapedRecipe(fenceTierFive, "ABA", "BAB", "ABA", 'A',
                Blocks.IRON_BARS, 'B',
                Blocks.OBSIDIAN);

        //Hard Walls
        JSONRecipeBuilder.addShapedRecipe(hardWallTierOne, "ABA", "BCB", "ABA", 'A',
                Blocks.GRAVEL, 'B',
                Blocks.COBBLESTONE, 'C',
                Blocks.SAND);

        JSONRecipeBuilder.addShapedRecipe(hardWallTierTwo, "ABA", "BAB", "ABA", 'A',
                hardWallTierOne, 'B', Blocks.STONE);

        JSONRecipeBuilder.addShapedRecipe(hardWallTierThree, "ABA", "BAB", "ABA", 'A',
                        hardWallTierTwo, 'B', Blocks.BRICK_BLOCK);

        JSONRecipeBuilder.addShapedRecipe(hardWallTierFour, "ABA", "BAB", "ABA", 'A',
                hardWallTierThree, 'B',
                Blocks.NETHER_BRICK);

        JSONRecipeBuilder.addShapedRecipe(hardWallTierFive, "ABA", "BAB", "ABA", 'A',
                hardWallTierFour, 'B', Blocks.OBSIDIAN);

        for (int i= 0; i<5 ;i++) {
            JSONRecipeBuilder.addShapedRecipe(new ItemStack(ModBlocks.wall,6,i), "   ", "AAA", "AAA", 'A', new ItemStack( ModBlocks.hardened,1,i));
        }

        JSONRecipeBuilder.generateConstants();
    }
}
