package omtteam.ompd.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import omtteam.omlib.api.block.IHasItemBlock;
import omtteam.ompd.OpenModularPassiveDefense;
import omtteam.ompd.config.ConfigHandler;
import omtteam.ompd.config.DefenseTier;
import omtteam.ompd.tileentity.HeavyDoorMotion;
import omtteam.ompd.item.ItemBlockHeavyDoor;
import omtteam.ompd.reference.OMPDNames;
import omtteam.ompd.reference.Reference;
import omtteam.ompd.tileentity.TileEntityHeavyDoor;
import omtteam.ompd.util.BlockHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

/**
 * The tier 5 heavy door: four cells (2 wide, 2 high, see {@link HeavyDoorMotion}) in the plane across {@link #FACING};
 * column 1 is to the right of column 0 seen along {@code FACING}. Cell 0 holds the {@link TileEntityHeavyDoor}; the
 * wings are drawn by its renderer and collide where they currently are. The owner and trusted players open it by hand,
 * redstone at any cell for everyone. Breaking any cell removes the door and drops it once.
 */
@SuppressWarnings("deprecation")
@ParametersAreNonnullByDefault
public class BlockHeavyDoor extends Block implements IHasItemBlock {
    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    public static final PropertyInteger CELL = PropertyInteger.create("cell", 0, HeavyDoorMotion.CELLS - 1);
    public static final int TIER = DefenseTier.MAX;
    private static final double DEPTH_FROM = 6.0D / 16.0D;
    private static final double DEPTH_TO = 10.0D / 16.0D;

