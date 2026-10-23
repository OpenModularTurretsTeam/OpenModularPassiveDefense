package omtteam.ompd.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import omtteam.omlib.api.permission.TrustedPlayersManager;
import omtteam.omlib.api.tile.IHasTrustManager;
import omtteam.omlib.tileentity.TileEntityOwnedBlock;
import omtteam.omlib.util.player.Player;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Created by Keridos on 28/11/16.
 * This is the base for most of OMPDs passive blocks
 */
public class TileEntityPassiveTiered extends TileEntityOwnedBlock implements IHasTrustManager {
    protected int tier;
    protected TrustedPlayersManager tpm;

    public TileEntityPassiveTiered() {
        this.tpm = new TrustedPlayersManager(this.getOwner(), this);
    }

    @Override
    public TrustedPlayersManager getTrustManager() {
        return tpm;
    }

    @Override
    @ParametersAreNonnullByDefault
    public void readFromNBT(NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        this.tier = nbtTagCompound.getInteger("tier");
        this.tpm.readFromNBT(nbtTagCompound);
        this.tpm.setOwner(getOwner());
    }

    @Override
    @Nonnull
    @ParametersAreNonnullByDefault
    public NBTTagCompound writeToNBT(NBTTagCompound nbtTagCompound) {
        super.writeToNBT(nbtTagCompound);
        nbtTagCompound.setInteger("tier", this.tier);
        tpm.writeToNBT(nbtTagCompound);
        return nbtTagCompound;
    }

    public int getTier() {
        return tier;
    }

    public void setTier(int tier) {
        this.tier = tier;
    }

    /** The trust list needs the owner for its rules (fence contact, see {@code FenceRules}). */
    @Override
    public void setOwner(Player owner) {
        tpm.setOwner(owner);
        super.setOwner(owner);
    }

    @Override
    public void informUpdate() {
        // TODO: implement Message for Sync of trusted players in omlib
    }
}
