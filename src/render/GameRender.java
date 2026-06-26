package render;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.TexturePaint;
import java.awt.image.BufferedImage;

import apple.Apple;
import config.GameConfig;
import controller.GameController;
import model.Snake;

public class GameRender {
    private final ImageManager imageManager;

    public GameRender(){
        imageManager = new ImageManager();
    }

    public void render(Graphics g, GameController gameController){
        drawBoard(g);
        if(gameController.isRunning()){
            drawApple(g, gameController.getApple());
            if(gameController.getPoisonApple() != null){
                drawApple(g, gameController.getPoisonApple());
            }
            drawSnake((Graphics2D)g, gameController);

            if(gameController.isPaused()){
                drawPauseMessage(g);
            }
        }else{
            drawGameOver(g);
        }
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

    public void drawSnake(Graphics2D g2, GameController gameController){
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        setSnakeBodyPaint(g2);

        Snake snake = gameController.getSnake();
        int[] x = snake.getX();
        int[] y = snake.getY();
        int[] previousX = snake.getPreviousX();
        int[] previousY = snake.getPreviousY();
        int bodyParts = snake.getBodyParts();
        double animationProgress = gameController.getAnimationProgress();

        int tailSize = GameConfig.SNAKE_THICKNESS - 4;
        int bodySize = GameConfig.SNAKE_THICKNESS;
        int headSize = GameConfig.UNIT_SIZE;

        for(int i = bodyParts - 1; i > 0; i--){
            double t = 1.0 - ((double)i / (bodyParts - 1)); //controls how big the segment should be;
            int partSize = (int)(tailSize + (bodySize - tailSize) * t);
            int x1 = (int)(previousX[i] + (x[i] - previousX[i]) * animationProgress) + GameConfig.UNIT_SIZE / 2;
            int y1 = (int)(previousY[i] + (y[i] - previousY[i]) * animationProgress) + GameConfig.UNIT_SIZE / 2;
            int x2 = (int)(previousX[i - 1] + (x[i - 1] - previousX[i - 1]) * animationProgress) + GameConfig.UNIT_SIZE / 2;
            int y2 = (int)(previousY[i - 1] + (y[i - 1] - previousY[i - 1]) * animationProgress) + GameConfig.UNIT_SIZE / 2;                
            // If this segment connects to the head, stop it at the edge of the head.
            if(i == 1){
                double dx = x2 - x1;
                double dy = y2 - y1;
                double distance = Math.sqrt(dx * dx + dy * dy);

                if(distance > 0){
                    double headRadius = headSize / 2.0 - 3;
                    x2 = (int)(x2 - (dx / distance) * headRadius);
                    y2 = (int)(y2 - (dy / distance) * headRadius);
                }   
            }
            g2.setStroke(new BasicStroke(partSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.drawLine(x1, y1, x2, y2);
        }
        drawSnakeHead(g2, gameController);
    }

    public void setSnakeBodyPaint(Graphics2D g2){
        BufferedImage snakeSkinImage = imageManager.getSnakeSkinImage();
        if(snakeSkinImage != null){
            TexturePaint snakeSkin = new TexturePaint(snakeSkinImage, new Rectangle(0, 0, 150, 150));
            g2.setPaint(snakeSkin);
        }else{
            g2.setColor(new Color(120, 190, 60)); //green;
        }
    }

    public void drawSnakeHead(Graphics2D g2, GameController gameController){
        Snake snake = gameController.getSnake();
        int imageSize = GameConfig.UNIT_SIZE + 12;
        int imageOffSet = (GameConfig.UNIT_SIZE - imageSize) / 2;
        int headX = interpolate(snake.getPreviousX()[0], snake.getX()[0], gameController.getAnimationProgress()) + imageOffSet;
        int headY = interpolate(snake.getPreviousY()[0], snake.getY()[0], gameController.getAnimationProgress()) + imageOffSet;            
        int centerX = headX + imageSize / 2;
        int centerY = headY + imageSize / 2;
        Graphics2D headG = (Graphics2D) g2.create();
        headG.rotate(gameController.getCurrentHeadAngle(), centerX, centerY);
        Image headImage = imageManager.getSnakeHeadImage();
        if(headImage != null){
            headG.drawImage(headImage, headX, headY, imageSize, imageSize, null);
        }else{
            headG.setColor(new Color(160, 210, 50)); //lime green;
            headG.fillOval(headX, headY, imageSize, imageSize);
        }
        headG.dispose();
    }

    public int interpolate(int previous, int current, double progress){
        return (int)(previous + (current - previous) * progress);
    }

    public void drawGameOver(Graphics g){ 
        g.setColor(Color.red);
        g.setFont(new Font("Times New Roman", Font.BOLD, 40));
        FontMetrics metrics = g.getFontMetrics(); //this is used to center the text on the screen;
        g.drawString("Game Over.", (GameConfig.SCREEN_WIDTH - metrics.stringWidth("Game Over."))/2, GameConfig.SCREEN_HEIGHT/2);
    }

    public void drawScore(Graphics g, int score){
        g.setColor(GameConfig.HUD_TEXT);
        g.setFont(new Font("Times New Roman", Font.BOLD, 20));
        FontMetrics metrics = g.getFontMetrics(); //this is used to center the text on the screen;
        g.drawString("Score: " + score, (GameConfig.SCREEN_WIDTH - metrics.stringWidth("Score: " + score))/2, g.getFont().getSize());
    }

    public void drawHighScore(Graphics g, int highScore){
        g.setColor(GameConfig.HUD_TEXT);
        g.setFont(new Font("Times New Roman", Font.BOLD, 20));
        g.drawString("High Score: " + highScore, 20, g.getFont().getSize());
    }

    public void drawPauseMessage(Graphics g){
        g.setColor(Color.blue);
        g.setFont(new Font("Times New Roman", Font.BOLD, 40));
        FontMetrics metrics = g.getFontMetrics();
        g.drawString("Game is paused.", (GameConfig.SCREEN_WIDTH - metrics.stringWidth("Game is paused."))/2, GameConfig.SCREEN_HEIGHT/2);
    }

}
