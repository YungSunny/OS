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

        try {
            vm.loadProgramToCodeSegment("$prog1", "C:\\Users\\gsnio\\Desktop\\OS\\os-lab-master\\hdd.txt");
        } catch (Exception ex) {

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
}
