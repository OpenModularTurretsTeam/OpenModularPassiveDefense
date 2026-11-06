package omtteam.ompd.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TextFormatting;
import omtteam.omlib.api.permission.IOwnedHost;
import omtteam.ompd.reference.OMPDNames;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * The Hwyla and The One Probe overlay of the hardened blocks, walls and fences (1.20.1+ {@code OwnedBlockJadeProvider}):
 * their owner, or that they have none and can be claimed. {@link #data} runs on the server, {@link #lines} turns its
 * result into text. The caller passes the translation: Hwyla translates on the client, The One Probe sends the keys.
 */
public final class OwnerOverlay {
    // not keys the block saves: Hwyla falls back to the client's tile entity data until the server's arrives
    private static final String KNOWN = "overlayKnown";
    private static final String OWNER = "overlayOwner";
    private static final String UNOWNED = "text.omlib.unowned";

    private OwnerOverlay() {
    }

    /** The data the overlay shows, read on the server; empty for a tile entity without owner support. */
    public static NBTTagCompound data(@Nullable TileEntity te) {
        NBTTagCompound data = new NBTTagCompound();
        if (!(te instanceof IOwnedHost)) return data;
        data.setBoolean(KNOWN, true);
        IOwnedHost host = (IOwnedHost) te;
        if (host.getOwnerOrNull() != null) data.setString(OWNER, host.getOwner().getName());
        return data;
    }

    /**
     * The owner line in the colours of OMT's overlays: gold label, white name, or a green "nobody" with a hint to
     * claim the block (it is up for grabs); none if the data is missing.
     */
    public static List<String> lines(NBTTagCompound data, Function<String, String> localize) {
        List<String> lines = new ArrayList<>();
        if (!data.hasKey(KNOWN)) return lines;
        String owner = data.hasKey(OWNER) ? TextFormatting.WHITE + data.getString(OWNER)
                : TextFormatting.GREEN + localize.apply(UNOWNED);
        lines.add(TextFormatting.GOLD + localize.apply(OMPDNames.Localizations.OWNER) + ": " + owner);
        return lines;
    }
}
