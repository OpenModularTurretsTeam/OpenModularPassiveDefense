package omtteam.ompd.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import omtteam.omlib.blocks.BlockAbstractTileEntity;
import omtteam.omlib.tileentity.TileEntityOwnedBlock;
import omtteam.ompd.OpenModularPassiveDefence;
import omtteam.ompd.handler.ConfigHandler;
import omtteam.ompd.init.ModBlocks;
import omtteam.ompd.tileentity.TileEntityPassiveOwnedBlock;

import javax.annotation.Nullable;
import java.util.List;

public class BlockHardWall extends BlockAbstractTileEntity {
    public static final PropertyInteger TIER = PropertyInteger.create("tier", 1, 5);

    public BlockHardWall() {
        super(Material.ROCK);
        this.setCreativeTab(OpenModularPassiveDefence.modularPassiveDefenceTab);
        if (!ConfigHandler.turretBreakable) {
            this.setBlockUnbreakable();
        }
        this.setSoundType(SoundType.STONE);
        this.setHarvestLevel("pickaxe", 2);
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityPassiveOwnedBlock();
    }

    @Override
    public boolean isOpaqueCube(IBlockState blockState) {
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(TIER, meta + 1);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(TIER) - 1;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, TIER);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote && !player.isSneaking() && (hand == EnumHand.MAIN_HAND && player.getHeldItemMainhand() == null)) {
            TileEntityOwnedBlock base = (TileEntityOwnedBlock) world.getTileEntity(pos);
            if (player.getUniqueID().toString().equals(base.getOwner())) {
                world.destroyBlock(base.getPos(), true);
            } else {
                player.addChatMessage(new TextComponentString(I18n.translateToLocal("status.ownership")));
            }
        }
        return true;
    }


    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        if (!worldIn.isRemote && worldIn.getTileEntity(pos) instanceof TileEntityOwnedBlock) {
            EntityPlayerMP player = (EntityPlayerMP) placer;
            TileEntityOwnedBlock base = (TileEntityOwnedBlock) worldIn.getTileEntity(pos);
            base.setOwner(player.getUniqueID().toString());
            switch (state.getValue(TIER)) {
                case 1:
                    this.setResistance(10.0F);
                    this.setHardness(10.0F);
                    break;
                case 2:
                    this.setResistance(20.0F);
                    this.setHardness(20.0F);
                    break;
                case 3:
                    this.setResistance(30.0F);
                    this.setHardness(30.0F);
                    break;
                case 4:
                    this.setResistance(50.0F);
                    this.setHardness(50.0F);
                    break;
                case 5:
                    this.setResistance(80.0F);
                    this.setHardness(80.0F);
            }
        }
    }

    @Override
    public int damageDropped(IBlockState state) {
        return state.getValue(TIER) - 1;
    }

    @Override
    @SideOnly(Side.CLIENT)
    @SuppressWarnings("unchecked")
    public void getSubBlocks(Item item, CreativeTabs tab, List subItems) {
        for (int i = 0; i < 5; i++) {
            subItems.add(new ItemStack(ModBlocks.hardWall, 1, i));
        }
    }
}
