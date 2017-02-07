package omtteam.ompd.tileentity;

import com.sun.istack.internal.NotNull;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import omtteam.omlib.tileentity.ICamoSupport;
import omtteam.omlib.tileentity.TileEntityOwnedBlock;
import omtteam.ompd.reference.OMPDNames;
import omtteam.ompd.reference.Reference;

import javax.annotation.Nonnull;

import static omtteam.omlib.util.BlockUtil.getBlockStateFromNBT;
import static omtteam.omlib.util.BlockUtil.writeBlockFromStateToNBT;

/**
 * Created by Keridos on 31/01/17.
 * This Class
 */
public class TileEntityCamo extends TileEntityOwnedBlock implements ICamoSupport {
    @NotNull
    private IBlockState camoBlockState;

    public TileEntityCamo(IBlockState camoBlockState) {
        this.camoBlockState = camoBlockState;
    }

    @Nonnull
    @Override
    public IBlockState getCamoState() {
        return camoBlockState;
    }

    @Override
    public void setCamoState(IBlockState state) {
        this.camoBlockState = state;
    }

    @Override
    public IBlockState getDefaultCamoState() {
        return ForgeRegistries.BLOCKS.getValue(
                new ResourceLocation(Reference.MOD_ID + ":" + OMPDNames.Blocks.camoTrap)).getDefaultState();
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        if (getBlockStateFromNBT(nbtTagCompound) != null) {
            this.camoBlockState = getBlockStateFromNBT(nbtTagCompound);
        } else {
            this.camoBlockState = getDefaultCamoState();
        }
        if (dropBlock) {
            worldObj.destroyBlock(pos, false);
        }
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbtTagCompound) {
        super.writeToNBT(nbtTagCompound);
        writeBlockFromStateToNBT(nbtTagCompound, this.camoBlockState);
        return nbtTagCompound;
    }
}
