package ru.spbau.kaysin.game;


import static java.lang.System.exit;
import static ru.spbau.kaysin.game.GameModel.N;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import ru.spbau.kaysin.game.GameModel.ButtonState;
import ru.spbau.kaysin.game.GameModel.GameState;

/**
 * Javafx application itself
 */
public class Main extends Application {

    private static final int BUTTON_PADDING = 20;
    private final GameModel game = new GameModel();


    @Override
    public void start(Stage stage) throws Exception{
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(BUTTON_PADDING));
        grid.setHgap(BUTTON_PADDING);
        grid.setVgap(BUTTON_PADDING);

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                int posI = i, posJ = j;
                ToggleButton button = new ToggleButton();
                button.setPrefWidth(50);
                button.setPrefHeight(50);
                game.setButtonController(i, j, new ButtonController() {
                    @Override
                    public void release() {
                        button.setText("");
                        button.setDisable(false);
                        button.setSelected(false);
                    }

                    @Override
                    public void press() {
                        button.setSelected(true);
                        button.setText(String.valueOf(game.getButtonValue(posI, posJ)));
                    }
                });

                button.setOnAction(event -> {
                    if (button.isSelected()) {

                        GameState stateBefore = game.getGameState();
                        game.press(posI, posJ);
                        GameState stateAfter = game.getGameState();

                        button.setText(String.valueOf(game.getButtonValue(posI, posJ)));
                        button.setDisable(true);

                       if (stateBefore == GameState.SECOND_BUTTON_CHOOSING) {
                            if (game.getButtonState(posI, posJ) == ButtonState.FREE) {
                                game.releaseChosenButton();
                                game.releaseButton(posI, posJ);
                            }
                       }
                       if (stateAfter == GameState.END) {
                           Alert alert = new Alert(AlertType.INFORMATION);
                           alert.setContentText(null);
                           alert.setTitle("Victory!");
                           alert.setHeaderText("Victory!");
                           alert.setOnCloseRequest(event1 -> exit(0));
                           alert.show();
                       }
                    }
                });
                grid.add(button, j, i);
            }
        }

        ScrollPane scrollPane = new ScrollPane(grid);

        stage.setScene(new Scene(scrollPane));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
