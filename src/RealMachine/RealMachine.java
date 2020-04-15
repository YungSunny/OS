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
            System.out.println(ex.getStackTrace());
        }

        String commandToExecute;
        while (!(commandToExecute = getCommand(vm)).equals("HALT")) {
            System.out.println(commandToExecute);
        }

        displayRegisters();
    }

    public void displayRegisters() {
        System.out.println("---------REGISTERS--------");
        String registersString = String.format("IC:=%s\tSF:=%s\tPTR:=%s\tMODE:=%s\t\nCH1:=%s\tCH2:=%s\tCH3:=%s\t",
                                                processor.ic, processor.sf.toString(), processor.ptr, processor.mode, processor.ch1, processor.ch2, processor.ch3);
        System.out.println(registersString);
        System.out.println("--------------------------");
    }

    public String getCommand(VirtualMachine vm) {
        return vm.memory.getCode(vm.ic++);
    }
}
