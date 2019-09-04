package com.study.ui_master.operate_canvas.split;

public class Ball {
    private int color;//粒子颜色
    private float x;//粒子x坐标
    private float y;//粒子y坐标
    private float r;//粒子半径
    //速度
    private float vX;//粒子x方向速度
    private float vY;//粒子y方向速度
    //加速度
    private float aX;//粒子x方向加速度
    private float aY;//粒子y方向加速度

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getR() {
        return r;
    }

    public void setR(float r) {
        this.r = r;
    }

    public float getvX() {
        return vX;
    }

    public void setvX(float vX) {
        this.vX = vX;
    }

    public float getvY() {
        return vY;
    }

    public void setvY(float vY) {
        this.vY = vY;
    }

    public float getaX() {
        return aX;
    }

    public void setaX(float aX) {
        this.aX = aX;
    }

    public float getaY() {
        return aY;
    }

    public void setaY(float aY) {
        this.aY = aY;
    }
}
