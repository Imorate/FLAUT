package ir.imorate.grammar;

import ir.imorate.grammar.model.Grammar;
import ir.imorate.utils.VariableGenerator;
import lombok.Data;

@Data
public class ChomskyNormalForm {
    private Grammar grammar;
    private VariableGenerator variableGenerator;

    public ChomskyNormalForm(Grammar grammar) {
        this.grammar = grammar;
        this.variableGenerator = new VariableGenerator(grammar);
    }

    public Grammar toChomskyNormalForm() {

        return this.grammar;
    }
}
