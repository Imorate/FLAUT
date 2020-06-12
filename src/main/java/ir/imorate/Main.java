package ir.imorate;

import ir.imorate.grammar.FinalGrammar;

public class Main {
    public static void main(String[] args) {
        FinalGrammar finalGrammar = new FinalGrammar();
        System.out.println("Parsed grammar:\t" + finalGrammar.mainGrammar());
        System.out.println("Left recursion fix:\t" + finalGrammar.leftRecursionGrammar());
        System.out.println("Lambda removal:\t" + finalGrammar.lambdaRemovalGrammar());
        System.out.println("Unit removal:\t" + finalGrammar.unitRemovalGrammar());
        System.out.print(finalGrammar.formattedGrammar());
    }
}
