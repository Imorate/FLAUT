package ir.imorate.grammar;

import com.rits.cloning.Cloner;
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

    public static Set<String> mergeSet(Set<String> a, Set<String> b) {
        return new HashSet<>() {
            {
                addAll(a);
                addAll(b);
            }
        };
    }

    @Override
    public Grammar unitRemovalFix() {
        if (hasLoopyNode()) {
            Grammar nonUnitGrammar = filterNonUnitProduction();
            Grammar newGrammar = generateNewGrammar();
            for (Map.Entry<String, Set<String>> entry : newGrammar.getProductions().entrySet()) {
                entry.setValue(mergeSet(entry.getValue(), nonUnitGrammar.getProductions().get(entry.getKey())));
            }
            this.grammar = newGrammar;
        } else {
            this.grammar = fixWithoutLoopyNode();
        }
        return this.grammar;
    }

    public Grammar generateNewGrammar() {
        Grammar newGrammar = new Grammar();
        newGrammar.setStartVariable(this.grammar.getStartVariable());
        for (Map.Entry<String, Set<String>> entry : this.grammar.getProductions().entrySet()) {
            for (String rule : entry.getValue()) {
                if (ProductionType.isUnitProduction(rule)) {
                    String key = entry.getKey();
                    newGrammar.getProductions().put(key, bfsTraverse(key));
                }
            }
        }
        return newGrammar;
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
            if (!visitedNode.equals(sourceNode)) {
                aggregateRules.addAll(getTerminalProduction(visitedNode));
            }
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

    public Set<String> getTerminalProduction(Node<String> node) {
        return this.grammar.getProductions().entrySet().stream()
                .filter(stringSetEntry -> stringSetEntry.getKey().equals(node.getLabel()))
                .flatMap(stringSetEntry -> stringSetEntry.getValue().stream())
                .filter(ProductionType::isTerminalProduction)
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

    public Grammar filterNonUnitProduction() {
        Grammar nonUnitGrammar = new Cloner().deepClone(this.grammar);
        nonUnitGrammar.getProductions().entrySet()
                .forEach(entry -> entry.setValue(entry.getValue().stream()
                        .filter(ProductionType::isNonUnitProduction).collect(Collectors.toSet())));
        return nonUnitGrammar;
    }

    private boolean hasLoopyNode() {
        for (Map.Entry<Node<String>, LinkedList<Node<String>>> entry : dependencyGraph.getAdjacencyMap().entrySet()) {
            if (!entry.getValue().isEmpty()) {
                for (Node<String> node : entry.getValue()) {
                    if (dependencyGraph.hasBidirectionalTraverse(entry.getKey().getLabel(), node.getLabel())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private Grammar fixWithoutLoopyNode() {
        Grammar fixedGrammar = this.grammar;
        for (Map.Entry<Node<String>, LinkedList<Node<String>>> entry : dependencyGraph.getAdjacencyMap().entrySet()) {
            if (!entry.getValue().isEmpty()) {
                for (Node<String> node : entry.getValue()) {
                    Set<String> productionSet = fixedGrammar.getProductions().get(entry.getKey().getLabel());
                    productionSet.remove(node.getLabel());
                    productionSet.addAll(fixedGrammar.getProductions().get(node.getLabel()));
                }
            }
        }
        return fixedGrammar;
    }
}
