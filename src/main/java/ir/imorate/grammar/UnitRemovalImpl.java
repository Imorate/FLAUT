package ir.imorate.grammar;

import ir.imorate.grammar.interfaces.UnitRemoval;
import ir.imorate.grammar.model.Grammar;
import ir.imorate.graph.Graph;
import ir.imorate.graph.Node;
import ir.imorate.utils.ProductionType;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

@Data
public class UnitRemovalImpl implements UnitRemoval {
    private Grammar grammar;
    private Graph<String> dependencyGraph;

    public UnitRemovalImpl(Grammar grammar) {
        this.grammar = grammar;
        this.dependencyGraph = mapToDependencyGraph();
    }

    @Override
    public Grammar unitRemovalFix() {
        for (Map.Entry<Node<String>, LinkedList<Node<String>>> entry : this.dependencyGraph.getAdjacencyMap().entrySet()) {
            if (!entry.getValue().isEmpty()) {
                Node<String> nodeVar = entry.getKey();
                for (Node<String> node : entry.getValue()) {
                    if (ProductionType.isUnitProduction(node.getLabel())) {
                        this.grammar = updateGrammar(nodeVar.getLabel(), node.getLabel(), bfsTraverse(node.getLabel()));
                    }
                }
            }
        }
        return this.grammar;
    }

    public Grammar updateGrammar(String var, String rule, Set<String> ruleSet) {
        this.grammar.getProductions().get(var).remove(rule);
        this.grammar.getProductions().get(var).addAll(ruleSet);
        return this.grammar;
    }

    public Set<String> bfsTraverse(String source) {
        Node<String> sourceNode = new Node<>(source);
        List<Node<String>> visitedNodes = new ArrayList<>();
        Queue<Node<String>> queue = new LinkedList<>();
        Set<String> aggregateRules = new LinkedHashSet<>();
        queue.add(sourceNode);
        while (!queue.isEmpty()) {
            Node<String> visitedNode = queue.remove();
            visitedNodes.add(visitedNode);
            aggregateRules.addAll(getNonUnitProduction(visitedNode));
            List<Node<String>> neighbours = this.dependencyGraph.getAdjacencyMap().get(visitedNode);
            if (neighbours != null) {
                for (Node<String> neighbourNode : neighbours) {
                    if (!visitedNodes.contains(neighbourNode)) {
                        queue.add(neighbourNode);
                    }
                }
            }
        }
        return aggregateRules;
    }

    public Set<String> getNonUnitProduction(Node<String> node) {
        return this.grammar.getProductions().entrySet().stream()
                .filter(stringSetEntry -> stringSetEntry.getKey().equals(node.getLabel()))
                .flatMap(stringSetEntry -> stringSetEntry.getValue().stream())
                .filter(ProductionType::isNonUnitProduction)
                .collect(Collectors.toSet());
    }

    public Graph<String> mapToDependencyGraph() {
        Graph<String> graph = new Graph<>();
        for (Map.Entry<String, Set<String>> entry : this.grammar.getProductions().entrySet()) {
            for (String rule : entry.getValue()) {
                if (ProductionType.isUnitProduction(rule)) {
                    graph.insert(entry.getKey(), rule);
                }
            }
        }
        return graph;
    }
}
