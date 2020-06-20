package ir.imorate.grammar;

import ir.imorate.grammar.model.Grammar;
import ir.imorate.utils.ProductionType;
import ir.imorate.utils.VariableGenerator;
import lombok.Data;

import java.util.*;

@Data
public class GreibachNormalForm {
    private Grammar grammar;
    private VariableGenerator variableGenerator;
    private Map<String, Set<String>> newProductions;

    public GreibachNormalForm(Grammar grammar) {
        this.grammar = grammar;
        this.variableGenerator = new VariableGenerator(grammar);
        this.newProductions = new LinkedHashMap<>();
    }

    public Grammar toGreibachNormalForm() {
        if (!isGrammarInGNF()) {
            this.grammar = fixGrammarForm();
            this.grammar = toGNF();
        }
        return this.grammar;
    }

    public Grammar fixGrammarForm() {
        while (!isGrammarRuleFirstCharacterTerminal()) {
            for (Map.Entry<String, Set<String>> entry : this.grammar.getProductions().entrySet()) {
                Set<String> productionSet = new LinkedHashSet<>();
                for (String rule : entry.getValue()) {
                    List<String> ruleList = ProductionType.toList(rule);
                    if (!isFirstCharacterOfRuleTerminal(rule)) {
                        productionSet.addAll(generateReplacedVarProduction(ruleList.get(0), rule));
                    } else {
                        productionSet.add(rule);
                    }
                }
                entry.setValue(productionSet);
            }
        }
        return this.grammar;
    }

    public Set<String> generateReplacedVarProduction(String var, String rule) {
        Set<String> productionSet = new LinkedHashSet<>();
        List<String> ruleList = ProductionType.toList(rule);
        ruleList.remove(0);
        String newRule = String.join("", ruleList);
        for (String rules : this.grammar.getProductions().get(var)) {
            productionSet.add(rules + newRule);
        }
        return productionSet;
    }

    public Grammar toGNF() {
        for (Map.Entry<String, Set<String>> entry : this.grammar.getProductions().entrySet()) {
            Set<String> ruleSet = new LinkedHashSet<>();
            for (String rule : entry.getValue()) {
                if (!ProductionType.isGreibachProduction(rule)) {
                    ruleSet.add(transform(rule));
                } else {
                    ruleSet.add(rule);
                }
            }
            entry.setValue(ruleSet);
        }
        this.grammar.getProductions().putAll(this.newProductions);
        return this.grammar;
    }

    public String transform(String rule) {
        List<String> ruleList = ProductionType.toList(rule);
        for (int i = 1; i < ruleList.size(); i++) {
            if (ruleList.get(i).matches("[a-z0-9]")) {
                String var = getVar(ruleList.get(i));
                if (var == null) {
                    String newVar = this.variableGenerator.popVariableStack();
                    this.newProductions.put(newVar, new LinkedHashSet<>(Collections.singleton(ruleList.get(i))));
                    ruleList.set(i, newVar);
                } else {
                    ruleList.set(i, var);
                }
            }
        }
        return String.join("", ruleList);
    }

    public boolean isVarInGNF(String var) {
        Set<String> varSet = this.grammar.getProductions().get(var);
        for (String rule : varSet) {
            if (!ProductionType.isGreibachProduction(rule)) {
                return false;
            }
        }
        return true;
    }

    public boolean isGrammarInGNF() {
        for (Map.Entry<String, Set<String>> entry : this.grammar.getProductions().entrySet()) {
            if (!isVarInGNF(entry.getKey())) {
                return false;
            }
        }
        return true;
    }

    public boolean isFirstCharacterOfRuleTerminal(String rule) {
        return ProductionType.toList(rule).get(0).matches("[a-z0-9]");
    }

    public boolean isGrammarRuleFirstCharacterTerminal() {
        for (Map.Entry<String, Set<String>> entry : this.grammar.getProductions().entrySet()) {
            for (String rule : entry.getValue()) {
                if (!isFirstCharacterOfRuleTerminal(rule)) {
                    return false;
                }
            }
        }
        return true;
    }

    public String getVar(String production) {
        for (Map.Entry<String, Set<String>> entry : this.grammar.getProductions().entrySet()) {
            String rule = String.valueOf(entry.getValue().toArray()[0]);
            if (entry.getValue().size() == 1 && rule.equals(production) && rule.matches("[a-z0-9]")) {
                return entry.getKey();
            }
        }
        for (Map.Entry<String, Set<String>> entry : this.newProductions.entrySet()) {
            String rule = String.valueOf(entry.getValue().toArray()[0]);
            if (entry.getValue().size() == 1 && rule.equals(production) && rule.matches("[a-z0-9]")) {
                return entry.getKey();
            }
        }
        return null;
    }

}
