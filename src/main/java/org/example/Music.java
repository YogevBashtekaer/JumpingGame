package org.example;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.io.IOException;

public class Music {
     protected Clip clip;
     private AudioInputStream sound;

    public Music(String path) {
        try {
            File file = null;
                    file = new File(path);
            sound = AudioSystem.getAudioInputStream(file);
            clip = AudioSystem.getClip();
            clip.open(sound);
        }catch (Exception e){
            System.out.println("problem with sound");
        }
    }
    public  void play() {
        clip.start();
    }
    public  void stop() throws IOException {
        sound.close();
        clip.close();
        clip.stop();
    }
    public  void loadMusic(String path) {
        try {
            if (clip != null && clip.isRunning()) {
                stop();
            }
            File file = new File(path);
            sound = AudioSystem.getAudioInputStream(file);
            clip = AudioSystem.getClip();
            clip.open(sound);
        } catch (Exception e) {
            System.out.println("Problem with loading sound: " + e.getMessage());
        }
    }
    public  void changeMusic(String newPath) {
        loadMusic(newPath);
        play();
    }


    public static void main(String[] args) {
         //Music music = new Music();
         //music.play();

    }
}
