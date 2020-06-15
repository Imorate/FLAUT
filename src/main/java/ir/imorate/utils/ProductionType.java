package ir.imorate.utils;

public class ProductionType {
    public static boolean isNonTerminalProduction(String str) {
        return str.matches("[a-zA-Z0-9]+'?");
    }

    public static boolean isTerminalProduction(String str) {
        return str.matches("[a-z0-9]+");
    }

    public static boolean isLambda(String str) {
        return str.equals("λ");
    }

    public static boolean isUnitProduction(String str) {
        return str.matches("[A-Z0-9]'?");
    }

    public static boolean isNonUnitProduction(String str) {
        return str.matches("[a-zA-Z0-9']{2,}|[a-z]");
    }
}
