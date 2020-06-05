package grammar;

import grammar.interfaces.GrammarParser;
import grammar.model.Grammar;
import lombok.Data;

import java.util.Map;
import java.util.Set;

@Data
public class FinalGrammar {
    private Grammar grammar;

    public FinalGrammar() {
        GrammarParser grammarParser = new GrammarParserImpl();
        this.grammar = grammarParser.parseGrammar();
    }

    public Grammar mainGrammar() {
        return this.grammar;
    }

    public Grammar leftRecursionGrammar() {
        return new LeftRecursionImpl(this.grammar).fixLeftRecursion();
    }

    public Grammar removeLambdaGrammar() {
        return new RemoveLambdaImpl(leftRecursionGrammar()).removeLambda();
    }

    public String formattedGrammar() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, Set<String>> entry : this.grammar.getProductions().entrySet()) {
            stringBuilder.append(entry.getKey()).append("\t->\t");
            entry.getValue().forEach(s -> stringBuilder.append(s).append("|"));
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            stringBuilder.append("\n");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }
}
