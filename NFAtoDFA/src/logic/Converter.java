package logic;

import model.*;

import java.util.*;

public class Converter {
    List<NFAState> statesQueue = new ArrayList<>();
    Map<DKey, List<NFAState>> transitionTable = new LinkedHashMap<>();
    List<List<NFAState>> temp = new ArrayList<>();

    public DFA convert(NFA nfa) {

        DFA returnDfa = new DFA();
        returnDfa.setAlphabet(nfa.getAlphabet());
        returnDfa.setStartNFAState(nfa.getStartState());

        NFAState startState = nfa.getStartState();
        List<NFAState> tstate = new ArrayList<>();
        tstate.add(startState);
        temp.add(tstate);
        while (!temp.isEmpty()) {
            for (int i = 0; i < temp.size(); i++) {
                List<NFAState> ll = temp.get(i);
                for (String s : nfa.getAlphabet()) {
                    Set<NFAState> value = new HashSet<>();
                    DKey dKey = new DKey(ll, s);
                    for (NFAState loopState : temp.get(i)) {
                        if (loopState.findRouteByTransitionValue(s) != null)
                            for (NFAState state : loopState.findRouteByTransitionValue(s)) {
                                value.add(state);
                            }
                    }
                    for (NFAState var : dKey.getNfaStateList()) {
                        if (var.isFinalState()) returnDfa.getEndNFAState().add(dKey);
                    }
                    if (value.isEmpty()) {

                    } else
                        transitionTable.put(dKey, new ArrayList<>(value));
                }
            }

            List<List<NFAState>> keyList = new ArrayList<>();
            for (Map.Entry<DKey, List<NFAState>> keys : transitionTable.entrySet()) {
                DKey key = keys.getKey();
                if (!keyList.contains(key.getNfaStateList())) keyList.add(key.getNfaStateList());
            }

            for (Map.Entry<DKey, List<NFAState>> values : transitionTable.entrySet()) {
                List<NFAState> val = values.getValue();
                if (!keyList.contains(val)) {
                    if (!temp.contains(val)) temp.add(val);
                } else temp.remove(val);
            }
        }
        returnDfa.setTransitionTable(transitionTable);
        return returnDfa;
    }


}