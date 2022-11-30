package model;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class NFA {
    private List<String> alphabet = new ArrayList<>();
    private List<NFAState> NFAStates = new ArrayList<>();
    private NFAState startNFAState;
    private NFAState endNFAState;
    public NFAState findStateByName(String stateName){
        ListIterator<NFAState> iterator = this.NFAStates.listIterator();
        for (int i = 0; i < this.NFAStates.size(); i++) {
            NFAState NFAState = iterator.next();
            if (NFAState.getStateName().equals(stateName)) {
                return NFAState;
            }
        }
        return new NFAState("ERR"); //TODO: mechanism
    }

    public List<String> getAlphabet() {
        return alphabet;
    }

    public void setAlphabet(List<String> alphabet) {
        this.alphabet = alphabet;
    }

    public List<NFAState> getStates() {
        return NFAStates;
    }

    public void setStates(List<NFAState> NFAStates) {
        this.NFAStates = NFAStates;
    }

    public NFAState getStartState() {
        return startNFAState;
    }

    public void setStartState(NFAState startNFAState) {
        this.startNFAState = startNFAState;
    }

    public NFAState getEndState() {
        return endNFAState;
    }

    public void setEndState(NFAState endNFAState) {
        this.endNFAState = endNFAState;
    }
}
