package model;

import java.util.*;

public class NFAState {
    private String stateName;
    private Map<String, ArrayList<NFAState>> possibleRoutes = new HashMap<>();
    private boolean isFinalState;
    private boolean isStartState;

    public NFAState(String stateName) {
        this.stateName = stateName;
    }

    public List<NFAState> findRouteByTransitionValue(String transitionValue) {
        return possibleRoutes.get(transitionValue);
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public Map<String, ArrayList<NFAState>> getPossibleRoutes() {
        return possibleRoutes;
    }

    public void setPossibleRoutes(Map<String, ArrayList<NFAState>> possibleRoutes) {
        this.possibleRoutes = possibleRoutes;
    }

    public boolean isFinalState() {
        return isFinalState;
    }

    public void setFinalState(boolean finalState) {
        isFinalState = finalState;
    }

    public boolean isStartState() {
        return isStartState;
    }

    public void setStartState(boolean startState) {
        isStartState = startState;
    }



    public void print(){
        List<String> lines = new ArrayList<>();
        possibleRoutes.forEach((key, value) -> {
            ListIterator<NFAState> iterator = value.listIterator();
            for (int i = 0; i < value.size(); i++) {
                String line = stateName + " " + key + " " + iterator.next().stateName;
                lines.add(line);
            }
        });
        Set<String> set = new HashSet<>(lines);
        lines.clear();
        lines.addAll(set);
        for (String l : lines) {
            System.out.println(l);
        }
    }


}
