package com.Bforecaster.Activity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.Bforecaster.R;
import com.Bforecaster.Utility.GlobalVariables;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileManagementFullScreenVideoActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    @BindView(R.id.videoView) VideoView videoView;
    @BindView(R.id.play_iv) ImageView play_iv;
    @BindView(R.id.progressbar) ProgressBar progressbar;
    String videouri_str;
    Uri videouri;
    MediaController mediacontroller;
    SeekBar seekbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_management_full_screen);
        ButterKnife.bind(this);
        init();
        videouri_str = getIntent().getStringExtra(GlobalVariables.videouri);
        videouri = Uri.parse(videouri_str);
        settingThumbnail();
    }

    private void init() {
        play_iv.setOnClickListener(this);

    }
    private void settingThumbnail() {

        play_iv.setVisibility(View.VISIBLE);
        videoView.setVideoURI(videouri);
        videoView.seekTo(1);

    }

    @Override
    public void onBackPressed() {
//        Intent intent=new Intent(FullScreenVideoActivity.this,ProfileSetupActivity.class);
//        intent.putExtra("profile_setup",getIntent().getSerializableExtra("profile_setup"));
//        startActivity(intent);
//        finish();
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play_iv:
                progressbar.setVisibility(View.VISIBLE);
                play_iv.setVisibility(View.GONE);
                playingVideo();
                break;
        }
    }

    private void playingVideo() {
        play_iv.setVisibility(View.GONE);

        try {
            mediacontroller = new MediaController(ProfileManagementFullScreenVideoActivity.this);
            mediacontroller.setAnchorView(videoView);
            videoView.setMediaController(mediacontroller);
            videoView.setVideoURI(videouri);
            videoView.seekTo(0);

            final int topContainerId1 = getResources().getIdentifier("mediacontroller_progress", "id", "android");
            seekbar = (SeekBar) mediacontroller.findViewById(topContainerId1);
            seekbar.setOnSeekBarChangeListener(this);

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();

        }
        videoView.requestFocus();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            public void onPrepared(MediaPlayer mp) {
                progressbar.setVisibility(View.GONE);
                mp.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT);
                play_iv.setVisibility(View.GONE);
                videoView.start();

            }
        });


        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                play_iv.setVisibility(View.VISIBLE);

            }
        });
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        if(seekBar.getMax()==progress)
        {
            if(mediacontroller.isShowing()) {
                mediacontroller.hide();
                Log.e("prepare", String.valueOf(progress));
                play_iv.setVisibility(View.VISIBLE);
                videoView.stopPlayback();
            }
        }
        else {
            play_iv.setVisibility(View.GONE);


        }


    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        seekbar.setProgress(seekBar.getProgress());
        videoView.seekTo(seekBar.getProgress());
        videoView.resume();
        Log.e("Start", String.valueOf(seekBar.getProgress()));

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        seekbar.setProgress(seekBar.getProgress());
        videoView.seekTo(seekBar.getProgress());
        videoView.resume();
        Log.e("Stop", String.valueOf(seekBar.getProgress()));

    }

}
