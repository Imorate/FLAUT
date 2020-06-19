package ir.imorate.grammar;

import ir.imorate.grammar.model.Grammar;
import ir.imorate.utils.VariableGenerator;
import lombok.Data;

import java.util.Map;
import java.util.Set;

@Data
public class GreibachNormalForm {
    private Grammar grammar;
    private VariableGenerator variableGenerator;
    private Map<String, Set<String>> newProductions;

    public GreibachNormalForm(Grammar grammar) {
        this.grammar = grammar;
        this.variableGenerator = new VariableGenerator(grammar);
    }

    public Grammar toGreibachNormalForm() {

        return this.grammar;
    }

    public Grammar fixGrammarForm() {
        
        return this.grammar;
    }

}
