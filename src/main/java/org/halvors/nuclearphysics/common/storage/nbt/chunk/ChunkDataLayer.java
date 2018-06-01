package org.halvors.nuclearphysics.common.storage.nbt.chunk;

public class ChunkDataLayer {
    private static final int CHUNK_WIDTH = 16;

    private final int y; // Index of this layer.
    private final int[] data = new int[CHUNK_WIDTH * CHUNK_WIDTH];

    public int blocksUsed = 0; // Number of non-zero slots, used to track if layer is empty

    public ChunkDataLayer(int y) {
        this.y = y;
    }

    /**
     * Index of the x z location
     *
     * @param x - location 0-15
     * @param z - location 0-15
     * @return index between 0-255, -1 returns if input data is invalid
     */
    public final int getIndex(int x, int z) {
        // Bound check to prevent index values from generating outside range
        // Is needed as a negative z can cause a value to overlap values normally in range
        // Ex: 15x -1z -> 239, which is in range but not the right index

        if (x >= 0 && x < CHUNK_WIDTH && z >= 0 && z < CHUNK_WIDTH) {
            return x * CHUNK_WIDTH + z;
        }

        return -1;
    }

    public int getY() {
        return y;
    }

    public int[] getData() {
        return data;
    }

    public boolean isEmpty() {
        return blocksUsed <= 0;
    }

    public int getData(int x, int z) {
        int index = getIndex(x, z);

        if (index >= 0) {
            return data[index];
        }

        return 0;
    }

    public boolean setData(int x, int z, int value) {
        int index = getIndex(x, z);

        if (index >= 0) {
            int prev = data[index];
            data[index] = value;

            if (prev != 0 && value == 0) {
                blocksUsed--;
            } else if (prev == 0 && value != 0) {
                blocksUsed++;
            }

            return true;
        }

        return false;
    }
}
