package grammar.interfaces;

import grammar.model.Grammar;

import java.util.Map;
import java.util.Queue;
import java.util.Set;

public interface LambdaRemoval {
    Grammar removeLambda();

    Set<String> generateRuleSet(Map.Entry<String, Set<String>> entry, Queue<String> lambdaList);

    Queue<String> calculateLambda();

    Grammar fixSingleLambdaProduction();

    Set<String> singleLambdaProductionSet();
}