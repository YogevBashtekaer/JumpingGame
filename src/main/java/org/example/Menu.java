package org.example;

import javax.swing.*;
import java.awt.*;

public class Menu extends JPanel {
    private JButton startGameButton;
    private JButton controlButton;
    private Window parentWindow;

    public Menu(Window parentWindow){
        this.parentWindow = parentWindow;

        this.setBackground(Color.black);
        this.setLayout(null);
        this.setPreferredSize(new Dimension(Utils.WINDOW_WIDTH, Utils.WINDOW_HEIGHT));

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
}
