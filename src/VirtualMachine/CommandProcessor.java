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

    public void PT(String address) {
        String data = memory.pop(VirtualMachine.sp--).trim();
        memory.pushData(data, Integer.valueOf(address));
    }

    public String PP() {
        return memory.pop(VirtualMachine.sp--);
    }

    //  Jeigu rezultatas netelpa, OF = 1. Jeigu reiksmes zenklo bitas yra 1, SF = 1.
    public void ADD() {
        int x = Integer.parseInt(memory.pop(VirtualMachine.sp--).trim(), 16);
        int y = Integer.parseInt(memory.pop(VirtualMachine.sp--).trim(), 16);

        if (x + y > MAX_INT)
            Processor.setCF();

        x += y;

        if (((x >> 6) & 1) == 1)
            Processor.setSF();

        memory.push(Integer.toHexString(x), VirtualMachine.sp++);
        ++Processor.ic;
    }

    public void SUB() {
        int x = Integer.parseInt(memory.pop(VirtualMachine.sp--).trim(), 16);
        int y = Integer.parseInt(memory.pop(VirtualMachine.sp--).trim(), 16);

        if (y - x < 0) {
            Processor.setOF();
            y -= x;
        } else {
            y -= x;
        }
        if (((y >> 6) & 1) == 1) {
            Processor.setSF();
        }

        memory.push(Integer.toHexString(y), VirtualMachine.sp++);
        ++Processor.ic;
    }

    public void MUL() {
        int x = Integer.parseInt(memory.pop(VirtualMachine.sp--), 16);
        int y = Integer.parseInt(memory.pop(VirtualMachine.sp--), 16);

        if (x * y > MAX_INT) {
            Processor.setOF();
            return;
        } else
            x *= y;

        if (((x >> 6) & 1) == 1)
            Processor.setSF();

        memory.push(Integer.toHexString(x), ++VirtualMachine.sp);
        ++Processor.ic;
    }

    // Padalina R1 is R2, irasoma i R1. Jeigu reiksmes zenklo bitas yra 1, SF = 1.
    public void DIV() {
        int x = Integer.parseInt(memory.pop(VirtualMachine.sp--), 16);
        int y = Integer.parseInt(memory.pop(VirtualMachine.sp--), 16);

        x /= y;
        if (((x >> 6) & 1) == 1) {
            Processor.setSF();
        }

        memory.push(Integer.toHexString(x), ++VirtualMachine.sp);
        ++Processor.ic;
    }

    //si komanda palygina registre R1 ir R2 esancias reiksmes. Jeigu reiksmes lygios, ZF = 1, priesingu atveju ZF = 0.
    public void CMP() {
        int x = Integer.parseInt(memory.pop(VirtualMachine.sp--), 16);
        int y = Integer.parseInt(memory.pop(VirtualMachine.sp--), 16);

        if (x - y == 0) {
            Processor.setZF();
            memory.push(Integer.toHexString(0), ++VirtualMachine.sp);
        } else if (x - y > 0) {
            memory.push(Integer.toHexString(1), ++VirtualMachine.sp);
            Processor.clearZF();
        } else if (x - y < 0) {
            memory.push(Integer.toHexString(2), ++VirtualMachine.sp);
            Processor.clearZF();
        }

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

    public void PR(String address) {
        System.out.print(memory.getData(Integer.valueOf(address)));
    }

    public void PRTN() {
        //TODO
    }

    public void PRNL() {
        System.out.println();
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
