package grammar;

import grammar.interfaces.GrammarParser;
import grammar.model.Grammar;
import lombok.Data;

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
}
