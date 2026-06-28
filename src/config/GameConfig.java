package config;

import java.awt.Color;

public class GameConfig {
    public static final int SCREEN_WIDTH = 600;
    public static final int SCREEN_HEIGHT = 600;
    public static final int UNIT_SIZE = 50;
    public static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / (UNIT_SIZE * UNIT_SIZE);
    public static final int FRAME_DELAY = 16;
    public static final int SNAKE_THICKNESS = 35;
    public static final int INITIAL_BODY_PARTS = 6;
    public static final int POISON_APPLE_DURATION = 7000;
    public static final int POISON_APPLE_CHANCE = 8;
    public static final int MIN_MOVE_DELAY = 80;
    public static final int SPEED_INCREASE = 4;
    public static final int SPEED_INCREASE_SCORE_INTERVAL = 2;
    public static final long FPS_SAMPLE_INTERVAL_NANOS = 500_000_000L;

    public static final Color LIGHT_TILE = new Color(78, 72, 88);
    public static final Color DARK_TILE = new Color(68, 62, 78);
    //public static final Color LIGHT_SANDY_TILE = new Color(242, 216, 143);
    //public static final Color DARK_SANDY_TILE = new Color(230, 197, 112);
    public static final Color PAUSE_TEXT = Color.ORANGE;
    public static final Color HUD_TEXT = Color.ORANGE;

    private GameConfig(){
    }
}
