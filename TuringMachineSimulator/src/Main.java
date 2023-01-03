import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        createTuringMachine();
    }

    static class TuringMachine {
        int inputAlphabetVariableCount = 0;
        int tapeAlphabetVariableCount = 0;
        int stateCount = 0;
        String startStateName;
        State startState;
        String acceptStateName;
        State acceptState;
        String rejectStateName;
        State rejectState;
        CopyOnWriteArrayList<String> inputAlphabet = new CopyOnWriteArrayList<>();
        CopyOnWriteArrayList<String> tapeAlphabet = new CopyOnWriteArrayList<>();
        CopyOnWriteArrayList<State> states = new CopyOnWriteArrayList<>();
        CopyOnWriteArrayList<String> tape = new CopyOnWriteArrayList<>();

        public TuringMachine() {
            tapeAlphabet.add("b");
        }

        public void finalizeStates() {
            states.forEach(s -> {
                if (s.symbol.equals(startStateName)) {
                    s.isStart = true;
                    startState = s;
                }
                if (s.symbol.equals(acceptStateName)) {
                    s.isAccept = true;
                    acceptState = s;
                }
                if (s.symbol.equals(rejectStateName)) {
                    s.isReject = true;
                    rejectState = s;
                }
            });
        }

        public void readTape() {
            ArrayList<String> traversedStates = new ArrayList<>();
            boolean isAccepted = false;

            State currState = startState;
            int currTapeIndex = 0;
            String inputValue;

            while (!currState.symbol.equals(acceptStateName) || !currState.symbol.equals(rejectStateName)) {
                traversedStates.add(currState.symbol);

                if (this.tape.size() <= currTapeIndex) {
                    this.tape.add("b");
                }

                try {
                    inputValue = this.tape.get(currTapeIndex);
                }
                catch (Exception e) {
                    break;
                }

                if (!this.tapeAlphabet.contains(inputValue)) {
                    break;
                }
                SKey currSKey = currState.rulesMap.get(inputValue);
                this.tape.set(0, currSKey.changeInputValueTo);
                if (currSKey.moveTo.equals("R")) {
                    currTapeIndex++;
                }
                else if (currSKey.moveTo.equals("L")) currTapeIndex--;
                else break;

                String nextStateSymbol = currSKey.goToState;
                for (State state : this.states) {
                    if (state.symbol.equals(nextStateSymbol)) {
                        currState = state;
                    }
                }

                if (currState.isReject) {
                    break;
                }

                if (currState.isAccept) {
                    isAccepted = true;
                    break;
                }
            }
            printTuring(traversedStates, isAccepted);
        }

        public void printTuring(ArrayList<String> traversedStates, boolean isAccepted) {
            String route = "";
            for (String s : traversedStates) {
                route = route.concat(" " + s);
            }
            System.out.println("ROUT:" + route);

            String result = "";
            if (isAccepted) {
                result = result.concat("accepted");
            }
            else  result = result.concat("rejected");
            System.out.println("RESULT: " + result);
        }
    }

    static class State {
        public String symbol = "b";
        public boolean isStart = false;
        public boolean isAccept = false;
        public boolean isReject = false;

        /**
         * Map< input value, SKey >
         */
        public Map<String, SKey> rulesMap = new HashMap<>();
    }

    static class SKey {
        String changeInputValueTo;
        String moveTo;
        String goToState;

        public SKey(String changeInputValueTo, String moveTo, String goToState) {
            this.changeInputValueTo = changeInputValueTo;
            this.moveTo = moveTo;
            this.goToState = goToState;
        }
    }

    static void createTuringMachine() throws FileNotFoundException {
        TuringMachine tm = new TuringMachine();
        Scanner scanner = new Scanner(new File("./src/input.txt"));
        String flag = null;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (Arrays.asList("(number of variables in input alphabet)", "(the input alphabet)", "(number of variables in tape alphabet)", "(the tape alphabet)", "(number of states)", "(states)", "(start state)", "(accept state)", "(reject state)", "(tape input)", "(transitions)").contains(line)) {
                flag = line;
            } else if (line.equals("(end)")) {
                tm.readTape();
            } else {
                switch (Objects.requireNonNull(flag)) {
                    case "(number of variables in input alphabet)":
                        tm.inputAlphabetVariableCount = Integer.parseInt(line);
                        break;
                    case "(the input alphabet)":
                        tm.inputAlphabet.add(line);
                        break;
                    case "(number of variables in tape alphabet)":
                        tm.tapeAlphabetVariableCount = Integer.parseInt(line);
                        break;
                    case "(the tape alphabet)":
                        tm.tapeAlphabet.add(line);
                        break;
                    case "(number of states)":
                        tm.stateCount = Integer.parseInt(line);
                        break;
                    case "(states)":
                        State state = new State();
                        state.symbol = line;
                        tm.states.add(state);
                        break;
                    case "(start state)":
                        tm.startStateName = line;
                        break;
                    case "(accept state)":
                        tm.acceptStateName = line;
                        break;
                    case "(reject state)":
                        tm.rejectStateName = line;
                        break;
                    case "(transitions)":
                        tm.finalizeStates();

                        char[] lineAsCharArr = line.toCharArray();
                        String currStateName = lineAsCharArr[0] + String.valueOf(lineAsCharArr[1]);
                        String readValue = String.valueOf(lineAsCharArr[3]);
                        String changeSymbolTo = String.valueOf(lineAsCharArr[5]);
                        String moveTo = String.valueOf(lineAsCharArr[7]);
                        String goToState = lineAsCharArr[9] + String.valueOf(lineAsCharArr[10]);

                        CopyOnWriteArrayList<State> states = tm.states;
                        states.forEach(s -> {
                            if (Objects.equals(s.symbol, currStateName)) {
                                SKey sKey = new SKey(changeSymbolTo, moveTo, goToState);
                                s.rulesMap.put(readValue, sKey);
                            }
                        });
                        break;
                    case "(tape input)":
                        char[] lineAsCharArray = line.toCharArray();
                        for (char c : lineAsCharArray) {
                            String charAsStr = String.valueOf(c);
                            tm.tape.add(charAsStr);
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }
}