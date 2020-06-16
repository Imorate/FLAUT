package ir.imorate.utils;

import ir.imorate.grammar.model.Grammar;
import lombok.Data;

import java.util.Set;
import java.util.Stack;

@Data
public class VariableGenerator {
    private Grammar grammar;
    private Stack<Character> variableStack;

    public VariableGenerator(Grammar grammar) {
        this.grammar = grammar;
        this.variableStack = new Stack<>();
        initialStack();
    }

    private void initialStack() {
        char grammarStartVariable = this.grammar.getStartVariable();
        Set<String> characterSet = this.grammar.getProductions().keySet();
        for (int i = 65; i < 91; i++) {
            char ch = (char) i;
            if (ch != grammarStartVariable && !characterSet.contains(String.valueOf(ch))) {
                this.variableStack.push((char) i);
            }
        }
    }

    public char popVariableStack() {
        if (!this.variableStack.isEmpty()) {
            return this.variableStack.pop();

        }
        return 0;
    }
}