    public BlockHeavyDoor() {
        super(Material.IRON);
        this.setCreativeTab(OpenModularPassiveDefense.modularPassiveDefenseTab);
        this.setSoundType(SoundType.METAL);
        this.setHarvestLevel("pickaxe", 2);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(CELL, 0));
        this.setTranslationKey(OMPDNames.Blocks.heavyDoor);
        this.setRegistryName(Reference.MOD_ID, OMPDNames.Blocks.heavyDoor);
    }

    @Override
    public ItemBlock getItemBlock(Block block) {
        return new ItemBlockHeavyDoor(block);
    }

    @Override
    @Nonnull
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, CELL);
    }

    @Override
    @Nonnull
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(FACING, EnumFacing.byHorizontalIndex(meta & 3)).withProperty(CELL, (meta >> 2) & 3);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getHorizontalIndex() | state.getValue(CELL) << 2;
    }

    /** The position of a cell of the door whose cell 0 is at {@code origin}. */
    public static BlockPos cellPos(BlockPos origin, EnumFacing facing, int cell) {
        return origin.offset(facing.rotateY(), HeavyDoorMotion.column(cell)).up(HeavyDoorMotion.row(cell));
    }

    /** The position of cell 0 of the door this cell belongs to. */
    public static BlockPos origin(IBlockState state, BlockPos pos) {
        int cell = state.getValue(CELL);
        return pos.offset(state.getValue(FACING).rotateY(), -HeavyDoorMotion.column(cell)).down(HeavyDoorMotion.row(cell));
    }

    @Nullable
    private static TileEntityHeavyDoor door(IBlockAccess world, IBlockState state, BlockPos pos) {
        BlockPos origin = origin(state, pos);
        TileEntity tileEntity = world instanceof ChunkCache
                ? ((ChunkCache) world).getTileEntity(origin, Chunk.EnumCreateEntityType.CHECK) : world.getTileEntity(origin);
        return tileEntity instanceof TileEntityHeavyDoor ? (TileEntityHeavyDoor) tileEntity : null;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return state.getValue(CELL) == 0;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return state.getValue(CELL) == 0 ? new TileEntityHeavyDoor() : null;
    }

    @Override
    @Nonnull
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    @Nonnull
    public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    @Nonnull
    public EnumPushReaction getPushReaction(IBlockState state) {
        return EnumPushReaction.BLOCK;
    }

    /** The wing's part of a cell between {@code from} and {@code to} (0 = left, 1 = right, seen along the facing). */
    private static AxisAlignedBB box(EnumFacing facing, double from, double to) {
        EnumFacing right = facing.rotateY();
        double min = right.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE ? from : 1.0D - to;
        double max = right.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE ? to : 1.0D - from;
        return right.getAxis() == EnumFacing.Axis.X
                ? new AxisAlignedBB(min, 0.0D, DEPTH_FROM, max, 1.0D, DEPTH_TO)
                : new AxisAlignedBB(DEPTH_FROM, 0.0D, min, DEPTH_TO, 1.0D, max);
    }

    /** The closed wing, also while open, so the door can be clicked to close it. */
    @Override
    @Nonnull
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        return box(state.getValue(FACING), 0.0D, 1.0D);
    }

    @Override
    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntityHeavyDoor door = door(world, state, pos);
        float[] span = HeavyDoorMotion.wingSpan(HeavyDoorMotion.column(state.getValue(CELL)), door == null ? 0.0F : door.getProgress());
        return span[0] >= span[1] ? NULL_AABB : box(state.getValue(FACING), span[0], span[1]);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
                                    EnumFacing side, float hitX, float hitY, float hitZ) {
        BlockPos origin = origin(state, pos);
        if (BlockHelper.isRemovalClick(player, hand)) {
            return BlockHelper.onBlockActivated(world, origin, player, hand);
        }
        if (world.isRemote) {
            return true;
        }
        TileEntityHeavyDoor door = door(world, state, pos);
        if (door == null) {
            return false;
        }
        if (!BlockHelper.canOpenByHand(world, origin, player)) {
            player.sendStatusMessage(new TextComponentTranslation("status.ownership"), true);
            return true;
        }
        door.setOpen(!door.isOpen());
        return true;
    }

    /** Redstone at any cell: powered opens, unpowered closes (as vanilla doors, on a change of the input only). */
    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {
        TileEntityHeavyDoor door = door(world, state, pos);
        if (world.isRemote || door == null) {
            return;
        }
        BlockPos origin = origin(state, pos);
        EnumFacing facing = state.getValue(FACING);
        boolean powered = false;
        for (int cell = 0; cell < HeavyDoorMotion.CELLS; cell++) {
            powered |= world.isBlockPowered(cellPos(origin, facing, cell));
        }
        if (powered != door.isPowered()) {
            door.setPowered(powered);
            door.setOpen(powered);
        }
    }

    /**
     * Removes the other cells; the first cell to go (all three others still there) drops the door. The cell is already
     * gone from the world when this runs.
     */
    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        if (!world.isRemote) {
            BlockPos origin = origin(state, pos);
            EnumFacing facing = state.getValue(FACING);
            int remaining = 0;
            for (int cell = 0; cell < HeavyDoorMotion.CELLS; cell++) {
                if (isCell(world, cellPos(origin, facing, cell), facing, cell)) {
                    remaining++;
                }
            }
            if (remaining == HeavyDoorMotion.CELLS - 1) {
                spawnAsEntity(world, pos, new ItemStack(this));
            }
            for (int cell = 0; cell < HeavyDoorMotion.CELLS; cell++) {
                BlockPos cellPos = cellPos(origin, facing, cell);
                if (isCell(world, cellPos, facing, cell)) {
                    world.setBlockToAir(cellPos);
                }
            }
        }
        super.breakBlock(world, pos, state);
    }

    private boolean isCell(World world, BlockPos pos, EnumFacing facing, int cell) {
        IBlockState state = world.getBlockState(pos);
        return state.getBlock() == this && state.getValue(FACING) == facing && state.getValue(CELL) == cell;
    }

    @Override
    @Nonnull
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Items.AIR;
    }

    @Override
    @Nonnull
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(this);
    }

    @Override
    public float getBlockHardness(IBlockState state, World world, BlockPos pos) {
        return ConfigHandler.getDefenseStats().hardness(TIER);
    }

    @Override
    public float getExplosionResistance(World world, BlockPos pos, @Nullable Entity exploder, Explosion explosion) {
        return ConfigHandler.getDefenseStats().explosionResistance(TIER);
    }
}
