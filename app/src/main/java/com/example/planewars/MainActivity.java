package com.example.planewars;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.planewars.data.Airplane;
import com.example.planewars.data.BigAirplane;
import com.example.planewars.data.Bullet;
import com.example.planewars.data.FlyingObject;
import com.example.planewars.data.Hero;
import com.example.planewars.data.Player;
import com.example.planewars.data.ShootMain;
import com.example.planewars.data.Sky;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class MainActivity extends AppCompatActivity {

    private ShootMain maingame;
    private AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        maingame = new ShootMain(this);
        initView();
        maingame.action();
    }

    private void initView(){
        RelativeLayout relativeLayout = findViewById(R.id.relative);
        relativeLayout.addView(maingame);
        relativeLayout.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                switch(event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        switch (maingame.state) {
                            case 0:
                                maingame.state = maingame.RUNNING;
                                break;
                            case 3:
                                gameOver();
                                maingame.sky = new Sky();
                                maingame.hero = new Hero();
                                maingame.flys = new FlyingObject[0];
                                maingame.bullets = new Bullet[0];
                                Airplane.speed = 5;
                                Airplane.bloodNum = 1;
                                BigAirplane.speed = 2;
                                BigAirplane.bloodNum = 2;
                                maingame.state = maingame.START;//修改状态为开始状态
                                break;
                        }
                        maingame.hero.movedTo(event.getX(),event.getY());
                        break;
                    case MotionEvent.ACTION_MOVE:
                        maingame.hero.movedTo(event.getX(),event.getY());
                        break;
                }
                return true;
            }
        });
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        maingame.SCREEN_WIDTH = displayMetrics.widthPixels;
        maingame.SCREEN_HEIGHT = displayMetrics.heightPixels;
    }

    private void gameOver(){
        LayoutInflater inflater=LayoutInflater.from(MyApplication.getContext());
        View view1 = inflater.inflate(R.layout.dialog,null);
        TextView textView = view1.findViewById(R.id.dialog_content);
        final EditText editText = view1.findViewById(R.id.edit_name);
        Button restart = view1.findViewById(R.id.restart_button);
        Button rankList = view1.findViewById(R.id.rank_list);
        textView.setText("最终的得分为 "+maingame.score);
        rankList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = editText.getText().toString();
                //保存数据
                new Player(id,maingame.score).save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null)
                            Toast.makeText(MyApplication.getContext(),"数据保存成功！！",Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(MyApplication.getContext(),"数据保存失败："+e.getMessage(),Toast.LENGTH_SHORT).show();
                        maingame.score = 0;
                    }
                });
                dialog.dismiss();
                Intent intent = new Intent(MainActivity.this,RankListActivity.class);
                startActivity(intent);
            }
        });
        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                maingame.score = 0;
                maingame.sky = new Sky();
                maingame.hero = new Hero();
                maingame.flys = new FlyingObject[0];
                maingame.bullets = new Bullet[0];
                Airplane.speed = 5;
                Airplane.bloodNum = 1;
                BigAirplane.speed = 2;
                BigAirplane.bloodNum = 2;
                maingame.state = maingame.START;//修改状态为开始状态
            }
        });
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setView( view1 );
        dialog=builder.create();//创建对话框
        dialog.show();//显示对话框
    }
}
