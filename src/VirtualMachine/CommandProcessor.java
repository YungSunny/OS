package VirtualMachine;

import Memory.VirtualMemory;
import RealMachine.Processor;

public class CommandProcessor {

    public static final int MAX_INT = 65535;
    /*public String[] commands = {"add", "sub", "mul", "div", "je*", "jn*", "jm*", "ja*", "cmp", "jb*", "jl*", "push",
            "pop", "prnl", "gd**", "pd*", "halt", "ps*", "pd*", "pp*"};*/
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

    public void AND() {
        int x = Integer.parseInt(memory.pop(VirtualMachine.sp--), 16);
        int y = Integer.parseInt(memory.pop(VirtualMachine.sp--), 16);

        x = x & y;

        memory.push(Integer.toHexString(x), ++VirtualMachine.sp);
        ++Processor.ic;
    }

    public void OR() {
        int x = Integer.parseInt(memory.pop(VirtualMachine.sp--), 16);
        int y = Integer.parseInt(memory.pop(VirtualMachine.sp--), 16);

        x = x | y;

        memory.push(Integer.toHexString(x), ++VirtualMachine.sp);
        ++Processor.ic;
    }

    public void NOT() {
        int x = Integer.parseInt(memory.pop(VirtualMachine.sp--), 16);

        x = (-1)*x;

        memory.push(Integer.toHexString(x), ++VirtualMachine.sp);
        ++Processor.ic;
    }

    public void XOR() {
        int x = Integer.parseInt(memory.pop(VirtualMachine.sp--), 16);
        int y = Integer.parseInt(memory.pop(VirtualMachine.sp--), 16);

        x = x ^ y;

        memory.push(Integer.toHexString(x), ++VirtualMachine.sp);
        ++Processor.ic;
    }

    public void LSF() {
        int x = Integer.parseInt(memory.pop(VirtualMachine.sp--), 16);

        x = x << 1;

        memory.push(Integer.toHexString(x), ++VirtualMachine.sp);
        ++Processor.ic;
    }

    public void RSF() {
        int x = Integer.parseInt(memory.pop(VirtualMachine.sp--), 16);

        x = x >> 1;

        memory.push(Integer.toHexString(x), ++VirtualMachine.sp);
        ++Processor.ic;
    }

    //JMx1x2 - besalyginio valdymo perdavimo komanda. Ji reiskia, kad valdymas turi buti perduotas kodo segmento zodziui, nurodytam adresu 16 * x1 + x2
    public void JP(String address) {
        VirtualMachine.ic = Integer.parseInt(address, 16);
    }

    //JEx1x2 - valdymas turi buti perduotas kodo segmento zodziui, nurodytam adresu 16* x1 + x2 jeigu ZF = 1
    public void JE(String address) {
        if (Processor.getZF() == 1) {
            VirtualMachine.ic = Integer.parseInt(address, 16);
        }
    }

    //JGx1x2 - valdymas turi buti perduotas kodo segmento zodziui, nurodytam adresu 16* x1 + x2 jeigu CMP rezultatas buvo 1
    public void JG(String address) {
        int cmpResult = Integer.parseInt(memory.pop(VirtualMachine.sp--), 16);
        if (cmpResult == 1) {
            VirtualMachine.ic = Integer.parseInt(address, 16);
        }
    }

    //JLx1x2 - valdymas turi buti perduotas kodo segmento zodziui, nurodytam adresu 16* x1 + x2 jeigu CMP rezultatas buvo 2
    public void JL(String address) {
        int cmpResult = Integer.parseInt(memory.pop(VirtualMachine.sp--), 16);
        if (cmpResult == 2) {
            VirtualMachine.ic = Integer.parseInt(address, 16);
        }
    }

    public void PUSH() {
        memory.getBlock(memory.getCurrentStackBlock()).push(String.valueOf(0), ++VirtualMachine.sp);
        ++Processor.ic;
    }

    public void POP() {
        int poppedValue = Integer.parseInt(memory.pop(VirtualMachine.sp--), 16);

        memory.pushData(Integer.toHexString(poppedValue));
        ++Processor.ic;
    }

    public void PR(String address) {
        System.out.print(memory.getData(Integer.valueOf(address)));
    }

    public void PRTN() {
        int x = Integer.parseInt(memory.pop(VirtualMachine.sp--), 16);

        System.out.println("PRTN: " + x);
    }

    public void PRNL() {
        System.out.println();
        ++Processor.ic;
    }

    public void PP(String address) {
        ++Processor.ic;
        String stackData = memory.pop(VirtualMachine.sp--);
        memory.pushData(stackData, Integer.valueOf(address));
    }

}
