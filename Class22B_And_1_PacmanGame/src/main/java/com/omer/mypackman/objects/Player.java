package com.omer.mypackman.objects;

public class Player {

    private int xPos;
    private int yPos;
    private int direction;

    //Default constructor
    public Player() { }

    public Player(int x, int y,int direction) {
        setX(x);
        setY(y);
        setDirection(direction);
    }

    //Setters and Getters
    public int getX() {
        return xPos;
    }

    public void setX(int xPos) {
        this.xPos = xPos;
    }

    public int getY() {
        return yPos;
    }

    public void setY(int yPos) {
        this.yPos = yPos;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }
}
