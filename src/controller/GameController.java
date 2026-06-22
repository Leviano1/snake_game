package controller;

import apple.Apple;
import apple.AppleSpawner;
import difficulty.DifficultyBehaviour;

public class GameController {
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
        appleSpawner = new AppleSpawner();
        startDelay = difficulty.getDelay();
        highScore = 0;

        
    }

    public void restartGame(){
        applesEaten = 0;
        moveDelay = startDelay;
        //paused = false;
        //poisonApple = null;    
        animationProgress = 1.0;
        //currentHeadAngle = getHeadAngle(direction);
        targetHeadAngle = currentHeadAngle;
    }
    
}
