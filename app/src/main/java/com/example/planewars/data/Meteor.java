package com.example.planewars.data;

import android.graphics.Bitmap;

import com.example.planewars.R;
import com.example.planewars.func.Award;

import java.util.Random;

public class Meteor extends FlyingObject implements Award {

    private static Bitmap[] images;

    // 静态代码块
    static {
        images = new Bitmap[3];
        images[0] = loadImage(R.drawable.meteor0);
        images[1] = loadImage(R.drawable.meteor1);
        images[2] = loadImage(R.drawable.meteor2);
    }

    private int xSpeed;// x坐标的速度
    private int ySpeed;// y坐标的速度
    private int awardType;// 获取奖励的类型

    public Meteor() {
        super(images[0].getWidth(), images[0].getHeight());
        ySpeed = 2;
        Random rand = new Random();
        xSpeed = rand.nextInt(3)-1;
        awardType = rand.nextInt(4);
    }

    public void step() {
        x += xSpeed;
        y += ySpeed;
        if (x < 0 || x >= ShootMain.SCREEN_WIDTH - this.width) {
            xSpeed *= -1;
        }

    }

    // 得到图片
    int index = 1;

    @Override
    public Bitmap getImage() {// 10M
        if (isLife()) {
            return images[0];
        } else if (isDead()) {// 图片的切换
            Bitmap img = images[index++];
            if (index == images.length) {
                state = REMOVE;
            }
            return img;
        }

        return null;
    }

    @Override
    public int getAwardType() {
        // TODO Auto-generated method stub
        if (awardType != 0)
            awardType = 1;
        return awardType;
    }

}