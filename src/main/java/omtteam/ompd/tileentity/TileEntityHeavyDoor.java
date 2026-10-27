package omtteam.ompd.tileentity;

import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import omtteam.ompd.core.HeavyDoorMotion;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * The state of a heavy door, kept in its cell 0: owner and trust list (as every OMPD block), open, powered (last
 * redstone input) and the slide progress, which runs on server and client alike (only open is synced).
 */
public class TileEntityHeavyDoor extends TileEntityPassiveTiered implements ITickable {
    private boolean open;
    private boolean powered;
    private float progress;
    private float prevProgress;

    @Override
    public void update() {
        prevProgress = progress;
        progress = HeavyDoorMotion.advance(progress, open);
    }

    public boolean isOpen() {
        return open;
    }

    /** Opens or closes the door (server side), with sound, and sends it to the clients. */
    public void setOpen(boolean open) {
        if (this.open == open || world == null) {
            return;
        }
        this.open = open;
        markDirty();
        world.playSound(null, pos, open ? SoundEvents.BLOCK_PISTON_EXTEND : SoundEvents.BLOCK_PISTON_CONTRACT,
                        SoundCategory.BLOCKS, 0.5F, world.rand.nextFloat() * 0.15F + 0.6F);
        markBlockForUpdate();
    }

    public boolean isPowered() {
        return powered;
    }

    public void setPowered(boolean powered) {
        this.powered = powered;
        markDirty();
    }

    public float getProgress() {
        return progress;
    }

    /** The progress between the last and the current tick, for rendering. */
    public float getProgress(float partialTicks) {
        return prevProgress + (progress - prevProgress) * partialTicks;
    }

    @Override
    @ParametersAreNonnullByDefault
    public void readFromNBT(NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        open = nbtTagCompound.getBoolean("open");
        powered = nbtTagCompound.getBoolean("powered");
        progress = prevProgress = nbtTagCompound.getFloat("progress");
    }

    /** Updates after the chunk data change open only; the client keeps sliding from where it is. */
    @Override
    @SideOnly(Side.CLIENT)
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        float current = progress;
        float previous = prevProgress;
        super.onDataPacket(net, pkt);
        progress = current;
        prevProgress = previous;
    }

    @Override
    @Nonnull
    @ParametersAreNonnullByDefault
    public NBTTagCompound writeToNBT(NBTTagCompound nbtTagCompound) {
        super.writeToNBT(nbtTagCompound);
        nbtTagCompound.setBoolean("open", open);
        nbtTagCompound.setBoolean("powered", powered);
        nbtTagCompound.setFloat("progress", progress);
        return nbtTagCompound;
    }

    /** The wings slide one block beyond the door on either side. */
    @Override
    @Nonnull
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(pos.add(-2, 0, -2), pos.add(3, 2, 3));
    }
}
