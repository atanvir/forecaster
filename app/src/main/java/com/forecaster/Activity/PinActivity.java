package com.forecaster.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.forecaster.R;
import com.forecaster.Utility.GlobalVariables;
import com.forecaster.Utility.SharedPreferenceWriter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PinActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.main_cl) ConstraintLayout main_cl;
    @BindView(R.id.one_txt) TextView one_txt;
    @BindView(R.id.two_txt) TextView two_txt;
    @BindView(R.id.three_txt) TextView three_txt;
    @BindView(R.id.four_txt) TextView four_txt;
    @BindView(R.id.fifth_txt) TextView fifth_txt;
    @BindView(R.id.six_txt) TextView six_txt;
    @BindView(R.id.seven_txt) TextView seven_txt;
    @BindView(R.id.eight_txt) TextView eight_txt;
    @BindView(R.id.nine_txt) TextView nine_txt;
    @BindView(R.id.zero_txt) TextView zero_txt;
    CountDownTimer timer;
    @BindView(R.id.first_iv) ImageView first_iv;
    @BindView(R.id.secound_iv) ImageView secound_iv;
    @BindView(R.id.third_iv) ImageView third_iv;
    @BindView(R.id.fourth_iv) ImageView fourth_iv;
    int count_image=0;
    @BindView(R.id.back_txt) TextView back_txt;
    String code="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);
        ButterKnife.bind(this);
        init();
        Animation  animation=AnimationUtils.loadAnimation(this,R.anim.scroll_up_anim2);
        main_cl.startAnimation(animation);
    }

    private void init() {
        one_txt.setOnClickListener(this);
        two_txt.setOnClickListener(this);
        three_txt.setOnClickListener(this);
        four_txt.setOnClickListener(this);
        fifth_txt.setOnClickListener(this);
        six_txt.setOnClickListener(this);
        seven_txt.setOnClickListener(this);
        eight_txt.setOnClickListener(this);
        nine_txt.setOnClickListener(this);
        zero_txt.setOnClickListener(this);
        back_txt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.one_txt:
                count_image++;
                settingBackground(one_txt,count_image,1);
                break;

            case R.id.two_txt:
                count_image++;
                settingBackground(two_txt,count_image,2);
                break;
            case R.id.three_txt:
                count_image++;
                settingBackground(three_txt,count_image,3);

                break;
            case R.id.four_txt:
                count_image++;
                settingBackground(four_txt,count_image,4);
                break;
            case R.id.fifth_txt:
                count_image++;
                settingBackground(fifth_txt,count_image,5);
                break;
            case R.id.six_txt:
                count_image++;
                settingBackground(six_txt,count_image,6);
                break;
            case R.id.seven_txt:
                count_image++;
                settingBackground(seven_txt,count_image,7);
                break;
            case R.id.eight_txt:
                count_image++;
                settingBackground(eight_txt,count_image,8);
                break;
            case R.id.nine_txt:
                count_image++;
                settingBackground(nine_txt,count_image,9);
                break;
            case R.id.zero_txt:
                count_image++;
                settingBackground(zero_txt,count_image,0);

                break;
            case R.id.back_txt:
                if(back_txt.getText().equals(getString(R.string.delete)))
                {

                    if(count_image==1)
                    {
                        count_image--;
                        first_iv.setBackground(getDrawable(R.drawable.pin_circle_background));
                        code=code.substring(0,code.length()-1);
                        back_txt.setText(getString(R.string.back));

                    }else if(count_image==2)
                    {
                        count_image--;
                        secound_iv.setBackground(getDrawable(R.drawable.pin_circle_background));
                        code=code.substring(0,code.length()-1);
                    }
                    else if(count_image==3)
                    {
                        count_image--;
                        third_iv.setBackground(getDrawable(R.drawable.pin_circle_background));
                        code=code.substring(0,code.length()-1);
                    }
                    else if(count_image==4)
                    {
                        count_image--;
                        fourth_iv.setBackground(getDrawable(R.drawable.pin_circle_background));
                        code=code.substring(0,code.length()-1);
                    }

                }
                else
                {
                    Intent intent=new Intent(PinActivity.this,FingerprintActivity.class);
                    finish();
                    startActivity(intent);
                }
                break;
        }

    }

    private void settingBackground(TextView txtview,int count,int value) {
        if (count <= 4) {
            timer = new CountDownTimer(300, 300) {
                @Override
                public void onTick(long millisUntilFinished) {
                    txtview.setBackground(getDrawable(R.drawable.green_circle_shape4));
                    txtview.setTextColor(getColor(R.color.white));
                }

                @Override
                public void onFinish() {
                    txtview.setBackground(getDrawable(R.drawable.green_circle_shape3));
                    txtview.setTextColor(getColor(R.color.black));
                    if(count==1)
                    {
                        first_iv.setBackground(getDrawable(R.drawable.pin_circle_background2));
                        back_txt.setText(getString(R.string.delete));
                        code+=value;


                    }
                    else if(count==2) {
                        code+=value;
                        secound_iv.setBackground(getDrawable(R.drawable.pin_circle_background2));
                    }
                    else if(count==3)
                    {
                        code+=value;
                        third_iv.setBackground(getDrawable(R.drawable.pin_circle_background2));
                    }
                    else if(count==4) {
                        code += value;
                        fourth_iv.setBackground(getDrawable(R.drawable.pin_circle_background2));

                        if(SharedPreferenceWriter.getInstance(PinActivity.this).getString(GlobalVariables.old_passcode).equalsIgnoreCase(code))
                        {
                            Intent intent1=new Intent(PinActivity.this,SplashActivity.class);
                            intent1.putExtra("pass_splash","yes");
                            finish();
                            startActivity(intent1);

                        }
                        else
                        {
                            Vibrator v= (Vibrator) getSystemService(VIBRATOR_SERVICE);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                v.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
                            } else {
                                //deprecated in API 26
                                v.vibrate(200);
                            }


                            Toast.makeText(PinActivity.this,getString(R.string.password_not_match),Toast.LENGTH_LONG).show();
                            count_image=0;
                            code="";
                            Log.e("count", String.valueOf(code));
                            first_iv.setBackground(getDrawable(R.drawable.pin_circle_background));
                            secound_iv.setBackground(getDrawable(R.drawable.pin_circle_background));
                            third_iv.setBackground(getDrawable(R.drawable.pin_circle_background));
                            fourth_iv.setBackground(getDrawable(R.drawable.pin_circle_background));




                            //Toast.makeText(PinActivity.this,getString(R.string.confirm_password_not_match),Toast.LENGTH_LONG).show();

                        }

                    }


             }
            }.start();

        }
        else
        {

            count_image--;



        }

    }
}
