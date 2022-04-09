package com.omer.mypackman;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.android.material.textview.MaterialTextView;

import java.util.Timer;
import java.util.TimerTask;

public class activity_main extends AppCompatActivity {
    //Final Variables
    public static final int LEFT = 0;
    public static final int UP = 1;
    public static final int DOWN = 2;
    public static final int RIGHT = 3;

    public static final int PLAYER_START_POS_X = 4;
    public static final int PLAYER_START_POS_Y = 1;
    public static final int RIVAL_START_POS_X = 0;
    public static final int RIVAL_START_POS_Y = 0;

    public static int PLAYER_DIRECTION = -1;
    public static int RIVAL_DIRECTION = -1;

    private ImageView panel_IMG_background;

    private ImageView[] panel_IMG_balls;
    private int lives;

    private ImageButton[] panel_IMG_arrows;

    //Game Manager
    private final Game_Manager gameManager = new Game_Manager();

    //Panel
    private ImageView[][] panelGame;

    //Timer
    private Timer timer = new Timer();
    private final int DELAY = 1000;
    private int counter = 0;
    private MaterialTextView main_LBL_time;
    private enum TIMER_STATUS{
        OFF,
        RUNNING,
        PAUSE
    }
    private TIMER_STATUS timerStatus = TIMER_STATUS.OFF;

