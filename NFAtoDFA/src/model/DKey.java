package model;

import java.util.ArrayList;
import java.util.List;

public class DKey {
    List<NFAState> nfaStateList = new ArrayList<>();
    String transitionValue;

    String dfaStateName = "";

    public String getDfaStateName() {
        return dfaStateName;
    }

    public void setDfaStateName(String dfaStateName) {
        this.dfaStateName = dfaStateName;
    }

    public List<NFAState> getNfaStateList() {
        return nfaStateList;
    }

    public void setNfaStateList(List<NFAState> nfaStateList) {
        this.nfaStateList = nfaStateList;
    }

    public String getTransitionValue() {
        return transitionValue;
    }

    public void setTransitionValue(String transitionValue) {
        this.transitionValue = transitionValue;
    }

    public DKey(List<NFAState> nfaStateList, String transitionValue) {
        this.nfaStateList = nfaStateList;
        this.transitionValue = transitionValue;
    }

    @Override
    public String toString() {
        return "DKey{" +
                "nfaStateList=" + nfaStateList +
                ", transitionValue='" + transitionValue + '\'' +
                '}';
    }
}
