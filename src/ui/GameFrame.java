package ui;
import difficulty.*;

import java.awt.*;
import javax.swing.*;

public class GameFrame extends JFrame {
    public GameFrame(){
        this.setTitle("Snake");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);

        showDifficultyMenu();
        this.pack();
        this.setVisible(true); //makes the window appear on the screen
        this.setLocationRelativeTo(null);
    }

    private void showDifficultyMenu(){
        JPanel menuPanel = new JPanel();
        menuPanel.setPreferredSize(new Dimension(600,600));
        menuPanel.setBackground(Color.black);
        menuPanel.setLayout(new GridBagLayout());
        
        JButton easyButton = new JButton("Easy");
        JButton mediumButton = new JButton("Medium");
        JButton hardButton = new JButton("Hard");

        easyButton.addActionListener(e -> startGame(new EasyDifficulty()));
        mediumButton.addActionListener(e -> startGame(new MediumDifficulty()));
        hardButton.addActionListener(e -> startGame(new HardDifficulty()));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.black);
        buttonPanel.setLayout(new GridLayout(3,1,0,20));

        buttonPanel.add(easyButton);
        buttonPanel.add(mediumButton);
        buttonPanel.add(hardButton);

        menuPanel.add(buttonPanel);
        this.setContentPane(menuPanel);

    }

    private void startGame(DifficultyBehaviour difficulty){
        GamePanel gamePanel = new GamePanel(difficulty);
        this.setContentPane(gamePanel);
        this.pack();

        SwingUtilities.invokeLater(() -> {
            gamePanel.requestFocusInWindow();
        });
    }
}