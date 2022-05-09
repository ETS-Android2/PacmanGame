package com.omer.mypackman.APP;

public class Game_Manager {

    //Final variables
    public static final int MAX_LIVES = 1;
    public static final int COLUMNS = 5;
    public static final int ROWS = 7;

    private int lives = MAX_LIVES;
    private String userName;
    private int score;

    //Default Constructor
    public Game_Manager() {

    }

    //Setters and Getters
    public int getLives() {
        return lives;
    }

    public void reduceLives() {
        lives--;
    }

    public static int getROWS() {
        return ROWS;
    }

    public static int getCOLUMNS() {
        return COLUMNS;
    }

    public static int getMaxLives() {
        return MAX_LIVES;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}