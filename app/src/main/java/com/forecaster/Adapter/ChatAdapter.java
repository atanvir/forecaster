package com.forecaster.Adapter;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.forecaster.Activity.ProfileManagementActivity;
import com.forecaster.Modal.Data;
import com.forecaster.R;
import com.forecaster.Utility.GlobalVariables;
import com.forecaster.Utility.SharedPreferenceWriter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder>  {
    private Context context;
    private List<Data> data;
    private MediaPlayer mediaPlayer;
    private CountDownTimer timer;
    boolean playing=false;
    boolean pause=false;
    private SeekBar seekBar;
    private Handler handler=new Handler();
    private MediaRecorder mediaRecorder;
    int lastplay_position=0;
    int positiony=0;



    public ChatAdapter(Context context, List<Data> data)
    {
        this.context=context;
        this.data=data;
    }


    @Override
    public int getItemViewType(int position) {
        if(SharedPreferenceWriter.getInstance(context).getString(GlobalVariables._id).equalsIgnoreCase(data.get(position).getSenderId()))
        {
            return 0;
        }
        else
        {
            return 1;
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=null;
        if(viewType==0)
        {
            view=LayoutInflater.from(context).inflate(R.layout.right_chat_adapter,parent,false);
        }
        else if(viewType==1 )
        {

            view=LayoutInflater.from(context).inflate(R.layout.adapter_left_chat,parent,false);


        }


        return new ChatAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if(data.get(position).getMessageType().equalsIgnoreCase("Media"))
        {
            holder.progressbar.setVisibility(View.GONE);
            holder.audio_cl.setVisibility(View.VISIBLE);
            holder.mesage_txt.setVisibility(View.GONE);
            settingSeekbar(holder,position);
        }
        else
        {
            holder.progressbar.setVisibility(View.GONE);
            holder.audio_cl.setVisibility(View.GONE);
            holder.mesage_txt.setVisibility(View.VISIBLE);
            holder.mesage_txt.setText(data.get(position).getMessage());

        }
        positiony=position;


        String getDate = data.get(position).getCreatedAt();
        String server_format = getDate;    //server comes format ?
        String server_format1 = "2019-04-04T13:27:36.591Z";    //server comes format ?
        String myFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));


        try {
            Date date = sdf.parse(server_format);
            String server_date_complete = new SimpleDateFormat("dd/MM/yyyy hh:mm aa").format(date);

            Date currrent_date=Calendar.getInstance().getTime();

            long difference=currrent_date.getTime()-date.getTime();
            int differ=currrent_date.compareTo(date);
            Log.e("difference", String.valueOf(difference));

            if(difference<=60000)
            {
                holder.time_txt.setText(context.getString(R.string.just_now));
            }
            else if(difference>60000 && difference<86400000-currrent_date.getTime()/60000)
            {
                holder.time_txt.setText(context.getString(R.string.today)+" "+server_date_complete.split(" ")[1]+" "+server_date_complete.split(" ")[2]);
            }
            else if(difference>(86400000-currrent_date.getTime()/60000) && difference<86400000*2) {

                holder.time_txt.setText(context.getString(R.string.yesterday)+" " + server_date_complete.split(" ")[1]+" "+server_date_complete.split(" ")[2]);
            }
            else
            {
                holder.time_txt.setText(server_date_complete.split(" ")[0]);
            }


        }catch (Exception e)
        {
            e.printStackTrace();
        }




    }

    private void settingSeekbar(MyViewHolder holder, int position) {
        try {

            if(data.get(position).getMedia()!=null)
            mediaPlayer=new MediaPlayer();
            mediaPlayer.setDataSource(context, Uri.parse(data.get(position).getMedia()));


            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    Log.e("duration", String.valueOf(mp.getDuration()));
                    holder.seekBar2.setMax(300);
                    holder.seekBar2.setProgress(mp.getDuration() / 100);

                    if (mp.getDuration() / 1000 < 10) {
                        holder.timer_txt.setText("0:0" + mp.getDuration() / 1000);
                    } else {
                        holder.timer_txt.setText("0:" + mp.getDuration() / 1000 );
                    }

                }
            });
            mediaPlayer.prepareAsync();




        } catch (IOException e) {
            e.printStackTrace();
        }




    }

    @Override
    public int getItemCount() {
        return data.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.time_txt) TextView time_txt;
        @BindView(R.id.mesage_txt) TextView mesage_txt;
        @BindView(R.id.audio_cl) ConstraintLayout audio_cl;
        @BindView(R.id.stop_iv) ImageView stop_iv;
        @BindView(R.id.seekBar2) SeekBar seekBar2;
        @BindView(R.id.play_iv) ImageView play_iv;
        @BindView(R.id.pause_iv) ImageView pause_iv;
        @BindView(R.id.timer_txt) TextView timer_txt;
        @BindView(R.id.progressbar) ProgressBar progressbar;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if(playing) {
                        if (progress / 1000 < 10) {
                            timer_txt.setText("0:0" + progress / 1000);
                        } else {
                            timer_txt.setText("0:" + progress / 1000);
                        }
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            play_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if(positiony!=getAdapterPosition()) {


                            try {
                                playing = false;
                                mediaPlayer.release();
                                if (timer != null) {
                                    timer.cancel();
                                    timer.onFinish();
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                play_iv.setVisibility(View.VISIBLE);
                            }

                        }


                        Log.e("position", String.valueOf(positiony));


                        stop_iv.setVisibility(View.VISIBLE);
                        pause_iv.setVisibility(View.VISIBLE);
                        play_iv.setVisibility(View.GONE);
                        if (timer != null) {
                            timer.cancel();
                            timer.onFinish();

                        }
                        playing = true;
                        mediaPlayer = new MediaPlayer();
                        if (data.get(getAdapterPosition()).getMedia() != null) {
                            mediaPlayer.setDataSource(context, Uri.parse(data.get(getAdapterPosition()).getMedia()));
                        }

                        mediaPlayer.prepare();
                        if (pause) {
                            mediaPlayer.seekTo(lastplay_position);
                        }
                        mediaPlayer.start();

                        seekBar2.setProgress(0);
                        seekBar2.setMax(mediaPlayer.getDuration());
                        seekBar2.setClickable(false);
                        seekBar=seekBar2;
                        seekUpdation();

                        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {

                                play_iv.setVisibility(View.VISIBLE);
                                pause_iv.setVisibility(View.GONE);
                                pause = false;

                            }
                        });


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            stop_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        playing = false;
                        mediaPlayer.release();
                        if (timer != null) {
                            timer.cancel();
                            timer.onFinish();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    finally {
                        play_iv.setVisibility(View.VISIBLE);
                    }
                }
            });

            pause_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pause = true;
                    try {
                        pause_iv.setVisibility(View.GONE);
                        play_iv.setVisibility(View.VISIBLE);
                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.pause();
                            lastplay_position = mediaPlayer.getCurrentPosition();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });


        }


        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                seekUpdation();
            }
        };

        private void seekUpdation() {
            if (playing) {
                if (mediaPlayer != null) {
                    int mCurrentPosition = mediaPlayer.getCurrentPosition();
                    seekBar.setProgress(mCurrentPosition);
                }
                handler.postDelayed(runnable, 100);
            }
        }}}



