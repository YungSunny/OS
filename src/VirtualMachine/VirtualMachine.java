package VirtualMachine;

import Memory.VirtualMemory;
import RealMachine.RealMachine;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class VirtualMachine {

    private RealMachine realMachine;
    private CommandProcessor commandProcessor;
    public VirtualMemory memory;

    private int ptr;
    public short sp;
    public short ic;

    public VirtualMachine() {
        this.memory = new VirtualMemory(ptr);

        sp = 0;
        ic = 0;
    }

    public void loadProgram(String codeName) throws Exception {

        // temporary
        String hddName = "hdd.txt";
        String line;
        String[] tokens;
        try(BufferedReader br = new BufferedReader(new FileReader(hddName))) {
            int wordIndex = 0;

            while(!(line = br.readLine()).equals(codeName)){

            }

            while((line = br.readLine()) != null){
                if(line.equals("")){
                    break;
                }
                //Checks if code is not too big
                if(wordIndex >= 128) {
                    throw new Exception("4");
                }

                // kolkas apsieinam be sito ifo
                if(validateKeyword(line)){
                    if(line.matches("[C][O][D][E]") || line.matches("[D][A][T][A]"))
                        continue;
                    
                    if(line.matches("[D][W]\\s.+")){
                        tokens = line.split(" ");
                        wordIndex++;
                        memory.pushData(tokens[1]);
                    } else {
                        memory.pushCode(line);
                        wordIndex++;
                    }
                }
            }
        } catch (FileNotFoundException fe) {
            throw new Exception("HDD file was not found!");
        } catch (NullPointerException e){
            throw new Exception("Program with specified name was not found in hdd");
        } catch (Exception e) {
            if(e.getMessage().substring(0, 1).matches("\\d")) {
                //setPi(Byte.parseByte(e.getMessage().substring(0, 1)));
            }
        }
    }

    public boolean validateKeyword(String keyword) {
        switch (keyword) {
            case "CODE":
                return true;
            case "ADD" :
                return true;
            case "SUB" :
                return true;
            case "CMP" :
                return true;
            case "HALT" :
                return true;
            case "PRTS" :
                return true;
            case "PRTN" :
                return true;
            case "READ" :
                return true;
            case "DATA" :
                return true;
            case "DN" :
                return true;
            default:
                return true;

        }
    }

    public void executeCommand(String command) {

        // cia kaip suprantu reiks suhandlint visas komandas kurios bus ivedamos
        switch(command) {
            case "ADD":
                commandProcessor.ADD();
                break;
            case "SUB":
                commandProcessor.SUB();
                break;
            case "MUL":
                commandProcessor.MUL();
                break;
            case "DIV":
                commandProcessor.DIV();
                break;
            case "CMP":
                commandProcessor.CMP();
                break;
            default:
                break;
        }

    }

}
