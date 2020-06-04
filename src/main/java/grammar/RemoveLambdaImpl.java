package grammar;

import grammar.model.Grammar;
import lombok.Data;
import utils.ProductionType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
public class RemoveLambdaImpl {
    private final Grammar grammar;

    public RemoveLambdaImpl(Grammar grammar) {
        this.grammar = grammar;
    }

    public Grammar removeLambda() {
        List<String> lambdaList = calculateLambdaList(grammar);
        return null;
    }

    public List<String> calculateLambdaList(Grammar grammar) {
        List<String> lambdaList = new ArrayList<>();
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
