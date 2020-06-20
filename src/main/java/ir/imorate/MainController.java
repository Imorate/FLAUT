package ir.imorate;

import ir.imorate.grammar.FinalGrammar;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.Map;
import java.util.Set;

public class MainController {

    public Button convertButton;
    public Button clearButton;
    public TextArea grammarTextArea;
    public VBox grammarStepVbox;
    public Button finalStepButton;
    public ListView<String> grammarListView;
    public ProgressBar progressBar;
    public ToggleGroup resultGrammar;
    public RadioButton chomskyRadioButton;
    public RadioButton GreibachRadioButton;
    public Label levelLabel;
    public Button nextStepButton;
    private int level;
    private FinalGrammar grammar;

    public MainController() {
        this.level = 0;
    }

    @FXML
    public void clearGrammarTextArea() {
        grammarTextArea.clear();
        convertButton.setVisible(true);
        grammarStepVbox.setVisible(false);
        grammarListView.getItems().clear();
        progressBar.setProgress(0);
        this.grammar = null;
        this.level = 0;
        grammarTextArea.setEditable(true);
        levelLabel.setText("#1");
    }

    @FXML
    public void exit() {
        System.exit(0);
    }

    @FXML
    public void convert() {
        if (grammarTextArea.getText().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Grammar cannot be empty", ButtonType.CLOSE).show();
        } else {
            convertButton.setVisible(false);
            grammarStepVbox.setVisible(true);
            nextStepButton.setVisible(true);
            finalStepButton.setVisible(true);
            this.grammar = new FinalGrammar(grammarTextArea.getText());
            grammarTextArea.setEditable(false);
            if (resultGrammar.getSelectedToggle().equals(chomskyRadioButton)) {
                progressBar.setProgress(0.2);
            } else {
                progressBar.setProgress(0.1666);
            }
        }
    }

    @FXML
    public void finalStep() {
        grammarListView.getItems().clear();
        if (resultGrammar.getSelectedToggle().equals(chomskyRadioButton)) {
            this.grammar.lambdaRemovalGrammar();
            this.grammar.unitRemovalGrammar();
            this.grammar.uselessProductionRemovalGrammar();
            this.grammar.toChomskyNormalForm();
        } else {
            this.grammar.leftRecursionGrammar();
            this.grammar.lambdaRemovalGrammar();
            this.grammar.unitRemovalGrammar();
            this.grammar.uselessProductionRemovalGrammar();
            this.grammar.toGreibachNormalForm();
        }
        showOnGrammarListView();
        progressBar.setProgress(100);
        levelLabel.setText("#4");
        nextStepButton.setVisible(false);
        finalStepButton.setVisible(false);
    }

    @FXML
    public void nextStep() {
        grammarListView.getItems().clear();
        if (resultGrammar.getSelectedToggle().equals(chomskyRadioButton)) {
            switch (level) {
                case 0:
                    this.grammar.lambdaRemovalGrammar();
                    break;
                case 1:
                    this.grammar.unitRemovalGrammar();
                    break;
                case 2:
                    this.grammar.uselessProductionRemovalGrammar();
                    break;
                case 3:
                    this.grammar.toChomskyNormalForm();
                    break;
            }
            showOnGrammarListView();
            progressBar.setProgress(progressBar.progressProperty().get() + 0.2);
            levelLabel.setText("#" + (++this.level + 1));
            if (this.level == 4) {
                grammarStepVbox.setVisible(false);
            }
        } else {
            switch (level) {
                case 0:
                    this.grammar.leftRecursionGrammar();
                    break;
                case 1:
                    this.grammar.lambdaRemovalGrammar();
                    break;
                case 2:
                    this.grammar.unitRemovalGrammar();
                    break;
                case 3:
                    this.grammar.uselessProductionRemovalGrammar();
                    break;
                case 4:
                    this.grammar.toGreibachNormalForm();
                    break;
            }
            showOnGrammarListView();
            progressBar.setProgress(progressBar.progressProperty().get() + 0.1666);
            levelLabel.setText("#" + (++this.level + 1));
            if (this.level == 5) {
                grammarStepVbox.setVisible(false);
            }
        }
    }

    private void showOnGrammarListView() {
        for (Map.Entry<String, Set<String>> entry : this.grammar.getGrammar().getProductions().entrySet()) {
            grammarListView.getItems().add(entry.getKey() + "\t→\t" + String.join("|", entry.getValue()));
        }
    }

}
