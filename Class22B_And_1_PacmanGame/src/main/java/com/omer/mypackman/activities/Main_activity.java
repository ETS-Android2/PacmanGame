package com.omer.mypackman.activities;

import android.Manifest;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;
import com.omer.mypackman.APP.Game_Manager;
import com.omer.mypackman.gps.Location_Manager;
import com.omer.mypackman.DB.MSP;
import com.omer.mypackman.R;
import com.omer.mypackman.objects.Bitcoin;
import com.omer.mypackman.objects.Player;
import com.omer.mypackman.objects.Records;
import com.omer.mypackman.objects.Sensors;
import com.omer.mypackman.objects.Sounds_Manager;
import com.omer.mypackman.objects.StepDetector;
import com.omer.mypackman.DB.myDataBase;

import java.util.Timer;
import java.util.TimerTask;

public class Main_activity extends AppCompatActivity {
    //Final Variables
    public static final int LEFT = 0;
    public static final int UP = 1;
    public static final int DOWN = 2;
    public static final int RIGHT = 3;

    public static final int PLAYER_START_POS_X = 4;
    public static final int PLAYER_START_POS_Y = 1;
    public static final int RIVAL_START_POS_X = 0;
    public static final int RIVAL_START_POS_Y = 0;
    public static final int COIN_POS_X = 4;
    public static final int COIN_POS_Y = 4;

    public static int PLAYER_DIRECTION = -1;
    public static int RIVAL_DIRECTION = -1;

    private ImageView panel_IMG_background;

    private ImageView[] panel_IMG_balls;
    private int lives;

    private ImageButton[] panel_IMG_arrows;



    //Panel
    private ImageView[][] panelGame;

    //Timer
    private Timer timer = new Timer();
    private final int DELAY = 1000;
    private int counter = 0;
    private MaterialTextView main_LBL_score;
    private int sensorFlag;
    private Sounds_Manager gameSound;


    private enum TIMER_STATUS{
        OFF,
        RUNNING,
        PAUSE
    }
    private TIMER_STATUS timerStatus = TIMER_STATUS.OFF;

    //Players
    private Player player;
    private Player rival;
    private Bitcoin bitcoin;
    private StepDetector stepDetector = new StepDetector();

