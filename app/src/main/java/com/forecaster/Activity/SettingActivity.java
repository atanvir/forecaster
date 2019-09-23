package com.forecaster.Activity;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
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

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.forecaster.Modal.ForgotPassword;
import com.forecaster.Modal.Logout;
import com.forecaster.Modal.Setting;
import com.forecaster.R;
import com.forecaster.Retrofit.RetroInterface;
import com.forecaster.Retrofit.RetrofitInit;
import com.forecaster.Utility.GlobalVariables;
import com.forecaster.Utility.ProgressDailogHelper;
import com.forecaster.Utility.SharedPreferenceWriter;
import com.forecaster.Utility.Validation;

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
    private ProgressDailogHelper dailogHelper;
    //change password Pop
    EditText oldpass_ed,newpass_ed,confirm_pass_ed;


    ProgressDialog dailog;
    String laguague;
    int clickcount3=0,clickcount2=0,clickcount4=0;

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
        changelanguage_cl.setOnClickListener(this::OnClick);
        changepass_cl.setOnClickListener(this::OnClick);
        back_ll.setOnClickListener(this::OnClick);
        logout_cl.setOnClickListener(this::OnClick);
        contactus_cl.setOnClickListener(this::OnClick);
        notification_im.setOnClickListener(this::OnClick);
        dailogHelper=new ProgressDailogHelper(this,"");
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
//                startActivity(intent);
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

        if(!Validation.hasText(oldpass_ed)) ret=false;
        if(!Validation.hasText(newpass_ed)) ret=false;
        if(!Validation.hasText(confirm_pass_ed) || !confirm_pass_ed.getText().toString().equalsIgnoreCase(newpass_ed.getText().toString()))
        {
            if(!Validation.hasText(confirm_pass_ed))
            {
                ret=false;
            }
            else if(!confirm_pass_ed.getText().toString().trim().equalsIgnoreCase(newpass_ed.getText().toString().trim()))
            {
                ret=false;
                confirm_pass_ed.setError("Confirm password does not match");
                confirm_pass_ed.setFocusable(true);
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
                    dialog2.dismiss();

                    ForgotPassword server_response=response.body();
                    if(server_response.getStatus().equalsIgnoreCase(GlobalVariables.SUCCESS))
                    {
                        Toast.makeText(SettingActivity.this,server_response.getResponseMessage(),Toast.LENGTH_LONG).show();

                    }else if(server_response.getStatus().equalsIgnoreCase(GlobalVariables.FAILURE))
                    {
                        Toast.makeText(SettingActivity.this,server_response.getResponseMessage(),Toast.LENGTH_LONG).show();

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
                        //Toast.makeText(SettingActivity.this,server_response.getResponseMessage(),Toast.LENGTH_LONG).show();

                    }else if(server_response.getStatus().equalsIgnoreCase(GlobalVariables.FAILURE))
                    {

                        Toast.makeText(SettingActivity.this,server_response.getResponseMessage(),Toast.LENGTH_LONG).show();
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
                        dailogHelper.dismissDailog();
                        Toast.makeText(SettingActivity.this,server_response.getResponseMessage(),Toast.LENGTH_LONG).show();
                    }
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
