package logic;

import handler.InputHandler;
import model.NFA;

import java.io.File;
import java.util.Scanner;

public class FileReader {
    private final InputHandler inputHandler = new InputHandler();
    private Scanner scanner;
    private final String fileName;

    public FileReader(String fileName) {
        this.fileName = fileName;
    }

    public FileReader() {
        this.fileName = "NFA1.txt";
    }

    public NFA readFile(){
        try {
            scanner = new Scanner(new File("./inputs/"+fileName));
        }
        catch (Exception e){
            System.out.println("File not found. Current working directory is :" + System.getProperty("user.dir"));
            return null;
        }
        NFA nfa = inputHandler.initInputs(scanner);
        return nfa;
    }
}
