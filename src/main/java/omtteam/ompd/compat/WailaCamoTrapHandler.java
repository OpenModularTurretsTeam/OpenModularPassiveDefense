package omtteam.ompd.compat;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaRegistrar;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import omtteam.omlib.compat.hwyla.IOMLibWailaDataProvider;
import omtteam.ompd.tileentity.TileEntityCamo;

import javax.annotation.Nonnull;

/**
 * Hwyla overlay of the camouflage trap (1.20.1+ Jade, as the trap's The One Probe info): players who may not manage
 * the trap see its disguise. Registered through OMLib's Hwyla plugin ({@link ModCompatibility}).
 */
public class WailaCamoTrapHandler implements IOMLibWailaDataProvider {
    // not a key the trap saves: Hwyla falls back to the client's tile entity data until the server's arrives
    private static final String SEE_THROUGH = "overlaySeeThrough";

    @Override
    public void callbackRegister(IWailaRegistrar register) {
        register.registerNBTProvider(this, TileEntityCamo.class);
        register.registerStackProvider(this, TileEntityCamo.class);
    }

    /** The disguise, unless the server said the player may see through it (so also while no data has arrived yet). */
    @Nonnull
    @Override
    public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
        TileEntity te = accessor.getTileEntity();
        if (!(te instanceof TileEntityCamo) || accessor.getNBTData().getBoolean(SEE_THROUGH)) return ItemStack.EMPTY;
        IBlockState camo = ((TileEntityCamo) te).getCamoState();
        if (camo.getBlock() == accessor.getBlock()) return ItemStack.EMPTY;
        return camo.getBlock().getPickBlock(camo, accessor.getMOP(), accessor.getWorld(), accessor.getPosition(),
                accessor.getPlayer());
    }

    @Nonnull
    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, BlockPos pos) {
        if (te instanceof TileEntityCamo) {
            tag.setBoolean(SEE_THROUGH, ((TileEntityCamo) te).canSetCamo(player));
        }
        return tag;
    }
}
