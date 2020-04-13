package VirtualMachine;

import RealMachine.RealMachine;

public class VirtualMachine {

    private RealMachine realMachine;
    private CommandProcessor commandProcessor;

    public VirtualMachine(RealMachine rm) {
        this.realMachine = rm;
    }

    public void executeCommand(String command) {

        // cia kaip suprantu reiks suhandlint visas komandas kurios bus ivedamos
        switch(command) {
            case "XXX":
                //commandProcessor.XXX();
                break;
            default:
                break;
        }

    }

}
