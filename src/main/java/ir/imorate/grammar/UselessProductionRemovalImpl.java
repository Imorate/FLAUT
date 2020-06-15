package ir.imorate.grammar;

import ir.imorate.grammar.model.Grammar;
import ir.imorate.graph.Graph;
import ir.imorate.graph.Node;
import ir.imorate.utils.ProductionType;
import lombok.Data;

import java.util.*;

@Data
public class UselessProductionRemovalImpl {
    private Grammar grammar;
    private Graph<String> dependencyGraph;

    public UselessProductionRemovalImpl(Grammar grammar) {
        this.grammar = grammar;
        this.dependencyGraph = mapToDependencyGraph();
    }

    public Grammar uselessProductionRemovalFix() {
        Set<Node<String>> participatedNodes = bfsTraverse(String.valueOf(this.grammar.getStartVariable()));
        Grammar grammar = new Grammar();
        grammar.setStartVariable(this.grammar.getStartVariable());
        grammar.getProductions().put(String.valueOf(grammar.getStartVariable()),
                this.grammar.getProductions().get(String.valueOf(grammar.getStartVariable())));
        for (Node<String> node : participatedNodes) {
            if (isTerminalVar(node.getLabel())) {
                grammar.getProductions().put(node.getLabel(), this.grammar.getProductions().get(node.getLabel()));
            }
        }
        return fixUselessVariables(grammar, this.grammar);
    }

    public Grammar fixUselessVariables(Grammar grammar, Grammar oldGrammar) {
        Set<String> variables = oldGrammar.getProductions().keySet();
        variables.removeAll(grammar.getProductions().keySet());
        for (Map.Entry<String, Set<String>> entry : grammar.getProductions().entrySet()) {
            Set<String> productions = new LinkedHashSet<>();
            for (String rule : entry.getValue()) {
                if (ProductionType.isNonTerminalProduction(rule) && isValidRule(rule, variables) || ProductionType.isTerminalProduction(rule)) {
                    productions.add(rule);
                }
            }
            entry.setValue(productions);
        }
        return grammar;
    }

    public boolean isValidRule(String str, Set<String> variables) {
        for (String var : variables) {
            if (str.contains(var)) {
                return false;
            }
        }
        return true;
    }

    public Graph<String> mapToDependencyGraph() {
        Graph<String> graph = new Graph<>();
        for (Map.Entry<String, Set<String>> entry : this.grammar.getProductions().entrySet()) {
            for (String rule : entry.getValue()) {
                ruleToSet(entry.getKey(), rule).forEach(var -> graph.insert(entry.getKey(), var));
            }
        }
        return graph;
    }

    public Set<String> ruleToSet(String var, String rule) {
        Set<String> set = new LinkedHashSet<>();
        for (int i = 0; i < rule.length(); i++) {
            char ch = rule.charAt(i);
            if (Character.isUpperCase(ch) && (i + 1) < rule.length() && rule.charAt(i + 1) == '\'' && !(ch + "'").equals(var)) {
                set.add(ch + "'");
            } else if (Character.isUpperCase(ch) && !(ch + "'").equals(var) && !String.valueOf(ch).equals(var)) {
                set.add(String.valueOf(ch));
            }
        }
        return set;
    }

    public boolean isTerminalVar(String var) {
        for (String rule : this.grammar.getProductions().get(var)) {
            if (ProductionType.isTerminalProduction(rule)) {
                return true;
            }
        }
        return false;
    }

    public Set<Node<String>> bfsTraverse(String source) {
        Node<String> sourceNode = new Node<>(source);
        List<Node<String>> visitedNodes = new ArrayList<>();
        Queue<Node<String>> queue = new LinkedList<>();
        Set<Node<String>> nodes = new LinkedHashSet<>();
        queue.add(sourceNode);
        while (!queue.isEmpty()) {
            Node<String> visitedNode = queue.remove();
            visitedNodes.add(visitedNode);
            if (!visitedNode.equals(sourceNode)) {
                nodes.add(visitedNode);
            }
            List<Node<String>> neighbours = this.dependencyGraph.getAdjacencyMap().get(visitedNode);
            if (!neighbours.isEmpty()) {
                for (Node<String> neighbourNode : neighbours) {
                    if (!visitedNodes.contains(neighbourNode)) {
                        queue.add(neighbourNode);
                    }
                }
            }
        }
        return nodes;
    }
}
