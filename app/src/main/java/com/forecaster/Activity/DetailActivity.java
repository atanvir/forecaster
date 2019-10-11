package com.forecaster.Activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.forecaster.Modal.Data;
import com.forecaster.R;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class DetailActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {
    @BindView(R.id.profile_iv)  CircleImageView profile_iv;
    @BindView(R.id.username_txt) TextView username_txt;
    @BindView(R.id.dob_txt) TextView dob_txt;
    @BindView(R.id.gender_txt) TextView gender_txt;
    @BindView(R.id.maritalStatus_txt) TextView maritalStatus_txt;
    @BindView(R.id.question_txt) TextView question_txt;
    @BindView(R.id.stop_iv) ImageView stop_iv;
    @BindView(R.id.seekBar) SeekBar seekBar;
    @BindView(R.id.play_iv) ImageView play_iv;
    @BindView(R.id.timer_txt) TextView timer_txt;
    @BindView(R.id.pause_iv) ImageView pause_iv;
    @BindView(R.id.recorded_audio_iv) ImageView recorded_audio_iv;
    @BindView(R.id.recorded_voice_txt) TextView recorded_voice_txt;
    MediaPlayer mediaPlayer;
    MediaRecorder mRecorder;
    boolean recording;
    boolean playing;
    CountDownTimer timer;
    Uri audio_uri;
    private Handler handler=new Handler();
    int medialast_position=0;
    boolean pause=false;
    ArrayList<Data> data;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        init();
        settingDetails();




    }

    private void init() {
        stop_iv.setOnClickListener(this::OnClick);
        play_iv.setOnClickListener(this::OnClick);
        seekBar.setOnSeekBarChangeListener(this);
        pause_iv.setOnClickListener(this::OnClick);
    }
    
    

    private void settingDetails() {
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null) {
                data = bundle.getParcelableArrayList("RequestManagement");
                Glide.with(this).load(data.get(0).getDreamerData().getProfilePic()).into(profile_iv);
                username_txt.setText(data.get(0).getDreamerData().getName());
                dob_txt.setText(data.get(0).getDreamerData().getDob());
                gender_txt.setText(data.get(0).getDreamerData().getGender());
                maritalStatus_txt.setText(data.get(0).getDreamerData().getMaritalStatus());
                question_txt.setText(data.get(0).getQuestion());
                settingSekkbar(data);
        }


    }

    private void settingSekkbar(ArrayList<Data> data) {
        if(audio_uri!=null) {
            seekBar.setVisibility(View.VISIBLE);
            stop_iv.setVisibility(View.VISIBLE);
            play_iv.setVisibility(View.VISIBLE);
            recorded_audio_iv.setVisibility(View.VISIBLE);
            recorded_voice_txt.setVisibility(View.VISIBLE);
            timer_txt.setVisibility(View.VISIBLE);

            audio_uri = Uri.parse(data.get(0).getVoiceNote());
            try {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setDataSource(this, Uri.parse(data.get(0).getVoiceNote()));
                mediaPlayer.prepare();

            } catch (IOException e) {
                e.printStackTrace();
            }
            seekBar.setMax(300);

            seekBar.setProgress(mediaPlayer.getDuration() / 1000);
            if (mediaPlayer.getDuration() / 1000 < 10) {
                timer_txt.setText("0:0" + mediaPlayer.getDuration() / 1000);
            } else {
                timer_txt.setText("0:" + mediaPlayer.getDuration() / 1000);
            }
        }
        else
        {
            seekBar.setVisibility(View.GONE);
            stop_iv.setVisibility(View.GONE);
            play_iv.setVisibility(View.GONE);
            recorded_audio_iv.setVisibility(View.GONE);
            recorded_voice_txt.setVisibility(View.GONE);
            timer_txt.setVisibility(View.GONE);

        }

    }

    @OnClick({R.id.back_ll,R.id.chat_btn})
    void OnClick(View view)
    {
        switch (view.getId())
        {
            case R.id.back_ll:
                Intent intent=new Intent(DetailActivity.this,RequestManagementActivity.class);
                finish();
                startActivity(intent);
                break;


            case R.id.chat_btn:
                Intent intent1=new Intent(DetailActivity.this,ChatDetailsActivity.class);
                intent1.putExtra("chat_details", data.get(0));
                Log.e("datac",data.get(0).toString());
                startActivity(intent1);
                break;
                
            case R.id.stop_iv:
                stoppingAudio();
                break;
                
            case R.id.play_iv:
                playingAudio();
                break;

            case R.id.pause_iv:
                pauseAudio();
                break;
        }
    }

    private void settingModalValues() {
    }

    private void pauseAudio() {
        pause=true;
        play_iv.setVisibility(View.VISIBLE);
        pause_iv.setVisibility(View.GONE);
        try {
            mRecorder.stop();
            mRecorder.release();

        } catch (Exception e) {
            e.printStackTrace();
        }
        try
        {
            recording=false;
            playing=false;
            if(mediaPlayer.isPlaying())
            {
                Log.e("pasue","paused");
                medialast_position=mediaPlayer.getCurrentPosition();
                mediaPlayer.pause();

            }
            if(timer!=null) {
                timer.cancel();
            }

        }catch (Exception e)
        {
            e.printStackTrace();
        }


    }

    private void playingAudio() {
        try {
            play_iv.setVisibility(View.GONE);
            pause_iv.setVisibility(View.VISIBLE);
            if(timer!=null) {
                timer.cancel();

            }
            recording=false;
            playing=true;
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(this,audio_uri);
            mediaPlayer.prepare();
            if(pause) {
                mediaPlayer.seekTo(medialast_position);
            }mediaPlayer.start();
            seekBar.setProgress(0);
            seekBar.setMax(mediaPlayer.getDuration());
            seekBar.setClickable(false);
            seekUpdation();

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    play_iv.setVisibility(View.VISIBLE);
                    pause_iv.setVisibility(View.GONE);
                    pause=false;

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            seekUpdation();
        }
    };

    private void seekUpdation() {
        if(playing) {
            if (mediaPlayer != null) {
                int mCurrentPosition = mediaPlayer.getCurrentPosition();
                seekBar.setProgress(mCurrentPosition);
            }
            handler.postDelayed(runnable, 100);
        }
    }



    private void stoppingAudio() {
        try {
            mRecorder.stop();
            mRecorder.release();

        } catch (Exception e) {
            e.printStackTrace();
        }
        try
        {
            recording=false;
            playing=false;
            mediaPlayer.release();
            if(timer!=null) {
                timer.cancel();
            }

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if(progress/1000>10)
        {
            timer_txt.setText(""+progress/1000+":00");
        }
        else
        {
            timer_txt.setText("0:0"+progress/1000);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
