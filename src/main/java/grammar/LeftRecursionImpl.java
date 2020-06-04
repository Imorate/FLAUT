package grammar;

import grammar.interfaces.LeftRecursion;
import grammar.model.Grammar;
import lombok.Data;
import utils.ProductionType;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

@Data
public class LeftRecursionImpl implements LeftRecursion {

    private Grammar grammar;

    public LeftRecursionImpl(Grammar grammar) {
        this.grammar = grammar;
    }

    private boolean hasLeftRecursionFlag(Map.Entry<String, Set<String>> entry) {
        for (String rule : entry.getValue()) {
            if (hasLeftRecursion(entry.getKey(), rule)) {
                return true;
            }
        }
        return false;
    }

    public Grammar fixLeftRecursion() {
        Map<String, Set<String>> grammarProduction = new LinkedHashMap<>();
        for (Map.Entry<String, Set<String>> entry : this.grammar.getProductions().entrySet()) {
            boolean leftRecursionFlag = hasLeftRecursionFlag(entry);
            if (!leftRecursionFlag) {
                grammarProduction.put(entry.getKey(), entry.getValue());
            } else {
                leftRecursionProductions(entry, grammarProduction);
            }
        }
        this.grammar.setProductions(grammarProduction);
        return this.grammar;
    }

    private void leftRecursionProductions(Map.Entry<String, Set<String>> entry, Map<String, Set<String>> productions) {
        Set<String> mainProduction = new LinkedHashSet<>();
        Set<String> secondaryProduction = new LinkedHashSet<>();
        for (String rule : entry.getValue()) {
            if (ProductionType.isTerminal(rule)) {
                mainProduction.add(rule + entry.getKey() + "'");
            } else if (ProductionType.isNonTerminal(rule) && !hasLeftRecursion(entry.getKey(), rule)) {
                mainProduction.add(rule + entry.getKey() + "'");
            } else if (ProductionType.isNonTerminal(rule) && hasLeftRecursion(entry.getKey(), rule)) {
                secondaryProduction.add(rule.substring(1).concat(entry.getKey() + "'"));
            } else {
                mainProduction.add(rule);
            }
        }
        secondaryProduction.add("λ");
        productions.put(entry.getKey(), mainProduction);
        productions.put(entry.getKey() + "'", secondaryProduction);
    }
}
