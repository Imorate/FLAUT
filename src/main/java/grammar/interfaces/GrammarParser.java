package grammar.interfaces;

import grammar.model.Grammar;

import java.util.Map;
import java.util.Set;

public interface GrammarParser {
    Grammar parseGrammar();

    Map<String, Set<String>> mapProductions(String productionStr);

    default boolean isAlpha(String str) {
        return str.matches("[A-Z0-9]+'?");
    }

    default boolean isBeta(String str) {
        return str.matches("[a-zA-Z0-9]+'?|λ");
    }
}
