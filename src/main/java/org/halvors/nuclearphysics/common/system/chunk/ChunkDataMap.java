package org.halvors.nuclearphysics.common.system.chunk;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import org.halvors.nuclearphysics.common.NuclearPhysics;

import java.util.HashMap;

public class ChunkDataMap {
    public static final String NBT_CHUNK_DATA = "temperatureData";

    private final HashMap<Long, ChunkData> loadedChunks = new HashMap<>();
    private final IBlockAccess world;
    //private final boolean isMaterialMap;

    public ChunkDataMap(final IBlockAccess world) {
        this.world = world;
        //this.isMaterialMap = isMaterialMap;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public int getData(int x, int y, int z) {
        ChunkData chunk = getChunkFromPosition(x, z, false);

        if (chunk != null) {
            return chunk.getValue(x & 15, y, z & 15);
        }

        return 0;
    }

    public int getData(BlockPos pos) {
        return getData(pos.getX(), pos.getY(), pos.getZ());
    }

    public boolean setData(int x, int y, int z, int value) {
        // TODO: Remove debugging message.
        NuclearPhysics.getLogger().info("Setting ChunkDataMap with value: " + value);

        final ChunkData chunk = getChunkFromPosition(x, z, value > 0);

        if (chunk != null) {
            // Fire change event for modification and to trigger exposure map update
            /*
            if (isMaterialMap) {
                int prev_value = getData(x, y, z);

                RadiationMapEvent.UpdateRadiationMaterial event = new RadiationMapEvent.UpdateRadiationMaterial(this, x, y, z, prev_value, amount);

                if (MinecraftForge.EVENT_BUS.post(event)) {
                    return false;
                }

                amount = event.new_value;
            }
            */

            ////////////////////////////////////////////////////////////////////////////////////////////////////////////

            /*
            protected void updateLocation(RadChange change) {
                // Get map
                ChunkDataMap map;

                synchronized (RadiationSystem.INSTANCE) {
                    map = RadiationSystem.INSTANCE.getExposureMap(change.dim, true);
                }

                long time = System.nanoTime();

                // Clear old values
                removeValue(map, change.old_value, x, change.y, change.z);

                // Add new value, completed as a separate step due to range differences
                setValue(map, change.new_value, change.x, change.y, change.z);

                time = System.nanoTime() - time;
                NuclearPhysics.getLogger().info(String.format("ThreadRadExposure: %sx %sy %sz | %so %sn | took %s",
                        change.x, change.y, change.z,
                        change.old_value, change.new_value,
                        StringHelpers.formatNanoTime(time)
                ));
            }

            protected void removeValue(RadiationMap map, int value, int cx, int cy, int cz) {
                final int rad = getRadFromMaterial(value);
                final int edit_range = (int) Math.floor(getDecayRange(rad));

                for (int mx = -edit_range; mx <= edit_range; mx++) {
                    for (int mz = -edit_range; mz <= edit_range; mz++) {
                        for (int my = -edit_range; my <= edit_range; my++) {
                            int x = cx + mx;
                            int y = cy + my;
                            int z = cz + mz;

                            //Stay inside map
                            if (y >= 0 && y < ChunkData.CHUNK_HEIGHT) {
                                //Get data
                                double distance = Math.sqrt(mx * mx + my * my + mz * mz);
                                int current_value = map.getData(x, y, z);

                                //Remove old value
                                current_value -= getRadForDistance(rad, distance);

                                //Save
                                map.setData(x, y, z, Math.max(0, current_value));
                            }
                        }
                    }
                }
            }

            protected void setValue(RadiationMap map, int value, int cx, int cy, int cz){
                NuclearPhysics.getLogger().info(String.format("ThreadReadExposure: Settiing position[%s %s %s] " + "to %s mats", cx, cy, cz, value));

                final int rad = getRadFromMaterial(value);
                final int edit_range = (int) Math.floor(getDecayRange(rad));

                for (int mx = -edit_range; mx <= edit_range; mx++) {
                    for (int mz = -edit_range; mz <= edit_range; mz++) {
                        for (int my = -edit_range; my <= edit_range; my++) {
                            int x = cx + mx;
                            int y = cy + my;
                            int z = cz + mz;

                            // Stay inside map
                            if (y >= 0 && y < ChunkData.CHUNK_HEIGHT) {
                                // Get data
                                double distance = Math.sqrt(mx * mx + my * my + mz * mz);
                                int current_value = map.getData(x, y, z);

                                // Update value
                                current_value += getRadForDistance(rad, distance);

                                // Save
                                map.setData(x, y, z, Math.max(0, current_value));

                                if (AtomicScience.runningAsDev) {
                                    AtomicScience.logger.info(String.format("\t\tRad at position[%s %s %s] " +
                                            "is %s", x, y, z, current_value));
                                }
                            }
                        }
                    }
                }
            }
            */

            ////////////////////////////////////////////////////////////////////////////////////////////////////////////

            // set value
            boolean hasChanged = chunk.setValue(x & 15, y, z & 15, value);

            // if changed mark chunk so it saves
            if (hasChanged && world != null) {
                ((World) world).getChunkFromChunkCoords(x, z).setChunkModified();
            }

            return hasChanged;
        }

        return true;
    }

