package ir.imorate.grammar;

import ir.imorate.grammar.interfaces.LeftRecursion;
import ir.imorate.grammar.model.Grammar;
import ir.imorate.utils.Constants;
import ir.imorate.utils.ProductionType;
import lombok.Data;

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

    @Override
    public Grammar fixLeftRecursion() {
        Map<String, Set<String>> grammarProduction = new LinkedHashMap<>();
        for (Map.Entry<String, Set<String>> entry : this.grammar.getProductions().entrySet()) {
            if (!hasLeftRecursionFlag(entry)) {
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
            if (ProductionType.isTerminalProduction(rule)) {
                mainProduction.add(rule + entry.getKey() + "'");
            } else if (ProductionType.isNonTerminalProduction(rule) && !hasLeftRecursion(entry.getKey(), rule)) {
                mainProduction.add(rule + entry.getKey() + "'");
            } else if (ProductionType.isNonTerminalProduction(rule) && hasLeftRecursion(entry.getKey(), rule)) {
                secondaryProduction.add(rule.substring(1).concat(entry.getKey() + "'"));
            } else {
                mainProduction.add(rule);
            }
        }
        secondaryProduction.add(Constants.LAMBDA);
        productions.put(entry.getKey(), mainProduction);
        productions.put(entry.getKey() + "'", secondaryProduction);
    }
}