    //Players
    private Player player;
    private Player rival;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitGameView();
    }

    //Init Functions
    private void InitGameView() {
        //Background
        panel_IMG_background = findViewById(R.id.panel_IMG_background);

        //Timer
        main_LBL_time = findViewById(R.id.main_LBL_score);

        //Panel
        panelGame = new ImageView[][]{
                {findViewById(R.id.panel_IMG_00),findViewById(R.id.panel_IMG_01),findViewById(R.id.panel_IMG_02)},
                {findViewById(R.id.panel_IMG_10),findViewById(R.id.panel_IMG_11),findViewById(R.id.panel_IMG_12)},
                {findViewById(R.id.panel_IMG_20),findViewById(R.id.panel_IMG_21),findViewById(R.id.panel_IMG_22)},
                {findViewById(R.id.panel_IMG_30),findViewById(R.id.panel_IMG_31),findViewById(R.id.panel_IMG_32)},
                {findViewById(R.id.panel_IMG_40),findViewById(R.id.panel_IMG_41),findViewById(R.id.panel_IMG_42)},

        };

        //balls
        panel_IMG_balls = new ImageView[] {
                findViewById(R.id.panel_IMG_life1),
                findViewById(R.id.panel_IMG_life2),
                findViewById(R.id.panel_IMG_life3)
        };
        lives = gameManager.getMaxLives();

        //Arrows
        panel_IMG_arrows = new ImageButton[] {
                findViewById(R.id.panel_BTN_left),
                findViewById(R.id.panel_BTN_up),
                findViewById(R.id.panel_BTN_down),
                findViewById(R.id.panel_BTN_right)
        };
        InitArrowsButtons();

        //Players
        player = new Player(PLAYER_START_POS_X, PLAYER_START_POS_Y,PLAYER_DIRECTION);
        rival = new Player(RIVAL_START_POS_X, RIVAL_START_POS_Y,RIVAL_DIRECTION);

        //Player
        panelGame[player.getX()][player.getY()].setImageResource(R.drawable.ic_soccer_player);
        panelGame[player.getX()][player.getY()].setVisibility(View.VISIBLE);

        //Rival
        panelGame[rival.getX()][rival.getY()].setImageResource(R.drawable.ic_referee);
        panelGame[rival.getX()][rival.getY()].setVisibility(View.VISIBLE);
    }

    private void InitArrowsButtons() {
        //Each time the player changes the direction of his movement, the rival also changes the direction of his movement randomly
        //Left
        panel_IMG_arrows[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RIVAL_DIRECTION = getRandomRivalDirection();
                PLAYER_DIRECTION = LEFT;
            }
        });

        //Up
        panel_IMG_arrows[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RIVAL_DIRECTION = getRandomRivalDirection();
                PLAYER_DIRECTION = UP;
            }
        });

        //Down
        panel_IMG_arrows[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RIVAL_DIRECTION = getRandomRivalDirection();
                PLAYER_DIRECTION = DOWN;
            }
        });

        //Right
        panel_IMG_arrows[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RIVAL_DIRECTION = getRandomRivalDirection();
                PLAYER_DIRECTION = RIGHT;
            }
        });
    }


    //Timer Functions
    @Override
    protected void onStart() {
        super.onStart();
        if(timerStatus == TIMER_STATUS.OFF){
            startTimer();
        } else if(timerStatus == TIMER_STATUS.RUNNING ){
            stopTimer();
        }else{
            startTimer();
        }
    }

    private void startTimer() {
        timerStatus = TIMER_STATUS.RUNNING;
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tick();
                        runLogic();
                    }
                });

            }
        }, 0, DELAY);

    }
    private void tick() {
        ++counter;
        main_LBL_time.setText("" + counter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(timerStatus == TIMER_STATUS.RUNNING){
            stopTimer();
            timerStatus = TIMER_STATUS.PAUSE;
        }
    }

    private void stopTimer() {
        timerStatus = TIMER_STATUS.OFF;
        timer.cancel();

    }

    //Logic Functions
    public void runLogic(){
        moveRival();
        movePlayer();
        checkLocations();
    }

    //Move Functions
    private void movePlayer() {
        if(lives == 0)
            panelGame[player.getX()][player.getY()].setVisibility(View.INVISIBLE);
        switch (PLAYER_DIRECTION){
            case LEFT:
                panelGame[player.getX()][player.getY()].setVisibility(View.INVISIBLE);
                if(player.getY() == 0)
                    player.setY(gameManager.getCOLUMNS() -1);
                else
                    player.setY(player.getY()-1);
                panelGame[player.getX()][player.getY()].setImageResource(R.drawable.ic_soccer_player);
                panelGame[player.getX()][player.getY()].setVisibility(View.VISIBLE);
                break;
            case UP:
                panelGame[player.getX()][player.getY()].setVisibility(View.INVISIBLE);
                if(player.getX() == 0)
                    player.setX(gameManager.getROWS()-1);
                else
                    player.setX(player.getX()-1);
                panelGame[player.getX()][player.getY()].setImageResource(R.drawable.ic_soccer_player);
                panelGame[player.getX()][player.getY()].setVisibility(View.VISIBLE);
                break;
            case DOWN:
                panelGame[player.getX()][player.getY()].setVisibility(View.INVISIBLE);
                if(player.getX() == (gameManager.getROWS() - 1))
                    player.setX(0);
                else
                    player.setX(player.getX()+1);
                panelGame[player.getX()][player.getY()].setImageResource(R.drawable.ic_soccer_player);
                panelGame[player.getX()][player.getY()].setVisibility(View.VISIBLE);
                break;
            case RIGHT:
                panelGame[player.getX()][player.getY()].setVisibility(View.INVISIBLE);
                if(player.getY() == (gameManager.getCOLUMNS() - 1))
                    player.setY(0);
                else
                    player.setY(player.getY()+1);
                panelGame[player.getX()][player.getY()].setImageResource(R.drawable.ic_soccer_player);
                panelGame[player.getX()][player.getY()].setVisibility(View.VISIBLE);
                break;
        }

    }

    private void moveRival() {
        if(lives == 0)
            panelGame[rival.getX()][rival.getY()].setVisibility(View.INVISIBLE);
        switch (RIVAL_DIRECTION){
            case LEFT:
                panelGame[rival.getX()][rival.getY()].setVisibility(View.INVISIBLE);
                if(rival.getY() == 0)
                    rival.setY(gameManager.getCOLUMNS() - 1);
                else
                    rival.setY(rival.getY()-1);
                panelGame[rival.getX()][rival.getY()].setImageResource(R.drawable.ic_referee);
                panelGame[rival.getX()][rival.getY()].setVisibility(View.VISIBLE);
                break;
            case UP:
                panelGame[rival.getX()][rival.getY()].setVisibility(View.INVISIBLE);
                if(rival.getX() == 0)
                    rival.setX(gameManager.getROWS()-1);
                else
                    rival.setX(rival.getX()-1);
                panelGame[rival.getX()][rival.getY()].setImageResource(R.drawable.ic_referee);
                panelGame[rival.getX()][rival.getY()].setVisibility(View.VISIBLE);
                break;
            case DOWN:
                panelGame[rival.getX()][rival.getY()].setVisibility(View.INVISIBLE);
                if(rival.getX() == (gameManager.getROWS() - 1))
                    rival.setX(0);
                else
                    rival.setX(rival.getX()+1);
                panelGame[rival.getX()][rival.getY()].setImageResource(R.drawable.ic_referee);
                panelGame[rival.getX()][rival.getY()].setVisibility(View.VISIBLE);
                break;
            case RIGHT:
                panelGame[rival.getX()][rival.getY()].setVisibility(View.INVISIBLE);
                if(rival.getY() == (gameManager.getCOLUMNS() - 1))
                    rival.setY(0);
                else
                    rival.setY(rival.getY()+1);
                panelGame[rival.getX()][rival.getY()].setImageResource(R.drawable.ic_referee);
                panelGame[rival.getX()][rival.getY()].setVisibility(View.VISIBLE);
                break;
        }

    }
    // Rival functions
    private int getRandomRivalDirection(){
        RIVAL_DIRECTION = (int) (Math.random() * 4); // 4 directions
        return RIVAL_DIRECTION;
    }


    //Loss function
    private void checkLocations()
    {
        if((rival.getX() == player.getX()) && (rival.getY() == player.getY()))
        {
            panelGame[player.getX()][player.getY()].setVisibility(View.INVISIBLE);
            if(gameManager.getLives()>1) {
                gameManager.reduceLives();
                panel_IMG_balls[gameManager.getLives()].setVisibility(View.INVISIBLE);
                Toast.makeText(this,"CRASH",Toast.LENGTH_LONG).show();
                setPlayersOnStartingPoint();


            } else {
                panel_IMG_balls[0].setVisibility(View.INVISIBLE);
                stopTimer();
                panelGame[player.getX()][player.getY()].setVisibility(View.INVISIBLE);
                Toast.makeText(this,"Game Over",Toast.LENGTH_LONG).show();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 1000);


            }
        }

    }

    private void setPlayersOnStartingPoint(){

        player.setX(PLAYER_START_POS_X);
        player.setY(PLAYER_START_POS_Y);
        rival.setX(RIVAL_START_POS_X);
        rival.setY(RIVAL_START_POS_Y);

        panelGame[rival.getX()][rival.getY()].setImageResource(R.drawable.ic_referee);
        panelGame[rival.getX()][rival.getY()].setVisibility(View.VISIBLE);

        panelGame[player.getX()][player.getY()].setImageResource(R.drawable.ic_soccer_player);
        panelGame[player.getX()][player.getY()].setVisibility(View.VISIBLE);

    }





}



