package Memory;

public class RealMemory {
    public MemoryBlock[] memoryBlocks;
    public static final int MAX_MEMORY_BLOCKS = 16;

    public RealMemory() {
        memoryBlocks = new MemoryBlock[MAX_MEMORY_BLOCKS];
        
        for (int k = 0; k < MAX_MEMORY_BLOCKS; k++) {
            memoryBlocks[k] = new MemoryBlock();
        }
    }

    public MemoryBlock getBlock(int index) {
        return memoryBlocks[index];
    }

    public int getMemoryBlocksCount() {
        return MAX_MEMORY_BLOCKS;
    }

    public void setBlock(int index, MemoryBlock memoryBlock) {
        memoryBlocks[index] = memoryBlock;
    }
}