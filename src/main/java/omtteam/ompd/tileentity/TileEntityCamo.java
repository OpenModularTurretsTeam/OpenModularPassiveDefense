package omtteam.ompd.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import omtteam.omlib.api.permission.TrustedPlayersManager;
import omtteam.omlib.api.render.camo.ICamoSupport;
import omtteam.omlib.api.tile.IHasTrustManager;
import omtteam.omlib.core.camo.CamoPermissions;
import omtteam.omlib.core.security.AccessProfile;
import omtteam.omlib.network.MessageCamoSettings;
import omtteam.omlib.network.OMLibNetworkingHandler;
import omtteam.omlib.tileentity.TileEntityOwnedBlock;
import omtteam.omlib.util.camo.CamoSettings;
import omtteam.omlib.util.player.OMLibSecurityEnvironment;
import omtteam.omlib.util.player.Player;
import omtteam.omlib.util.player.PlayerUtil;
import omtteam.ompd.reference.OMPDNames;
import omtteam.ompd.reference.Reference;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Created by Keridos on 31/01/17.
 * This Class
 */
public class TileEntityCamo extends TileEntityOwnedBlock implements ICamoSupport, IHasTrustManager {
    /**
     * Trust list with two levels ({@link AccessProfile#TRUSTED_AND_ADMIN}, as on 1.20.1+): trusted players can stand on
     * the trap, admins (the owner always is one) also change its camouflage and manage the list. 3.x traps have none.
     */
    private final TrustedPlayersManager trustManager = new TrustedPlayersManager(null, this,
            OMLibSecurityEnvironment.INSTANCE, AccessProfile.TRUSTED_AND_ADMIN);
    private CamoSettings camoSettings;
    private IBlockState camoBlockStateTemp;

    public TileEntityCamo() {
        this.camoSettings = new CamoSettings();
        this.camoSettings.setCamoBlockState(this.getDefaultCamoState());
    }

    public TileEntityCamo(IBlockState camoBlockState) {
        this.camoSettings = new CamoSettings();
        this.camoSettings.setCamoBlockState(camoBlockState);
    }

    @Nonnull
    @Override
    public IBlockState getCamoState() {
        return this.getCamoSettings().getCamoBlockState() != null
                && this.getCamoSettings().getCamoBlockState() instanceof IExtendedBlockState
                ? (IExtendedBlockState) this.getCamoSettings().getCamoBlockState()
                : this.getCamoSettings().getCamoBlockState() != null
                ? this.getCamoSettings().getCamoBlockState().getBlock()
                .getExtendedState(this.getCamoSettings().getCamoBlockState(), this.getWorld(), this.getPos())
                : this.getDefaultCamoState();
    }

    @Override
    public void setCamoState(IBlockState state) {
        if (!(state instanceof IExtendedBlockState)) {
            if (this.getCamoState() == state.getBlock().getExtendedState(state, this.getWorld(), this.getPos())) {
                return;
            }
            this.getCamoSettings().setCamoBlockState(state.getBlock().getExtendedState(state, this.getWorld(), this.getPos()));
        } else {
            if (this.getCamoState() == state) {
                return;
            }
            this.getCamoSettings().setCamoBlockState(state);
        }
        this.camoBlockStateTemp = state;
        if (world.isRemote) {
            remesh();
        } else {
            OMLibNetworkingHandler.INSTANCE.sendToAllAround(new MessageCamoSettings(this),
                                                            new NetworkRegistry.TargetPoint(this.getWorld().provider.getDimension(), this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(), 160));
            this.markBlockForUpdate();
        }
    }

    @Nonnull
    @Override
    public IBlockState getDefaultCamoState() {
        return ForgeRegistries.BLOCKS.getValue(
                new ResourceLocation(Reference.MOD_ID + ":" + OMPDNames.Blocks.camoTrap)).getDefaultState();
    }

    @Override
    @ParametersAreNonnullByDefault
    public void loadFromNBT(NBTTagCompound tag) {
        super.loadFromNBT(tag);
        trustManager.readFromNBT(tag);
        trustManager.setOwner(getOwnerOrNull());
        this.camoSettings = CamoSettings.getSettingsFromNBT(tag);
        if (camoSettings.getCamoBlockState() != null) {
            this.camoBlockStateTemp = camoSettings.getCamoBlockState();
        } else {
            this.camoBlockStateTemp = getDefaultCamoState();
        }
    }

    @Nonnull
    @Override
    @ParametersAreNonnullByDefault
    public NBTTagCompound saveToNBT(NBTTagCompound tag) {
        super.saveToNBT(tag);
        trustManager.writeToNBT(tag);
        camoSettings.writeNBT(tag);
        return tag;
    }

    @Nonnull
    @Override
    public CamoSettings getCamoSettings() {
        return camoSettings;
    }

    @Nonnull
    @Override
    public TileEntity getCamoTileEntity() {
        return this;
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (camoBlockStateTemp instanceof IExtendedBlockState) {
            this.camoSettings.setCamoBlockState(camoBlockStateTemp);
        } else if (camoBlockStateTemp != null) {
            this.setCamoState(camoBlockStateTemp.getBlock().getExtendedState(camoBlockStateTemp, this.getWorld(), this.getPos()));
        } else {
            this.setCamoState(this.getDefaultCamoState());
        }
        this.updateNBT = true;
    }

    @Override
    public boolean isCamoEnabled() {
        return true;
    }

    @Override
    public boolean supportsLight() {
        return true;
    }

    @Override
    public boolean canSetCamo(EntityPlayer player) {
        return CamoPermissions.canManageTrap(trustManager.core(), PlayerUtil.getPlayerInfo(player));
    }

    @Override
    public boolean canChangeLight(EntityPlayer player) {
        return CamoPermissions.canManageTrap(trustManager.core(), PlayerUtil.getPlayerInfo(player));
    }

    /** Whether the player may stand on the trap: the owner, trusted players and the owner's team. */
    public boolean isTrusted(EntityPlayer player) {
        return trustManager.core().isOwnerOrTrusted(PlayerUtil.getPlayerInfo(player).identity());
    }

    @Override
    public TrustedPlayersManager getTrustManager() {
        return trustManager;
    }

    @Override
    public void setOwner(Player owner) {
        trustManager.setOwner(owner);
        super.setOwner(owner);
    }

    /** Disowning: the trust list goes too, and anyone may claim the trap. */
    @Override
    public void clearOwner() {
        trustManager.clearOwner();
        super.clearOwner();
    }

    /** The trust list changed: save it and send it to the clients (they need it for the collision of the trap). */
    @Override
    public void informUpdate() {
        markDirty();
        if (world != null && !world.isRemote) {
            markBlockForUpdate();
        }
    }
}
