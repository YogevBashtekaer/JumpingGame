package org.example;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;

public class IceBall extends Attack{
    public IceBall(int speed) {
        super(Arrays.asList(
                new ImageIcon("src/main/java/Photos/ice_ball1.png"),
                new ImageIcon("src/main/java/Photos/ice_ball2.png"),
                new ImageIcon("src/main/java/Photos/ice_ball3.png"),
                new ImageIcon("src/main/java/Photos/ice_ball4.png")
        ), speed);
        this.attackType = AttackType.ICEBALL;
    }
}
