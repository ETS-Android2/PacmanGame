package com.omer.mypackman.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.omer.mypackman.APP.Game_Manager;
import com.omer.mypackman.R;


public class Welcome_Activity extends AppCompatActivity {

    private EditText main_EDT_name;
    private Button main_BTN_enter;

    private Button main_BTN_controlsGame;
    private Button main_BTN_sensorsGame;
    private Button main_BTN_topTen;
    private Game_Manager game_manager;
    private Bundle bundle;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_page);
        game_manager = new Game_Manager();

        if (getIntent().getBundleExtra("Bundle") != null){
            this.bundle = getIntent().getBundleExtra("Bundle");
            game_manager.setUserName(bundle.getString("playerName"));
        } else {
            this.bundle = new Bundle();
        }

        findViews();
        InitGameAfterLogin();
    }

    private void findViews() {
        main_EDT_name = findViewById(R.id.main_EDT_name);
        main_BTN_enter = findViewById(R.id.main_BTN_enter);
        main_BTN_controlsGame = findViewById(R.id.main_BTN_controlsGame);
        main_BTN_sensorsGame = findViewById(R.id.main_BTN_sensorsGame);
        main_BTN_topTen = findViewById(R.id.main_BTN_topTen);
    }

    public void InitGameAfterLogin() {
        main_BTN_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeybaord(v);
                userName = main_EDT_name.getText().toString();

                game_manager.setUserName(main_EDT_name.getText().toString());

                main_BTN_sensorsGame.setVisibility(View.VISIBLE);
                main_BTN_controlsGame.setVisibility(View.VISIBLE);
                main_BTN_topTen.setVisibility(View.VISIBLE);
                main_EDT_name.setVisibility(View.VISIBLE);
                main_BTN_enter.setVisibility(View.VISIBLE);
            }
        });

        main_BTN_sensorsGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tryActivity("sensors");
            }
        });


        main_BTN_controlsGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tryActivity("buttons");
            }
        });

        main_BTN_topTen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                topTenActivity("fromMenu");
            }
        });

    }

    private void tryActivity(String game) {
        Intent intent = new Intent(this, Main_activity.class);
        Bundle bundle = new Bundle();
        bundle.putString("fromMenu",userName);
        bundle.putString("game",game);
        intent.putExtra("Bundle",bundle);
        startActivity(intent);
    }

    public void topTenActivity(String fromMenu){
        Intent intent = new Intent(this, top_ten_activity.class);
        Bundle bundle = new Bundle();
        bundle.putString("fromMenu",fromMenu);
        intent.putExtra("Bundle",bundle);
        startActivity(intent);
    }
    private void hideKeybaord(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(),0);
    }
}