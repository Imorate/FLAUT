package ir.imorate.grammar;

import ir.imorate.grammar.model.Grammar;
import ir.imorate.utils.ProductionType;
import ir.imorate.utils.VariableGenerator;
import lombok.Data;

import java.util.*;

@Data
public class ChomskyNormalForm {
    private Grammar grammar;
    private VariableGenerator variableGenerator;
    private Map<String, Set<String>> newProductions;

    public ChomskyNormalForm(Grammar grammar) {
        this.grammar = grammar;
        this.variableGenerator = new VariableGenerator(grammar);
    }

    public Grammar toChomskyNormalForm() {
        // TODO Grammar with left recursion not supported
        if (isntInChomskyNormalForm()) {
            this.grammar = initializeToCNF();
            while (isntInChomskyNormalForm()) {
                this.grammar = toCNF();
            }
        }
        return this.grammar;
    }

    public Grammar toCNF() {
        this.newProductions = new LinkedHashMap<>();
        for (Map.Entry<String, Set<String>> entry : this.grammar.getProductions().entrySet()) {
            Set<String> ruleSet = new LinkedHashSet<>();
            for (String rule : entry.getValue()) {
                if (isInPreCNF(rule) && rule.length() > 0) {
                    if (rule.length() == 2) {
                        ruleSet.add(rule);
                    } else {
                        String[] splitRule = pickFirstVar(rule);
                        String var = this.variableGenerator.popVariableStack();
                        ruleSet.add(splitRule[0] + var);
                        this.newProductions.put(var, new LinkedHashSet<>(Collections.singleton(splitRule[1])));
                    }
                } else {
                    ruleSet.add(rule);
                }
            }
            entry.setValue(ruleSet);
        }
        this.grammar.getProductions().putAll(this.newProductions);
        return this.grammar;
    }

    public String[] pickFirstVar(String rule) {
        return new String[]{rule.substring(0, 1), rule.substring(1)};
    }

    public Grammar initializeToCNF() {
        this.newProductions = new LinkedHashMap<>();
        for (Map.Entry<String, Set<String>> entry : this.grammar.getProductions().entrySet()) {
            Set<String> ruleSet = new LinkedHashSet<>();
            for (String rule : entry.getValue()) {
                if (isInTerminalChomskyNormalForm(rule)) {
                    ruleSet.add(rule);
                } else {
                    ruleSet.add(transformRuleToCFG(rule));
                }
            }
            entry.setValue(ruleSet);
        }
        this.grammar.getProductions().putAll(newProductions);
        return this.grammar;
    }

    public String transformRuleToCFG(String rule) {
        List<String> ruleList = ProductionType.toList(rule);
        StringBuilder stringBuilder = new StringBuilder();
        for (String unitRule : ruleList) {
            if (unitRule.matches("[a-z0-9]")) {
                String production = getVar(unitRule);
                if (production == null) {
                    String var = this.variableGenerator.popVariableStack();
                    stringBuilder.append(var);
                    this.newProductions.put(var, new LinkedHashSet<>(Collections.singleton(unitRule)));
                } else {
                    stringBuilder.append(production);
                }
            } else {
                stringBuilder.append(unitRule);
            }
        }
        return stringBuilder.toString();
    }

    public boolean isntInChomskyNormalForm() {
        for (Map.Entry<String, Set<String>> entry : this.grammar.getProductions().entrySet()) {
            for (String rule : entry.getValue()) {
                if (!ProductionType.isChomskyProduction(rule)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isInTerminalChomskyNormalForm(String rule) {
        return rule.matches("[a-z]");
    }

    public boolean isInPreCNF(String rule) {
        return rule.matches("[A-Z]{2,}");
    }

    public String getVar(String production) {
        for (Map.Entry<String, Set<String>> entry : this.newProductions.entrySet()) {
            String rule = String.valueOf(entry.getValue().toArray()[0]);
            if (entry.getValue().size() == 1 && rule.equals(production) && isInTerminalChomskyNormalForm(rule)) {
                return entry.getKey();
            }
        }
        return null;
    }
}
