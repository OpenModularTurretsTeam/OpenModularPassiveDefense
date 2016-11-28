package omtteam.ompd.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import omtteam.omlib.tileentity.TileEntityOwnedBlock;

/**
 * Created by Keridos on 28/11/16.
 * This Class
 */
public class TileEntityPassiveOwnedBlock extends TileEntityOwnedBlock {
    protected int tier = 1;

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        this.tier = nbtTagCompound.getInteger("tier");
        if (dropBlock) {
            worldObj.destroyBlock(pos, false);
        }
    }
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbtTagCompound) {
        super.writeToNBT(nbtTagCompound);
        nbtTagCompound.setInteger("tier", this.tier);
        return nbtTagCompound;
    }

    public int getTier() {
        return tier;
    }

    public void setTier(int tier) {
        this.tier = tier;
    }
}
