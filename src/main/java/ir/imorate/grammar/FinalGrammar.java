package ir.imorate.grammar;

import ir.imorate.grammar.model.Grammar;
import lombok.Data;

import java.util.Map;
import java.util.Set;

@Data
public class FinalGrammar {
    private Grammar grammar;

    public FinalGrammar() {
        this.grammar = new GrammarParserImpl().parseGrammar();
    }

    public Grammar mainGrammar() {
        return this.grammar;
    }

    public Grammar leftRecursionGrammar() {
        this.grammar = new LeftRecursionImpl(this.grammar).fixLeftRecursion();
        return this.grammar;
    }

    public Grammar lambdaRemovalGrammar() {
        this.grammar = new LambdaRemovalRemovalImpl(this.grammar).removeLambda();
        return this.grammar;
    }

    public Grammar unitRemovalGrammar() {
        this.grammar = new UnitRemovalImpl(this.grammar).unitRemovalFix();
        return this.grammar;
    }

    public Grammar uselessProductionRemovalGrammar() {
        this.grammar = new UselessProductionRemovalImpl(this.grammar).uselessProductionRemovalFix();
        return this.grammar;
    }

    public Grammar toChomskyNormalForm() {
        this.grammar = new ChomskyNormalForm(this.grammar).toChomskyNormalForm();
        return this.grammar;
    }

    public Grammar toGreibachNormalForm() {
        this.grammar = new GreibachNormalForm(this.grammar).toGreibachNormalForm();
        return this.grammar;
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
