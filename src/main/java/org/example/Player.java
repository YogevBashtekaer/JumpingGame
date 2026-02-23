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

    public Player(){
        this.x = PLAYER_X;
        this.y = PLAYER_Y_START;
        this.image = new ImageIcon("src/main/java/Photos/davis_stand.png").getImage();
        if (image == null){
            System.out.println("player image problem");
        }
        this.height = this.image.getHeight(null);
        this.width = this.image.getWidth(null);
        this.playerMovement = PlayerMovement.PLAYER_STAND;
        this.runningImages = Arrays.asList(
                new ImageIcon("src/main/java/Photos/davis_running1.png").getImage(),
                new ImageIcon("src/main/java/Photos/davis_running2.png").getImage(),
                new ImageIcon("src/main/java/Photos/davis_running3.png").getImage()
        );
        this.music = new Music("src/main/java/Music/run_tick.wav");
        this.runningMusicPath = Arrays.asList("src/main/java/Music/run_tick.wav",
                "src/main/java/Music/run_tok.wav");


    }
    public void hit(){
        try {
            if (this.music.clip.isRunning()) {
                this.music.stop(); // עצירת המוזיקה
            }
        } catch (IOException e) {
            System.out.println("Error stopping music: " + e.getMessage());
        }
        if (this.playerMovement.equals(PlayerMovement.PLAYER_HIT_BY_FIREBALL)) {
            System.out.println("player hit BY FIREBALL!!!!");
            this.music = new Music(Utils.HIT_BY_FIREBALL_MUSIC);
            this.music.play();
            new Thread(() -> {
                this.image = new ImageIcon("src/main/java/Photos/davis_hit_by_fireball1.png").getImage();
                Utils.sleep(100);
                this.image = new ImageIcon("src/main/java/Photos/davis_hit_by_fireball2.png").getImage();
                Utils.sleep(100);
                this.image = new ImageIcon("src/main/java/Photos/davis_hit_by_fireball3.png").getImage();
                Utils.sleep(100);
                this.image = new ImageIcon("src/main/java/Photos/davis_hit_by_fireball4.png").getImage();
                Utils.sleep(100);
                this.image = new ImageIcon("src/main/java/Photos/davis_dead.png").getImage();
            }).start();
        } else if (this.playerMovement.equals(PlayerMovement.PLAYER_HIT_BY_ICEBALL)) {
            System.out.println("player hit BY ICEBALL!!!!");
            this.music = new Music(Utils.HIT_BY_ICEBALL_MUSIC);
            this.music.play();
            new Thread(() -> {
                this.image = new ImageIcon("src/main/java/Photos/davis_hit_by_iceball1.png").getImage();
                Utils.sleep(100);
                this.image = new ImageIcon("src/main/java/Photos/davis_hit_by_iceball2.png").getImage();
            }).start();
        }
    }
    public void jump() {
        try {
            if (this.music.clip.isRunning()) {
                this.music.stop(); // עצירת המוזיקה
            }
        } catch (IOException e) {
            System.out.println("Error stopping music: " + e.getMessage());
        }
        new Thread(()->{
            this.playerMovement = PlayerMovement.PLAYER_JUMP;
            System.out.println("PLAYER JUMP!!!");
            int i = 0;
            this.image = new ImageIcon("src/main/java/Photos/davis_jump.png").getImage();
            while (i!=JUMP_COUNTER){
                i++;
                this.y-=5;
                Utils.sleep(10);
            }

            while (i!=0){
                i--;
                this.y+=5;
                Utils.sleep(10);
            }

            if(!this.getPlayerMovement().equals(PlayerMovement.PLAYER_HIT_BY_FIREBALL) &&
                    !this.getPlayerMovement().equals(PlayerMovement.PLAYER_HIT_BY_ICEBALL)) {
                this.playerMovement = PlayerMovement.PLAYER_RUNNING;
                running();
            }
        }).start();
    }
    public void rolling(){
        try {
            if (this.music.clip.isRunning()) {
                this.music.stop(); // עצירת המוזיקה
            }
        } catch (IOException e) {
            System.out.println("Error stopping music: " + e.getMessage());
        }
        System.out.println("PLAYER ROLLING!!!");
        this.playerMovement = PlayerMovement.PLAYER_ROLLING;
        new Thread(()->{
        this.image = new ImageIcon("src/main/java/Photos/davis_rolling1.png").getImage();
        Utils.sleep(100);
        this.image = new ImageIcon("src/main/java/Photos/davis_rolling2.png").getImage();
        Utils.sleep(100);
        this.image = new ImageIcon("src/main/java/Photos/davis_rolling3.png").getImage();
        Utils.sleep(100);
        if(!this.getPlayerMovement().equals(PlayerMovement.PLAYER_HIT_BY_FIREBALL) &&
                !this.getPlayerMovement().equals(PlayerMovement.PLAYER_HIT_BY_ICEBALL)) {
            this.playerMovement = PlayerMovement.PLAYER_RUNNING;
            running();
        }
        }).start();
    }

    public void running(){
        this.playerMovement = PlayerMovement.PLAYER_RUNNING;
        System.out.println("PLAYER RUN!!!");

        // בדיקה אם המוזיקה לא מתנגנת כבר, ואז טוענים אותה ומנגנים אותה בלולאה
        if (!this.music.clip.isRunning()) {
            currentRunningMusicPathIndex = (currentRunningMusicPathIndex + 1) % this.runningMusicPath.size();
            this.music.loadMusic(runningMusicPath.get(this.currentRunningMusicPathIndex));
            this.music.clip.loop(Clip.LOOP_CONTINUOUSLY); // מנגן את המוזיקה בלולאה אינסופית
        }

        new Thread(() -> {
            while (this.playerMovement.equals(PlayerMovement.PLAYER_RUNNING)) {
                // החלפת תמונות הריצה
                currentImageIndex = (currentImageIndex + 1) % runningImages.size();
                this.image = runningImages.get(currentImageIndex);
                Utils.sleep(100); // זמן המתנה בין התמונות
            }

            // עצירת המוזיקה כאשר הריצה נעצרת
            try {
                if (this.music.clip.isRunning()) {
                    this.music.stop(); // עצירת המוזיקה
                }
            } catch (IOException e) {
                System.out.println("Error stopping music: " + e.getMessage());
            }
        }).start();
    }

    public void paint(Graphics g){
        g.fillRect(this.x,this.y,this.width,this.height);
        g.drawImage(this.image,this.x,this.y,this.width,this.height,null);
    }

    public PlayerMovement getPlayerMovement() {
        return playerMovement;
    }



}
