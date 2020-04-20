package Memory;

public class Word {

    private static final short WORD_SIZE = 4;
    private String data;

    public Word() {
        data = "0000";
    }

    public Word(int s) {
        setIntValue(s);
    }

    public Word(String s) {
        setValue(s);
    }

    public int getIntValue() {
        
        try {
           return Integer.valueOf(data);
        } catch (Exception e) {
            System.out.println("error word");
         return 0;
        }
        
    }

    public void setIntValue(int integer) {
        String stringInt = String.valueOf(integer);
        
        while (stringInt.length() < WORD_SIZE) {
            stringInt = "0" + stringInt;
        }
        
        setValue(stringInt);
    }

    public String getValue() {
        return data.toUpperCase();
    }

    public void setValue(String s) {
        if (s.trim().length() <= WORD_SIZE) {
            
            while (s.length() < WORD_SIZE) {
                s = s + " ";
            }
            data = s;
        }
        else 
            data = s.substring(0, WORD_SIZE);
    }

}