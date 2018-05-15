package org.halvors.nuclearphysics.common.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import org.halvors.nuclearphysics.api.BlockPos;

import java.util.HashSet;
import java.util.Set;

public class TileBase extends TileEntity {
    private final Set<EntityPlayer> playersUsing = new HashSet<>();

    protected final BlockPos pos;

    public void open(final EntityPlayer player) {
        playersUsing.add(player);
    }

    public void close(final EntityPlayer player) {
        playersUsing.remove(player);
    }

    public Set<EntityPlayer> getPlayersUsing() {
        return playersUsing;
    }

    public TileBase() {
        this.pos = new BlockPos(xCoord, yCoord, zCoord);
    }

    public BlockPos getPos() {
        return pos;
    }
}
