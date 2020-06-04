package utils;

public class ProductionType {
    public static boolean isNonTerminal(String str) {
        return str.matches("[a-zA-Z0-9]+'?");
    }

    public static boolean isTerminal(String str) {
        return str.matches("[a-z0-9]+");
    }

    public static boolean isLambda(String str) {
        return str.equals("λ");
    }
}
