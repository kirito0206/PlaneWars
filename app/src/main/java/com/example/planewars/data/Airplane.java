package com.example.planewars.data;

import android.graphics.Bitmap;

import com.example.planewars.R;
import com.example.planewars.func.Enemy;

public class Airplane extends FlyingObject implements Enemy {

    private static Bitmap[] images;
    public static float speed;
    public static float bloodNum;
    private int blood;
    //静态代码块
    static {
        images = new Bitmap[5];
        images[0] = loadImage(R.drawable.airplane0);
        images[1] = loadImage(R.drawable.airplane1);
        images[2] = loadImage(R.drawable.airplane2);
        images[3] = loadImage(R.drawable.airplane3);
        images[4] = loadImage(R.drawable.airplane4);
        speed = 5;
        bloodNum = 1;
    }

    /**构造方法*/
    public Airplane(){
        super(images[0].getWidth(), images[0].getHeight());
        blood = (int)bloodNum;
    }

    /**移动*/
    public void step(){
        this.y += speed;
    }

    //得到图片
    int index = 1;
    @Override
    public Bitmap getImage() {//10M
        if (isLife()) {
            return images[0];
        }else if(isDead()){//图片的切换
            Bitmap img = images[index++];
            if (index == images.length) {
                state = REMOVE;
            }
            return img;
        }
        return null;
    }

    @Override
    public int getScore() {
        return 1;
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