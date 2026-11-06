package omtteam.ompd.compat;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaRegistrar;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import omtteam.omlib.compat.hwyla.IOMLibWailaDataProvider;
import omtteam.omlib.util.GeneralUtil;
import omtteam.ompd.tileentity.OwnerOverlay;
import omtteam.ompd.tileentity.TileEntityPassiveTiered;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Hwyla overlay of the hardened blocks, walls and fences: the owner line of {@link OwnerOverlay}. Registered through
 * OMLib's Hwyla plugin ({@link ModCompatibility}).
 */
public class WailaTileHandler implements IOMLibWailaDataProvider {
    @Override
    public void callbackRegister(IWailaRegistrar register) {
        register.registerNBTProvider(this, TileEntityPassiveTiered.class);
        register.registerBodyProvider(this, TileEntityPassiveTiered.class);
    }

    @Nonnull
    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        currenttip.addAll(OwnerOverlay.lines(accessor.getNBTData(), GeneralUtil::safeLocalize));
        return currenttip;
    }

    @Nonnull
    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, BlockPos pos) {
        tag.merge(OwnerOverlay.data(te));
        return tag;
    }
}
