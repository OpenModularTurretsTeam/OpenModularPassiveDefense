package omtteam.ompd.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import omtteam.omlib.tileentity.TileEntityOwnedBlock;

/**
 * Created by Keridos on 28/11/16.
 * This Class
 */
public class TileEntityPassiveOwnedBlock extends TileEntityOwnedBlock {
    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        if (dropBlock) {
            worldObj.destroyBlock(pos, false);
        }
    }
}
