package org.halvors.nuclearphysics.common.block.particle;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.halvors.nuclearphysics.common.block.BlockConnectedTexture;
import org.halvors.nuclearphysics.common.tile.particle.TileFulmination;

import javax.annotation.Nonnull;

public class BlockFulmination extends BlockConnectedTexture {
    public BlockFulmination() {
        super("fulmination", Material.IRON);

        setHardness(10);
        setResistance(25000);
    }

    @Override
    @Nonnull
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        return new TileFulmination();
    }
}