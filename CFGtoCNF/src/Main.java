import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class Main {
    static Scanner scanner;
    static String filePath = "./src/G1.txt";
    static String flag = null;

    static class CFG {
        private String startVar = null;
        private List<String> terminalsList = new ArrayList<>();
        private List<String> variablesList = new ArrayList<>();
        private Map<String, CopyOnWriteArrayList<String>> rulesMap = new ConcurrentHashMap<>();
        private Map<String, ArrayList<ArrayList<Integer>>> indexOfNullable = new ConcurrentHashMap<>();


        /**
         * adds new startVariable to the cfg
         */
        void addNewStartVar() {
            for (Map.Entry<String, CopyOnWriteArrayList<String>> entry : rulesMap.entrySet()) {
                for (String rule : entry.getValue()) {
                    if (rule.contains(startVar)) {
                        String newStartVar = "S'";
                        rulesMap.put(newStartVar, new CopyOnWriteArrayList<>());
                        rulesMap.get(newStartVar).add(startVar);
                    }
                }
            }
        }

        void organizeRHS() {
            for (String var : rulesMap.keySet()) {
                for (String production : rulesMap.get(var)) {
                    boolean containsTerminalAndVar = isContainsTerminalAndVar(production);


                    while (production.length() > 2) {
                        String newVar = getAvailableLetter();
                        String newProduction=production.substring(0, 2);
                        String remainingProduction=production.substring(2);

                        String modifiedProduction = newVar + remainingProduction;
                        rulesMap.get(var).remove(production);
                        production = modifiedProduction;
                        if (!rulesMap.get(var).contains(modifiedProduction)) {
                            rulesMap.get(var).add(modifiedProduction);
                        }

                        variablesList.add(newVar);
                        CopyOnWriteArrayList<String> prods = new CopyOnWriteArrayList<>();
                        prods.add(newProduction);
                        rulesMap.put(newVar, prods);
                    }
                }
            }
            System.out.println("rulesMap after organizeRHS: " + rulesMap);

        }

        private boolean isContainsTerminalAndVar(String production) {
            List<String> chars = new ArrayList<>();
            for (int i = 0; i< production.length(); i++) {
                chars.add(String.valueOf(production.charAt(i)));
            }
            boolean containsTerminalAndVar = false;
            for (int i=0; i<chars.size();i++){
                for (int j=i+1; j<chars.size(); j++){
                    if (variablesList.contains(chars.get(i)) != variablesList.contains(chars.get(j))) {
                        containsTerminalAndVar = true;
                    }
                }
            }
            return containsTerminalAndVar;
        }

        String getAvailableLetter() {
            List<String> availableLetterList = new ArrayList<>();
            for (char i = 'A'; i <= 'Z'; i++) {
                availableLetterList.add(String.valueOf(i));
            }
            availableLetterList.removeAll(variablesList);
            return availableLetterList.get(0);
        }

        void removeUnitProductions() {
            for (String var : variablesList) {
                List<String> unitProductions = checkIfContainsUnitProduction(var);
                if (unitProductions != null) {
                    for (String unitProduction : unitProductions) {
                        CopyOnWriteArrayList<String> unitVarProductions = rulesMap.get(unitProduction);
                        for (String prod : unitVarProductions) {
                            rulesMap.get(var).add(prod);
                        }
                        rulesMap.get(var).remove(unitProduction);
                    }
                }
            }
            System.out.println("rulesMap after removeUnitProductions: " + rulesMap);
        }

        /**
         * @param var
         * @return list if var contains unit production else null
         */
        List<String> checkIfContainsUnitProduction(String var) {
            List<String> unitProductions = new CopyOnWriteArrayList<>();
            for (String production : rulesMap.get(var)) {
                if (production.length() == 1) {
                    String productionChar = String.valueOf(production.charAt(0));
                    if (variablesList.contains(productionChar) && !productionChar.equals(var)) {
                        unitProductions.add(production);
                    }
                }
            }
            if (unitProductions.isEmpty()) return null;
            else return unitProductions;
        }

        void removalOfNullProductions() {

            List<String> nullableVars = getNullableVars();
            while (!nullableVars.isEmpty()) {
                System.out.println("nullable vars are : " + nullableVars);
                initIndexOfNullable(nullableVars);
                createNewProductionsFromNullables();
                for (String key : indexOfNullable.keySet()) {
                    indexOfNullable.remove(key);
                }
                nullableVars.removeAll(nullableVars);
                nullableVars = getNullableVars();
            }

            System.out.println("rulesMap after removalOfNullProductions: " + rulesMap);

        }

        /**
         * Foreach nullableProduction, traverse the given indexes and remove each of them from the current production
         * If newProduction is unique, add it to the rulesMap
         */
        private void createNewProductionsFromNullables() {
            for (String variable : rulesMap.keySet()) {
                String newProduction = null;
                for (String nullableProduction : indexOfNullable.keySet()) {
                    for (String a : rulesMap.get(variable)) {
                        if (nullableProduction.equals(a)) {
                            for (ArrayList<Integer> subset : indexOfNullable.get(nullableProduction)) {
                                StringBuilder sb = new StringBuilder();
                                List<Character> list = nullableProduction.chars().mapToObj(e -> (char) e).toList();
                                List<Character> characters = new ArrayList<>(list);
                                int balance = 0;
                                for (Integer integer : subset) {
                                    characters.remove(integer - balance);
                                    balance++;
                                }
                                for (Character c : characters) {
                                    sb.append(c);
                                }
                                newProduction = sb.toString();
                                CopyOnWriteArrayList<String> current = rulesMap.get(variable);
                                if (!current.contains(newProduction)) if (newProduction.equals("")) current.add("e");
                                else current.add(newProduction);
                            }
                        }
                    }
                }
            }
        }

        /**
         * initializes indexes of nullable objects
         */
        private void initIndexOfNullable(List<String> nullableVars) {
            for (String var : rulesMap.keySet()) {
                for (String production : rulesMap.get(var)) {
                    for (String nullableVar : nullableVars) {
                        if (production.contains(nullableVar)) {
                            ArrayList<Integer> locationsOfNullVars = getNullableVarLocation(nullableVar, production);
                            ArrayList<ArrayList<Integer>> list = new ArrayList<>();
                            int n = locationsOfNullVars.size();
                            for (int i = 0; i < locationsOfNullVars.size(); i++) {
                                initIndexOfNullable(production, locationsOfNullVars, n, i + 1, list);
                            }
                        }
                    }
                }
            }
        }

        /**
         * @return the nullable variables list
         */
        List<String> getNullableVars() {
            List<String> nullableVars = new CopyOnWriteArrayList<>();
            for (Map.Entry<String, CopyOnWriteArrayList<String>> entry : rulesMap.entrySet()) {
                String key = entry.getKey();
                for (String production : entry.getValue()) {
                    if (production.contains("e")) {
                        rulesMap.get(key).remove(production);
                        nullableVars.add(entry.getKey());
                    }
                }
            }
            return nullableVars;
        }

        /**
         * @return the locations of the nullable variable in the production
         */
        ArrayList<Integer> getNullableVarLocation(String nullableVar, String production) {
            ArrayList<Integer> locations = new ArrayList<>();
            String temp = production;
            while (temp.contains(nullableVar)) {
                int index = temp.indexOf(nullableVar);
                locations.add(index);
                temp = temp.replaceFirst(nullableVar, "!");
            }
            return locations;
        }

        void subsetUtility(String production, ArrayList<Integer> arr, ArrayList<Integer> data, int start, int end, int index, int r, ArrayList<ArrayList<Integer>> integerArrayList) {
            if (index == r) {
                ArrayList<Integer> list = new ArrayList<>();
                for (int j = 0; j < r; j++) {
                    Integer datum = data.get(j);
                    list.add(datum);
                }
                integerArrayList.add(list);
                return;
            }
            for (int i = start; i <= end && end - i + 1 >= r - index; i++) {
                data.set(index, arr.get(i));
                subsetUtility(production, arr, data, i + 1, end, index + 1, r, integerArrayList);
            }
            //System.out.println("arr for i="+ index+" "+ integerArrayList);
            this.indexOfNullable.put(production, integerArrayList);
        }

        void initIndexOfNullable(String production, ArrayList<Integer> arr, int n, int r, ArrayList<ArrayList<Integer>> list) {
            ArrayList<Integer> data = new ArrayList<>();
            for (int i = 0; i < r; i++) {
                data.add(-1);
            }
            subsetUtility(production, arr, data, 0, n - 1, 0, r, list);
        }


    }


    public static void main(String[] args) {
        CFG cfg = readFile();
        assert cfg != null;
        cfg.addNewStartVar();
        cfg.removalOfNullProductions();
        cfg.removeUnitProductions();
        cfg.organizeRHS();
    }

    private static CFG readFile() {
        try {
            scanner = new Scanner(new File(filePath));
        } catch (Exception e) {
            System.out.println("File not found. Current working directory is :" + System.getProperty("user.dir"));
        }
        try {
            CFG cfg = new CFG();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (Arrays.asList("NON-TERMINAL", "TERMINAL", "RULES", "START").contains(line)) {
                    flag = line;
                } else {
                    switch (Objects.requireNonNull(flag)) {
                        case "NON-TERMINAL" -> cfg.variablesList.add(line);
                        case "TERMINAL" -> cfg.terminalsList.add(line);
                        case "RULES" -> initRules(line, cfg.rulesMap);
                        case "START" -> cfg.startVar = line;
                        default -> {
                        }
                    }
                }
            }
            return cfg;
        } catch (Exception e) {
            System.out.println("Parsing unsuccessful");
        }
        return null;
    }


    /**
     * Rule format is "S:00S"
     * Our key is line.charAt(0)
     * Our value is the remaining str after ":"
     */
    private static void initRules(String line, Map<String, CopyOnWriteArrayList<String>> rulesMap) {
        String variable = String.valueOf(line.charAt(0));

        String result = line.substring(0, 0) + line.substring(1);
        result = result.replaceAll(":", "");
        String rule = result;

        if (rulesMap.containsKey(variable)) {
            rulesMap.get(variable).add(rule);
        } else {
            rulesMap.put(variable, new CopyOnWriteArrayList<>());
            rulesMap.get(variable).add(rule);
        }
    }


}