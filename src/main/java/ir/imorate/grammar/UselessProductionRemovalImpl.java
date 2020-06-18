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
        this.grammar = fixVariablesCanGenerateString();
        this.grammar = fixAccessibleVariablesFromStartVar();
        return this.grammar;
    }

    public Grammar fixAccessibleVariablesFromStartVar() {
        Set<Node<String>> participatedVars = bfsTraverse(String.valueOf(this.grammar.getStartVariable()));
        participatedVars.add(new Node<>(String.valueOf(this.grammar.getStartVariable())));
        Set<Node<String>> vars = new LinkedHashSet<>();
        this.grammar.getProductions().keySet().forEach(var -> vars.add(new Node<>(var)));
        vars.removeAll(participatedVars);
        vars.forEach(var -> this.grammar.getProductions().remove(var.getLabel()));
        return this.grammar;
    }

    public Grammar fixVariablesCanGenerateString() {
        Set<String> participatedVars = new LinkedHashSet<>();
        for (Map.Entry<String, Set<String>> entry : this.grammar.getProductions().entrySet()) {
            if (!entry.getKey().equals(String.valueOf(this.grammar.getStartVariable()))) {
                if (!canGenerateString(entry.getKey())) {
                    participatedVars.add(entry.getKey());
                }
            }
        }
        participatedVars.forEach(var -> {
            this.grammar.getProductions().remove(var);
            this.grammar = fixVariablesInGrammar(var);
        });
        this.dependencyGraph = mapToDependencyGraph();
        return this.grammar;
    }

    public Grammar fixVariablesInGrammar(String var) {
        for (Map.Entry<String, Set<String>> entry : this.grammar.getProductions().entrySet()) {
            Set<String> variables = new LinkedHashSet<>();
            for (String rule : entry.getValue()) {
                if (rule.contains(var)) {
                    variables.add(rule);
                }
            }
            entry.getValue().removeAll(variables);
        }
        return this.grammar;
    }

    public boolean canGenerateString(String var) {
        boolean status = false;
        Set<Node<String>> participatedSet = bfsTraverse(var);
        if (!participatedSet.isEmpty()) {
            for (Node<String> node : participatedSet) {
                if (isOneRecursionProduction(node.getLabel())) {
                    return false;
                } else if (isTerminalVariable(node.getLabel())) {
                    status = true;
                }
            }
        }
        return (isTerminalVariable(var) || status) && !isOneRecursionProduction(var);
    }

    public boolean isOneRecursionProduction(String var) {
        return this.grammar.getProductions().get(var).size() == 1
                && this.grammar.getProductions().get(var).toArray()[0].toString().contains(var);
    }

    public boolean isTerminalVariable(String var) {
        for (String rule : this.grammar.getProductions().get(var)) {
            if (ProductionType.isTerminalProduction(rule)) {
                return true;
            }
        }
        return false;
    }

    public Graph<String> mapToDependencyGraph() {
        Graph<String> graph = new Graph<>();
        for (Map.Entry<String, Set<String>> entry : this.grammar.getProductions().entrySet()) {
            for (String rule : entry.getValue()) {
                ruleToVariableSet(entry.getKey(), rule).forEach(var -> graph.insert(entry.getKey(), var));
            }
        }
        return graph;
    }

    public Set<String> ruleToVariableSet(String var, String rule) {
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

    public Set<Node<String>> bfsTraverse(String source) {
        Node<String> sourceNode = new Node<>(source);
        List<Node<String>> visitedNodes = new ArrayList<>();
        Queue<Node<String>> queue = new LinkedList<>();
        Set<Node<String>> nodes = new LinkedHashSet<>();
        if (!this.dependencyGraph.getAdjacencyMap().containsKey(sourceNode)) {
            return nodes;
        }
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
