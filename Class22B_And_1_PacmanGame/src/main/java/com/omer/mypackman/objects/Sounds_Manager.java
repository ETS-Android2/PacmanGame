package com.omer.mypackman.objects;
import android.content.ContextWrapper;
import android.media.MediaPlayer;

public class Sounds_Manager {
    private MediaPlayer myMediaPlayer;

    public Sounds_Manager() {
        MediaPlayer mp = new MediaPlayer();
    }


    public MediaPlayer getMp() {
        return this.myMediaPlayer;
    }

    public void setMpAndPlay(ContextWrapper contextWrapper, int sample) {
        this.myMediaPlayer = MediaPlayer.create(contextWrapper,sample);
        this.myMediaPlayer.start();
    }
}
