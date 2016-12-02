package omtteam.ompd.compatability;

import igwmod.gui.GuiWiki;
import igwmod.gui.tabs.BaseWikiTab;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import omtteam.ompd.init.ModBlocks;

import static omtteam.ompd.reference.Reference.MOD_ID;

/**
 * Created by Keridos on 23/01/2015.
 * This Class
 */
class OMPDWikiTab extends BaseWikiTab {
    public OMPDWikiTab() {
        pageEntries.add("ompd:block/wall");
        pageEntries.add("ompd:block/fence");
        pageEntries.add("ompd:block/hardened");
    }

    @Override
    public String getName() {
        return "OpenModularPassiveDefense";
    }

    @Override
    public ItemStack renderTabIcon(GuiWiki gui) {
        return new ItemStack(ModBlocks.wall, 1, 4);
    }

    @Override
    protected String getPageName(String pageEntry) {
        if (pageEntry.startsWith("item") || pageEntry.startsWith("block")) {
            return I18n.format(pageEntry.replace("/", ".").replace("block", "tile") + ".name");
        } else {
            return I18n.format("igwtab.entry." + pageEntry);
        }
    }

    @Override
    protected String getPageLocation(String pageEntry) {
        if (pageEntry.startsWith("item") || pageEntry.startsWith("block")) {
            return MOD_ID + ":" + pageEntry;
        }
        return MOD_ID + ":menu/" + pageEntry;
    }
}
