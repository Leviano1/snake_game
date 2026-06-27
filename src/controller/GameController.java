package controller;

import apple.Apple;
import apple.AppleSpawner;
import config.GameConfig;
import difficulty.DifficultyBehaviour;
import model.Direction;
import model.Snake;

public class GameController {
    private final Snake snake;
    private final AppleSpawner appleSpawner;
    private final int startDelay;

    private Apple apple;
    private Apple poisonApple;
    private long poisonAppleDuration;
    private long pauseTime;

    private int applesEaten;
    private int highScore;
    private int moveDelay;
    private boolean running;
    private boolean paused;
    private double animationProgress;
    private double currentHeadAngle;
    private double targetHeadAngle;


    public GameController(DifficultyBehaviour difficulty){
        snake = new Snake();
        appleSpawner = new AppleSpawner();
        startDelay = difficulty.getDelay(); //maybe rename this one;
        highScore = 0;
        restartGame();
    }

    public void restartGame(){
        snake.reset();
        applesEaten = 0;
        moveDelay = startDelay;
        paused = false;
        running = true;
        poisonApple = null;    
        animationProgress = 1.0;
        currentHeadAngle = getHeadAngle(snake.getDirection());
        targetHeadAngle = currentHeadAngle;
        newApple();
    }

    public void updateFrame(double deltaTime) {
        if(running && !paused){
            animationProgress += deltaTime / moveDelay;
            updateHeadAngle();

            while(animationProgress >= 1.0){
                animationProgress -= 1.0;
                snake.previousPosition();
                snake.move();
                checkApple();
                checkPoisonApple();
                updatePoisonApple();
                checkCollisions();
            } 
        }
    }

    public void togglePause(){
        if(!paused){
            pauseTime = System.currentTimeMillis();
            paused = true;
        }else{
            long pauseDuration = System.currentTimeMillis() - pauseTime;
            if(poisonApple != null){
                poisonAppleDuration += pauseDuration;
            }
            paused = false;
        }
    }

    public void newApple(){
        apple = appleSpawner.spawnApple(GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT, GameConfig.UNIT_SIZE);
    }

    public void checkApple(){
        if(snake.getHeadX() == apple.getX() && snake.getHeadY() == apple.getY()){
            snake.grow();
            applesEaten += apple.getPoints();

            if(applesEaten > highScore){
                highScore = applesEaten;
            }

            if(applesEaten % GameConfig.SPEED_INCREASE_SCORE_INTERVAL == 0){
                moveDelay = Math.max(GameConfig.MIN_MOVE_DELAY, moveDelay - GameConfig.SPEED_INCREASE);
            }
            newApple();
            newPoisonApple();
        }
    }

    public void newPoisonApple(){
        if(poisonApple == null && Math.random() * GameConfig.POISON_APPLE_CHANCE < 1){
            poisonApple = appleSpawner.spawnPoisonApple(GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT, GameConfig.UNIT_SIZE);
            poisonAppleDuration = System.currentTimeMillis();
        }
    }

    public void checkPoisonApple(){
        if(poisonApple != null && snake.getHeadX() == poisonApple.getX() && snake.getHeadY() == poisonApple.getY()){
            snake.shrink();
            applesEaten += poisonApple.getPoints();

            if(applesEaten <= 0){
                applesEaten = 0;
            }
            poisonApple = null;
        }
    }

    public void updatePoisonApple(){
        if(poisonApple != null){
            long currentTime = System.currentTimeMillis();
            if(currentTime - poisonAppleDuration >= GameConfig.POISON_APPLE_DURATION){
                poisonApple = null;
            }
        } 
    }

    public void checkCollisions(){
        if(snake.collidesWithSelf() || snake.isOutsideBoard()){
            running = false;
        }
    }

    public void updateHeadAngle(){
        double difference = targetHeadAngle - currentHeadAngle;

        while(difference > Math.PI){
            difference -= Math.PI * 2;
        }

        while(difference < -Math.PI){
            difference += Math.PI * 2;
        }

        currentHeadAngle += difference * 0.2;
    }

    public double getHeadAngle(Direction direction){
        switch(direction){
            case UP:
                return Math.PI;
            case DOWN:
                return 0;
            case LEFT:
                return Math.PI / 2;
            case RIGHT:
                return -Math.PI / 2;
            default:
                return 0;
        }
    }

    public void changeDirection(Direction direction){
        if(snake.changeDirection(direction)){
            targetHeadAngle = getHeadAngle(direction);
        }
    }

    public Snake getSnake(){
        return snake;
    }

    public Apple getApple(){
        return apple;
    }

    public Apple getPoisonApple(){
        return poisonApple;
    }

    public int getApplesEaten(){
        return applesEaten;
    }

    public int getHighScore(){
        return highScore;
    }

    public boolean isRunning(){
        return running;
    }

    public boolean isPaused(){
        return paused;
    }

    public double getAnimationProgress(){
        return animationProgress;
    }

    public double getCurrentHeadAngle(){
        return currentHeadAngle;
    }
}
