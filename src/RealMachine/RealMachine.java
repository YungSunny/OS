package RealMachine;

import Memory.Memory;
import VirtualMachine.VirtualMachine;

public class RealMachine {
    Processor processor;
    Memory memory;

    public RealMachine(){
        VirtualMachine vm = new VirtualMachine();
        processor = Processor.getInstance();
        memory = new Memory();

        try {
            vm.loadProgram("$prog1");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        showMemory(vm);
        displayRegisters();
    }

    public void showMemory(VirtualMachine vm) {
        System.out.println("---- VM ----");
        for (int blockIter = 0; blockIter < 8; blockIter++) {
            System.out.print("BLOCK" + blockIter + " ");
            for (int i = 0; i < 16; i++) {
                System.out.print(i + ":" + vm.memory.getBlock(blockIter).getWord(i).getValue() + " ");
            }
            System.out.print('\n');
        }
        System.out.println("------------");
    }

    public void displayRegisters() {
        System.out.println("---------REGISTERS--------");
        String registersString = String.format("IC:=%s\tSF:=%s\tPTR:=%s\tMODE:=%s\t\nCH1:=%s\tCH2:=%s\tCH3:=%s\t",
                                                processor.ic, processor.sf.toString(), processor.ptr, processor.mode, processor.ch1, processor.ch2, processor.ch3);
        System.out.println(registersString);
        System.out.println("--------------------------");
    }
}
