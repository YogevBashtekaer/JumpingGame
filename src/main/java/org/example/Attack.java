package org.example;

import javax.swing.*;
import java.awt.*;
import java.util.List;


abstract class Attack extends Rectangle {
    protected Image image;
    protected List<Image> images;
    protected int speed;
    protected int currentImageIndex = 0;
    protected AttackType attackType;
    final static int ATTACK_Y = 205;
    public Attack(List<ImageIcon> imageIcons , int speed) {
        this.images = imageIcons.stream().map(ImageIcon::getImage).toList();
        this.image = this.images.get(0);
        this.x = Utils.WINDOW_WIDTH;
        this.y = ATTACK_Y;
        this.height = this.image.getHeight(null);
        this.width = this.image.getWidth(null);
        this.speed = speed;
    }

    public void paint(Graphics g) {
        g.fillRect(this.x,this.y,this.width,this.height);
        g.drawImage(this.image,this.x,this.y,this.width,this.height,null);
    }
    public void move() {
        new Thread(() -> {
            while (this.x != -90) {
                this.x -= speed;
                currentImageIndex = (currentImageIndex + 1) % images.size();
                this.image = images.get(currentImageIndex);
                Utils.sleep(100);
            }
        }).start();
    }


    public AttackType getAttackType() {
        return attackType;
    }
    public boolean attackOutOfBounds(){
        return this.x<= 0;
    }
}
