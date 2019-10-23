package com.forecaster.Activity;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.forecaster.Modal.CheckMobileNumber;
import com.forecaster.Modal.ForgotPassword;
import com.forecaster.Modal.Login;
import com.forecaster.R;
import com.forecaster.Retrofit.RetroInterface;
import com.forecaster.Retrofit.RetrofitInit;
import com.forecaster.Utility.GlobalVariables;
import com.forecaster.Utility.ProgressDailogHelper;
import com.forecaster.Utility.SharedPreferenceWriter;
import com.forecaster.Utility.Validation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.hbb20.CountryCodePicker;
import com.heetch.countrypicker.Country;
import com.heetch.countrypicker.CountryPickerCallbacks;
import com.heetch.countrypicker.CountryPickerDialog;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements ViewTreeObserver.OnGlobalLayoutListener {
    @BindView(R.id.signuptext) TextView signuptext;
    @BindView(R.id.forgetPasswordText) TextView forgetPasswordText;
    @BindView(R.id.loginBtn) Button loginBtn;
    @BindView(R.id.username_ed) EditText username_ed;
    @BindView(R.id.pass_ed) EditText pass_ed;
    @BindView(R.id.scrollview) ScrollView scrollview;
    ProgressDialog dailog;
    EditText first_ed,secound_ed,third_ed,fourth_ed,fifth_ed,sixth_ed;
    EditText newpass_ed,confirm_pass_ed;
    private String verificationCode;
    FirebaseAuth auth;
    int clickcount=0;
    String countrycode="";
    private ProgressDailogHelper dailogHelper;
    int clickcount3=0,clickcount2=0;
    @BindView(R.id.constraintLayout) ConstraintLayout constraintLayout;
    int height;
    int width;
    Validation validation;
    String langCode="";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Locale locale=new Locale(SharedPreferenceWriter.getInstance(this).getString(GlobalVariables.langCode));
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(GlobalVariables.langCode, String.valueOf(locale));
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        init();
        auth=FirebaseAuth.getInstance();
        settingPasswordVisiblity();



    }

    private void settingPasswordVisiblity() {
        pass_ed.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;


                if(event.getAction() == MotionEvent.ACTION_UP) {
                    String langCode=SharedPreferenceWriter.getInstance(LoginActivity.this).getString(GlobalVariables.langCode);
                    if(langCode.equalsIgnoreCase("ar"))
                    {
                        Log.e("X", String.valueOf(event.getX()));


                     //   Log.e("where", String.valueOf(pass_ed.getCompoundDrawables()[DRAWABLE_LEFT].getBounds().width())-pass_ed.getPaddingLeft());

                        if (event.getRawX()  <= (pass_ed.getCompoundDrawables()[DRAWABLE_LEFT].getBounds().width())+pass_ed.getPaddingLeft()+pass_ed.getPaddingRight()+pass_ed.getPaddingBottom()+pass_ed.getPaddingTop()+pass_ed.getPaddingEnd()) {
                            if(clickcount % 2 ==0)
                            {
                                clickcount=clickcount+1;
                                pass_ed.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                                pass_ed.setCompoundDrawablesWithIntrinsicBounds(R.drawable.view_icon, 0, 0, 0);
                                return true;
                            }
                            else
                            {
                                clickcount=clickcount+1;
                                pass_ed.setTransformationMethod(PasswordTransformationMethod.getInstance());
                                pass_ed.setCompoundDrawablesWithIntrinsicBounds(R.drawable.un_view_icon, 0, 0, 0);
                                return true;


                            }

                        }
                    }
                    else
                    {

                        if (event.getRawX() >= (pass_ed.getRight() - pass_ed.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                            if(clickcount % 2 ==0)
                            {
                                clickcount=clickcount+1;
                                pass_ed.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                                pass_ed.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.view_icon, 0);
                                return true;
                            }
                            else
                            {
                                clickcount=clickcount+1;
                                pass_ed.setTransformationMethod(PasswordTransformationMethod.getInstance());
                                pass_ed.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.un_view_icon, 0);
                                return true;


                            }

                        }

                    }

                }


                return false;
            }
        });

    }



    private void init() {
        signuptext.setOnClickListener(this::OnClick);
        forgetPasswordText.setOnClickListener(this::OnClick);
        loginBtn.setOnClickListener(this::OnClick);
        dailogHelper=new ProgressDailogHelper(this,"");
        constraintLayout.getViewTreeObserver().addOnGlobalLayoutListener(this);
        validation=new Validation(this);
        langCode=SharedPreferenceWriter.getInstance(this).getString(GlobalVariables.langCode);
    }

    @OnClick()
    void OnClick(View view) {
        switch (view.getId()) {
            case R.id.signuptext:
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.forgetPasswordText:
                forgetPasswordDailog();
                break;

            case R.id.submitbtn:
                break;

            case R.id.loginBtn:
                if (checkValidation()) {
                loginApi();
             }

        break;


    }
    }

    private void forgetPasswordDailog() {

        final Dialog dialog=new Dialog(LoginActivity.this,android.R.style.Theme_Black);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.activity_forget_popup);
        EditText phone_ed=dialog.findViewById(R.id.phone_ed);
        TextView countrycode_txt=dialog.findViewById(R.id.countrycode_txt);
        LinearLayout close_ll=dialog.findViewById(R.id.close_ll);
        CountryCodePicker ccode=dialog.findViewById(R.id.ccode);
        Button submitbtn =dialog.findViewById(R.id.submitbtn);
        ccode.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                countrycode_txt.setText("+"+ccode.getSelectedCountryCodeWithPlus());
                countrycode=countrycode_txt.getText().toString();
            }
        });






        close_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validation.isPhoneNumber(phone_ed,true))
                {
                    dailogHelper.showDailog();
                    CheckMobileNumber number=new CheckMobileNumber();
                    number.setMobileNumber(phone_ed.getText().toString().trim());
                    if(countrycode.equalsIgnoreCase(""))
                    {
                        number.setCountryCode("+91");
                    }
                    else
                    {
                        number.setCountryCode(countrycode);
                    }

                    number.setType("Forgot");
                    number.setLangCode("en");

                    RetroInterface api_service=RetrofitInit.getConnect().createConnection();
                    Call<CheckMobileNumber> call=api_service.checkForecasterMobileNumber(number,SharedPreferenceWriter.getInstance(LoginActivity.this).getString(GlobalVariables.jwtToken));
                    call.enqueue(new Callback<CheckMobileNumber>() {
                        @Override
                        public void onResponse(Call<CheckMobileNumber> call, Response<CheckMobileNumber> response) {
                            if(response.isSuccessful())
                            {
                                CheckMobileNumber server_response=response.body();
                                if(server_response.getStatus().equalsIgnoreCase("SUCCESS"))
                                {
                                    dialog.dismiss();
                                    sendVerificationCode(phone_ed.getText().toString().trim());
                                    setPreferencesForgetPassword(server_response);

                                    dailogHelper.dismissDailog();
                                    OtpDailogPop();


                                }else if(server_response.getStatus().equalsIgnoreCase("FAILURE"))
                                {
                                    if(server_response.getResponseMessage().equalsIgnoreCase(GlobalVariables.invalidoken))
                                    {
                                        Toast.makeText(LoginActivity.this,getString(R.string.other_device_logged_in),Toast.LENGTH_LONG).show();
                                        finish();
                                        startActivity(new Intent(LoginActivity.this,LoginActivity.class));
                                        SharedPreferenceWriter.getInstance(LoginActivity.this).clearPreferenceValues();
                                        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                                if (!task.isSuccessful()) {
                                                    // Log.w(TAG, "getInstanceId failed", task.getException());
                                                    return;
                                                }

                                                String auth_token = task.getResult().getToken();
                                                Log.w("firebaese","token: "+auth_token);
                                                SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(GlobalVariables.deviceToken,auth_token);
                                            }
                                        });
                                    }
                                    else {

                                        dailogHelper.dismissDailog();
                                        if (server_response.getResponseMessage().equalsIgnoreCase("Mobile number is not registered")) {
                                            phone_ed.setError(server_response.getResponseMessage());
                                            phone_ed.requestFocus();
                                            phone_ed.setFocusable(true);

                                        }
                                    }


                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<CheckMobileNumber> call, Throwable t) {

                        }
                    });
                }
            }
        });
        dialog.show();

    }

    private void setPreferencesForgetPassword(CheckMobileNumber server_response) {
        SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(GlobalVariables.profilePic,server_response.getData().getProfilePic());
        SharedPreferenceWriter.getInstance(LoginActivity.this).writeBooleanValue(GlobalVariables.notificationStatus,server_response.getData().getNotificationStatus());
        SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(GlobalVariables.language,server_response.getData().getLanguage());
        SharedPreferenceWriter.getInstance(LoginActivity.this).writeBooleanValue(GlobalVariables.profileSetup,server_response.getData().getProfileSetup());
        SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(GlobalVariables.totalRating, String.valueOf(server_response.getData().getTotalRating()));
        SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(GlobalVariables.avgRating, String.valueOf(server_response.getData().getAvgRating()));
        SharedPreferenceWriter.getInstance(LoginActivity.this).writeBooleanValue(GlobalVariables.onlineStatus,server_response.getData().getOnlineStatus());
        SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(GlobalVariables.responseTime,server_response.getData().getResponseTime());
        SharedPreferenceWriter.getInstance(LoginActivity.this).writeIntValue(GlobalVariables.pendingQueue,server_response.getData().getPendingQueue());
        SharedPreferenceWriter.getInstance(LoginActivity.this).writeBooleanValue(GlobalVariables.profileComplete,server_response.getData().getProfileComplete());
        SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(GlobalVariables.status,server_response.getData().getStatus());
        SharedPreferenceWriter.getInstance(LoginActivity.this).writeIntValue(GlobalVariables.bookingCount,server_response.getData().getBookingCount());
        SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(GlobalVariables._id,server_response.getData().getId());
        SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(GlobalVariables.name,server_response.getData().getName());
        SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(GlobalVariables.username,server_response.getData().getUsername());
        SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(GlobalVariables.email,server_response.getData().getEmail());
        SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(GlobalVariables.countryCode,server_response.getData().getCountryCode());
        SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(GlobalVariables.mobileNumber,server_response.getData().getMobileNumber());
        SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(GlobalVariables.createdAt,server_response.getData().getCreatedAt());
        SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(GlobalVariables.updatedAt,server_response.getData().getUpdatedAt());
        SharedPreferenceWriter.getInstance(LoginActivity.this).writeIntValue(GlobalVariables.__v,server_response.getData().getV());
        SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(GlobalVariables.jwtToken,server_response.getData().getJwtToken());
        SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(GlobalVariables.deviceToken,server_response.getData().getDeviceToken());
        SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(GlobalVariables.device_type,server_response.getData().getDeviceType());
    }

    private void sendVerificationCode(String phone_number) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber("+91"+phone_number, 60, TimeUnit.SECONDS, LoginActivity.this, mCallback);

    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(PhoneAuthCredential credential) {
            Log.d("Completed", "onVerificationCompleted:" + credential);
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Log.w("Failed", "onVerificationFailed", e);
            if (e instanceof FirebaseAuthInvalidCredentialsException) {
            } else if (e instanceof FirebaseTooManyRequestsException) {

            }

        }

        @Override
        public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {
            Log.d("Code Sent", "onCodeSent:" + verificationId);
            verificationCode = verificationId;
            //Toast.makeText(LoginActivity.this,"Otp send successfully",Toast.LENGTH_LONG).show();
        }
    };



    private void OtpDailogPop() {

        final Dialog dialog=new Dialog(LoginActivity.this,android.R.style.Theme_Black);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.activity_otp_verify);
        LinearLayout close_ll=dialog.findViewById(R.id.close_ll);
        first_ed=dialog.findViewById(R.id.first_ed);
        secound_ed=dialog.findViewById(R.id.secound_ed);
        third_ed=dialog.findViewById(R.id.third_ed);
        fourth_ed=dialog.findViewById(R.id.fourth_ed);
        fifth_ed=dialog.findViewById(R.id.fifth_ed);
        sixth_ed=dialog.findViewById(R.id.sixth_ed);
        addTextChangeListner();

        close_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Button verifyBtn=dialog.findViewById(R.id.verifyBtn);
        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             if(checkValidation2())
            {
              dailogHelper.showDailog();
//              dialog.dismiss();
              verifyVerificationCode(first_ed.getText().toString().trim()+secound_ed.getText().toString().trim()+third_ed.getText().toString().trim()+fourth_ed.getText().toString().trim()+fifth_ed.getText().toString().trim()+sixth_ed.getText().toString().trim(),dialog);

            }
             else
             {
                 Toast toast=Toast.makeText(LoginActivity.this, getString(R.string.please_enter_otp), Toast.LENGTH_SHORT);
                 toast.setGravity(Gravity.CENTER,0,0);
                 toast.show();
             }

            }
        });

        dialog.show();
    }

    private void verifyVerificationCode(String otp, Dialog dialog) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, otp);
        signInWithPhoneAuthCredential(credential,dialog);

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential,Dialog dialog) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            dialog.dismiss();
                            Log.e("here it is:","yes");
                            dailogHelper.dismissDailog();
                            changePasswordPop();
                        }
                        else {

                            first_ed.setText("");
                            sixth_ed.clearFocus();
                            first_ed.requestFocus();
                            first_ed.setBackground(getDrawable(R.drawable.edit_text_otp_background2));
                            secound_ed.setText("");
                            secound_ed.setBackground(getDrawable(R.drawable.edit_text_otp_background2));
                            third_ed.setText("");
                            third_ed.setBackground(getDrawable(R.drawable.edit_text_otp_background2));
                            fourth_ed.setText("");
                            fourth_ed.setBackground(getDrawable(R.drawable.edit_text_otp_background2));
                            fifth_ed.setText("");
                            fifth_ed.setBackground(getDrawable(R.drawable.edit_text_otp_background2));
                            sixth_ed.setText("");
                            sixth_ed.setBackground(getDrawable(R.drawable.edit_text_otp_background2));
                            dailogHelper.dismissDailog();
                            String message = "Somthing is wrong, we will fix it soon...";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid otp entered...";
                                Toast.makeText(LoginActivity.this,message,Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });

    }

    private void changePasswordPop() {
        final Dialog dialog=new Dialog(LoginActivity.this,android.R.style.Theme_Black);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.change_password_popup);
        EditText oldpass_ed=dialog.findViewById(R.id.oldpass_ed);
        newpass_ed=dialog.findViewById(R.id.newpass_ed);
        confirm_pass_ed=dialog.findViewById(R.id.confirm_pass_ed);
        TouchListner();

        Button submitBtn=dialog.findViewById(R.id.submitBtn);

        oldpass_ed.setVisibility(View.GONE);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkValidationPassword())
                {
                    dailogHelper.showDailog();
                    ForgotPassword password=new ForgotPassword();
                    password.setForecasterId(SharedPreferenceWriter.getInstance(LoginActivity.this).getString(GlobalVariables._id));
                    password.setPassword(newpass_ed.getText().toString().trim());
                    password.setLangCode("en");
                    RetroInterface api_service=RetrofitInit.getConnect().createConnection();
                    Call<ForgotPassword> call=api_service.forecasterResetPassword(password);
                    call.enqueue(new Callback<ForgotPassword>() {
                        @Override
                        public void onResponse(Call<ForgotPassword> call, Response<ForgotPassword> response) {
                            if(response.isSuccessful())
                            {
                                ForgotPassword server_response=response.body();
                                if(server_response.getStatus().equalsIgnoreCase("SUCCESS"))
                                {
                                    dailogHelper.dismissDailog();
                                    dialog.dismiss();
                                    //setPreferencesForgetPassword(server_response);
                                    Toast.makeText(LoginActivity.this,server_response.getResponseMessage(),Toast.LENGTH_LONG).show();

                                }
                                else if(server_response.getStatus().equalsIgnoreCase("FAILURE")) {
                                    if (server_response.getResponseMessage().equalsIgnoreCase(GlobalVariables.invalidoken)) {
                                        Toast.makeText(LoginActivity.this, getString(R.string.other_device_logged_in), Toast.LENGTH_LONG).show();
                                        finish();
                                        startActivity(new Intent(LoginActivity.this, LoginActivity.class));
                                        SharedPreferenceWriter.getInstance(LoginActivity.this).clearPreferenceValues();
                                        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                                if (!task.isSuccessful()) {
                                                    // Log.w(TAG, "getInstanceId failed", task.getException());
                                                    return;
                                                }

                                                String auth_token = task.getResult().getToken();
                                                Log.w("firebaese", "token: " + auth_token);
                                                SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(GlobalVariables.deviceToken, auth_token);
                                            }
                                        });
                                    } else {
                                        dailogHelper.dismissDailog();
                                        dialog.dismiss();
                                        Toast.makeText(LoginActivity.this, server_response.getResponseMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }



                            }
                        }

                        @Override
                        public void onFailure(Call<ForgotPassword> call, Throwable t) {

                        }
                    });
                }
            }
        });




        dialog.show();



    }

    private void TouchListner() {
        newpass_ed.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;


                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(langCode.equalsIgnoreCase("ar"))
                    {
                        if (event.getRawX()  <= (newpass_ed.getCompoundDrawables()[DRAWABLE_LEFT].getBounds().width())+newpass_ed.getPaddingLeft()+newpass_ed.getPaddingRight()+newpass_ed.getPaddingBottom()+newpass_ed.getPaddingTop()+newpass_ed.getPaddingEnd()) {
                            if(clickcount3 % 2 ==0)
                            {
                                clickcount3=clickcount3+1;
                                newpass_ed.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                                newpass_ed.setCompoundDrawablesWithIntrinsicBounds(R.drawable.view_icon, 0, 0, 0);
                                return true;
                            }
                            else
                            {
                                clickcount3=clickcount3+1;
                                newpass_ed.setTransformationMethod(PasswordTransformationMethod.getInstance());
                                newpass_ed.setCompoundDrawablesWithIntrinsicBounds(R.drawable.un_view_icon, 0, 0, 0);
                                return true;


                            }

                        }
                    }

                    else
                    {
                        if (event.getRawX() >= (newpass_ed.getRight() - newpass_ed.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                            if(clickcount3 % 2 ==0)
                            {
                                clickcount3=clickcount3+1;
                                newpass_ed.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                                newpass_ed.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.view_icon, 0);
                                return true;
                            }
                            else
                            {
                                clickcount3=clickcount3+1;
                                newpass_ed.setTransformationMethod(PasswordTransformationMethod.getInstance());
                                newpass_ed.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.un_view_icon, 0);
                                return true;


                            }

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
                    if(langCode.equalsIgnoreCase("ar"))
                    {
                        if (event.getRawX()  <= (newpass_ed.getCompoundDrawables()[DRAWABLE_LEFT].getBounds().width())+newpass_ed.getPaddingLeft()+newpass_ed.getPaddingRight()+newpass_ed.getPaddingBottom()+newpass_ed.getPaddingTop()+newpass_ed.getPaddingEnd()) {
                            if(clickcount2 % 2 ==0)
                            {
                                clickcount2=clickcount2+1;
                                confirm_pass_ed.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                                confirm_pass_ed.setCompoundDrawablesWithIntrinsicBounds(R.drawable.view_icon,0,0, 0);
                                return true;
                            }
                            else
                            {
                                clickcount2=clickcount2+1;
                                confirm_pass_ed.setTransformationMethod(PasswordTransformationMethod.getInstance());
                                confirm_pass_ed.setCompoundDrawablesWithIntrinsicBounds( R.drawable.un_view_icon,0,0, 0);
                                return true;


                            }



                        }
                    }
                    else
                    {
                        if (event.getRawX() >= (confirm_pass_ed.getRight() - confirm_pass_ed.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                            if(clickcount2 % 2 ==0)
                            {
                                clickcount2=clickcount2+1;
                                confirm_pass_ed.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                                confirm_pass_ed.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.view_icon, 0);
                                return true;
                            }
                            else
                            {
                                clickcount2=clickcount2+1;
                                confirm_pass_ed.setTransformationMethod(PasswordTransformationMethod.getInstance());
                                confirm_pass_ed.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.un_view_icon, 0);
                                return true;


                            }

                        }
                    }


                }


                return false;
            }
        });

    }

    private boolean checkValidationPassword() {
        boolean ret=true;
        Validation validation=new Validation(this);
        if(!validation.hasText(newpass_ed,getResources().getString(R.string.enter_new_pass))
        || !validation.hasText(confirm_pass_ed,getResources().getString(R.string.enter_confirm_pass))
        || !confirm_pass_ed.getText().toString().equalsIgnoreCase(newpass_ed.getText().toString().trim())
        )
        {
            if(!validation.hasText(newpass_ed,getResources().getString(R.string.enter_new_pass)))
            {
                ret=false;
                newpass_ed.requestFocus();
            }
            else if(!validation.hasText(confirm_pass_ed,getResources().getString(R.string.enter_confirm_pass)))
            {
                ret=false;
                confirm_pass_ed.requestFocus();
            }
            else if(!confirm_pass_ed.getText().toString().equalsIgnoreCase(newpass_ed.getText().toString().trim()))
            {
                ret=false;
                confirm_pass_ed.setError(getResources().getString(R.string.pass_not_match));
                confirm_pass_ed.requestFocus();
            }

        }

        return ret;
    }

    private void addTextChangeListner() {
        first_ed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s!=null && s.length()==1) {
                    first_ed.setBackground(getDrawable(R.drawable.edit_text_otp_background));
                    secound_ed.requestFocus();

                }
                else
                {
                    first_ed.setBackground(getDrawable(R.drawable.edit_text_otp_background2));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        secound_ed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s!=null && s.length()==1) {
                    secound_ed.setBackground(getDrawable(R.drawable.edit_text_otp_background));
                    third_ed.requestFocus();
                }
                else
                {
                    secound_ed.setBackground(getDrawable(R.drawable.edit_text_otp_background2));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        third_ed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s!=null && s.length()==1) {
                    third_ed.setBackground(getDrawable(R.drawable.edit_text_otp_background));
                    fourth_ed.requestFocus();
                }
                else
                {
                    third_ed.setBackground(getDrawable(R.drawable.edit_text_otp_background2));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        fourth_ed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s!=null && s.length()==1) {
                    fourth_ed.setBackground(getDrawable(R.drawable.edit_text_otp_background));
                    fifth_ed.requestFocus();
                }
                else
                {
                    fourth_ed.setBackground(getDrawable(R.drawable.edit_text_otp_background2));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        fifth_ed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s!=null) {
                    fifth_ed.setBackground(getDrawable(R.drawable.edit_text_otp_background));
                    sixth_ed.requestFocus();
                }
                else
                {
                    fifth_ed.setBackground(getDrawable(R.drawable.edit_text_otp_background2));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        sixth_ed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s!=null) {
                    sixth_ed.setBackground(getDrawable(R.drawable.edit_text_otp_background));
                }
                else
                {
                    sixth_ed.setBackground(getDrawable(R.drawable.edit_text_otp_background2));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private boolean checkValidation2() {
        boolean ret=true;
        if(!Validation.hasText(first_ed,true)) ret=false;
        if(!Validation.hasText(secound_ed,true)) ret=false;
        if(!Validation.hasText(third_ed,true)) ret=false;
        if(!Validation.hasText(fourth_ed,true)) ret=false;
        if(!Validation.hasText(fifth_ed,true)) ret=false;
        if(!Validation.hasText(sixth_ed,true)) ret=false;

        return ret;
    }

    private void loginApi() {
         dailogHelper.showDailog();
         RetroInterface api_service=RetrofitInit.getConnect().createConnection();
         Login login=new Login();
         login.setUsername(username_ed.getText().toString().trim());
         login.setPassword(pass_ed.getText().toString().trim());
         login.setDeviceType(GlobalVariables.device_type);
         login.setLangCode(GlobalVariables.arabicCode);
         login.setDeviceToken(SharedPreferenceWriter.getInstance(LoginActivity.this).getString(GlobalVariables.deviceToken));
         Call<Login> call=api_service.forecasterLogin(login);
         call.enqueue(new Callback<Login>() {
             @Override
             public void onResponse(Call<Login> call, Response<Login> response) {
                 if(response.isSuccessful())
                 {
                     Login server_response=response.body();
                     if(server_response.getStatus().equalsIgnoreCase("SUCCESS")) {
                         dailogHelper.dismissDailog();
                         Locale locale=null;

                         if(response.body().getData().getLanguage().equalsIgnoreCase(GlobalVariables.arabic))
                         {
                             locale = new Locale("ar");

                         }else if(response.body().getData().getLanguage().equalsIgnoreCase(GlobalVariables.english))
                         {
                             locale = new Locale("en");
                         }
                         else if(response.body().getData().getLanguage().equalsIgnoreCase(GlobalVariables.urdu))
                         {
                             locale = new Locale("ur");

                         }

                         Locale.setDefault(locale);
                         Configuration config = new Configuration();
                         config.locale = locale;
                         getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
                         SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(GlobalVariables.langCode, String.valueOf(locale));




                         setPreferences(server_response);
                         Intent intent=new Intent(LoginActivity.this,CategorySelectionActivity.class);
                         finish();
                         startActivity(intent);




                     } else if(server_response.getStatus().equalsIgnoreCase("FAILURE"))
                     {

                         if(server_response.getResponseMessage().equalsIgnoreCase("Please complete your profile first"))
                         {
                             Intent intent=new Intent(LoginActivity.this,ProfileSetupActivity.class);
                             SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(GlobalVariables.forcaster_name,server_response.getData().getName());
                             SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(GlobalVariables._id,server_response.getData().getId());
                             finish();
                             startActivity(intent);
                         }
                         else if (server_response.getResponseMessage().equalsIgnoreCase(GlobalVariables.invalidoken)) {
                             Toast.makeText(LoginActivity.this, getString(R.string.other_device_logged_in), Toast.LENGTH_LONG).show();
                             finish();
                             startActivity(new Intent(LoginActivity.this, LoginActivity.class));
                             SharedPreferenceWriter.getInstance(LoginActivity.this).clearPreferenceValues();
                             FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                 @Override
                                 public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                     if (!task.isSuccessful()) {
                                         // Log.w(TAG, "getInstanceId failed", task.getException());
                                         return;
                                     }

                                     String auth_token = task.getResult().getToken();
                                     Log.w("firebaese", "token: " + auth_token);
                                     SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(GlobalVariables.deviceToken, auth_token);
                                 }
                             });
                         }
                         else {
                             dailogHelper.dismissDailog();
                             Toast.makeText(LoginActivity.this, server_response.getResponseMessage(), Toast.LENGTH_LONG).show();
                         }
                     }

                 }
             }

             @Override
             public void onFailure(Call<Login> call, Throwable t) {
                 dailogHelper.dismissDailog();
                 Toast.makeText(LoginActivity.this,t.getMessage(),Toast.LENGTH_LONG).show();

             }
         });




    }

    private void setPreferences(Login server_response) {
        SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(GlobalVariables.profilePic,server_response.getData().getProfilePic());
        SharedPreferenceWriter.getInstance(LoginActivity.this).writeBooleanValue(GlobalVariables.notificationStatus,server_response.getData().getNotificationStatus());
        SharedPreferenceWriter.getInstance(LoginActivity.this).writeBooleanValue(GlobalVariables.profileSetup,server_response.getData().getProfileSetup());
        SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(GlobalVariables.totalRating, String.valueOf(server_response.getData().getTotalRating()));
        SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(GlobalVariables.avgRating, String.valueOf(server_response.getData().getAvgRating()));
        SharedPreferenceWriter.getInstance(LoginActivity.this).writeBooleanValue(GlobalVariables.onlineStatus,server_response.getData().getOnlineStatus());
        SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(GlobalVariables.responseTime,server_response.getData().getResponseTime());
        SharedPreferenceWriter.getInstance(LoginActivity.this).writeIntValue(GlobalVariables.pendingQueue,server_response.getData().getPendingQueue());
        SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(GlobalVariables.language,server_response.getData().getLanguage());
        SharedPreferenceWriter.getInstance(LoginActivity.this).writeBooleanValue(GlobalVariables.profileComplete,server_response.getData().getProfileComplete());
        SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(GlobalVariables._id,server_response.getData().getId());
        SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(GlobalVariables.name,server_response.getData().getName());
        SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(GlobalVariables.username,server_response.getData().getUsername());
        SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(GlobalVariables.email,server_response.getData().getEmail());
        SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(GlobalVariables.countryCode,server_response.getData().getCountryCode());
        SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(GlobalVariables.mobileNumber,server_response.getData().getMobileNumber());
        SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(GlobalVariables.createdAt,server_response.getData().getCreatedAt());
        SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(GlobalVariables.updatedAt,server_response.getData().getUpdatedAt());
        SharedPreferenceWriter.getInstance(LoginActivity.this).writeIntValue(GlobalVariables.__v,server_response.getData().getV());
        SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(GlobalVariables.jwtToken,server_response.getData().getJwtToken());
        SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(GlobalVariables.accountHolderName,server_response.getData().getAccountHolderName());
        SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(GlobalVariables.accountNumber,server_response.getData().getAccountNumber());
        SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(GlobalVariables.bankName,server_response.getData().getBankName());
        SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(GlobalVariables.categoryName,server_response.getData().getCategoryName());
        SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(GlobalVariables.dob,server_response.getData().getDob());
        SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(GlobalVariables.gender,server_response.getData().getGender());
        SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(GlobalVariables.pricePerQues,server_response.getData().getPricePerQues());
        SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(GlobalVariables.uploadedVideo,server_response.getData().getUploadedVideo());






    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    private boolean checkValidation() {
        boolean ret=true;
        Validation validation=new Validation(this);
        if(!validation.hasText(username_ed,this.getResources().getString(R.string.enter_user_name))
        || !validation.hasText(pass_ed,getResources().getString(R.string.enter_password)))
        {
            if(!validation.hasText(username_ed,this.getResources().getString(R.string.enter_user_name)))
            {
                ret=false;
                username_ed.requestFocus();

            }else if(!validation.hasText(pass_ed,getResources().getString(R.string.enter_password)))
            {
                ret=false;
                pass_ed.requestFocus();

            }
        }

        return ret;
    }

    @Override
    public void onGlobalLayout() {
        constraintLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        height=constraintLayout.getHeight();
        width=constraintLayout.getWidth();


    }
}
