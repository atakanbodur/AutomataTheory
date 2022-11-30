import logic.Converter;
import logic.FileReader;
import model.DFA;
import model.NFA;


public class Main {
    public static void main(String[] args){
        FileReader fileReader = new FileReader("NFA1.txt");
        Converter converter = new Converter();
        NFA nfa = fileReader.readFile();
        DFA dfa = new DFA();
        if (nfa!=null){
           dfa = converter.convert(nfa);
        }
        else System.out.println("File read unsuccessful");
        if (dfa!=null){
            dfa.printTransitions();
        }
        else System.out.println("Conversion unsuccessful");
    }
}