package ir.imorate.utils;

import java.util.ArrayList;
import java.util.List;

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

    public static boolean isChomskyProduction(String str) {
        return str.matches("[A-Z]{2}|[A-Z]'[A-Z]|[A-Z][A-Z]'|[a-z]");
    }

    public static boolean isGreibachProduction(String str) {
        return str.matches("[A-Z]{2}|[A-Z]'[A-Z]|[A-Z][A-Z]'|[a-z]");
    }

    public static List<String> toList(String rule) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < rule.length(); i++) {
            char ch = rule.charAt(i);
            if (Character.isUpperCase(ch) && (i + 1) < rule.length() && rule.charAt(i + 1) == '\'') {
                list.add(ch + "'");
            } else if (Character.isUpperCase(ch) || Character.isLowerCase(ch)) {
                list.add(String.valueOf(ch));
            }
        }
        return list;
    }
}
