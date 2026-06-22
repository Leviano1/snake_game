package ui;
import difficulty.DifficultyBehaviour;
import render.ImageManager;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import javax.swing.*;
import java.util.Random;
import apple.Apple;
import apple.AppleSpawner;
import apple.AppleType;

public class GamePanel extends JPanel implements ActionListener{
    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 50; //the size of each unit in the game, the snake and the apple will be in multiples of this unit size;
    static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT) / (UNIT_SIZE * UNIT_SIZE);
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    final int previousX[] = new int[GAME_UNITS];
    final int previousY[] = new int[GAME_UNITS];
    static final int FRAME_DELAY = 16;
    static final int SNAKE_THICKNESS = 35;
    long lastFrameTime;
    double animationProgress = 1.0;
    int moveDelay;
    Timer animationTimer;
    int bodyParts = 6;
    int applesEaten = 0;
    Apple apple;
    Apple poisonApple;
    AppleType appleType;
    AppleSpawner appleSpawner;
    long poisonAppleDuration;
    char direction = 'R'; //R = right, L = left, U = up, D = down;
    boolean running = false;
    Timer timer;
    Random random;
    Color snakeColor;
    JButton restartButton;
    boolean paused = false;
    int highScore = 0;
    int startingDelay;
    ImageManager imageManager;
    TexturePaint snakeSkin;
    double currentHeadAngle = getHeadAngle(direction);
    double targetHeadAngle = getHeadAngle(direction);
    long pauseStartTime;
    

    public GamePanel(DifficultyBehaviour difficulty){
        random = new Random();
        imageManager = new ImageManager();
        BufferedImage snakeSkinImage = imageManager.getSnakeSkinImage();
        if(snakeSkinImage != null){
            Rectangle textureRect = new Rectangle(0, 0, 150, 150);
            snakeSkin = new TexturePaint(snakeSkinImage, textureRect);
        }
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.setLayout(null);
        this.addKeyListener(new MyKeyAdapter());
        this.startingDelay = difficulty.getDelay();
        this.moveDelay = startingDelay;
        timer = new Timer(FRAME_DELAY, this); // runs every 15ms;
        appleSpawner = new AppleSpawner();
        restartButton = new JButton("Restart.");
        restartButton.setBounds(225, 350, 150, 40);
        restartButton.setVisible(false);
        add(restartButton);
        restartButton.addActionListener(e -> {
            restartGame();
        });

        startGame();
    }

    public void startGame(){
        snakeColor = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
        newApple();
        running = true;
        animationProgress = 1.0;
        lastFrameTime = System.nanoTime();
        timer.start();
    }

    public void previousPosition(){
        for(int i = 0; i < bodyParts; i++){
            previousX[i] = x[i];
            previousY[i] = y[i];
        }
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        if(running){
            //Color lightSandyTile = new Color(242, 216, 143);
            //Color darkSandyTile = new Color(230, 197, 112);
            Color lightTile = new Color(78, 72, 88);
            Color darkTile = new Color(68, 62, 78);
            for(int row = 0; row < SCREEN_HEIGHT/UNIT_SIZE; row++){
                for(int col = 0; col < SCREEN_WIDTH/UNIT_SIZE; col++){
                    if((row + col) % 2 == 0){
                        g.setColor(lightTile);
                    }else{
                        g.setColor(darkTile);
                    }
                    g.fillRect(col * UNIT_SIZE, row * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
                }
            }
            drawApple(g, apple);
            if(poisonApple != null){
                drawApple(g, poisonApple);
            }

            Graphics2D g2 = (Graphics2D) g;
            if(snakeSkin != null){
                g2.setPaint(snakeSkin);
            }else{
                g2.setColor(snakeColor);
            }

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int tailSize = SNAKE_THICKNESS - 4;
            int bodySize = SNAKE_THICKNESS;
            int headSize = UNIT_SIZE;

            for(int i = bodyParts - 1; i > 0; i--){
                double t = 1.0 - ((double)i / (bodyParts - 1)); //controls how big the segment should be;
                int partSize = (int)(tailSize + (bodySize - tailSize) * t);

                int x1 = (int)(previousX[i] + (x[i] - previousX[i]) * animationProgress) + UNIT_SIZE / 2;
                int y1 = (int)(previousY[i] + (y[i] - previousY[i]) * animationProgress) + UNIT_SIZE / 2;
                int x2 = (int)(previousX[i - 1] + (x[i - 1] - previousX[i - 1]) * animationProgress) + UNIT_SIZE / 2;
                int y2 = (int)(previousY[i - 1] + (y[i - 1] - previousY[i - 1]) * animationProgress) + UNIT_SIZE / 2;
                
                // If this segment connects to the head, stop it at the edge of the head.
                if(i == 1){
                    double dx = x2 - x1;
                    double dy = y2 - y1;
                    double distance = Math.sqrt(dx * dx + dy * dy);

                    double headRadius = headSize / 2.0 - 3;

                    x2 = (int)(x2 - (dx / distance) * headRadius);
                    y2 = (int)(y2 - (dy / distance) * headRadius);
                }

                g2.setStroke(new BasicStroke(partSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawLine(x1, y1, x2, y2);
            }
            int imageSize = UNIT_SIZE + 12;
            int imageOffSet = (UNIT_SIZE - imageSize) / 2;
            int headX = (int)(previousX[0] + (x[0] - previousX[0]) * animationProgress) + imageOffSet;
            int headY = (int)(previousY[0] + (y[0] - previousY[0]) * animationProgress) + imageOffSet;
            
            int centerX = headX + imageSize / 2;
            int centerY = headY + imageSize / 2;
            Graphics2D headG = (Graphics2D) g2.create();
            headG.rotate(currentHeadAngle, centerX, centerY);
            headG.drawImage(imageManager.getSnakeHeadImage(), headX, headY, imageSize, imageSize, null);
            headG.dispose();

            if(paused){
                pauseDisplay(g);
            }
            }else{
                gameOver(g);
            }
            scoreDisplay(g);
            highScoreDisplay(g);
    }

    public void drawApple(Graphics g, Apple apple){
        Image appleImage = imageManager.getAppleImage(apple.getAppleType());

        int appleSize = UNIT_SIZE + 12;
        int appleOffset = (UNIT_SIZE - appleSize) / 2;

        g.drawImage(
            appleImage,
            apple.getX() + appleOffset,
            apple.getY() + appleOffset,
            appleSize,
            appleSize,
            null
        );
    }

    public void newApple(){
        apple = appleSpawner.spawnApple(SCREEN_WIDTH, SCREEN_HEIGHT, UNIT_SIZE);
    }

    public void newPoisonApple(){
        if(poisonApple == null && random.nextInt(8) == 0){
            poisonApple = appleSpawner.spawnPoisonApple(SCREEN_WIDTH, SCREEN_HEIGHT, UNIT_SIZE);
            poisonAppleDuration = System.currentTimeMillis();
        }
    }

    public double getHeadAngle(char direction){
        switch(direction){
            case 'U':
                return Math.PI;
            case 'D':
                return 0;
            case 'L':
                return Math.PI / 2;
            case 'R':
                return -Math.PI / 2;
            default:
                return 0;
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

    public void move(){
        for(int i = bodyParts -1; i > 0; i--){
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
        switch(direction){
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }
    }

    public void checkApple(){
        if((x[0] == apple.getX()) && (y[0] == apple.getY())){
            bodyParts++;
            x[bodyParts - 1] = x[bodyParts - 2];
            y[bodyParts - 1] = y[bodyParts - 2];
            previousX[bodyParts - 1] = x[bodyParts - 2];
            previousY[bodyParts - 1] = y[bodyParts - 2];
            applesEaten += apple.getPoints();

            if(applesEaten > highScore){
                highScore = applesEaten;
            }

            if(applesEaten % 2 == 0){
                moveDelay = Math.max(80, moveDelay - 10);
            }
            newApple();
            newPoisonApple();
        }
    }

    public void checkPoisonApple(){
        if(poisonApple != null && x[0] == poisonApple.getX() && y[0] == poisonApple.getY()){
            bodyParts--;
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
            if(currentTime - poisonAppleDuration >= 7000){
                poisonApple = null;
            }
        }
        
    }

    public void checkCollisions(){
        //checks if head collides with body;
        for(int i = bodyParts; i > 0; i--){
            if((x[0] == x[i]) && (y[0] == y[i])){ //if the head collides with the body;
                running = false;
            }
        }
        if(x[0] < 0){ //if the head collides with the left border;
            running = false;
        }
        if(x[0] >= SCREEN_WIDTH){ //if the head collides with the right border;
            running = false;
        }
        if(y[0] < 0){ //if the head collides with the top border;
            running = false;
        }
        if(y[0] >= SCREEN_HEIGHT){ //if the head collides with the bottom border;
            running = false;
        }
        if(!running){
            timer.stop();
        }

        if(!running){
            timer.stop();
            restartButton.setVisible(true);
        }
    }

    public void gameOver(Graphics g){ 
        g.setColor(Color.red);
        g.setFont(new Font("Times New Roman", Font.BOLD, 40));
        FontMetrics metrics = getFontMetrics(g.getFont()); //this is used to center the text on the screen;
        g.drawString("Game Over.", (SCREEN_WIDTH - metrics.stringWidth("Game Over."))/2, SCREEN_HEIGHT/2);
    }

    public void scoreDisplay(Graphics g){
        g.setColor(Color.red);
        g.setFont(new Font("Times New Roman", Font.BOLD, 20));
        FontMetrics metrics = getFontMetrics(g.getFont()); //this is used to center the text on the screen;
        g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten))/2, g.getFont().getSize());
    }

    public void highScoreDisplay(Graphics g){
            g.setColor(Color.red);
            g.setFont(new Font("Times New Roman", Font.BOLD, 20));
            g.drawString("High Score: " + highScore, 20, g.getFont().getSize());
    }

    public void pauseDisplay(Graphics g){
        g.setColor(Color.blue);
        g.setFont(new Font("Times New Roman", Font.BOLD, 40));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game is paused.", (SCREEN_WIDTH - metrics.stringWidth("Game is paused."))/2, SCREEN_HEIGHT/2);
    }

    public void restartGame(){
        bodyParts = 6;
        applesEaten = 0;
        direction = 'R';
        currentHeadAngle = getHeadAngle(direction);
        targetHeadAngle = getHeadAngle(direction);
        paused = false;
        poisonApple = null;
        moveDelay = startingDelay;
        animationProgress = 1.0;
        for(int i = 0; i < x.length; i++){
            x[i] = 0;
            y[i] = 0;
            previousX[i] = 0;
            previousY[i] = 0;
        }

        restartButton.setVisible(false);
        startGame();
        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        long now = System.nanoTime();
        double deltaTime = (now - lastFrameTime) / 1_000_000.0;
        lastFrameTime = now;

        if(running && !paused){
            animationProgress += deltaTime / moveDelay;
            updateHeadAngle();
            while(animationProgress >= 1.0){
                animationProgress -= 1.0;
                previousPosition();
                move();
                checkApple();
                checkPoisonApple();
                updatePoisonApple();
                checkCollisions();
            } 
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            switch(e.getKeyCode()){
                case KeyEvent.VK_LEFT:
                    if(direction != 'R'){
                        direction = 'L';
                        targetHeadAngle = getHeadAngle(direction);
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction != 'L'){
                        direction = 'R';
                        targetHeadAngle = getHeadAngle(direction);
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(direction != 'D'){
                        direction = 'U';
                        targetHeadAngle = getHeadAngle(direction);
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction != 'U'){
                        direction = 'D';
                        targetHeadAngle = getHeadAngle(direction);
                    }
                    break;
                case KeyEvent.VK_P:
                    if(!paused){
                        pauseStartTime = System.currentTimeMillis();
                        paused = true;
                    }else{
                        long pausedDuration = System.currentTimeMillis() - pauseStartTime;
                        if(poisonApple != null){
                            poisonAppleDuration += pausedDuration;
                        }
                        paused = false;
                    }
                    break;
            }
            repaint();
        }
    }

}