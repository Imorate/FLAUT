package ir.imorate.grammar.model;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@Data
public class Grammar{
    private char startVariable;
    private Map<String, Set<String>> productions;

    public Grammar() {
        this.startVariable = 'S';
        this.productions = new LinkedHashMap<>();
    }

    public Grammar(char startVariable, Map<String, Set<String>> productions) {
        this.startVariable = startVariable;
        this.productions = productions;
    }
}
