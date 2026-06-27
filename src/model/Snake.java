package model;

import config.GameConfig;

public class Snake {    
    private final int[] x;
    private final int[] y;
    private final int[] previousX;
    private final int[] previousY;
    private int bodyParts;
    private Direction direction;

    public Snake(){
        x = new int[GameConfig.GAME_UNITS];
        y = new int[GameConfig.GAME_UNITS];
        previousX = new int[GameConfig.GAME_UNITS];
        previousY = new int[GameConfig.GAME_UNITS];
    }

    public void reset(){
        bodyParts = GameConfig.INITIAL_BODY_PARTS;
        direction = Direction.RIGHT;

        for(int i = 0; i < bodyParts; i++){
            x[i] = 0;
            y[i] = 0;
            previousX[i] = 0;
            previousY[i] = 0;
        }
    }

    public void previousPosition(){ //for smoothness 
        for(int i = 0; i < bodyParts; i++){
            previousX[i] = x[i];
            previousY[i] = y[i];
        }
    }

    public void move(){
        for(int i = bodyParts -1; i > 0; i--){
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        switch(direction){
            case UP -> y[0] -= GameConfig.UNIT_SIZE;
            case DOWN -> y[0] += GameConfig.UNIT_SIZE;
            case LEFT -> x[0] -= GameConfig.UNIT_SIZE;
            case RIGHT -> x[0] += GameConfig.UNIT_SIZE;
        }
    }

    public boolean changeDirection(Direction newDirection){
        if(direction.isOpposite(newDirection)){
            return false;
        }

        direction = newDirection;
        return true;
    }

    public void grow(){
        if(bodyParts >= x.length){
            return;
        }

        int oldTail = bodyParts - 1;
        x[bodyParts] = previousX[oldTail];
        y[bodyParts] = previousY[oldTail];
        previousX[bodyParts] = previousX[oldTail];
        previousY[bodyParts] = previousY[oldTail];
        bodyParts++;
    }

    public void shrink(){
        if(bodyParts > 2){
            bodyParts--;
        }
    }

    public boolean collidesWithSelf(){
        for(int i = 1; i < bodyParts; i++){
            if(x[0] == x[i] && y[0] == y[i]){
                return true;
            }
        }
        return false;
    }

    public boolean isOutsideBoard(){
        return x[0] < 0
            || x[0] >= GameConfig.SCREEN_WIDTH
            || y[0] < 0
            || y[0] >= GameConfig.SCREEN_HEIGHT;
    }

    public int getHeadX(){
        return x[0];
    }

    public int getHeadY(){
        return y[0];
    }

    public int[] getX(){
        return x;
    }

    public int[] getY(){
        return y;
    }

    public int[] getPreviousX(){
        return previousX;
    }

    public int[] getPreviousY(){
        return previousY;
    }

    public int getBodyParts(){
        return bodyParts;
    }

    public Direction getDirection(){
        return direction;
    }

}
