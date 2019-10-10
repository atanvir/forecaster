package com.forecaster.Activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import com.forecaster.Modal.ForgotPassword;
import com.forecaster.Modal.Login;
import com.forecaster.Modal.Logout;
import com.forecaster.Modal.Setting;
import com.forecaster.R;
import com.forecaster.Retrofit.RetroInterface;
import com.forecaster.Retrofit.RetrofitInit;
import com.forecaster.Utility.GlobalVariables;
import com.forecaster.Utility.ProgressDailogHelper;
import com.forecaster.Utility.SharedPreferenceWriter;
import com.forecaster.Utility.Validation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingActivity extends AppCompatActivity {

    @BindView(R.id.changelanguage_cl) ConstraintLayout changelanguage_cl;
    @BindView(R.id.changepass_cl) ConstraintLayout changepass_cl;
    @BindView(R.id.back_ll) LinearLayout back_ll;
    @BindView(R.id.logout_cl) ConstraintLayout logout_cl;
    @BindView(R.id.contactus_cl) ConstraintLayout contactus_cl;
    @BindView(R.id.notification_im) ImageView notification_im;
    @BindView(R.id.touch_id_iv) ImageView touch_id_iv;
    private ProgressDailogHelper dailogHelper;
    //change password Pop
    EditText oldpass_ed,newpass_ed,confirm_pass_ed;
    Dialog fingerprint_popup;

    ProgressDialog dailog;
    String laguague;
    int clickcount3=0,clickcount2=0,clickcount4=0,clickcount5=0;
    FingerprintManager fingerprintManager;
    boolean touch_id=false;

    TextView one_txt,two_txt,three_txt,four_txt,fifth_txt,six_txt,seven_txt,eight_txt,nine_txt,zero_txt,back_txt,create_passcode_txt;
    ImageView first_iv,secound_iv,third_iv,fourth_iv;
    int count_image=0;
    CountDownTimer timer;
    String code="";


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        init();
        boolean notificationStatus=SharedPreferenceWriter.getInstance(SettingActivity.this).getBoolean(GlobalVariables.notificationStatus);
        if(notificationStatus)
        {
            notification_im.setImageDrawable(getDrawable(R.drawable.on));
        }
        else
        {
            notification_im.setImageDrawable(getDrawable(R.drawable.off));
        }


    }


    private void init() {
        touch_id_iv.setOnClickListener(this::OnClick);
        changelanguage_cl.setOnClickListener(this::OnClick);
        changepass_cl.setOnClickListener(this::OnClick);
        back_ll.setOnClickListener(this::OnClick);
        logout_cl.setOnClickListener(this::OnClick);
        contactus_cl.setOnClickListener(this::OnClick);
        notification_im.setOnClickListener(this::OnClick);
        dailogHelper=new ProgressDailogHelper(this,"");
        touch_id=SharedPreferenceWriter.getInstance(SettingActivity.this).getBoolean(GlobalVariables.touchid);
        if(touch_id)
        {
            touch_id_iv.setImageResource(R.drawable.on);
        }
        else
        {
            touch_id_iv.setImageResource(R.drawable.off);
        }

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @OnClick()
    void OnClick(View view)
    {
        switch (view.getId())
        {
            case R.id.changelanguage_cl:
                changeLanguage();
                break;


            case R.id.changepass_cl:
                changePasswordDailog();
                break;


            case R.id.back_ll:
                finish();
                break;

            case R.id.logout_cl:
                logoutApi();

                break;

            case R.id.contactus_cl:
                Intent intent=new Intent(SettingActivity.this,ContactUsActivity.class);
                startActivity(intent);
                break;
            case R.id.notification_im:
                boolean notificationStatus=SharedPreferenceWriter.getInstance(SettingActivity.this).getBoolean(GlobalVariables.notificationStatus);
                if(notificationStatus)
                {
                    notification_im.setImageDrawable(getDrawable(R.drawable.off));
                    updateForecasterSettingsApi(false,SharedPreferenceWriter.getInstance(SettingActivity.this).getString(GlobalVariables.language));
                }
                else
                {
                    notification_im.setImageDrawable(getDrawable(R.drawable.on));
                    updateForecasterSettingsApi(true,SharedPreferenceWriter.getInstance(SettingActivity.this).getString(GlobalVariables.language));
                }
                break;

            case R.id.touch_id_iv:
               settingTouchId();
                break;

        }

    }

    private void settingTouchId() {
        fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
        fingerprint_popup =new Dialog(SettingActivity.this,android.R.style.Theme_Black);
        fingerprint_popup.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        fingerprint_popup.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        fingerprint_popup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        if(!touch_id)
        {

            if(!fingerprintManager.isHardwareDetected())
            {
                settingPopup("Error","Your device doesn't support fingerprint authentication");
                touch_id_iv.setImageDrawable(getDrawable(R.drawable.off));
                touch_id=false;

            }
            else
            {
                settingPopup("Done","");
                //touch_id_iv.setImageDrawable(getDrawable(R.drawable.on));


            }
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                // If your app doesn't have this permission, then display the following text//
                settingPopup("Error","Please enable the fingerprint permission");

                touch_id_iv.setImageDrawable(getDrawable(R.drawable.off));

                touch_id=false;

            }
            else
            {
                settingPopup("Done","");
                //touch_id_iv.setImageDrawable(getDrawable(R.drawable.on));

            }
            if (!fingerprintManager.hasEnrolledFingerprints()) {
                // If the user hasn’t configured any fingerprints, then display the following message//
                settingPopup("Error","No fingerprint configured. Please register at least one fingerprint in your device's Settings");
//                        text.setText("No fingerprint configured. Please register at least one fingerprint in your device's Settings");
                touch_id_iv.setImageDrawable(getDrawable(R.drawable.off));
                touch_id=false;
            }
            else
            {
                settingPopup("Done","");
               // touch_id_iv.setImageDrawable(getDrawable(R.drawable.on));

            }


        }
        else
        {

            touch_id=false;
            touch_id_iv.setImageDrawable(getDrawable(R.drawable.off));
            SharedPreferenceWriter.getInstance(SettingActivity.this).writeBooleanValue(GlobalVariables.touchid,false);

        }
    }

    private void settingPopup(String view,String error) {
        if(view.equalsIgnoreCase("Error")) {
            fingerprint_popup.setContentView(R.layout.fingerprint_error_popup);
            LinearLayout close_ll= fingerprint_popup.findViewById(R.id.close_ll);
            TextView text=fingerprint_popup.findViewById(R.id.text);
            text.setText(error);
            close_ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fingerprint_popup.dismiss();
                }
            });
            fingerprint_popup.show();
            SharedPreferenceWriter.getInstance(SettingActivity.this).writeBooleanValue(GlobalVariables.touchid,false);

        }else if(view.equalsIgnoreCase("Done"))
        {
            fingerprint_popup.setContentView(R.layout.pop_pin);
            init2();
            fingerprint_popup.show();


        }


    }

    private void init2() {
        first_iv=fingerprint_popup.findViewById(R.id.first_iv);
        secound_iv=fingerprint_popup.findViewById(R.id.secound_iv);
        third_iv=fingerprint_popup.findViewById(R.id.third_iv);
        fourth_iv=fingerprint_popup.findViewById(R.id.fourth_iv);
        one_txt=fingerprint_popup.findViewById(R.id.one_txt);
        two_txt=fingerprint_popup.findViewById(R.id.two_txt);
        three_txt=fingerprint_popup.findViewById(R.id.three_txt);
        four_txt=fingerprint_popup.findViewById(R.id.four_txt);
        fifth_txt=fingerprint_popup.findViewById(R.id.fifth_txt);
        six_txt=fingerprint_popup.findViewById(R.id.six_txt);
        seven_txt=fingerprint_popup.findViewById(R.id.seven_txt);
        eight_txt=fingerprint_popup.findViewById(R.id.eight_txt);
        nine_txt=fingerprint_popup.findViewById(R.id.nine_txt);
        zero_txt=fingerprint_popup.findViewById(R.id.zero_txt);
        back_txt=fingerprint_popup.findViewById(R.id.back_txt);
        create_passcode_txt=fingerprint_popup.findViewById(R.id.create_passcode_txt);

        one_txt.setOnClickListener(this::DailogOnClickListner);
        two_txt.setOnClickListener(this::DailogOnClickListner);
        three_txt.setOnClickListener(this::DailogOnClickListner);
        four_txt.setOnClickListener(this::DailogOnClickListner);
        fifth_txt.setOnClickListener(this::DailogOnClickListner);
        six_txt.setOnClickListener(this::DailogOnClickListner);
        seven_txt.setOnClickListener(this::DailogOnClickListner);
        eight_txt.setOnClickListener(this::DailogOnClickListner);
        nine_txt.setOnClickListener(this::DailogOnClickListner);
        zero_txt.setOnClickListener(this::DailogOnClickListner);
        back_txt.setOnClickListener(this::DailogOnClickListner);

    }

    private void DailogOnClickListner(View v) {
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
                        code=code.substring(0,code.length()-1);
                        Log.e("DELETE",code);
                        first_iv.setBackground(getDrawable(R.drawable.pin_circle_background));
                        back_txt.setText(getString(R.string.back));

                    }else if(count_image==2)
                    {
                        count_image--;
                        code=code.substring(0,code.length()-1);
                        Log.e("DELETE",code);
                        secound_iv.setBackground(getDrawable(R.drawable.pin_circle_background));
                    }
                    else if(count_image==3)
                    {
                        count_image--;
                        code=code.substring(0,code.length()-1);
                        Log.e("DELETE",code);
                        third_iv.setBackground(getDrawable(R.drawable.pin_circle_background));
                    }
                    else if(count_image==4)
                    {
                        code=code.substring(0,code.length()-1);
                        count_image--;
                        Log.e("DELETE",code);
                        fourth_iv.setBackground(getDrawable(R.drawable.pin_circle_background));
                    }

                }
                else
                {
                    fingerprint_popup.dismiss();
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
                        Log.e("code",code);

                    }
                    else if(count==2) {
                        secound_iv.setBackground(getDrawable(R.drawable.pin_circle_background2));
                        code+=value;
                        Log.e("code",code);
                    }
                    else if(count==3)
                    {
                        third_iv.setBackground(getDrawable(R.drawable.pin_circle_background2));
                        code+=value;
                        Log.e("code",code);
                    }
                    else if(count==4) {
                        fourth_iv.setBackground(getDrawable(R.drawable.pin_circle_background2));
                        code+=value;



                        Log.e("code",code);



                        if(!create_passcode_txt.getText().toString().equalsIgnoreCase(getString(R.string.please_reenter_confirm_password)))
                        {
                            SharedPreferenceWriter.getInstance(SettingActivity.this).writeStringValue(GlobalVariables.old_passcode,code);
                            fingerprint_popup.setContentView(R.layout.pop_pin);
                            init2();

                            count_image=0;
                            code="";
                            TextView create_passcode_txt2=fingerprint_popup.findViewById(R.id.create_passcode_txt);
                            create_passcode_txt.setText(getString(R.string.please_reenter_confirm_password));
                            fingerprint_popup.show();



                        }
                        else
                        {
                            String old_pass= SharedPreferenceWriter.getInstance(SettingActivity.this).getString(GlobalVariables.old_passcode);
                            if(!code.equalsIgnoreCase("")) {

                                if (old_pass.equalsIgnoreCase(code)) {
                                    fingerprint_popup.dismiss();
                                    Log.e("matched","yes");
                                    Dialog dialog = new Dialog(SettingActivity.this, android.R.style.Theme_Black);
                                    dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                                    dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
                                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    dialog.setContentView(R.layout.fingerprint_done_popup);
                                    LinearLayout close_ll = dialog.findViewById(R.id.close_ll);
                                    close_ll.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();

                                        }
                                    });
                                    dialog.show();
                                    touch_id_iv.setImageDrawable(getDrawable(R.drawable.on));
                                    SharedPreferenceWriter.getInstance(SettingActivity.this).writeBooleanValue(GlobalVariables.touchid, true);
                                    touch_id = true;


                                } else {
                                    Log.e("matched","no");
                                    Vibrator v= (Vibrator) getSystemService(VIBRATOR_SERVICE);
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                        v.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
                                    } else {
                                        //deprecated in API 26
                                        v.vibrate(200);
                                    }

                                   // Toast.makeText(SettingActivity.this,getString(R.string.password_not_match),Toast.LENGTH_LONG).show();
                                    count_image=0;
                                    code="";
                                    Log.e("count", String.valueOf(code));
                                    first_iv.setBackground(getDrawable(R.drawable.pin_circle_background));
                                    secound_iv.setBackground(getDrawable(R.drawable.pin_circle_background));
                                    third_iv.setBackground(getDrawable(R.drawable.pin_circle_background));
                                    fourth_iv.setBackground(getDrawable(R.drawable.pin_circle_background));



                                    touch_id_iv.setImageDrawable(getDrawable(R.drawable.off));
                                    SharedPreferenceWriter.getInstance(SettingActivity.this).writeBooleanValue(GlobalVariables.touchid, false);
                                    Toast.makeText(SettingActivity.this, getString(R.string.confirm_password_not_match), Toast.LENGTH_LONG).show();
                                    touch_id = false;
                                }
                            }


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


    private void changePasswordDailog() {
        final Dialog dialog2=new Dialog(SettingActivity.this,android.R.style.Theme_Black);
        dialog2.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog2.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog2.setContentView(R.layout.change_password_popup);
        oldpass_ed=dialog2.findViewById(R.id.oldpass_ed);
        newpass_ed=dialog2.findViewById(R.id.newpass_ed);
        confirm_pass_ed=dialog2.findViewById(R.id.confirm_pass_ed);
        TouchListner();
        Button submitBtn=dialog2.findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkValidation()) {
                    forecasterChangePasswordApi(dialog2);
                }
                //dialog2.dismiss();
            }
        });
        dialog2.show();


    }

    private void TouchListner() {
        oldpass_ed.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    final int DRAWABLE_LEFT = 0;
                    final int DRAWABLE_TOP = 1;
                    final int DRAWABLE_RIGHT = 2;
                    final int DRAWABLE_BOTTOM = 3;


                    if(event.getAction() == MotionEvent.ACTION_UP) {
                        if (event.getRawX() >= (oldpass_ed.getRight() - oldpass_ed.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                            if(clickcount3 % 2 ==0)
                            {
                                clickcount3=clickcount3+1;
                                oldpass_ed.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                                oldpass_ed.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.view_icon, 0);
                                return true;
                            }
                            else
                            {
                                clickcount3=clickcount3+1;
                                oldpass_ed.setTransformationMethod(PasswordTransformationMethod.getInstance());
                                oldpass_ed.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.un_view_icon, 0);
                                return true;


                            }

                        }
                    }


                    return false;
                }
            });
        newpass_ed.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    final int DRAWABLE_LEFT = 0;
                    final int DRAWABLE_TOP = 1;
                    final int DRAWABLE_RIGHT = 2;
                    final int DRAWABLE_BOTTOM = 3;


                    if(event.getAction() == MotionEvent.ACTION_UP) {
                        if (event.getRawX() >= (newpass_ed.getRight() - newpass_ed.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                            if(clickcount2 % 2 ==0)
                            {
                                clickcount2=clickcount2+1;
                                newpass_ed.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                                newpass_ed.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.view_icon, 0);
                                return true;
                            }
                            else
                            {
                                clickcount2=clickcount2+1;
                                newpass_ed.setTransformationMethod(PasswordTransformationMethod.getInstance());
                                newpass_ed.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.un_view_icon, 0);
                                return true;


                            }

                        }
                    }


                    return false;
                }
            });

        confirm_pass_ed.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;


                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (confirm_pass_ed.getRight() - confirm_pass_ed.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        if(clickcount4 % 2 ==0)
                        {
                            clickcount4=clickcount4+1;
                            confirm_pass_ed.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            confirm_pass_ed.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.view_icon, 0);
                            return true;
                        }
                        else
                        {
                            clickcount4=clickcount4+1;
                            confirm_pass_ed.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            confirm_pass_ed.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.un_view_icon, 0);
                            return true;


                        }

                    }
                }


                return false;
            }
        });


    }

    private boolean checkValidation() {
        boolean ret=true;
        if(!Validation.hasText(oldpass_ed,getString(R.string.please_enter_oldpass))
        || !Validation.hasText(newpass_ed,getString(R.string.please_enter_newpass))
        || !Validation.hasText(confirm_pass_ed,getString(R.string.please_enter_confirm_password))
        || !confirm_pass_ed.getText().toString().equalsIgnoreCase(newpass_ed.getText().toString()))
        {
            if(!Validation.hasText(oldpass_ed,getString(R.string.please_enter_oldpass)))
            {
                ret=false;
                oldpass_ed.requestFocus();
            }else if(!Validation.hasText(newpass_ed,getString(R.string.please_enter_newpass)))
            {
                ret=false;
                newpass_ed.requestFocus();
            }else if(!Validation.hasText(confirm_pass_ed,getString(R.string.please_enter_confirm_password)))
            {
                ret=false;
                confirm_pass_ed.requestFocus();
            }
            else if(!confirm_pass_ed.getText().toString().equalsIgnoreCase(newpass_ed.getText().toString()))
            {
                ret=false;
                confirm_pass_ed.requestFocus();
            }


        }



        return ret;
    }

    private void forecasterChangePasswordApi(Dialog dialog2) {
        dailogHelper.showDailog();
        RetroInterface api_service=RetrofitInit.getConnect().createConnection();
        ForgotPassword password=new ForgotPassword();
        password.setPassword(oldpass_ed.getText().toString().trim());
        password.setNewPassword(newpass_ed.getText().toString().trim());
        password.setForecasterId(SharedPreferenceWriter.getInstance(SettingActivity.this).getString(GlobalVariables._id));
        password.setLangCode("en");
        Call<ForgotPassword> call=api_service.forecasterChangePassword(password,SharedPreferenceWriter.getInstance(SettingActivity.this).getString(GlobalVariables.jwtToken));
        call.enqueue(new Callback<ForgotPassword>() {
            @Override
            public void onResponse(Call<ForgotPassword> call, Response<ForgotPassword> response) {
                if(response.isSuccessful())
                {
                    dailogHelper.dismissDailog();


                    ForgotPassword server_response=response.body();
                    if(server_response.getStatus().equalsIgnoreCase(GlobalVariables.SUCCESS))
                    {
                        dialog2.dismiss();
                        Toast.makeText(SettingActivity.this,server_response.getResponseMessage(),Toast.LENGTH_LONG).show();

                    }else if(server_response.getStatus().equalsIgnoreCase(GlobalVariables.FAILURE))
                    {
                        if(server_response.getResponseMessage().equalsIgnoreCase(GlobalVariables.invalidoken))
                        {
                            Toast.makeText(SettingActivity.this,getString(R.string.other_device_logged_in),Toast.LENGTH_LONG).show();
                            finish();
                            startActivity(new Intent(SettingActivity.this,LoginActivity.class));
                            SharedPreferenceWriter.getInstance(SettingActivity.this).clearPreferenceValues();
                            FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                @Override
                                public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                    if (!task.isSuccessful()) {
                                        // Log.w(TAG, "getInstanceId failed", task.getException());
                                        return;
                                    }

                                    String auth_token = task.getResult().getToken();
                                    Log.w("firebaese","token: "+auth_token);
                                    SharedPreferenceWriter.getInstance(SettingActivity.this).writeStringValue(GlobalVariables.deviceToken,auth_token);
                                }
                            });
                        }



                        oldpass_ed.setError(server_response.getResponseMessage());
                        oldpass_ed.requestFocus();
                        oldpass_ed.setFocusable(true);
                        //Toast.makeText(SettingActivity.this,server_response.getResponseMessage(),Toast.LENGTH_LONG).show();

                    }

                }
            }

            @Override
            public void onFailure(Call<ForgotPassword> call, Throwable t) {

            }
        });

    }

    private void logoutApi() {
        dailogHelper.showDailog();
        RetroInterface api_service=RetrofitInit.getConnect().createConnection();
        Logout logout=new Logout();
        logout.setLangCode("en");
        logout.setForecasterId(SharedPreferenceWriter.getInstance(SettingActivity.this).getString(GlobalVariables._id));
        Call<Logout> call=api_service.forecasterLogout(logout);
        call.enqueue(new Callback<Logout>() {
            @Override
            public void onResponse(Call<Logout> call, Response<Logout> response) {

                if(response.isSuccessful())
                {
                    dailogHelper.dismissDailog();

                    Logout server_response=response.body();
                    if(server_response.getStatus().equalsIgnoreCase(GlobalVariables.SUCCESS))
                    {
                        Intent intent=new Intent(SettingActivity.this,LoginActivity.class);
                        finish();
                        startActivity(intent);
                        SharedPreferenceWriter.getInstance(SettingActivity.this).writeStringValue(GlobalVariables.islogin,"No");
                        SharedPreferenceWriter.getInstance(SettingActivity.this).clearPreferenceValues();
                        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                            @Override
                            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                if (!task.isSuccessful()) {
                                    // Log.w(TAG, "getInstanceId failed", task.getException());
                                    return;
                                }

                                String auth_token = task.getResult().getToken();
                                Log.w("firebaese","token: "+auth_token);
                                SharedPreferenceWriter.getInstance(SettingActivity.this).writeStringValue(GlobalVariables.deviceToken,auth_token);
                            }
                        });
                        //Toast.makeText(SettingActivity.this,server_response.getResponseMessage(),Toast.LENGTH_LONG).show();

                    }else if(server_response.getStatus().equalsIgnoreCase(GlobalVariables.FAILURE))
                    {
                        if(server_response.getResponseMessage().equalsIgnoreCase(GlobalVariables.invalidoken))
                        {
                            Toast.makeText(SettingActivity.this,getString(R.string.other_device_logged_in),Toast.LENGTH_LONG).show();
                            finish();
                            startActivity(new Intent(SettingActivity.this,LoginActivity.class));
                            SharedPreferenceWriter.getInstance(SettingActivity.this).clearPreferenceValues();
                            FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                @Override
                                public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                    if (!task.isSuccessful()) {
                                        // Log.w(TAG, "getInstanceId failed", task.getException());
                                        return;
                                    }

                                    String auth_token = task.getResult().getToken();
                                    Log.w("firebaese","token: "+auth_token);
                                    SharedPreferenceWriter.getInstance(SettingActivity.this).writeStringValue(GlobalVariables.deviceToken,auth_token);
                                }
                            });
                        }
                        else {
                            Toast.makeText(SettingActivity.this, server_response.getResponseMessage(), Toast.LENGTH_LONG).show();
                        }
                        }
                }

            }

            @Override
            public void onFailure(Call<Logout> call, Throwable t) {

            }
        });

    }

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void changeLanguage() {
        final Dialog dialog=new Dialog(SettingActivity.this,android.R.style.Theme_Black);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.change_language_popup);
        ConstraintLayout eng_cl=dialog.findViewById(R.id.eng_cl);
        ConstraintLayout arabic_cl=dialog.findViewById(R.id.arabic_cl);
        ConstraintLayout urdu_cl=dialog.findViewById(R.id.urdu_cl);
        ImageView eng_im=dialog.findViewById(R.id.eng_im);
        ImageView arabic_iv=dialog.findViewById(R.id.arabic_iv);
        ImageView urdu_iv=dialog.findViewById(R.id.urdu_iv);
        TextView eng_txt=dialog.findViewById(R.id.eng_txt);
        TextView arabic_txt=dialog.findViewById(R.id.arabic_txt);
        TextView urdu_txt=dialog.findViewById(R.id.urdu_txt);
        LinearLayout close_ll=dialog.findViewById(R.id.close_ll);

        close_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        laguague=SharedPreferenceWriter.getInstance(SettingActivity.this).getString(GlobalVariables.language);
        if(laguague.equalsIgnoreCase("English"))
        {
            eng_cl.setBackground(getDrawable(R.drawable.change_language_popup));
            eng_txt.setTextColor(getColor(R.color.green));
            eng_im.setImageDrawable(getDrawable(R.drawable.ellipse_));

        }
        else if(laguague.equalsIgnoreCase("Arabic"))
        {
            arabic_cl.setBackground(getDrawable(R.drawable.change_language_popup));
            arabic_txt.setTextColor(getColor(R.color.green));
            arabic_iv.setImageDrawable(getDrawable(R.drawable.ellipse_));

        }
        else if(laguague.equalsIgnoreCase("Urdu"))
        {
            urdu_cl.setBackground(getDrawable(R.drawable.change_language_popup));
            urdu_txt.setTextColor(getColor(R.color.green));
            urdu_iv.setImageDrawable(getDrawable(R.drawable.ellipse_));


        }





        eng_im.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {


                laguague="English";
                eng_cl.setBackground(getDrawable(R.drawable.change_language_popup));
                eng_txt.setTextColor(getColor(R.color.green));
                eng_im.setImageDrawable(getDrawable(R.drawable.ellipse_));

                arabic_cl.setBackground(getDrawable(R.drawable.change_language_popup2));
                arabic_txt.setTextColor(getColor(R.color.blurgrey));
                arabic_iv.setImageDrawable(getDrawable(R.drawable.ellipse_2_copy));

                urdu_cl.setBackground(getDrawable(R.drawable.change_language_popup2));
                urdu_txt.setTextColor(getColor(R.color.blurgrey));
                urdu_iv.setImageDrawable(getDrawable(R.drawable.ellipse_2_copy));



            }
        });
        arabic_iv.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                laguague="Arabic";
                arabic_cl.setBackground(getDrawable(R.drawable.change_language_popup));
                arabic_txt.setTextColor(getColor(R.color.green));
                arabic_iv.setImageDrawable(getDrawable(R.drawable.ellipse_));


                eng_cl.setBackground(getDrawable(R.drawable.change_language_popup2));
                eng_txt.setTextColor(getColor(R.color.blurgrey));
                eng_im.setImageDrawable(getDrawable(R.drawable.ellipse_2_copy));

                urdu_cl.setBackground(getDrawable(R.drawable.change_language_popup2));
                urdu_txt.setTextColor(getColor(R.color.blurgrey));
                urdu_iv.setImageDrawable(getDrawable(R.drawable.ellipse_2_copy));



            }
        });

        urdu_iv.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                laguague="Urdu";
                urdu_cl.setBackground(getDrawable(R.drawable.change_language_popup));
                urdu_txt.setTextColor(getColor(R.color.green));
                urdu_iv.setImageDrawable(getDrawable(R.drawable.ellipse_));


                eng_cl.setBackground(getDrawable(R.drawable.change_language_popup2));
                eng_txt.setTextColor(getColor(R.color.blurgrey));
                eng_im.setImageDrawable(getDrawable(R.drawable.ellipse_2_copy));

                arabic_cl.setBackground(getDrawable(R.drawable.change_language_popup2));
                arabic_txt.setTextColor(getColor(R.color.blurgrey));
                arabic_iv.setImageDrawable(getDrawable(R.drawable.ellipse_2_copy));

            }
        });



        Button changeBtn=dialog.findViewById(R.id.changeBtn);
        changeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                updateForecasterSettingsApi(SharedPreferenceWriter.getInstance(SettingActivity.this).getBoolean(GlobalVariables.notificationStatus),laguague);

            }
        });
        dialog.show();
    }

    private void updateForecasterSettingsApi(boolean notificationStatus,String language) {
        dailogHelper.showDailog();
        RetroInterface api_service= RetrofitInit.getConnect().createConnection();
        Setting setting=new Setting();
        setting.setForecasterId(SharedPreferenceWriter.getInstance(SettingActivity.this).getString(GlobalVariables._id));
        setting.setLanguage(language);
        setting.setNotificationStatus(notificationStatus);
        setting.setLangCode("en");;
        Call<Setting> call=api_service.updateForecasterSettings(setting,SharedPreferenceWriter.getInstance(SettingActivity.this).getString(GlobalVariables.jwtToken));
        call.enqueue(new Callback<Setting>() {
            @Override
            public void onResponse(Call<Setting> call, Response<Setting> response) {
                if(response.isSuccessful())
                {
                    Setting server_response=response.body();
                    if(server_response.getStatus().equalsIgnoreCase("SUCCESS"))
                    {
                        dailogHelper.dismissDailog();
                       // Toast.makeText(SettingActivity.this,server_response.getResponseMessage(),Toast.LENGTH_LONG).show();
                        setPreferences(server_response);
                    }
                    else if(server_response.getStatus().equalsIgnoreCase("FAILURE"))
                    {
                        if(server_response.getResponseMessage().equalsIgnoreCase(GlobalVariables.invalidoken))
                        {
                            Toast.makeText(SettingActivity.this,getString(R.string.other_device_logged_in),Toast.LENGTH_LONG).show();
                            finish();
                            startActivity(new Intent(SettingActivity.this,LoginActivity.class));
                            SharedPreferenceWriter.getInstance(SettingActivity.this).clearPreferenceValues();
                            FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                @Override
                                public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                    if (!task.isSuccessful()) {
                                        // Log.w(TAG, "getInstanceId failed", task.getException());
                                        return;
                                    }

                                    String auth_token = task.getResult().getToken();
                                    Log.w("firebaese","token: "+auth_token);
                                    SharedPreferenceWriter.getInstance(SettingActivity.this).writeStringValue(GlobalVariables.deviceToken,auth_token);
                                }
                            });
                        }
                        else
                        {

                        dailogHelper.dismissDailog();
                        Toast.makeText(SettingActivity.this,server_response.getResponseMessage(),Toast.LENGTH_LONG).show();
                    }}
                }
            }

            @Override
            public void onFailure(Call<Setting> call, Throwable t) {

            }
        });


    }

    private void setPreferences(Setting server_response) {
        SharedPreferenceWriter.getInstance(SettingActivity.this).writeBooleanValue(GlobalVariables.notificationStatus,server_response.getData().getNotificationStatus());
        SharedPreferenceWriter.getInstance(SettingActivity.this).writeStringValue(GlobalVariables.language,server_response.getData().getLanguage());
        SharedPreferenceWriter.getInstance(SettingActivity.this).writeStringValue(GlobalVariables._id,server_response.getData().getId());
    }
}
