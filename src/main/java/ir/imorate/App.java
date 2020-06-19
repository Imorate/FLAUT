package ir.imorate;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    private static Scene scene;

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
        /*FinalGrammar finalGrammar = new FinalGrammar();
        System.out.println("Parsed grammar:\t" + finalGrammar.mainGrammar());
        System.out.println("Left recursion fix:\t" + finalGrammar.leftRecursionGrammar());
        System.out.println("Lambda removal:\t" + finalGrammar.lambdaRemovalGrammar());
        System.out.println("Unit removal:\t" + finalGrammar.unitRemovalGrammar());
        System.out.println("Useless production removal:\t" + finalGrammar.uselessProductionRemovalGrammar());
        System.out.println("Chomsky normal form:\t" + finalGrammar.toChomskyNormalForm());
        System.out.println(finalGrammar.formattedGrammar());*/
    }

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("mainController"));
        stage.setScene(scene);
        stage.setTitle("FLAUT | CFG to Normal Forms");
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();
    }
}
