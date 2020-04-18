package com.example.planewars.data;

import android.graphics.Bitmap;

import com.example.planewars.R;

public class Bullet extends FlyingObject{

    private static Bitmap images;
    //静态代码块
    static {
        images = loadImage(R.drawable.bullet);
    }

    private int speed;//速度
    /**构造方法*/
    public Bullet(float x, float y){
        super(images.getWidth(), images.getHeight(), x, y);
        speed = 2;
    }
    /**子弹的移动*/
    public void step(){
        this.y -= speed;
    }
    /**重写getImage()方法获取到图片*/
    @Override
    public Bitmap getImage() {
        if (isLife()) {
            return images;
        }else if(isDead()){
            state = REMOVE;//修改状态为删除状态
        }
        return null;
    }
    /**检测是否越界*/
    @Override
    public boolean outOfBounds() {
        return this.y <= -this.height;
    }

}