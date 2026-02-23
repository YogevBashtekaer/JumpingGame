package org.example;

import java.awt.*;
import java.util.Optional;

public class Utils {
    final static int WINDOW_WIDTH = 700;
    final static int WINDOW_HEIGHT = 300;
    final static int SECOND = 1000;
    final static String BACKGROUND_MUSIC = "src/main/java/Music/background_music.wav";
    final static String END_MUSIC = "src/main/java/Music/end_music.wav";
    final static String HIT_BY_ICEBALL_MUSIC = "src/main/java/Music/hit_by_iceball.wav";
    final static String HIT_BY_FIREBALL_MUSIC = "src/main/java/Music/hit_by_fireball.wav";
    public static void sleep(int millis){
        try {
            Thread.sleep(millis);
        }catch (Exception e){
            System.out.println("problem with sleep!!!!!");
        }
    }

}
