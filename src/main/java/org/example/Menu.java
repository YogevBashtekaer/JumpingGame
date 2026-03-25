package org.example;

import javax.swing.*;
import java.awt.*;

public class Menu extends JPanel {
    private JButton startGameButton;
    private JButton controlButton;
    private Window parentWindow;
    private Image title;
    private Image coolPhoto1;
    private Image coolPhoto2;


    private static final String TITLE_IMAGE_PATH = "/Photos/title.png";
    private static final String COOL_PHOTO_1_IMAGE_PATH = "/Photos/cool_photo_1.png";
    private static final String COOL_PHOTO_2_IMAGE_PATH = "/Photos/cool_photo_2.png";
    public Menu(Window parentWindow){
        this.parentWindow = parentWindow;

        this.setBackground(Color.black);
        this.setLayout(null);
        this.setPreferredSize(new Dimension(Utils.WINDOW_WIDTH, Utils.WINDOW_HEIGHT));

        this.title = new ImageIcon(getClass().getResource(TITLE_IMAGE_PATH)).getImage();
        this.coolPhoto1 = new ImageIcon(getClass().getResource(COOL_PHOTO_1_IMAGE_PATH)).getImage();
        this.coolPhoto2 = new ImageIcon(getClass().getResource(COOL_PHOTO_2_IMAGE_PATH)).getImage();

        this.startGameButton = new JButton("start game");
        this.startGameButton.setBounds(Utils.WINDOW_WIDTH/2-50,100,100,25);
        this.startGameButton.addActionListener(e -> parentWindow.switchToGamePanel());
        this.add(startGameButton);

        this.controlButton = new JButton("Control");
        this.controlButton.setBounds(Utils.WINDOW_WIDTH / 2 - 50, 150, 100, 25);
        this.controlButton.addActionListener(e -> showGameRulesDialog());
        this.add(controlButton);
        this.setVisible(true);

    }
    private void showGameRulesDialog() {
        JDialog rulesDialog = new JDialog(parentWindow, "Game Rules", true);
        rulesDialog.setSize(400, 250);
        rulesDialog.setLocationRelativeTo(this);


        JTextArea rulesText = new JTextArea();
        rulesText.setText("Here are the game rules:\n" +
                "1. Press the RIGHT button to start running.\n" +
                "2. Press the UP button to jump.\n" +
                " You need to avoid objects");
        rulesText.setEditable(false);


        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(rulesText), BorderLayout.CENTER);


        rulesDialog.add(panel);
        rulesDialog.setVisible(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(title, 10 ,10, null);
        g.drawImage(coolPhoto1, 20 ,180, null);
        g.drawImage(coolPhoto2, 450 ,80, null);
    }
}
