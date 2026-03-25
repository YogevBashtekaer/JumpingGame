package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class GamePanel extends JPanel implements KeyListener, ActionListener {
    private Player player;
    private ArrayList<Attack> attacks;
    private GameModes gameMode;
    private Timer gameTimer;

    private Timer attackTimer;
    private Random random;
    private int elapsedTime = 0;
    private Timer clockTimer;
    private JButton restartButton;
    private JButton menuButton;
    private JLabel gameOverLabel;
    private Music music;
    private Window parentWindow;

    private int highScore = 0; // משתנה לשמירת הציון הגבוה
    private static final String HIGH_SCORE_FILE = "highscore.txt"; // מיקום הקובץ לשמירת הציון הגבוה

    public GamePanel(Window parentWindow){
        this.parentWindow = parentWindow;
        this.setBackground(Color.black);
        this.setLayout(null);
        this.setPreferredSize(new Dimension(Utils.WINDOW_WIDTH, Utils.WINDOW_HEIGHT));
        this.player = new Player();
        this.attacks = new ArrayList<>();
        this.gameMode = GameModes.BEFORE_STARTING;
        this.random = new Random();

        this.setFocusable(true);
        this.addKeyListener(this);
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.gameTimer = new Timer(10,this);
        this.gameTimer.start();
        this.music = new Music(Utils.BACKGROUND_MUSIC);
        this.requestFocusInWindow();


        this.attackTimer = new Timer(random.nextInt(3000) + 2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gameMode.equals(GameModes.RUNNING)) {
                    addRandomAttack();

                    attackTimer.setDelay(random.nextInt(3000) + 2000);
                }
            }
        });
        this.attackTimer.start();

        this.clockTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gameMode.equals(GameModes.RUNNING)) {
                    elapsedTime++;
                    repaint();
                }
            }
        });
        this.clockTimer.start();

        this.restartButton = new JButton("Restart");
        restartButton.setBounds(Utils.WINDOW_WIDTH/2-50, 150, 100, 50);
        restartButton.addActionListener(e -> restartGame());
        this.add(restartButton);
        restartButton.setVisible(false);

        this.menuButton = new JButton("Menu");
        menuButton.setBounds(Utils.WINDOW_WIDTH / 2 - 50, 220, 100, 50);
        menuButton.addActionListener(e -> parentWindow.switchToMenuPanel());
        this.add(menuButton);
        menuButton.setVisible(false);

        this.gameOverLabel = new JLabel("GAME OVER");
        this.gameOverLabel.setBounds(Utils.WINDOW_WIDTH/2-55,100,200,50);
        this.gameOverLabel.setForeground(Color.red);
        this.gameOverLabel.setFont(new Font("Verdana", Font.PLAIN, 18));
        this.add(this.gameOverLabel);
        this.gameOverLabel.setVisible(false);
        loadHighScore();
    }
    public void startGame() {
        this.requestFocusInWindow();
    }

    public void restartGame() {
        this.player = new Player();
        this.attacks.clear();
        this.elapsedTime = 0;
        this.gameMode = GameModes.BEFORE_STARTING;
        this.music.loadMusic(Utils.BACKGROUND_MUSIC);
        repaint();
    }

    public void addRandomAttack() {
        Attack newAttack;
        int attackType = random.nextInt(2);
        int speed = random.nextInt(3) + 1;
        if (attackType == 0) {
            newAttack = new FireBall(speed);
        } else {
            newAttack = new IceBall(speed);
        }
        attacks.add(newAttack);
        System.out.println("New attack added: " + (newAttack instanceof FireBall ? "FireBall" : "IceBall"));
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.player.paint(g);
        if(this.gameMode.equals(GameModes.RUNNING)) {
            for (Attack attack : attacks) {
                attack.paint(g);
                attack.move();
            }
        }
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Time: " + elapsedTime , 10, 20);
        g.drawString("High Score: " + highScore, 10, 50);
        if (gameMode.equals(GameModes.GAME_OVER)) {
            restartButton.setVisible(true);
            this.gameOverLabel.setVisible(true);
            this.menuButton.setVisible(true);
        } else {
            restartButton.setVisible(false);
            this.gameOverLabel.setVisible(false);
            this.menuButton.setVisible(false);
        }
    }

    public void checkCollisions() {
        Rectangle playerBounds = this.player.getBounds();
        ArrayList<Attack> attacksToRemove = new ArrayList<>();
        for (Attack attack : attacks) {
            Rectangle attackBounds = attack.getBounds();
            if (playerBounds.intersects(attackBounds)) {
                if (gameMode.equals(GameModes.GAME_OVER)) {
                    clockTimer.stop();
                }
                this.gameMode = GameModes.GAME_OVER;
                handlePlayerHit(attack);
                attacksToRemove.add(attack);
                if (elapsedTime > highScore) {
                    highScore = elapsedTime;
                    saveHighScore();
                }
                this.music.loadMusic(Utils.END_MUSIC);
                this.music.play();
            }
        }

        attacks.removeAll(attacksToRemove);
    }
    public void checkAttacksOutOfBounds(){
        ArrayList<Attack> attacksToRemove = new ArrayList<>();
        for (Attack attack : attacks) {
            if (attack.attackOutOfBounds()) {
                attacksToRemove.add(attack);
            }
        }
        attacks.removeAll(attacksToRemove);
    }

    public void handlePlayerHit(Attack attack) {
        if (attack.attackType.equals(AttackType.FIREBALL)){
            player.playerMovement = PlayerMovement.PLAYER_HIT_BY_FIREBALL;
        } else if (attack.attackType.equals(AttackType.ICEBALL)) {
            player.playerMovement = PlayerMovement.PLAYER_HIT_BY_ICEBALL;
        }
        this.player.hit();
        this.gameMode = GameModes.GAME_OVER;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if(!this.gameMode.equals(GameModes.GAME_OVER) &&
                !this.player.getPlayerMovement().equals(PlayerMovement.PLAYER_HIT_BY_FIREBALL) &&
                !this.player.getPlayerMovement().equals(PlayerMovement.PLAYER_HIT_BY_ICEBALL)){
            switch (keyCode) {
                case KeyEvent.VK_DOWN:
                    if (this.player.getPlayerMovement().equals(PlayerMovement.PLAYER_RUNNING)) {
                        this.player.rolling();
                    }
                    break;

                case KeyEvent.VK_UP:
                    if (this.player.getPlayerMovement().equals(PlayerMovement.PLAYER_RUNNING)) {
                        this.player.jump();
                    }
                    break;

                case KeyEvent.VK_RIGHT:
                    if (this.player.getPlayerMovement().equals(PlayerMovement.PLAYER_STAND)) {
                        this.player.running();
                        this.gameMode = GameModes.RUNNING;
                        this.music.play();
                    }
            }
        }
        switch (keyCode){
            case KeyEvent.VK_LEFT:
                System.out.println("number of attack: "+this.attacks.size());
        }
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {

          checkCollisions();
          checkAttacksOutOfBounds();
          repaint();
    }

    private void saveHighScore() {
        try {
            FileWriter writer = new FileWriter(HIGH_SCORE_FILE);
            writer.write(String.valueOf(highScore));
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadHighScore() {
        try {
            File file = new File(HIGH_SCORE_FILE);
            if (file.exists()) {
                Scanner scanner = new Scanner(file);
                if (scanner.hasNextInt()) {
                    highScore = scanner.nextInt();
                }
                scanner.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