    public boolean setData(BlockPos pos, int value) {
        return setData(pos.getX(), pos.getY(), pos.getZ(), value);
    }

    public void remove(long index) {
        /*if (loadedChunks.containsKey(index)) {

            if (isMaterialMap) {
                ChunkData chunk = loadedChunks.get(index);

                if (chunk != null) {
                    //RadiationChunkHandler.THREAD_RAD_EXPOSURE.queueChunkForRemoval(chunk);
                }
            }
            */
            //TODO maybe fire events?
            loadedChunks.remove(index);
        //}
    }

    public void remove(final Chunk chunk) {
        remove(ChunkPos.asLong(chunk.xPosition, chunk.zPosition));
    }

    public void clear() {
        loadedChunks.clear();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void readChunkFromNBT(final Chunk chunk, final NBTTagCompound data) {
        final long index = ChunkPos.asLong(chunk.xPosition, chunk.zPosition);

        // Get chunk
        ChunkData chunkData = null;

        if (loadedChunks.containsKey(index)) {
            chunkData = loadedChunks.get(index);
        }

        // Init chunk if missing
        if (chunkData == null) {
            chunkData = new ChunkData(chunk);
            loadedChunks.put(index, chunkData);
        }

        // Load
        chunkData.readFromNBT(data.getCompoundTag(NBT_CHUNK_DATA));

        /*
        // Queue to be scanned to update exposure map
        if (isMaterialMap) {
            //RadiationChunkHandler.THREAD_RAD_EXPOSURE.queueChunkForAddition(radiationChunk);
        }
        */
    }

    public void writeChunkFromNBT(final Chunk chunk, final NBTTagCompound data) {
        final long index = ChunkPos.asLong(chunk.xPosition, chunk.zPosition);

        if (loadedChunks.containsKey(index)) {
            final ChunkData chunkData = loadedChunks.get(index);

            if (chunkData != null) {
                final NBTTagCompound tag = new NBTTagCompound();
                chunkData.writeToNBT(tag);

                if (!tag.hasNoTags()) {
                    data.setTag(NBT_CHUNK_DATA, tag);
                }
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public ChunkData getChunkFromPosition(int x, int z, boolean init) {
        return getChunk(x >> 4, z >> 4, init);
    }

    public ChunkData getChunk(int x, int z, boolean init) {
        long index = ChunkPos.asLong(x, z);
        ChunkData chunk = loadedChunks.get(index);

        if (chunk == null && init) {
            chunk = new ChunkData(world, new ChunkPos(x, z));
            loadedChunks.put(index, chunk);
        }

        return chunk;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static ChunkDataMap getMap(final HashMap<IBlockAccess, ChunkDataMap> queryMap, final IBlockAccess world, final boolean init) {
        ChunkDataMap map = queryMap.get(world);

        if (map == null && init) {
            map = new ChunkDataMap(world);
            queryMap.put(world, map);
        }

        return map;
    }
}