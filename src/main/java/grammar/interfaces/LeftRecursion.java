package grammar.interfaces;

import grammar.model.Grammar;

public interface LeftRecursion {
    Grammar fixLeftRecursion();

    default boolean hasLeftRecursion(String key, String str) {
        return key.equals(str.substring(0, 1));
    }
}
