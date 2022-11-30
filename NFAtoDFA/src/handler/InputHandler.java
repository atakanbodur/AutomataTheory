package handler;

import model.NFA;
import model.NFAState;

import java.util.*;

public class InputHandler {

    public NFA initInputs(Scanner scanner) {
        NFA returnNFA = new NFA();
        String flag = null;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (Arrays.asList("ALPHABET", "STATES", "START", "FINAL", "TRANSITIONS").contains(line)) {
                flag = line;
            } else if (line.equals("END")) {
                return returnNFA;
            } else {
                switch (Objects.requireNonNull(flag)) {
                    case "ALPHABET":
                        initAlphabet(returnNFA, line);
                        break;
                    case "STATES":
                        initStates(returnNFA, line);
                        break;
                    case "START":
                        initStates(returnNFA, line, 0);
                        break;
                    case "FINAL":
                        initStates(returnNFA, line, 1);
                        break;
                    case "TRANSITIONS": //TODO
                        initRoutes(returnNFA, line);
                    default:
                        break;
                }
            }
        }
        return null;
    }

    private void initAlphabet(NFA nfa, String line) {
        nfa.getAlphabet().add(line);
    }

    private void initStates(NFA nfa, String line) {
        nfa.getStates().add(new NFAState(line));
            for (String alp : nfa.getAlphabet()) {
                nfa.findStateByName(line).getPossibleRoutes().put(alp, new ArrayList<>());
            }
    }

    private void initStates(NFA nfa, String line, int key) {
        //TODO: use nfa.findStateByName()
        ListIterator<NFAState> iterator = nfa.getStates().listIterator();
        for (int i = 0; i < nfa.getStates().size(); i++) {
            NFAState NFAState = iterator.next();
            if (NFAState.getStateName().equals(line)) {
                if (key == 0) { //START_STATE
                    NFAState.setStartState(true);
                    nfa.setStartState(NFAState);
                }
                if (key == 1) { //END_STATE
                    NFAState.setFinalState(true);
                    nfa.setEndState(NFAState);
                }
            }
        }
    }

    private void initRoutes(NFA nfa, String line) {
        line = line.replaceAll("\\s+", "");
        String sourceStateName = String.valueOf(line.charAt(0));
        String transitionValue = String.valueOf(line.charAt(1));
        String receiverStateName = String.valueOf(line.charAt(2));

        NFAState sourceNFAState = nfa.findStateByName(sourceStateName);
        NFAState receiverNFAState = nfa.findStateByName(receiverStateName);
        sourceNFAState.getPossibleRoutes().get(transitionValue).add(receiverNFAState);
    }
}
