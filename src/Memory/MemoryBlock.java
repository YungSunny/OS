package Memory;

public class MemoryBlock {
    public static final int BLOCK_SIZE = 16;
    public static final String ZEROED = "0000"; 
    private Word[] data;
    public boolean free;

    public MemoryBlock() {
        data = new Word[BLOCK_SIZE];
        
        for (Word currWord: data) {
            currWord = new Word();
            setFree(true);
        }
    }

    public Word getWord(int index) {
        return data[index];
    }

    public void setWord(int index, Word value) {
        data[index] = value;
    }

    public void pushWord(String data, int sp) {
        getWord(sp).setValue(data);
    }

    public String popWord(int sp) {
        getWord(sp+1).setValue(ZEROED);
        return getWord(sp).getValue();
    }

    public void putWord(String data, int offset) {
        getWord(offset).setValue(data);
    }

    public String get(int offset) {
        return getWord(offset).getValue();
    }

    public void pushData(int offset, String data) {
        this.data[offset].setValue(data);
    }

    public void setFree(boolean free) {
        this.free = free;
    }

    public boolean isEmpty() {
        return free;
    }
}