package omtteam.ompd.registry;


import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;
import omtteam.omlib.util.InitHelper;
import omtteam.ompd.OpenModularPassiveDefense;
import omtteam.ompd.item.ItemTiered;
import omtteam.ompd.reference.OMPDNames;
import omtteam.ompd.reference.Reference;

import java.util.ArrayList;
import java.util.List;

public class ModItems {
    public static List<Item> subBlocks = new ArrayList<>();

    /** Crafting base of all plating. */
    public static Item hardenedMix;
    /** Tiered crafting intermediate (metadata 0–4 = tiers 1–5) of the blocks of each tier. */
    public static Item hardenedPlating;

    public static void init(IForgeRegistry<Item> registry) {
        for (Item item : subBlocks) {
            registry.register(item);
        }
        hardenedMix = InitHelper.registerItem(new Item().setTranslationKey(OMPDNames.Items.hardenedMix)
                .setCreativeTab(OpenModularPassiveDefense.modularPassiveDefenseTab)
                .setRegistryName(Reference.MOD_ID, OMPDNames.Items.hardenedMix), registry);
        hardenedPlating = InitHelper.registerItem(new ItemTiered(OMPDNames.Items.hardenedPlating), registry);
    }
}
