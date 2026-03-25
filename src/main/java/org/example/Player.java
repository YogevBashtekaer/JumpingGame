package org.example;

import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Player extends Rectangle {
    private Image image;
    protected PlayerMovement playerMovement;
    private Music music;
    protected List<Image> runningImages;
    protected List<String> runningMusicPath;
    private int currentImageIndex = 0;
    private int currentRunningMusicPathIndex = 0;
    final static int PLAYER_X = 50;
    final static int PLAYER_Y_START = 175;
    final static int JUMP_COUNTER = 20;

    public Player() {
        this.x = PLAYER_X;
        this.y = PLAYER_Y_START;

        // טעינת תמונת עמידה
        this.image = loadImage("src/main/java/Photos/davis_stand.png");

        this.playerMovement = PlayerMovement.PLAYER_STAND;

        // טעינת תמונות ריצה
        this.runningImages = Arrays.asList(
                loadImage("src/main/java/Photos/davis_running1.png"),
                loadImage("src/main/java/Photos/davis_running2.png"),
                loadImage("src/main/java/Photos/davis_running3.png")
        );

        this.music = new Music("src/main/java/Music/run_tick.wav");
        this.runningMusicPath = Arrays.asList(
                "src/main/java/Music/run_tick.wav",
                "src/main/java/Music/run_tok.wav"
        );
    }

    private Image loadImage(String path) {
        Image img = new ImageIcon(path).getImage();
        if (img == null) {
            System.out.println("Error loading image: " + path);
        } else {
            this.width = img.getWidth(null);
            this.height = img.getHeight(null);
        }
        return img;
    }

    public void hit() {
        try {
            if (this.music.clip.isRunning()) {
                this.music.stop();
            }
        } catch (IOException e) {
            System.out.println("Error stopping music: " + e.getMessage());
        }

        if (this.playerMovement.equals(PlayerMovement.PLAYER_HIT_BY_FIREBALL)) {
            System.out.println("player hit BY FIREBALL!!!!");
            this.music = new Music(Utils.HIT_BY_FIREBALL_MUSIC);
            this.music.play();
            new Thread(() -> {
                this.image = loadImage("src/main/java/Photos/davis_hit_by_fireball1.png");
                Utils.sleep(100);
                this.image = loadImage("src/main/java/Photos/davis_hit_by_fireball2.png");
                Utils.sleep(100);
                this.image = loadImage("src/main/java/Photos/davis_hit_by_fireball3.png");
                Utils.sleep(100);
                this.image = loadImage("src/main/java/Photos/davis_hit_by_fireball4.png");
                Utils.sleep(100);
                this.image = loadImage("src/main/java/Photos/davis_dead.png");
                this.y += 30;
            }).start();
        } else if (this.playerMovement.equals(PlayerMovement.PLAYER_HIT_BY_ICEBALL)) {
            System.out.println("player hit BY ICEBALL!!!!");
            this.music = new Music(Utils.HIT_BY_ICEBALL_MUSIC);
            this.music.play();
            new Thread(() -> {
                this.image = loadImage("src/main/java/Photos/davis_hit_by_iceball1.png");
                Utils.sleep(100);
                this.image = loadImage("src/main/java/Photos/davis_hit_by_iceball2.png");
            }).start();
        }
    }

    public void jump() {
        try {
            if (this.music.clip.isRunning()) {
                this.music.stop();
            }
        } catch (IOException e) {
            System.out.println("Error stopping music: " + e.getMessage());
        }

        new Thread(() -> {
            this.playerMovement = PlayerMovement.PLAYER_JUMP;
            System.out.println("PLAYER JUMP!!!");

            int i = 0;
            this.image = loadImage("src/main/java/Photos/davis_jump.png");

            while (i != JUMP_COUNTER) {
                i++;
                this.y -= 5;
                Utils.sleep(10);
            }

            while (i != 0) {
                i--;
                this.y += 5;
                Utils.sleep(10);
            }

            if (!this.getPlayerMovement().equals(PlayerMovement.PLAYER_HIT_BY_FIREBALL) &&
                    !this.getPlayerMovement().equals(PlayerMovement.PLAYER_HIT_BY_ICEBALL)) {
                this.playerMovement = PlayerMovement.PLAYER_RUNNING;
                running();
            }
        }).start();
    }

    public void rolling() {
        try {
            if (this.music.clip.isRunning()) {
                this.music.stop();
            }
        } catch (IOException e) {
            System.out.println("Error stopping music: " + e.getMessage());
        }

        System.out.println("PLAYER ROLLING!!!");
        this.playerMovement = PlayerMovement.PLAYER_ROLLING;

        new Thread(() -> {
            this.image = loadImage("src/main/java/Photos/davis_rolling1.png");
            Utils.sleep(100);
            this.image = loadImage("src/main/java/Photos/davis_rolling2.png");
            Utils.sleep(100);
            this.image = loadImage("src/main/java/Photos/davis_rolling3.png");

            Utils.sleep(100);

            if (!this.getPlayerMovement().equals(PlayerMovement.PLAYER_HIT_BY_FIREBALL) &&
                    !this.getPlayerMovement().equals(PlayerMovement.PLAYER_HIT_BY_ICEBALL)) {
                this.playerMovement = PlayerMovement.PLAYER_RUNNING;
                running();
            }
        }).start();
    }

    public void running() {
        this.playerMovement = PlayerMovement.PLAYER_RUNNING;
        System.out.println("PLAYER RUN!!!");

        if (!this.music.clip.isRunning()) {
            currentRunningMusicPathIndex = (currentRunningMusicPathIndex + 1) % this.runningMusicPath.size();
            this.music.loadMusic(runningMusicPath.get(this.currentRunningMusicPathIndex));
            this.music.clip.loop(Clip.LOOP_CONTINUOUSLY);
        }

        new Thread(() -> {
            while (this.playerMovement.equals(PlayerMovement.PLAYER_RUNNING)) {
                currentImageIndex = (currentImageIndex + 1) % runningImages.size();
                this.image = runningImages.get(currentImageIndex);
                Utils.sleep(100);
            }

            try {
                if (this.music.clip.isRunning()) {
                    this.music.stop();
                }
            } catch (IOException e) {
                System.out.println("Error stopping music: " + e.getMessage());
            }
        }).start();
    }

    public void paint(Graphics g) {
        if (image != null) {
            g.drawImage(this.image, this.x, this.y, null); // ציור בגודל המקורי של התמונה
        } else {
            g.setColor(Color.RED);
            g.fillRect(this.x, this.y, 50, 50); // fallback במקרה שהתמונה לא נטענה
        }
    }

    public PlayerMovement getPlayerMovement() {
        return playerMovement;
    }
}