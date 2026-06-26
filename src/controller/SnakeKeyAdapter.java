package controller;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import model.Direction;

public class SnakeKeyAdapter extends KeyAdapter {
    private final GameController gameController;
    private final Runnable repaintCallback; // maybe rename;

    public SnakeKeyAdapter(GameController gameController, Runnable repaintCallback){
        this.gameController = gameController;
        this.repaintCallback = repaintCallback;
    }

    @Override
    public void keyPressed(KeyEvent e){
        switch(e.getKeyCode()){
            case KeyEvent.VK_LEFT:
                gameController.changeDirection(Direction.LEFT);
                break;
            case KeyEvent.VK_RIGHT:
                gameController.changeDirection(Direction.RIGHT);
                break;
            case KeyEvent.VK_UP:
                gameController.changeDirection(Direction.UP);
                break;
            case KeyEvent.VK_DOWN:
                gameController.changeDirection(Direction.DOWN);
                break;
            case KeyEvent.VK_P:
                gameController.togglePause();
                break;
        }

        repaintCallback.run();
    }
}
