package ru.spbau.kaysin.game;

import com.sun.istack.internal.Nullable;
import java.util.Random;

/**
 * Class represents the game logic model.
 */
public class GameModel {

    /**
     * Grid size.
     */
    public static final int N = 4;

    private final GameButton[][] grid = new GameButton[N][N];

    private GameState gameState;

    private GameButton chosenButton;

    private int closedButtons;

    /**
     * Initializes the game.
     */
    public GameModel() {
        Random random = new Random();
        int sum = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                int t = random.nextInt(2);
                sum += t;
                grid[i][j] = new GameButton(t);
            }
        }
        if (sum % 2 != 0) {
            grid[0][0].setValue(1 - grid[0][0].getValue()); // inverse
        }

        gameState = GameState.MAIN_LOOP;
    }

    /**
     * Processes the pressing on the button.
     * @param i - row index
     * @param j - column index
     */
    public void press(int i, int j) {
        GameButton button = grid[i][j];
        if (gameState == GameState.MAIN_LOOP) {
            if (button.getState() == ButtonState.FREE) {
                button.setState(ButtonState.PRESSED);
                chosenButton = button;
                gameState = GameState.SECOND_BUTTON_CHOOSING;
            }
        } else if (gameState == GameState.SECOND_BUTTON_CHOOSING){
            if (button.getState() == ButtonState.FREE) {
                if (chosenButton.getValue() != button.getValue()) {
                    button.setState(ButtonState.FREE);
                    chosenButton.setState(ButtonState.FREE);
                } else {
                    button.setState(ButtonState.CLOSED);
                    chosenButton.setState(ButtonState.CLOSED);
                    closedButtons += 2;
                }
                if (closedButtons == N * N) {
                    gameState = GameState.END;
                } else {
                    gameState = GameState.MAIN_LOOP;
                }
            }
        }
    }


    /**
     * Returns state of the button with given position.
     * @param i row index
     * @param j column index
     * @return state of the button
     */
    public ButtonState getButtonState(int i, int j) {
        return grid[i][j].getState();
    }

    /**
     * Returns the state of the game.
     * @return state of the game
     */
    public GameState getGameState() {
        return gameState;
    }

    /**
     * Returns the value contained in the button with given position.
     * @param i row index.
     * @param j column index.
     * @return the value in the button.
     */
    public int getButtonValue(int i, int j) {
        return grid[i][j].getValue();
    }

    /**
     * Sets the button controller which allows to control the button remotely.
     * @param i row index.
     * @param j column index.
     * @param controller - the controller itself
     */
    public void setButtonController(int i, int j, ButtonController controller) {
        grid[i][j].setController(controller);
    }

    /**
     * Releases the button using buttonController.
     * @param i row index
     * @param j column index
     */
    public void releaseButton(int i, int j) {
        if (grid[i][j].controller != null) {
            grid[i][j].controller.release();
        }
    }

    /**
     * Releases the button which was chosen as first button in the pair last time.
     */
    public void releaseChosenButton() {
        if (chosenButton != null && chosenButton.controller != null) {
            chosenButton.controller.release();
        }
    }

    /**
     * Describes the state of the button.
     */
    public enum ButtonState {

        /**
         * button is pressed.
         */
        PRESSED,

        /**
         * Button is released
         */
        FREE,

        /**
         * Button is disabled due to already paired.
         */
        CLOSED
    }

    /**
     * Describes the game state.
     */
    public enum GameState {
        /**
         * initial state, no chosen buttons.
         */
        MAIN_LOOP,

        /**
         * The player has chosen one button and now he's going to chose another one.
         */
        SECOND_BUTTON_CHOOSING,

        /**
         * Game over.
         */
        END
    }

    /**
     * Class representing button as logic entity.
     */
    private class GameButton {
        private ButtonState state;
        private int value;
        @Nullable
        private ButtonController controller;

        private GameButton(int value) {
            this.value = value;
            state = ButtonState.FREE;
        }

        private int getValue() {
            return value;
        }

        private void setValue(int value) {
            this.value = value;
        }

        private ButtonState getState() {
            return state;
        }

        private void setState(ButtonState state) {
            this.state = state;
        }

        private void setController(ButtonController controller) {
            this.controller = controller;
        }
    }
}
