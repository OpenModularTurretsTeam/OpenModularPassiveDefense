package omtteam.ompd.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import omtteam.omlib.network.OMLibNetworkingHandler;
import omtteam.omlib.network.messages.MessageCamoSettings;
import omtteam.omlib.tileentity.ICamoSupport;
import omtteam.omlib.tileentity.TileEntityOwnedBlock;
import omtteam.omlib.util.camo.CamoSettings;
import omtteam.ompd.reference.OMPDNames;
import omtteam.ompd.reference.Reference;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Created by Keridos on 31/01/17.
 * This Class
 */
public class TileEntityCamo extends TileEntityOwnedBlock implements ICamoSupport {
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
        if (!world.isRemote) {
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
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
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
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
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
    public TileEntityOwnedBlock getOwnedBlock() {
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
}
