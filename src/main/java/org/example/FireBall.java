package org.example;

import javax.swing.*;
import java.util.Arrays;

public class FireBall extends Attack{
    public FireBall(int speed) {
        super(Arrays.asList(
                new ImageIcon("src/main/java/Photos/fire_ball1.png"),
                new ImageIcon("src/main/java/Photos/fire_ball2.png"),
                new ImageIcon("src/main/java/Photos/fire_ball3.png"),
                new ImageIcon("src/main/java/Photos/fire_ball4.png"),
                new ImageIcon("src/main/java/Photos/fire_ball5.png"),
                new ImageIcon("src/main/java/Photos/fire_ball6.png")
        ), speed);
        this.attackType = AttackType.FIREBALL;

    }



}
