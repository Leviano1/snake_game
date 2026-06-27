package ui;
import difficulty.DifficultyBehaviour;
import render.GameRender;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import config.GameConfig;
import controller.GameController;
import controller.SnakeKeyAdapter;

public class GamePanel extends JPanel implements ActionListener{
    private final GameController gameController;
    private final GameRender gameRender;
    private final Timer timer;
    private final JButton restartButton;
    private long lastFrameTime;


    public GamePanel(DifficultyBehaviour difficulty){
        gameController = new GameController(difficulty);
        gameRender = new GameRender();
        setPreferredSize(new Dimension(GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT));
        //this.setBackground(Color.black);
        setFocusable(true);
        setLayout(null);
        addKeyListener(new SnakeKeyAdapter(gameController, this::repaint));
        restartButton = new JButton("Restart.");
        restartButton.setBounds(225, 350, 150, 40);
        restartButton.setVisible(false);
        restartButton.addActionListener(e -> restartGame());
        add(restartButton);
        lastFrameTime = System.nanoTime();
        timer = new Timer(GameConfig.FRAME_DELAY, this); // runs every 15ms;
        timer.start();
    }

    public void restartGame(){
        gameController.restartGame();
        restartButton.setVisible(false);
        requestFocusInWindow();
        repaint();
    }

    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        gameRender.render(g, gameController);
        restartButton.setVisible(!gameController.isRunning());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        long now = System.nanoTime();
        double deltaTime = (now - lastFrameTime) / 1_000_000.0;
        deltaTime = Math.min(deltaTime, 50);
        lastFrameTime = now;
        gameController.updateFrame(deltaTime);
        repaint();
    }

}