package Memory;

public class RealMemory {
    public MemoryBlock[] memory;
    public static final int MEMORY_BLOCKS = 16;

    public RealMemory() {
        memory = new MemoryBlock[MEMORY_BLOCKS];
        
        for (MemoryBlock block: memory) {
            block = new MemoryBlock();
        }
    }

    public MemoryBlock getBlock(int index) {
        return memory[index];
    }

    public int getMemoryBlocksCount() {
        return MEMORY_BLOCKS;
    }

    public void setBlock(int index, MemoryBlock memoryBlock) {
        memory[index] = memoryBlock;
    }
}