package org.example;

import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private Image icon;
    public Window(){
        this.setTitle("Jumping Game");
        this.setBounds(0,0,Utils.WINDOW_WIDTH,Utils.WINDOW_HEIGHT);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        this.icon = new ImageIcon("src/main/java/Photos/davis_f.png").getImage();
        this.setIconImage(this.icon);

        this.cardLayout = new CardLayout();
        this.mainPanel = new JPanel(cardLayout);

        Menu menuPanel = new Menu(this);
        GamePanel gamePanel = new GamePanel(this);

        mainPanel.add(menuPanel, "menu");
        mainPanel.add(gamePanel, "game");

        this.add(mainPanel);
        this.pack();
        this.setVisible(true);
    }

    public void switchToGamePanel() {
        cardLayout.show(mainPanel, "game");
        GamePanel gamePanel = (GamePanel) mainPanel.getComponent(1);
        gamePanel.startGame();
    }


    public void switchToMenuPanel() {
        cardLayout.show(mainPanel, "menu");
    }


}
