package org.halvors.quantum.common.tile.machine.old;

import org.halvors.quantum.Quantum;
import org.halvors.quantum.common.ConfigurationManager.General;
import org.halvors.quantum.common.base.MachineType;
import org.halvors.quantum.common.tile.TileRotatable;
import org.halvors.quantum.common.utility.location.Location;

public class TileEntityMachine extends TileRotatable {
	private final MachineType machineType;

	protected TileEntityMachine(MachineType machineType) {
		this.machineType = machineType;
	}

    @Override
    public void updateEntity() {
        // Remove disabled blocks.
        if (!worldObj.isRemote && General.destroyDisabledBlocks) {
            MachineType machineType = MachineType.getType(getBlockType(), getBlockMetadata());

            if (machineType != null && !machineType.isEnabled()) {
                Quantum.getLogger().info("Destroying machine of type '" + machineType.getLocalizedName() + "' at " + new Location(this) + " as according to configuration.");
                worldObj.setBlockToAir(xCoord, yCoord, zCoord);
            }
        }
    }

	public MachineType getMachineType() {
		return machineType;
	}
}
