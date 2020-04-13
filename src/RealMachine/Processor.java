package RealMachine;

public class Processor {

    //modo registras
    public static byte mode;
    //stacko registras
    public static int sp;
    //???
    public static int pc;
    public static int r;
    //puslapiu lenteles registras
    public static int ptr;
    //programiniu pertrukimu registras
    private static byte pi;
    //supervizoriniu pertraukimu registras
    private static byte si;
    //taimerio registras
    private static byte ti;
    //inputo registras
    public static byte ioi;

    //kanalu registras
    public static byte ch1; //kanalu registrai
    public static byte ch2;
    public static byte ch3;

    //pozymmiu registras
    public static byte[] sf = {0, 0, 0, 0}; //0-OF, 1-SF, 2-ZF, 3-CF

    public Processor() {
    }
}
