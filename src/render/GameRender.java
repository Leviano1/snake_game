package render;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import apple.Apple;
import config.GameConfig;

public class GameRender {
    private final ImageManager imageManager;

    public GameRender(){
        imageManager = new ImageManager();
    }

    public void drawBoard(Graphics g){
        for(int row = 0; row < GameConfig.SCREEN_HEIGHT/GameConfig.UNIT_SIZE; row++){
            for(int col = 0; col < GameConfig.SCREEN_WIDTH/GameConfig.UNIT_SIZE; col++){
                if((row + col) % 2 == 0){
                    g.setColor(GameConfig.LIGHT_TILE);
                }else{
                    g.setColor(GameConfig.DARK_TILE);
                }
                g.fillRect(col * GameConfig.UNIT_SIZE, row * GameConfig.UNIT_SIZE, GameConfig.UNIT_SIZE, GameConfig.UNIT_SIZE);
            }
        }
    }

    public void drawApple(Graphics g, Apple apple){
        Image appleImage = imageManager.getAppleImage(apple.getAppleType());
        int appleSize = GameConfig.UNIT_SIZE + 12;
        int appleOffset = (GameConfig.UNIT_SIZE - appleSize) / 2;

        g.drawImage(
            appleImage,
            apple.getX() + appleOffset,
            apple.getY() + appleOffset,
            appleSize,
            appleSize,
            null
        );
    }

    //public drawSnake(Graphics2D g2){}


}
