package com.omer.mypackman;

public class Bitcoin {
    private int BxPos;
    private int ByPos;

    public Bitcoin() { }
    public Bitcoin(int x, int y) {
        setX(x);
        setY(y);
    }
    //Setters and Getters
    public int getX() {
        return BxPos;
    }

    public void setX(int xPos) {
        this.BxPos = xPos;
    }

    public int getY() {
        return ByPos;
    }

    public void setY(int yPos) {
        this.ByPos = yPos;
    }

}
