package com.omer.mypackman.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.omer.mypackman.DB.MSP;
import com.omer.mypackman.R;
import com.omer.mypackman.gps.list_call_back;
import com.omer.mypackman.objects.Records;
import com.omer.mypackman.DB.myDataBase;

public class top_ten_fragment extends Fragment {
    private MaterialButton[] topTen;
    private com.omer.mypackman.gps.list_call_back list_call_back;
    public void setActivity(AppCompatActivity activity) {
    }

    public void setCallBackList(list_call_back callBackList) {
        this.list_call_back = callBackList;
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        findViews(view);
        initViews();
        return view;
    }


    @SuppressLint("SetTextI18n")
    private void initViews() {
        String js = MSP.getMe().getString("MY_DB", "");
        myDataBase md = new Gson().fromJson(js, myDataBase.class);
        md.sortByScore();
        for (int i = 0; i < md.getRecords().size(); i++) {
            Records winner = md.getSpecificRecord(i);
            topTen[i].setVisibility(View.VISIBLE);
            topTen[i].setText(i+1+". "+winner.getName()+"\n  Score: "+winner.getScore());
            topTen[i].setOnClickListener(v -> list_call_back.locateOnMap(winner.getLat(),winner.getLon(), winner.getName()));

        }
    }


    private void findViews(View view) {
        topTen = new MaterialButton[]{
                view.findViewById(R.id.frame1_BTN_winner1),
                view.findViewById(R.id.frame1_BTN_winner2),
                view.findViewById(R.id.frame1_BTN_winner3),
                view.findViewById(R.id.frame1_BTN_winner4),
                view.findViewById(R.id.frame1_BTN_winner5),
                view.findViewById(R.id.frame1_BTN_winner6),
                view.findViewById(R.id.frame1_BTN_winner7),
                view.findViewById(R.id.frame1_BTN_winner8),
                view.findViewById(R.id.frame1_BTN_winner9),
                view.findViewById(R.id.frame1_BTN_winner10),
        };

    }

}
