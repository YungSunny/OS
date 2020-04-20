package RealMachine;

import Memory.MemoryBlock;
import Memory.VirtualMemory;
import Memory.Word;

public class Paging {

    public MemoryBlock getPageTable(MemoryBlock[] realMemoryBlocks) {
        MemoryBlock pageTable = new MemoryBlock();

        pageTable.free = false;

        int usedBlocks = 0;
        for (int k = 0; k < RealMachine.MAX_REALMEMORY_BLOCKS && usedBlocks < VirtualMemory.VIRTUAL_MEMORY_BLOCKS; k++) {
            if (realMemoryBlocks[k].isEmpty()) {
                pageTable.setWord(usedBlocks++, new Word(k));
                realMemoryBlocks[k].free = false;
            }
        }
        if (usedBlocks != VirtualMemory.VIRTUAL_MEMORY_BLOCKS-1)
            return null;

        return pageTable;
    }

    public int getFreeBlock(MemoryBlock[] realMemoryBlocks) {
        for (int i = 0; i < RealMachine.MAX_REALMEMORY_BLOCKS; i++) {
            if (realMemoryBlocks[i].isEmpty()) {
                realMemoryBlocks[i].free = false;
                return i;
            }
        }
        return -1;
    }

}
