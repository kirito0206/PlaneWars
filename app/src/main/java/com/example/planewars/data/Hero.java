package com.example.planewars.data;

import android.graphics.Bitmap;

import com.example.planewars.R;

public class Hero extends FlyingObject{

    private static Bitmap[] images;
    //静态代码块
    static {
        images = new Bitmap[2];
        images[0] = loadImage(R.drawable.hero0);
        images[1] = loadImage(R.drawable.hero1);
    }
    //成员变量
    private int life;//生命值
    private int doubleFire;//火力

    /**构造方法*/
    public Hero() {
        super(images[0].getWidth(), images[0].getHeight(), ShootMain.SCREEN_WIDTH, ShootMain.SCREEN_HEIGHT*2);
        life = 6;
        doubleFire = 0;//单倍火力
    }
    /**英雄机的移动:随着鼠标的移动而动*/
    public void movedTo(float x, float y){
        this.x = x - this.width/2;
        this.y = y - this.height/2;
    }
    /**英雄机的移动*/
    public void step(){
        System.out.println("图片的切换");
    }
    int index = 0;
    @Override
    public Bitmap getImage() {
//		return images[0];
        return images[index++/10%images.length];
    }

    /**英雄机产生子弹*/
    public Bullet[] shoot(){
        float xStep = this.width/4;
        int yStep = 15;
        if (doubleFire > 0) {//双倍火力
            Bullet[] bs = new Bullet[2];
            bs[0] = new Bullet(this.x + xStep * 1, this.y - yStep);
            bs[1] = new Bullet(this.x + xStep * 3, this.y - yStep);
            doubleFire -= 2;
            return bs;
        }else{
            Bullet[] bs = new Bullet[1];
            bs[0] = new Bullet(this.x + xStep * 2, this.y - yStep);
            return bs;
        }
    }


    public void addDoubleFire(){
        doubleFire += 10;
    }

    public void addLife(){
        life++;
    }
    /**获得生命*/
    public int getLife(){
        return life;
    }

    /**减少生命*/
    public void substractLife(){
        life--;
    }
    /**清空火力*/
    public void clearDoubleFire(){
        doubleFire = 0;
    }

}