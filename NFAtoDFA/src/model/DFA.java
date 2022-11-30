package model;

import java.util.*;

public class DFA {
    private List<String> alphabet = new ArrayList<>();

    Map<DKey, List<NFAState>> transitionTable = new LinkedHashMap<>();

    private Set<DKey> endNFAState = new HashSet<>();
    private NFAState startNFAState;

    public void printTransitions() {
        initDkeyName();
        System.out.println("ALPHABET");
        for (String letter : alphabet) {
            System.out.println(letter);
        }
        System.out.println("STATES");
        List<String> stateNames = new ArrayList<>();
        for (DKey dKey : transitionTable.keySet()) {
            stateNames.add(dKey.getDfaStateName());
        }
        Set<String> set = new HashSet<>(stateNames);
        stateNames.clear();
        stateNames.addAll(set);
        for (String name : stateNames) {
            System.out.println(name);
        }
        System.out.println("START");
        System.out.println(startNFAState.getStateName());
        System.out.println("FINAL");
        Set<String> endStateNames = new HashSet<>();
        for (DKey dKey : endNFAState) {
            endStateNames.add(dKey.getDfaStateName());
        }
        for (String s : endStateNames) {
            System.out.println(s);
        }
        System.out.println("TRANSITIONS");
        for (DKey dfaState : transitionTable.keySet()) {
            System.out.println(
                    dfaState.getDfaStateName() + " "
                    + dfaState.getTransitionValue() + " "
                    + concatNFAListItemNames(transitionTable.get(dfaState))
            );
        }
        System.out.println("END");
    }

    private void initDkeyName() {
        for (DKey dKey : transitionTable.keySet()) {
            dKey.setDfaStateName(concatNFAListItemNames(dKey.getNfaStateList()));
        }
    }

    private String concatNFAListItemNames(List<NFAState> states) {
        StringBuilder sb = new StringBuilder();
        for (NFAState s : states) {
            sb.append(s.getStateName());
        }
        return sb.toString();
    }

    public Map<DKey, List<NFAState>> getTransitionTable() {
        return transitionTable;
    }

    public void setTransitionTable(Map<DKey, List<NFAState>> transitionTable) {
        this.transitionTable = transitionTable;
    }

    public NFAState getStartNFAState() {
        return startNFAState;
    }

    public void setStartNFAState(NFAState startNFAState) {
        this.startNFAState = startNFAState;
    }

    public Set<DKey> getEndNFAState() {
        return endNFAState;
    }

    public void setEndNFAState(Set<DKey> endNFAState) {
        this.endNFAState = endNFAState;
    }

    public List<String> getAlphabet() {
        return alphabet;
    }

    public void setAlphabet(List<String> alphabet) {
        this.alphabet = alphabet;
    }

   /* public void printDFA() {
        System.out.println("ALPHABET");
        for (String letter : alphabet) {
            System.out.println(letter);
        }
        System.out.println("STATES");
        for (NFAState state : NFAStates) {
            System.out.println(state.getStateName());
        }
        System.out.println("START");
        System.out.println(startNFAState);
        System.out.println("FINAL");
        System.out.println(endNFAState);
        System.out.println("TRANSITIONS");
        for(NFAState state : NFAStates) {
            state.print();
        }
        System.out.println("END");

    }

    */
}
