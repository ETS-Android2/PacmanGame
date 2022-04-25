package com.omer.mypackman;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;



public class Welcome_Activity extends AppCompatActivity {

    private EditText main_EDT_name;
    private MaterialButton main_BTN_send;
    private MaterialTextView main_LBL_status;

    private MaterialButton main_BTN_controlsGame;
    private MaterialButton main_BTN_sensorsGame;

    private Bundle data = null;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_page);
    }


    private void setForm() {
        main_BTN_send = findViewById(R.id.main_BTN_enter);
        main_EDT_name = findViewById(R.id.main_EDT_name);
        main_LBL_status = findViewById(R.id.main_LBL_status);

        main_LBL_status.setVisibility(View.INVISIBLE);
    }




    private void setStatus(int visibility, String message, int color) {
        main_LBL_status.setVisibility(visibility);
        main_LBL_status.setText(message);
        main_LBL_status.setTextColor(color);
    }


}