package VirtualMachine;

import Memory.VirtualMemory;
import RealMachine.Processor;

public class CommandProcessor {

    public static final int MAX_INT = 65535;
    public String[] commands = {"add", "sub", "mul", "div", "je*", "jn*", "jm*", "ja*", "cmp", "jb*", "jl*", "push",
            "pop", "prnl", "gd**", "pd*", "halt", "ps*", "pd*", "pp*"};
    public VirtualMemory memory;
    public int sp;
    public int pc;
    public int ptr;

    public CommandProcessor(VirtualMemory memory, int sp, int ptr) {
        memory = new VirtualMemory(ptr);
        sp = 0;
        pc = 0;
    }

    public void PS(String address) {
        String data = memory.getData(Integer.valueOf(address));
        memory.push(data, sp++);
    }

    public String PP() {
        return memory.pop(sp--);
    }

    //  Jeigu rezultatas netelpa, OF = 1. Jeigu reikšmės ženklo bitas yra 1, SF = 1.
    public void ADD() {
        String a = memory.pop(sp--);
        int a1 = Integer.parseInt(a, 16);

        String b = memory.pop(sp--);
        int b1 = Integer.parseInt(b, 16);

        if (a1 + b1 > MAX_INT) {
            Processor.setCF();
        }
        a1 += b1;
        if (((a1 >> 6) & 1) == 1) {
            Processor.setSF();
        }
        memory.push(String.valueOf(b1), sp++);
        memory.push(String.valueOf(a1), sp++);
        ++Processor.pc;
    }

    public void SUB() {
        String a = memory.getBlock(0).pop(sp--);
        int a1 = Integer.parseInt(a, 16);

        String b = memory.getBlock(0).pop(sp--);
        int b1 = Integer.parseInt(b, 16);

        if (a1 - b1 < 0) {
            Processor.setOF();
            return;
        } else {
            a1 -= b1;
        }
        if (((a1 >> 6) & 1) == 1) {
            Processor.setSF();
        }
        memory.getBlock(0).push(String.valueOf(b1), ++sp);
        memory.getBlock(0).push(String.valueOf(a1), ++sp);
        ++Processor.pc;
    }

    public void MUL() {
        String a = memory.getBlock(0).pop(sp--);
        int a1 = Integer.parseInt(a, 16);

        String b = memory.getBlock(0).pop(sp--);
        int b1 = Integer.parseInt(b, 16);
        if (a1 * b1 > MAX_INT) {
            Processor.setOF();
            return;
        } else {
            a1 *= b1;
        }
        if (((a1 >> 6) & 1) == 1) {
            Processor.setSF();
        }
        memory.getBlock(0).push(String.valueOf(b1), ++sp);
        memory.getBlock(0).push(String.valueOf(a1), ++sp);
        ++Processor.pc;
    }

    // Padalina R1 iš R2, įrašoma į R1. Jeigu reikšmės ženklo bitas yra 1, SF = 1.
    public void DIV() {
        String a = memory.getBlock(0).pop(sp--);
        int a1 = Integer.parseInt(a, 16);

        String b = memory.getBlock(0).pop(sp--);
        int b1 = Integer.parseInt(b, 16);
        a1 /= b1;
        if (((a1 >> 6) & 1) == 1) {
            Processor.setSF();
        }
        memory.getBlock(0).push(String.valueOf(b1), ++sp);
        memory.getBlock(0).push(String.valueOf(a1), ++sp);
        ++Processor.pc;
    }

    //Ši komanda palygina registre R1 ir R2 ęsančias reikšmes. Jeigu reikšmės lygios, ZF = 1, priešingu atveju ZF = 0.
    public void CMP() {
        String a = memory.getBlock(0).pop(sp--);
        int a1 = Integer.parseInt(a, 16);

        String b = memory.getBlock(0).pop(sp--);
        int b1 = Integer.parseInt(b, 16);

        if (a1 == b1) {
            Processor.setZF();
        } else {
            Processor.clearZF();
        }
        memory.getBlock(0).push(String.valueOf(b1), ++sp);
        memory.getBlock(0).push(String.valueOf(a1), ++sp);
        ++Processor.pc;
    }

    //TODO VISI JUMPAI NUŠOKA DUOTU ADRESU
    //JMx1x2 - besąlyginio valdymo perdavimo komanda. Ji reiškia, kad valdymas turi būti perduotas kodo segmento žodžiui, nurodytam adresu 16 * x1 + x2
    public void JM(String address) {
        pc = Integer.parseInt(address, 16);

    }

    //JEx1x2 - valdymas turi būti perduotas kodo segmento žodžiui, nurodytam adresu 16* x1 + x2 jeigu ZF = 1
    public void JE(String address) {
        if (Processor.getZF() == 1) {
            pc = Integer.parseInt(address, 16);
        }
    }

    //JNx1x2 - valdymas turi būti perduotas kodo segmentui, nurodytam adresu 16*x1+x2, jeigu ZF = 0
    public void JN(String address) {
        if (Processor.getZF() == 0) {
            pc = Integer.parseInt(address, 16);
        }

    }

    //JAx1x2 - valdymas turi būti perduotas kodo segmento žodžiui, nurodytam adresu 16* x1 + x2 jeigu CF = OF
    public void JA(String address) {
        if (Processor.getCF() == 0) {
            pc = Integer.parseInt(address, 16);
        }
    }

    //JBx1x2 - valdymas turi būti perduotas kodo segmento žodžiui, nurodytam adresu 16* x1 + x2 jeigu CF=1
    public void JB(String address) {
        if (Processor.getCF() == 1) {
            pc = Integer.parseInt(address, 16);
        }
    }

    //JGx1x2 - valdymas turi būti perduotas kodo segmento žodžiui, nurodytam adresu 16* x1 + x2 jeigu ZF = 0 IR SF = OF
    public void JG(String address) {
        if (Processor.getZF() == 0 && Processor.getSF() == Processor.getOF()) {
            pc = Integer.parseInt(address, 16);
        }
    }

    //JLx1x2 - valdymas turi būti perduotas kodo segmento žodžiui, nurodytam adresu 16* x1 + x2 jeigu SF != OF
    public void JL(String address) {
        if (Processor.getSF() != Processor.getOF()) {
            pc = Integer.parseInt(address, 16);
        }
    }

    public void PUSH() {
        memory.getBlock(0).push(String.valueOf(Processor.r), ++sp);
        ++Processor.pc;
    }

    public void POP() {
        Processor.r = Integer.parseInt(memory.getBlock(0).pop(sp--), 16);
        ++Processor.pc;
    }

    public void PRNL() {
        System.out.print('\n');
        ++Processor.pc;
    }

    public void GD(String x, String y) {
        Integer.parseInt(x, 16);
        ++Processor.pc;
    }

    public String PD(String x, String y) {
        ++Processor.pc;
        return memory.getData(2);
    }

    public void PP(String address) {
        ++Processor.pc;
        String stackData = memory.pop(sp--);
        memory.pushData(stackData, Integer.valueOf(address));
    }

}
