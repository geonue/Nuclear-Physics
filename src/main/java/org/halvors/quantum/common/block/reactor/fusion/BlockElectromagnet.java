package org.halvors.quantum.common.block.reactor.fusion;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.quantum.common.Quantum;
import org.halvors.quantum.common.block.BlockContainerQuantum;
import org.halvors.quantum.common.block.machine.BlockMachine;
import org.halvors.quantum.common.block.states.BlockStateElectromagnet;
import org.halvors.quantum.common.tile.reactor.fusion.TileElectromagnet;

import javax.annotation.Nonnull;
import java.util.List;

public class BlockElectromagnet extends BlockContainerQuantum {
    public BlockElectromagnet() {
        super("electromagnet", Material.IRON);

        setResistance(20);
        //setDefaultState(blockState.getBaseState().withProperty(type, EnumElectromagnet.NORMAL));
    }

    @Override
    public void registerItemModel(ItemBlock itemBlock) {
        for (EnumElectromagnet type : EnumElectromagnet.values()) {
            Quantum.getProxy().registerItemRenderer(itemBlock, type.ordinal(), name, "type=" + type.getName());
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, EnumFacing side) {
        IBlockState neighborBlockState = world.getBlockState(pos.offset(side));
        Block neighborBlock = neighborBlockState.getBlock();

        if (neighborBlock == this) {
            EnumElectromagnet neighborBlockType = neighborBlockState.getValue(BlockStateElectromagnet.typeProperty);

            if (neighborBlockType == EnumElectromagnet.GLASS) {
                return false;
            }
        }

        return super.shouldSideBeRendered(state, world, pos, side);
    }

    @SuppressWarnings("deprecation")
    @Override
    @SideOnly(Side.CLIENT)
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    @SideOnly(Side.CLIENT)
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    @Nonnull
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    @Nonnull
    public BlockStateContainer createBlockState() {
        return new BlockStateElectromagnet(this);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(BlockStateElectromagnet.typeProperty).ordinal();
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity, ItemStack itemStack) {
        world.setBlockState(pos, state.withProperty(BlockStateElectromagnet.typeProperty, EnumElectromagnet.values()[itemStack.getItemDamage()]), 2);
    }

    @Override
    public int damageDropped(IBlockState state) {
        return getMetaFromState(state);
    }

    @Override
    public void getSubBlocks(@Nonnull Item item, CreativeTabs creativeTabs, List<ItemStack> list) {
        for (EnumElectromagnet type : EnumElectromagnet.values()) {
            list.add(new ItemStack(item, 1, type.ordinal()));
        }
    }

    /*
    @Override
    @SideOnly(Side.CLIENT)
    public ISimpleBlockRenderer getRenderer() {
        return new ConnectedTextureRenderer(this, Reference.PREFIX + "atomic_edge");
    }

    @Override
    public boolean canRenderInLayer(BlockRenderLayer layer) {
        //return layer == BlockRenderLayer.SOLID || layer == BlockRenderLayer.TRANSLUCENT;
        return true;
    }
    */

    @Override
    @Nonnull
    public TileEntity createNewTileEntity(@Nonnull World world, int metadata) {
        return new TileElectromagnet();
    }

    public enum EnumElectromagnet implements IStringSerializable {
        NORMAL("normal"),
        GLASS("glass");

        private String name;

        EnumElectromagnet(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name.toLowerCase();
        }
    }
}