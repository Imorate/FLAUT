package ir.imorate.grammar;

import ir.imorate.grammar.interfaces.GrammarParser;
import ir.imorate.grammar.model.Grammar;
import lombok.Data;
import ir.imorate.utils.GrammarFile;

import java.util.*;
import java.util.regex.Pattern;

@Data
public class GrammarParserImpl implements GrammarParser {

    private Map<String, Set<String>> productions;

    public GrammarParserImpl() {
        this.productions = new LinkedHashMap<>();
    }

    @Override
    public Grammar parseGrammar() {
        Grammar grammar = new Grammar();
        Optional<List<String>> grammarFileContent = new GrammarFile().readGrammar();
        grammarFileContent.ifPresent(list -> grammar.setStartVariable(list.get(0).charAt(0)));
        grammarFileContent.ifPresent(lineList -> lineList.forEach(this::mapProductions));
        grammar.setProductions(this.productions);
        return grammar;
    }

    @Override
    public Map<String, Set<String>> mapProductions(String productionStr) {
        Pattern pattern = Pattern.compile("[|]|->|\\s");
        String[] productionArr = pattern.split(productionStr);
        Set<String> ruleSet = new LinkedHashSet<>();
        for (int i = 1; i < productionArr.length && isBeta(productionArr[i]); i++) {
            if(!productionArr[0].equals(productionArr[i])){
                ruleSet.add(productionArr[i].trim());
            }
        }
        if (isAlpha(productionArr[0]) && !this.productions.containsKey(productionArr[0])) {
            this.productions.put(productionArr[0], ruleSet);
        } else if (isAlpha(productionArr[0])) {
            this.productions.get(productionArr[0]).addAll(ruleSet);
        }
        return this.productions;
    }
}
