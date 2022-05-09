package com.omer.mypackman.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.omer.mypackman.fragments.Fragment_Map;
import com.omer.mypackman.R;
import com.omer.mypackman.fragments.top_ten_fragment;

public class top_ten_activity extends AppCompatActivity{
    private TextView game_top_ten_LBL_title;
    private TextView game_top_ten_LBL_map;
    Fragment_Map fragmentMap;
    top_ten_fragment fragmentList;
    Bundle bundle;
    private String fromMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_ten);
        if (getIntent().getBundleExtra("Bundle") != null){
            this.bundle = getIntent().getBundleExtra("Bundle");
            fromMenu = bundle.getString("fromMenu");
        } else {
            this.bundle = new Bundle();
        }
        findViews();
        //Init Fragments
        fragmentMap = new Fragment_Map();
        fragmentList = new top_ten_fragment();

        //Open Fragments
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout, fragmentMap)
                .commit();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.game_LAY_list, fragmentList)
                .commit();

        //Set callBacks
        fragmentList.setActivity(this);
        fragmentList.setCallBackList((lat, lon, playerName) -> fragmentMap.zoomMap(lat,lon));
    }


    private void findViews() {
        game_top_ten_LBL_title = findViewById(R.id.game_top_ten_LBL_title);
        game_top_ten_LBL_map = findViewById(R.id.game_top_ten_LBL_map);

    }


}
