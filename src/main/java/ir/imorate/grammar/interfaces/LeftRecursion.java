package ir.imorate.grammar.interfaces;

import ir.imorate.grammar.model.Grammar;

public interface LeftRecursion {
    Grammar fixLeftRecursion();

    default boolean hasLeftRecursion(String key, String str) {
        return key.equals(str.substring(0, 1));
    }
}
