package ru.spbau.kaysin.game;

/**
 *  Interface represent the controller which allows to call the callbacks to manage the button remotely.
 */
public interface ButtonController {

    /**
     * Releases the button.
     */
    void release();

    /**
     * Presses the button.
     */
    void press();
}
