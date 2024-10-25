package com.example.video_player;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

public class MainActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private SeekBar seekVolume, seekTrack;
    private Button btnPlay, btnPause, btnStop;
    private AudioManager audioManager;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.teste);
        initSeekBarVolume();
        initSeekBarTrack();

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mediaPlayer.isPlaying()) mediaPlayer.pause();
    }

    public void initSeekBarVolume() {
        seekVolume = findViewById(R.id.seekVolume);

        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        seekVolume.setMax(maxVolume);
        seekVolume.setProgress(currentVolume);

        seekVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    public void initSeekBarTrack() {
        seekTrack = findViewById(R.id.seekTrack);
        seekTrack.setMax(mediaPlayer.getDuration());

        mediaPlayer.setOnPreparedListener(mp -> {
            handler.post(updateSeekBar);
            mediaPlayer.start();
        });

        seekTrack.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
    }

    private Runnable updateSeekBar = new Runnable() {
        @Override
        public void run() {
            if (mediaPlayer.isPlaying()) {
                seekTrack.setProgress(mediaPlayer.getCurrentPosition());
                handler.postDelayed(this, 1000);
            }
        }
    };

    public void play(View view) {
        if (mediaPlayer == null) return;
        mediaPlayer.start();
    }

    public void pause(View view) {
        if(!mediaPlayer.isPlaying()) return;
        mediaPlayer.pause();
    }

    public void stop(View view) {
        if(!mediaPlayer.isPlaying()) return;
        mediaPlayer.stop();
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.teste);
    }
}