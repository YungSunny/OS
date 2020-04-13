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

    public static void setRegisters() {
        sp = 0;
        ch1 = 0;
        ch2 = 0;
        ch3 = 0;
        r = 0;
        mode = 0;
        ti = 1;
        si = 0;
        pi = 0;
        ioi = 0;
    }

    public static void clearOF(){
        sf[0] = 0;
    }
    public static void clearSF(){
        sf[1] = 0;
    }

    public static void clearCF() {
        sf[3] = 0;
    }

    public static void clearZF() {
        sf[2] = 0;
    }

    public static void setOF() {
        sf[0] = 1;
    }

    public static void setSF() {
        sf[1] = 1;
    }

    public static void setZF() {
        sf[2] = 1;
    }

    public static void setCF() {
        sf[3] = 1;
    }

    public static byte getOF() {
        return sf[0];
    }

    public static byte getSF() {
        return sf[1];
    }

    public static byte getZF() {
        return sf[2];
    }

    public static byte getCF() {
        return sf[3];
    }


}
