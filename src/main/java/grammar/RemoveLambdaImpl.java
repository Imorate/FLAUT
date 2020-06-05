package grammar;

import grammar.interfaces.RemoveLambda;
import grammar.model.Grammar;
import lombok.Data;
import utils.ProductionType;

import java.util.*;

@Data
public class RemoveLambdaImpl implements RemoveLambda {
    private final Grammar grammar;

    public RemoveLambdaImpl(Grammar grammar) {
        this.grammar = grammar;
    }

    @Override
    public Grammar removeLambda() {
        Queue<String> lambdaList = calculateLambda(grammar);
        System.out.println("Main Lambda list: \t" + lambdaList);
        while (!lambdaList.isEmpty()) {
            for (Map.Entry<String, Set<String>> entry : grammar.getProductions().entrySet()) {
                entry.setValue(generateRuleSet(entry, lambdaList));
            }
            lambdaList.remove();
            System.out.println("Lambda list: \t" + lambdaList);
        }
        return this.grammar;
    }

    @Override
    public Set<String> generateRuleSet(Map.Entry<String, Set<String>> entry, Queue<String> lambdaList) {
        Set<String> ruleSet = new LinkedHashSet<>();
        for (String rule : entry.getValue()) {
            if (lambdaList.peek() != null && rule.contains(lambdaList.peek())) {
                String replacedStr = rule.replace(lambdaList.peek(), "");
                if (replacedStr.equals("")) {
                    ruleSet.add(rule);
                    lambdaList.add(entry.getKey());
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
    public Queue<String> calculateLambda(Grammar grammar) {
        Queue<String> lambdaList = new LinkedList<>();
        for (Map.Entry<String, Set<String>> entry : grammar.getProductions().entrySet()) {
            for (String rule : entry.getValue()) {
                if (ProductionType.isLambda(rule) && !rule.equals(String.valueOf(grammar.getStartVariable()))) {
                    lambdaList.add(entry.getKey());
                }
            }
        }
        return lambdaList;
    }
}
