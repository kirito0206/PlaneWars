package com.example.planewars.data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.planewars.MyApplication;

import java.util.Random;

public abstract class FlyingObject {

    // 成员变量
    protected float width;// 宽
    protected float height;// 高
    protected float x;// x坐标
    protected float y;// y坐标
    // 设计三种状态
    public static final int LIFE = 0;// 存活
    public static final int DEAD = 1;// over
    public static final int REMOVE = 2;// 删除
    public static final Paint paint = new Paint();
    public int state = LIFE;// 当前状态为存活

    /** 提供敌人（小敌机+大敌机+蜜蜂） */
    public FlyingObject(float width, float height) {
        this.width = width;
        this.height = height;
        Random rand = new Random();
        // 小敌机出现的位置
        this.x = rand.nextInt((int)(ShootMain.SCREEN_WIDTH - this.width));
        this.y = -this.height;
    }

    /** 提供英雄机+子弹+天空的构造方法 */
    public FlyingObject(float width, float height,float x, float y) {
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
    }

    /** 移动的方法 */
    public void step() {
    }

    /** 读取图片 */
    public static Bitmap loadImage(int imageid) {
        try {
            // 同包之内的图片读取
            Bitmap img = BitmapFactory.decodeResource(MyApplication.getContext().getResources(),imageid);
            return img;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    /** 获取图片 */
    public abstract Bitmap getImage();

    /** 画图片 g:画笔 */
    public void paintObject(Canvas g) {
        if (this.getImage() != null)
        g.drawBitmap(this.getImage(), this.x, this.y, paint);
    }

    /** 判断当前状态是不是存活的 */
    public boolean isLife() {
        return state == LIFE;
    }

    /** 判断当前状态是不是over的 */
    public boolean isDead() {
        return state == DEAD;
    }

    /** 判断当前状态是不是删除的 */
    public boolean isRemove() {
        return state == REMOVE;
    }

    /** 检测越界的方法 */
    public boolean outOfBounds() {
        return this.y >= ShootMain.SCREEN_HEIGHT;
    }

    /* 实现子弹与敌人发生碰撞 other:子弹敌人 */
    public boolean hit(FlyingObject other) {
        float x1 = this.x - other.width;
        float y1 = this.y - other.height;
        float x2 = this.x + this.width;
        float y2 = this.y + this.height;
        float x = other.x;
        float y = other.y;
        if (other instanceof Hero){
            x1 += other.width/4;
            x2 -= other.width/4;
            y1 += other.height/4;
            y2 -= other.height/4;
        }
        return x >= x1 && x <= x2 && y >= y1 && y <= y2;
    }

    /** 飞行物over */
    public void goDead() {
        state = DEAD;// 将对象状态修改为DAED

    }

    public int getScore() {
        // TODO Auto-generated method stub
        return 0;
    }

}