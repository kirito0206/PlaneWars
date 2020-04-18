package com.example.planewars.data;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.planewars.MyApplication;
import com.example.planewars.R;
import com.example.planewars.func.Award;
import com.example.planewars.func.Enemy;

import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class ShootMain extends View {

    public static int SCREEN_WIDTH = 400;
    public static int SCREEN_HEIGHT = 700;

    public static final int START = 0;
    public static final int RUNNING = 1;
    public static final int PAUSE = 2;
    public static final int GAME_OVER = 3;
    public int state = START;//默认状态
    public static Bitmap start;
    public static Bitmap pause;
    public static Bitmap gameover;
    public static Paint paint = new Paint();
    //静态代码块
    static{
        start = FlyingObject.loadImage(R.drawable.start);
        pause = FlyingObject.loadImage(R.drawable.pause);
        gameover = FlyingObject.loadImage(R.drawable.gameover);
    }

    public Sky sky = new Sky();
    public Hero hero = new Hero();
    public FlyingObject[] flys = {};
    public Bullet[] bullets = {};

    public ShootMain(Context context) {
        super(context);
    }

    /** 产生敌人对象 */
    public FlyingObject nextOne() {
        Random rand = new Random();
        int type = rand.nextInt(20);
        if (type < 8) {
            return new Airplane();
        } else if (type < 12) {
            return new Meteor();
        } else {
            return new BigAirplane();
        }
    }

    /** 实现敌人入场 */
    int enterIndex = 0;
    int flag = 40;
    public void enterAction() {
        enterIndex++;
        if (enterIndex % flag == 0) {
            // 获取到敌人
            FlyingObject f = nextOne();
            // 敌人添加到数组的最后一位上
            flys = Arrays.copyOf(flys, flys.length + 1);
            flys[flys.length - 1] = f;
        }
    }

    /** 子弹入场 */
    int shootIndex = 0;
    public void shootAction() {
        shootIndex++;
        if (shootIndex % 40 == 0) {
            Bullet[] bs = hero.shoot();// 获取子弹数组
            bullets = Arrays.copyOf(bullets, bullets.length + bs.length);
            // 将产生的子弹数组放到源数组中的最后一个元素
            System.arraycopy(bs, 0, bullets, bullets.length - bs.length,
                    bs.length);
        }
    }

    /** 飞行物移动 */
    public void stepAction() {
        sky.step();
        for (int i = 0; i < flys.length; i++) {
            flys[i].step();
        }
        for (int i = 0; i < bullets.length; i++) {
            bullets[i].step();
        }
    }

    /**删除越界的飞行物*/
    public void outOfBoundsAction(){
        int index = 0;//存放不越界数组的下标，个数
        //新建不越界的敌人数组
        FlyingObject[] flysLive = new FlyingObject[flys.length];
        for (int i = 0; i < flys.length; i++) {
            //获取到每一个敌人
            FlyingObject f = flys[i];
            //判断敌人是否不越界
            if (!f.outOfBounds()) {//如果不越界，
                flysLive[index] = f;
                index++;
            }
            //将不越界的敌人存放到不越界的数组中
        }
        flys = Arrays.copyOf(flysLive, index);
        //System.out.println("index == " + index);
        index = 0;
        Bullet[] bulletLive = new Bullet[bullets.length];
        for (int i = 0; i < bullets.length; i++) {
            Bullet b = bullets[i];
            if (!b.outOfBounds()) {
                bulletLive[index] = b;
                index++;
            }
        }
        bullets = Arrays.copyOf(bulletLive, index);
    }

    /**子弹与敌人的碰撞*/
    public int score = 0;//玩家的得分记录
    public void bulletBangAction(){
        //遍历所有的子弹

        for (int i = 0; i < bullets.length; i++) {
            //获取每一个子弹
            Bullet b = bullets[i];
            //遍历所有的敌人
            for (int j = 0; j < flys.length; j++) {
                //获取每一个敌人
                FlyingObject f = flys[j];
                //判断碰撞
                if (f.isLife() && b.isLife() && f.hit(b)) {
                    f.goDead();//敌人over
                    b.goDead();//子弹over

                    if (f instanceof Enemy) {//如果撞上的是敌人能得分
                        Enemy e = (Enemy)f;
                        score += e.getScore();
                    }
                    //如果撞上的是奖励
                    if (f instanceof Award) {
                        Award a = (Award)f;
                        int type = a.getAwardType();
                        switch (type) {
                            case Award.DOUBLE_FIRE://火力
                                hero.addDoubleFire();
                                break;
                            case Award.LIFE://生命
                                hero.addLife();
                                break;
                        }
                    }
                }
            }
        }
    }

    /**英雄机与敌人发生碰撞*/
    public void heroBangAction(){
        //遍历所有的敌人
        for (int i = 0; i < flys.length; i++) {
            //获取每一个敌人
            FlyingObject f = flys[i];
            if (hero.isLife() && f.isLife() && f.hit(hero)) {
                f.goDead();
                hero.substractLife();//减少生命
                hero.clearDoubleFire();//清空火力
            }
        }
    }
    /**检测游戏是否结束*/
    public void checkGameOverAction(){
        //判断英雄机的生命值，
        //如果小于0的话，游戏结束，修改状态
        if(hero.getLife() <= 0){
            state = GAME_OVER;
        }
    }
    /** 测试方法 */
    public void action() {
        // 定时器对象
        Timer timer = new Timer();
        int inters = 10;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (state == RUNNING) {
                    enterAction();// 敌人入场
                    shootAction();// 子弹入场
                    stepAction();// 飞行物移动
                    outOfBoundsAction();//删除越界的飞行物
                    bulletBangAction();//子弹与敌人的碰撞
                    heroBangAction();//英雄机与敌人发生碰撞
                    checkGameOverAction();//检测游戏是都结束
                    update();//刷新难度
                }
                invalidate();// 重绘，调用paint方法
            }
        }, inters, inters);// 计划任务
    }

    public void onDraw(Canvas g) {
        sky.paintObject(g);
        hero.paintObject(g);
        // 画敌人
        for (int i = 0; i < flys.length; i++) {
            flys[i].paintObject(g);
        }
        for (int i = 0; i < bullets.length; i++) {
            bullets[i].paintObject(g);
        }

        paint.setTextSize(60);
        g.drawText("分数：" + score ,30,120,paint);
        g.drawText("生命：" + hero.getLife(), 30, 180,paint);

        switch (state) {//根据不同状态画不同的状态图
            case START://开始状态的启动图
                g.drawBitmap(start, 0, 0, paint);
                break;
            case PAUSE://暂停状态的图
                g.drawBitmap(pause, 0, 0, paint);
                break;
            case GAME_OVER://游戏结束的时候状态图
                g.drawBitmap(gameover, 0, 0, paint);
                break;
        }
    }

    public void update(){
        if (Airplane.speed < 10)
            Airplane.speed += 0.005;
        else if (Airplane.speed >= 10 && Airplane.bloodNum < 2)
            Airplane.bloodNum += 0.001;
        else if (flag > 30)
            flag = 30;

        if (BigAirplane.speed < 5)
            BigAirplane.speed += 0.002;
        else if (BigAirplane.speed >= 5 && BigAirplane.bloodNum < 3)
            BigAirplane.bloodNum += 0.0005;
        else if (flag > 20)
            flag = 20;
    }
}