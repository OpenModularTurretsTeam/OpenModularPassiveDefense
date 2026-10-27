package omtteam.ompd.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.UsernameCache;
import omtteam.omlib.core.security.PlayerIdentity;
import omtteam.omlib.tileentity.TileEntityOwnedBlock;
import omtteam.omlib.util.player.Player;
import omtteam.omlib.util.player.PlayerUtil;
import omtteam.ompd.core.DefenseTier;
import omtteam.ompd.core.FenceRules;
import omtteam.ompd.core.PassageRules;
import omtteam.ompd.tileentity.TileEntityPassiveTiered;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

import static omtteam.omlib.util.player.PlayerUtil.isPlayerOwner;

/**
 * Created by Keridos on 28/11/16.
 * This Class
 */
public class BlockHelper {

    /**
     * The tier (1–5) of a block that keeps it in its tile entity, tier 1 while there is none. Safe on the chunk render
     * thread (does not create tile entities there).
     */
    public static int tileEntityTier(IBlockAccess world, BlockPos pos) {
        TileEntity tileEntity = world instanceof ChunkCache
                ? ((ChunkCache) world).getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK) : world.getTileEntity(pos);
        return tileEntity instanceof TileEntityPassiveTiered
                ? ((TileEntityPassiveTiered) tileEntity).getDefenseTier() : DefenseTier.MIN;
    }

    /** Owner (the placing player, if any) and tier of one placed part of a door or gate, sent to the clients. */
    public static void initPlacedPart(World world, BlockPos pos, @Nullable EntityPlayer player, int meta) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (!world.isRemote && tileEntity instanceof TileEntityPassiveTiered) {
            if (player != null) {
                ((TileEntityPassiveTiered) tileEntity).setOwner(new Player(player));
            }
            ((TileEntityPassiveTiered) tileEntity).setTier(DefenseTier.clampMeta(meta));
            ((TileEntityPassiveTiered) tileEntity).markBlockForUpdate();
        }
    }

    /** Whether the player may open the door or gate at {@code pos} by hand, see {@link PassageRules}. */
    public static boolean canOpenByHand(World world, BlockPos pos, EntityPlayer player) {
        TileEntity tileEntity = world.getTileEntity(pos);
        return tileEntity instanceof TileEntityPassiveTiered && PassageRules.canOpenByHand(
                ((TileEntityPassiveTiered) tileEntity).getTrustManager().core(), PlayerUtil.getPlayerInfo(player).identity());
    }

    /**
     * Whether the fence or spikes at {@code pos} hurt the entity, see {@link FenceRules}; blocks without an OMPD tile
     * entity hurt everything.
     */
    public static boolean hurts(World world, BlockPos pos, Entity entity) {
        TileEntity tileEntity = world.getTileEntity(pos);
        return !(tileEntity instanceof TileEntityPassiveTiered) || FenceRules.hurts(
                ((TileEntityPassiveTiered) tileEntity).getTrustManager().core(), player(entity), petOwner(entity));
    }

    private static Optional<PlayerIdentity> player(Entity entity) {
        return entity instanceof EntityPlayer
                ? Optional.of(PlayerUtil.getPlayerInfo((EntityPlayer) entity).identity()) : Optional.empty();
    }

    private static Optional<PlayerIdentity> petOwner(Entity entity) {
        if (!(entity instanceof IEntityOwnable)) {
            return Optional.empty();
        }
        UUID owner = ((IEntityOwnable) entity).getOwnerId();
        if (owner == null) {
            return Optional.empty();
        }
        String name = UsernameCache.getLastKnownUsername(owner);
        return Optional.of(new PlayerIdentity(owner, name == null ? "" : name));
    }

    /** Owner removal (sneak + empty main hand, see {@link #onBlockActivated}) applies to this click. */
    public static boolean isRemovalClick(EntityPlayer player, EnumHand hand) {
        return player.isSneaking() && hand == EnumHand.MAIN_HAND && player.getHeldItemMainhand().isEmpty();
    }

    public static void onBlockPlacedByTiered(World worldIn, BlockPos pos, EntityLivingBase placer, ItemStack stack) {
        if (!worldIn.isRemote && worldIn.getTileEntity(pos) instanceof TileEntityPassiveTiered) {
            onBlockPlacedBy(worldIn, pos, placer);
            // hardness and resistance come from the block state (DefenseTier)
            ((TileEntityPassiveTiered) worldIn.getTileEntity(pos)).setTier(stack.getItemDamage());
        }
    }

    /** Makes the placing player the owner; blocks placed by other entities (deployers, other mods) stay unowned. */
    public static void onBlockPlacedBy(World worldIn, BlockPos pos, EntityLivingBase placer) {
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (placer instanceof EntityPlayer && tileEntity instanceof TileEntityOwnedBlock) {
            ((TileEntityOwnedBlock) tileEntity).setOwner(new Player((EntityPlayer) placer));
        }
    }

    public static boolean onBlockActivated(World world, BlockPos pos, EntityPlayer player, EnumHand hand) {
        if (!world.isRemote && player.isSneaking() && (hand == EnumHand.MAIN_HAND && player.getHeldItemMainhand().isEmpty())) {
            TileEntity tileEntity = world.getTileEntity(pos);
            if (!(tileEntity instanceof TileEntityPassiveTiered) || isPlayerOwner(player, (TileEntityPassiveTiered) tileEntity)) {
                world.destroyBlock(pos, true);
            } else {
                player.sendMessage(new TextComponentTranslation("status.ownership"));
            }
            return true;
        } else return world.isRemote;
    }
}
