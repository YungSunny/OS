package VirtualMachine;

import Memory.VirtualMemory;
import RealMachine.Processor;

public class CommandProcessor {

    public static final int MAX_INT = 65535;
    public String[] commands = {"add", "sub", "mul", "div", "je*", "jn*", "jm*", "ja*", "cmp", "jb*", "jl*", "push",
            "pop", "prnl", "gd**", "pd*", "halt", "ps*", "pd*", "pp*"};
    public VirtualMemory memory;

    public CommandProcessor(VirtualMemory memory) {
        this.memory = memory;
    }

    public void LD(String address) {
        String data = memory.getData(Integer.valueOf(address));
        memory.push(data, VirtualMachine.sp++);
    }

    public String PP() {
        return memory.pop(VirtualMachine.sp--);
    }

    //  Jeigu rezultatas netelpa, OF = 1. Jeigu reiksmes zenklo bitas yra 1, SF = 1.
    public void ADD() {
        String a = memory.pop(VirtualMachine.sp--);
        int a1 = Integer.parseInt(a, 16);

        String b = memory.pop(VirtualMachine.sp--);
        int b1 = Integer.parseInt(b, 16);

        if (a1 + b1 > MAX_INT) {
            Processor.setCF();
        }
        a1 += b1;
        if (((a1 >> 6) & 1) == 1) {
            Processor.setSF();
        }

        //memory.push(String.valueOf(b1), VirtualMachine.sp++);
        memory.push(String.valueOf(a1), VirtualMachine.sp++);
        ++Processor.ic;
    }

    public void SUB() {
        String a = memory.getBlock(0).pop(VirtualMachine.sp--);
        int a1 = Integer.parseInt(a, 16);

        String b = memory.getBlock(0).pop(VirtualMachine.sp--);
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
        memory.getBlock(0).push(String.valueOf(b1), ++VirtualMachine.sp);
        memory.getBlock(0).push(String.valueOf(a1), ++VirtualMachine.sp);
        ++Processor.ic;
    }

    public void MUL() {
        String a = memory.getBlock(0).pop(VirtualMachine.sp--);
        int a1 = Integer.parseInt(a, 16);

        String b = memory.getBlock(0).pop(VirtualMachine.sp--);
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
        memory.getBlock(0).push(String.valueOf(b1), ++VirtualMachine.sp);
        memory.getBlock(0).push(String.valueOf(a1), ++VirtualMachine.sp);
        ++Processor.ic;
    }

    // Padalina R1 is R2, irasoma i R1. Jeigu reiksmes zenklo bitas yra 1, SF = 1.
    public void DIV() {
        String a = memory.getBlock(0).pop(VirtualMachine.sp--);
        int a1 = Integer.parseInt(a, 16);

        String b = memory.getBlock(0).pop(VirtualMachine.sp--);
        int b1 = Integer.parseInt(b, 16);
        a1 /= b1;
        if (((a1 >> 6) & 1) == 1) {
            Processor.setSF();
        }
        memory.getBlock(0).push(String.valueOf(b1), ++VirtualMachine.sp);
        memory.getBlock(0).push(String.valueOf(a1), ++VirtualMachine.sp);
        ++Processor.ic;
    }

    //si komanda palygina registre R1 ir R2 esancias reiksmes. Jeigu reiksmes lygios, ZF = 1, priesingu atveju ZF = 0.
    public void CMP() {
        String a = memory.getBlock(0).pop(VirtualMachine.sp--);
        int a1 = Integer.parseInt(a, 16);

        String b = memory.getBlock(0).pop(VirtualMachine.sp--);
        int b1 = Integer.parseInt(b, 16);

        if (a1 == b1) {
            Processor.setZF();
        } else {
            Processor.clearZF();
        }
        memory.getBlock(0).push(String.valueOf(b1), ++VirtualMachine.sp);
        memory.getBlock(0).push(String.valueOf(a1), ++VirtualMachine.sp);
        ++Processor.ic;
    }

    //TODO VISI JUMPAI NUsOKA DUOTU ADRESU
    //JMx1x2 - besalyginio valdymo perdavimo komanda. Ji reiskia, kad valdymas turi buti perduotas kodo segmento zodziui, nurodytam adresu 16 * x1 + x2
    public void JM(String address) {
        VirtualMachine.ic = Integer.parseInt(address, 16);

    }

    //JEx1x2 - valdymas turi buti perduotas kodo segmento zodziui, nurodytam adresu 16* x1 + x2 jeigu ZF = 1
    public void JE(String address) {
        if (Processor.getZF() == 1) {
            VirtualMachine.ic = Integer.parseInt(address, 16);
        }
    }

    //JNx1x2 - valdymas turi buti perduotas kodo segmentui, nurodytam adresu 16*x1+x2, jeigu ZF = 0
    public void JN(String address) {
        if (Processor.getZF() == 0) {
            VirtualMachine.ic = Integer.parseInt(address, 16);
        }

    }

    //JAx1x2 - valdymas turi buti perduotas kodo segmento zodziui, nurodytam adresu 16* x1 + x2 jeigu CF = OF
    public void JA(String address) {
        if (Processor.getCF() == 0) {
            VirtualMachine.ic = Integer.parseInt(address, 16);
        }
    }

    //JBx1x2 - valdymas turi buti perduotas kodo segmento zodziui, nurodytam adresu 16* x1 + x2 jeigu CF=1
    public void JB(String address) {
        if (Processor.getCF() == 1) {
            VirtualMachine.ic = Integer.parseInt(address, 16);
        }
    }

    //JGx1x2 - valdymas turi buti perduotas kodo segmento zodziui, nurodytam adresu 16* x1 + x2 jeigu ZF = 0 IR SF = OF
    public void JG(String address) {
        if (Processor.getZF() == 0 && Processor.getSF() == Processor.getOF()) {
            VirtualMachine.ic = Integer.parseInt(address, 16);
        }
    }

    //JLx1x2 - valdymas turi buti perduotas kodo segmento zodziui, nurodytam adresu 16* x1 + x2 jeigu SF != OF
    public void JL(String address) {
        if (Processor.getSF() != Processor.getOF()) {
            VirtualMachine.ic = Integer.parseInt(address, 16);
        }
    }

    public void PUSH() {
        memory.getBlock(memory.getCurrentStackBlock()).push(String.valueOf(Processor.r), ++VirtualMachine.sp);
        ++Processor.ic;
    }

    public void POP() {
        Processor.r = Integer.parseInt(memory.getBlock(0).pop(VirtualMachine.sp--), 16);
        ++Processor.ic;
    }

    public void PRNL() {
        System.out.print('\n');
        ++Processor.ic;
    }

    public void GD(String x, String y) {
        Integer.parseInt(x, 16);
        ++Processor.ic;
    }

    public String PD(String x, String y) {
        ++Processor.ic;
        return memory.getData(2);
    }

    public void PP(String address) {
        ++Processor.ic;
        String stackData = memory.pop(VirtualMachine.sp--);
        memory.pushData(stackData, Integer.valueOf(address));
    }

}
