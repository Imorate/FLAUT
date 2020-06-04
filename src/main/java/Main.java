import grammar.FinalGrammar;

public class Main {
    public static void main(String[] args) {
        FinalGrammar finalGrammar = new FinalGrammar();
        System.out.println("Left recursion fix:\t" + finalGrammar.leftRecursionGrammar());
        System.out.println("Remove lambda:\t" + finalGrammar.removeLambdaGrammar());
    }
}
