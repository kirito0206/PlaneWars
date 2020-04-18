package com.example.planewars.data;

import cn.bmob.v3.BmobObject;

public class Player extends BmobObject{
    private String name;
    private Integer goal;

    public Player(String name, Integer goal) {
        this.name = name;
        this.goal = goal;
    }

    public Player() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getGoal() {
        return goal;
    }

    public void setGoal(Integer goal) {
        this.goal = goal;
    }
}
