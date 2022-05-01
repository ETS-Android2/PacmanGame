package com.omer.mypackman;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import com.google.android.material.button.MaterialButton;


public class Welcome_Activity extends AppCompatActivity {

    private EditText main_EDT_name;
    private MaterialButton main_BTN_enter;

    private MaterialButton main_BTN_controlsGame;
    private MaterialButton main_BTN_sensorsGame;
    private MaterialButton main_BTN_topTen;
    private Bundle data = null;
    private Game_Manager game_manager;
    private Bundle bundle;
    private String userName;
//    private String game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_page);
        game_manager = new Game_Manager();

        if (getIntent().getBundleExtra("Bundle") != null){
            this.bundle = getIntent().getBundleExtra("Bundle");
            game_manager.setUserName(bundle.getString("userName"));
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
                game_manager.setUserName(main_EDT_name.getText().toString());

                main_BTN_sensorsGame.setVisibility(View.VISIBLE);
                main_BTN_controlsGame.setVisibility(View.VISIBLE);
                main_BTN_topTen.setVisibility(View.VISIBLE);
                main_EDT_name.setVisibility(View.INVISIBLE);
                main_BTN_enter.setVisibility(View.INVISIBLE);
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
                topTenActivity();
            }
        });

    }

    private void tryActivity(String gameType) {
        Intent intent = new Intent(this,Main_activity.class);
        Bundle bundle = new Bundle();
        bundle.putString("playerName",game_manager.getUserName());
        bundle.putString("game",gameType);
        intent.putExtra("Bundle",bundle);
        startActivity(intent);
    }

    private void topTenActivity() {
        Intent intent = new Intent(this,top_ten.class);
        intent.putExtra("Bundle",bundle);
        startActivity(intent);
    }
    private void hideKeybaord(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(),0);
    }




    private void setForm() {
        main_BTN_enter = findViewById(R.id.main_BTN_enter);
        main_EDT_name = findViewById(R.id.main_EDT_name);
        main_BTN_sensorsGame = findViewById(R.id.main_BTN_sensorsGame);
        main_BTN_controlsGame = findViewById(R.id.main_BTN_controlsGame);
        main_BTN_topTen = findViewById(R.id.main_BTN_topTen);
    }


}