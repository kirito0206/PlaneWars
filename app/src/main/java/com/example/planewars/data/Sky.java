package com.example.planewars.data;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.planewars.R;

public class Sky extends FlyingObject {

    private static Bitmap images;
    private static Paint paint = new Paint();

    // 静态代码块
    static {
        images = loadImage(R.drawable.background);
    }

    private int speed;// 速度
    public float y1;// 第二张图片的坐标

    /** 构造方法 */
    public Sky() {
        super(ShootMain.SCREEN_WIDTH, images.getHeight(), 0, 0);
        speed = 1;
        y1 = -images.getHeight();
    }

    /** 天空移动 */
    public void step() {
        y += speed;
        y1 += speed;
        if (y >= ShootMain.SCREEN_HEIGHT) {
            y = -images.getHeight();
        }
        if (y1 >= ShootMain.SCREEN_HEIGHT) {
            y1 = -images.getHeight();
        }
    }

    @Override
    public Bitmap getImage() {
        return images;
    }

    /** 重写父类中的方法 */
    @Override
    public void paintObject(Canvas g) {
        g.drawBitmap(getImage(), x, y, paint);
        g.drawBitmap(getImage(), x, y1, paint);
    }

}