package ir.imorate.grammar;

import ir.imorate.grammar.interfaces.LambdaRemoval;
import ir.imorate.grammar.model.Grammar;
import ir.imorate.utils.ProductionType;
import lombok.Data;

import java.util.*;

@Data
public class LambdaRemovalRemovalImpl implements LambdaRemoval {
    private Grammar grammar;

    public LambdaRemovalRemovalImpl(Grammar grammar) {
        this.grammar = grammar;
    }

    @Override
    public Grammar removeLambda() {
        this.grammar = fixSingleLambdaProduction();
        Queue<String> lambdaList = calculateLambda();
        while (!lambdaList.isEmpty()) {
            for (Map.Entry<String, Set<String>> entry : grammar.getProductions().entrySet()) {
                entry.setValue(generateRuleSet(entry, lambdaList));
            }
            lambdaList.remove();
        }
        return this.grammar;
    }

    @Override
    public Set<String> generateRuleSet(Map.Entry<String, Set<String>> entry, Queue<String> lambdaList) {
        Set<String> ruleSet = new LinkedHashSet<>();
        for (String rule : entry.getValue()) {
            if (lambdaList.peek() != null && rule.contains(lambdaList.peek())) {
                String replacedStr;
                if (lambdaList.peek().contains("'")) {
                    replacedStr = rule.replace(lambdaList.peek(), "");
                } else {
                    replacedStr = replaceWithoutLeftRecursion(lambdaList.peek(), rule);
                }
                if (replacedStr.equals("") && !entry.getKey().equals(String.valueOf(this.grammar.getStartVariable()))) {
                    ruleSet.add(rule);
                    lambdaList.add(entry.getKey());
                } else if (replacedStr.equals("") && entry.getKey().equals(String.valueOf(this.grammar.getStartVariable()))) {
                    ruleSet.add(rule);
                    ruleSet.add("λ");
                } else {
                    ruleSet.add(rule);
                    ruleSet.add(replacedStr);
                }
            } else if (!ProductionType.isLambda(rule)) {
                ruleSet.add(rule);
            }
        }
        return ruleSet;
    }

    @Override
    public Queue<String> calculateLambda() {
        Queue<String> lambdaList = new LinkedList<>();
        for (Map.Entry<String, Set<String>> entry : this.grammar.getProductions().entrySet()) {
            for (String rule : entry.getValue()) {
                if (ProductionType.isLambda(rule) && !rule.equals(String.valueOf(this.grammar.getStartVariable()))) {
                    lambdaList.add(entry.getKey());
                }
            }
        }
        return lambdaList;
    }

    @Override
    public Grammar fixSingleLambdaProduction() {
        Set<String> singleLambdaProductionSet = singleLambdaProductionSet();
        for (String singleLambdaProduction : singleLambdaProductionSet) {
            this.grammar.getProductions().remove(singleLambdaProduction);
            for (Map.Entry<String, Set<String>> entry : this.grammar.getProductions().entrySet()) {
                Set<String> fixedSet = new LinkedHashSet<>();
                for (String rule : entry.getValue()) {
                    if (rule.contains(singleLambdaProduction)) {
                        fixedSet.add(rule.replace(singleLambdaProduction, ""));
                    } else {
                        fixedSet.add(rule);
                    }
                }
                entry.setValue(fixedSet);
            }
        }
        return this.grammar;
    }

    @Override
    public Set<String> singleLambdaProductionSet() {
        Set<String> singleLambdaSet = new LinkedHashSet<>();
        for (Map.Entry<String, Set<String>> entry : this.grammar.getProductions().entrySet()) {
            for (String rule : entry.getValue()) {
                if (ProductionType.isLambda(rule) && entry.getValue().size() == 1) {
                    singleLambdaSet.add(entry.getKey());
                }
            }
        }
        return singleLambdaSet;
    }

    @Override
    public String replaceWithoutLeftRecursion(String var, String rule) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < rule.length(); i++) {
            char ch = rule.charAt(i);
            if (ch != var.charAt(0)) {
                result.append(ch);
            } else if (ch == var.charAt(0) && i + 1 < rule.length() && rule.charAt(i + 1) == '\'') {
                result.append(ch);
            }
        }
        return result.toString();
    }
}
