package org.halvors.quantum.atomic.common.tile.reactor.fusion;

import net.minecraft.tileentity.TileEntity;
import org.halvors.quantum.api.tile.IElectromagnet;

public class TileElectromagnet extends TileEntity implements IElectromagnet {
    public TileElectromagnet() {

    }

    @Override
    public boolean isRunning() {
        return true;
    }
}