package com.phonemedia;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import java.io.File;
import java.util.ArrayList;

public class MusicActivity extends BaseActivity implements View.OnClickListener{
    MediaPlayer mediaPlayer;
    ArrayList<File> mySongs;
    SeekBar sb;
    Button pre_next,pre,play,next,next_to;
    int position;
    Uri uri;
    Thread updateSeek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        sb = (SeekBar) findViewById(R.id.seekBar);
        pre_next = (Button) findViewById(R.id.btn_pre_next);
        pre = (Button) findViewById(R.id.btn_pre);
        play = (Button) findViewById(R.id.btn_play);
        next = (Button) findViewById(R.id.btn_next);
        next_to = (Button) findViewById(R.id.btn_next_to);

        pre_next.setOnClickListener(this);
        pre.setOnClickListener(this);
        play.setOnClickListener(this);
        next.setOnClickListener(this);
        next_to.setOnClickListener(this);

        updateSeek = new Thread(){
            public void run(){
                int totalDuration =  mediaPlayer.getDuration();
                int currentPosition =0;
                sb.setMax(totalDuration);
                while (currentPosition < totalDuration) {
                    try {
                        sleep(500);
                        currentPosition = mediaPlayer.getCurrentPosition();
                        sb.setProgress(currentPosition);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
                //super.run();
            }
        };

        if (mediaPlayer !=null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }

        Intent i = getIntent();
        Bundle b = i.getExtras();
        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
        ArrayList<Object> mySongs = (ArrayList<Object>) args.getSerializable("ARRAYLIST");

        position = b.getInt("pos",0);

        uri = Uri.parse(mySongs.get(position).toString());
        mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
        mediaPlayer.start();
        sb.setMax(mediaPlayer.getDuration());
        updateSeek.start();

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
               // mediaPlayer.seekTo(seekBar.getProgress());

            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.btn_play:
                if (mediaPlayer.isPlaying()){
                    play.setText(">");
                    mediaPlayer.pause();
                }else {
                    play.setText("||");
                    mediaPlayer.start();
                }
                break;
            case R.id.btn_next_to:
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()+5000);
                break;
            case R.id.btn_pre_next:
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()-5000);
                break;

            case R.id.btn_next:
                mediaPlayer.stop();
                mediaPlayer.release();
                position =(position+1)%mySongs.size();
                uri = Uri.parse(mySongs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                mediaPlayer.start();
                sb.setMax(mediaPlayer.getDuration());
                break;
            case R.id.btn_pre:
                mediaPlayer.stop();
                mediaPlayer.release();
                position =(position-1<0)?mySongs.size()-1:position-1;
                if (position-1<0){
                    position = mySongs.size()-1;
                }else {
                    position = position-1;
                }
                uri = Uri.parse(mySongs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                mediaPlayer.start();
                sb.setMax(mediaPlayer.getDuration());
                break;

                }
    }
}
