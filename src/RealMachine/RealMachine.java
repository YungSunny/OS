package RealMachine;

import Memory.Memory;
import VirtualMachine.VirtualMachine;

public class RealMachine {
    Processor processor;
    Memory memory;

    public RealMachine(){
        VirtualMachine vm = new VirtualMachine(this);
        processor = Processor.getInstance();
        memory = new Memory();
    }

    public void displayRegisters() {
        System.out.println("---------REGISTERS--------");
        System.out.println("IC: " + Integer.toHexString(getIc()));
        System.out.println("SF: " + Integer.toHexString(getSf()));
        System.out.println("PTR: " + Integer.toHexString(this.ptr));
        System.out.println("------");
        System.out.println("MODE: " + Integer.toHexString(this.mode));
        System.out.println("CH1: " + Integer.toHexString(this.ch1));
        System.out.println("CH2: " + Integer.toHexString(this.ch2));
        System.out.println("CH3: " + Integer.toHexString(this.ch3));
        System.out.println("--------------------------");
    }
}
