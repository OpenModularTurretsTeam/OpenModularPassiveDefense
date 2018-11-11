package omtteam.ompd.init;

import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;
import java.util.List;

public class ModItems {
    public static List<Item> subBlocks = new ArrayList<>();

    public static void init(IForgeRegistry<Item> registry) {
        for (Item item : subBlocks) {
            registry.register(item);
        }
    }
}