    private final myDataBase myDB = myDataBase.initMyDB();
    private Location_Manager location_Manager;
    private Sensors sensors;
    private SensorManager sensor_manager;
    private String game;
    private Bundle bundle;
    private final Game_Manager gameManager = new Game_Manager();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getBundleExtra("Bundle") != null){
            this.bundle = getIntent().getBundleExtra("Bundle");
            gameManager.setUserName(bundle.getString("fromMenu"));
        } else {
            this.bundle = new Bundle();
        }
        game = bundle.getString("game");
        if(game.equals("buttons")){
            setContentView(R.layout.controlers_activity);
            InitGameView();
            InitArrowsButtons();
        }else{
            setContentView(R.layout.sensors_activity);
            sensorFlag = 1;
            InitGameView();
            sensors = new Sensors();
            initSensors();
        }
        gameSound = new Sounds_Manager();
        location_Manager = new Location_Manager(this);
        //----- Get Location use permission and check -----
        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initSensors() {
        sensor_manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensors.setSensorManager(sensor_manager);
        sensors.initSensor();
    }

    private SensorEventListener accSensorEventListener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];

            if (x < -3.5) {// move right
                RIVAL_DIRECTION = getRandomRivalDirection();
                PLAYER_DIRECTION = RIGHT;
            } else if (x > 3.5) {// move left
                RIVAL_DIRECTION = getRandomRivalDirection();
                PLAYER_DIRECTION = LEFT;
            } else if (y < -3) {// move up
                RIVAL_DIRECTION = getRandomRivalDirection();
                PLAYER_DIRECTION = UP;
            } else if (y > 3) {// move down
                RIVAL_DIRECTION = getRandomRivalDirection();
                PLAYER_DIRECTION = DOWN;
            }
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
        }
    };
    protected void onResume() {
        super.onResume();
        if(sensorFlag == 1) {
            sensor_manager.registerListener(accSensorEventListener, sensors.getAccSensor(), SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (sensorFlag == 1) {
            sensor_manager.unregisterListener(accSensorEventListener);
        }
    }


    //Init Functions

    private void InitGameView() {
        //Background
        panel_IMG_background = findViewById(R.id.panel_IMG_background);

        //Timer
        main_LBL_score = findViewById(R.id.main_LBL_score);

        //Panel
        panelGame = new ImageView[][]{
                {findViewById(R.id.panel_IMG_00),findViewById(R.id.panel_IMG_01),findViewById(R.id.panel_IMG_02),findViewById(R.id.panel_IMG_03),findViewById(R.id.panel_IMG_04)},
                {findViewById(R.id.panel_IMG_10),findViewById(R.id.panel_IMG_11),findViewById(R.id.panel_IMG_12),findViewById(R.id.panel_IMG_13),findViewById(R.id.panel_IMG_14)},
                {findViewById(R.id.panel_IMG_20),findViewById(R.id.panel_IMG_21),findViewById(R.id.panel_IMG_22),findViewById(R.id.panel_IMG_23),findViewById(R.id.panel_IMG_24)},
                {findViewById(R.id.panel_IMG_30),findViewById(R.id.panel_IMG_31),findViewById(R.id.panel_IMG_32),findViewById(R.id.panel_IMG_33),findViewById(R.id.panel_IMG_34)},
                {findViewById(R.id.panel_IMG_40),findViewById(R.id.panel_IMG_41),findViewById(R.id.panel_IMG_42),findViewById(R.id.panel_IMG_43),findViewById(R.id.panel_IMG_44)},
                {findViewById(R.id.panel_IMG_50),findViewById(R.id.panel_IMG_51),findViewById(R.id.panel_IMG_52),findViewById(R.id.panel_IMG_53),findViewById(R.id.panel_IMG_54)},
                {findViewById(R.id.panel_IMG_60),findViewById(R.id.panel_IMG_61),findViewById(R.id.panel_IMG_62),findViewById(R.id.panel_IMG_63),findViewById(R.id.panel_IMG_64)},

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
        if(game.equals("buttons")){
            InitArrowsButtons();
        }


        //Players
        player = new Player(PLAYER_START_POS_X, PLAYER_START_POS_Y,PLAYER_DIRECTION);
        rival = new Player(RIVAL_START_POS_X, RIVAL_START_POS_Y,RIVAL_DIRECTION);

        // coin
        bitcoin = new Bitcoin(COIN_POS_X, COIN_POS_Y);

        //Player
        panelGame[player.getX()][player.getY()].setImageResource(R.drawable.ic_soccer_player);
        panelGame[player.getX()][player.getY()].setVisibility(View.VISIBLE);

        //Rival
        panelGame[rival.getX()][rival.getY()].setImageResource(R.drawable.ic_referee);
        panelGame[rival.getX()][rival.getY()].setVisibility(View.VISIBLE);

        //coin
        panelGame[bitcoin.getX()][bitcoin.getY()].setImageResource(R.drawable.ic_bitcoin);
        panelGame[bitcoin.getX()][bitcoin.getY()].setVisibility(View.VISIBLE);
        stepDetector = new StepDetector();
        stepDetector.start();
    }

    private void InitArrowsButtons() {
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




    public void movement(){
        moveRival();
        movePlayer();
        if(stepDetector.getStepCount()%10==0){
            moveCoin();
        }
        updateScore();
        checkLocations();
    }

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
    private void moveCoin() {
        panelGame[bitcoin.getX()][bitcoin.getY()].setVisibility(View.INVISIBLE);
        do{
            bitcoin.setX((int) (Math.random() * gameManager.getROWS()));
            bitcoin.setY((int) (Math.random() * gameManager.getCOLUMNS()));
        }while(bitcoin.getX()!= player.getX() && bitcoin.getX()!= rival.getX() && bitcoin.getY()!= rival.getY()&& bitcoin.getY()!= player.getY());
        panelGame[bitcoin.getX()][bitcoin.getY()].setImageResource(R.drawable.ic_bitcoin);
        panelGame[bitcoin.getX()][bitcoin.getY()].setVisibility(View.VISIBLE);
    }

    private void updateScore() {
        if ((bitcoin.getX() == player.getX()) && (bitcoin.getY() == player.getY())){
            gameSound.setMpAndPlay((ContextWrapper) getApplicationContext(),R.raw.catch_money);
            counter += 1000;
            main_LBL_score.setText("" + counter);
            Toast.makeText(this,"+1000",Toast.LENGTH_SHORT).show();
            stepDetector.setStepCount(0);
        }
        if ((bitcoin.getX() == rival.getX()) && (bitcoin.getY() == rival.getY())){
            gameSound.setMpAndPlay((ContextWrapper) getApplicationContext(),R.raw.money_lost);
            if(counter<50)
                counter = 0;
            else
                counter-=100;
            stepDetector.setStepCount(0);
        }
    }
    private int getRandomRivalDirection(){
        RIVAL_DIRECTION = (int) (Math.random() * 4); // 4 directions
        return RIVAL_DIRECTION;
    }

    private void checkLocations()
    {
        if((rival.getX() == player.getX()) && (rival.getY() == player.getY()))
        {
            panelGame[player.getX()][player.getY()].setVisibility(View.INVISIBLE);
            if(gameManager.getLives()>1) {
                gameManager.reduceLives();
                panel_IMG_balls[gameManager.getLives()].setVisibility(View.INVISIBLE);
                Toast.makeText(this,"CRASH" ,Toast.LENGTH_LONG).show();
                setPlayersOnStartingPoint();
                gameSound.setMpAndPlay((ContextWrapper) getApplicationContext(),R.raw.crash_with_rival);

            }
            else {
                panel_IMG_balls[0].setVisibility(View.INVISIBLE);
                stopTimer();
                panelGame[player.getX()][player.getY()].setVisibility(View.INVISIBLE);
                Toast.makeText(this,"Game Over",Toast.LENGTH_LONG).show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Records rec = new Records(counter,gameManager.getUserName(),location_Manager.getLon(),location_Manager.getLat());
                        myDB.addRecord(rec);
                        String json = new Gson().toJson(myDB);
                        MSP.getMe().putString("MY_DB", json);
                        replaceActivity();
                        finish();
                    }
                }, 1000);


            }
        }
    }
    private void replaceActivity() {
        Intent intent = new Intent(this, top_ten_activity.class);
        intent.putExtra("Bundle",bundle);
        startActivity(intent);
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
                        movement();

                    }
                });

            }
        }, 0, DELAY);

    }
    private void tick() {

            ++counter;
            main_LBL_score.setText("" + counter);


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


}



