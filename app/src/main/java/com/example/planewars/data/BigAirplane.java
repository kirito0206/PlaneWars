package com.example.planewars.data;

import android.content.res.TypedArray;
import android.graphics.Bitmap;

import com.example.planewars.MyApplication;
import com.example.planewars.R;
import com.example.planewars.func.Enemy;

import java.util.Random;

public class BigAirplane extends FlyingObject implements Enemy {
    private static Bitmap[] images;
    public static int speed;// 速度
    public static float bloodNum;//血量
    private int blood;
    private int xSpeed;
    // 静态代码块
    static {
        images = new Bitmap[5];
        images[0] = loadImage(R.drawable.bigplane0);
        images[1] = loadImage(R.drawable.bigplane1);
        images[2] = loadImage(R.drawable.bigplane2);
        images[3] = loadImage(R.drawable.bigplane3);
        images[4] = loadImage(R.drawable.bigplane4);
        speed = 2;
        bloodNum = 2;
    }

    /** 构造方法 */
    public BigAirplane() {
        super(images[0].getWidth(), images[0].getHeight());
        blood = (int)bloodNum;
        Random rand = new Random();
        xSpeed = rand.nextInt(3)-1;
    }

    public void step(){
        this.y += speed;
        this.x += xSpeed;
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

    /**重写得分接口*/
    @Override
    public int getScore() {
        return 3;
    }

    @Override
    public void goDead() {
        if (blood > 1){
            blood--;
            return;
        }
        super.goDead();
    }
}