package com.example.planewars;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.planewars.adapter.PlayerAdapter;
import com.example.planewars.data.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class RankListActivity extends AppCompatActivity {

    private List<Player> playerList = new ArrayList<>();
    private PlayerAdapter adapter;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank_list);
        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.VISIBLE);
        queryRankList();
    }

    private void initView(){
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new PlayerAdapter(playerList);
        recyclerView.setAdapter(adapter);
        progressBar.setVisibility(View.GONE);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
    }

    private void queryRankList(){
        BmobQuery<Player> bmobQuery = new BmobQuery<>();
        bmobQuery.findObjects(new FindListener<Player>() {
            @Override
            public void done(List<Player> list, BmobException e) {
                if (e == null) {
                    for (int i = 0; i < list.size(); i++) {
                        playerList.add(list.get(i));
                    }
                    sortRankList();
                }
                else {
                    System.out.println(e.getErrorCode());
                }
            }
        });
    }

    private void sortRankList(){
        Collections.sort(playerList,new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                Player e1 = (Player) o1;
                Player e2 = (Player) o2;
                return e2.getGoal().compareTo(e1.getGoal());
            }
        });
        if (playerList.size() > 10){
            for (int i = playerList.size()-1; i >= 10; i--) {
                Player p = new Player();
                p.setObjectId(playerList.get(i).getObjectId());
                p.delete(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null)
                            Log.d("bmob","成功更新");
                        else
                            Log.d("bmob","删除失败:"+e.getMessage());
                    }
                });
                playerList.remove(playerList.get(i));
            }
        }
        initView();
    }

}
