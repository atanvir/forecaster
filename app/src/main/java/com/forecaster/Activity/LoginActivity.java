package com.forecaster.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

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
import com.heetch.countrypicker.Country;
import com.heetch.countrypicker.CountryPickerCallbacks;
import com.heetch.countrypicker.CountryPickerDialog;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.signuptext) TextView signuptext;
    @BindView(R.id.forgetPasswordText) TextView forgetPasswordText;
    @BindView(R.id.loginBtn) Button loginBtn;
    @BindView(R.id.username_ed) EditText username_ed;
    @BindView(R.id.pass_ed) EditText pass_ed;
    ProgressDialog dailog;
    EditText first_ed,secound_ed,third_ed,fourth_ed,fifth_ed,sixth_ed;
    EditText newpass_ed,confirm_pass_ed;
    private String verificationCode;
    FirebaseAuth auth;
    int clickcount=0;
    String countrycode="";
    private ProgressDailogHelper dailogHelper;
    int clickcount3=0,clickcount2=0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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


                return false;
            }
        });

    }



    private void init() {
        signuptext.setOnClickListener(this::OnClick);
        forgetPasswordText.setOnClickListener(this::OnClick);
        loginBtn.setOnClickListener(this::OnClick);
        dailogHelper=new ProgressDailogHelper(this,"");
    }

    @OnClick()
    void OnClick(View view)
    {
        switch (view.getId())
        {
            case R.id.signuptext:
                Intent intent=new Intent(LoginActivity.this,SignupActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.forgetPasswordText:
                forgetPasswordDailog();
                break;

            case R.id.submitbtn:
                break;

            case R.id.loginBtn:
                if(checkValidation())
                {
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
        Button submitbtn =dialog.findViewById(R.id.submitbtn);

        countrycode_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CountryPickerDialog countryPicker = new CountryPickerDialog(LoginActivity.this, new CountryPickerCallbacks() {
                    @Override
                    public void onCountrySelected(Country country, int flagResId) {
                        //country.toString();
                        countrycode_txt.setText("+"+country.getDialingCode());
                        countrycode=countrycode_txt.getText().toString();


                        // TODO handle callback
                    }
                });
                countryPicker.show();

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
                if(Validation.isPhoneNumber(phone_ed,true))
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
                                    dailogHelper.dismissDailog();
                                    if(server_response.getResponseMessage().equalsIgnoreCase("Mobile number is not registered"))
                                    {
                                        phone_ed.setError(server_response.getResponseMessage());
                                        phone_ed.requestFocus();
                                        phone_ed.setFocusable(true);

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
            Toast.makeText(LoginActivity.this,"Otp send successfully",Toast.LENGTH_LONG).show();
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
              dialog.dismiss();
              verifyVerificationCode(first_ed.getText().toString().trim()+secound_ed.getText().toString().trim()+third_ed.getText().toString().trim()+fourth_ed.getText().toString().trim()+fifth_ed.getText().toString().trim()+sixth_ed.getText().toString().trim());

            }

            }
        });

        dialog.show();
    }

    private void verifyVerificationCode(String otp) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, otp);
        signInWithPhoneAuthCredential(credential);

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.e("here it is:","yes");
                            dailogHelper.dismissDailog();
                            changePasswordPop();
                        }
                        else {
                            dailogHelper.dismissDailog();
                            String message = "Somthing is wrong, we will fix it soon...";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
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
                                else if(server_response.getStatus().equalsIgnoreCase("FAILURE"))
                                {
                                    dailogHelper.dismissDailog();
                                    dialog.dismiss();
                                    Toast.makeText(LoginActivity.this,server_response.getResponseMessage(),Toast.LENGTH_LONG).show();
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


                return false;
            }
        });

    }

    private boolean checkValidationPassword() {
        boolean ret=true;

        if(!Validation.hasText(newpass_ed)) ret=false;
        if(!Validation.hasText(confirm_pass_ed) && !confirm_pass_ed.getText().toString().equalsIgnoreCase(newpass_ed.getText().toString().trim()))
        {
            if(!Validation.hasText(confirm_pass_ed))
            {
                ret=false;



            }else if(!confirm_pass_ed.getText().toString().equalsIgnoreCase(newpass_ed.getText().toString().trim()))
            {
                ret=false;
                confirm_pass_ed.setError("Confirm password does not match");
                confirm_pass_ed.setFocusable(true);
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
        if(!Validation.hasText(first_ed)) ret=false;
        if(!Validation.hasText(secound_ed)) ret=false;
        if(!Validation.hasText(third_ed)) ret=false;
        if(!Validation.hasText(fourth_ed)) ret=false;
        if(!Validation.hasText(fifth_ed)) ret=false;
        if(!Validation.hasText(sixth_ed)) ret=false;

        return ret;
    }

    private void loginApi() {
         dailogHelper.showDailog();
         RetroInterface api_service=RetrofitInit.getConnect().createConnection();
         Login login=new Login();
         login.setUsername(username_ed.getText().toString().trim());
         login.setPassword(pass_ed.getText().toString().trim());
         login.setDeviceType(GlobalVariables.device_type);
         login.setLangCode("en");
         login.setDeviceToken(SharedPreferenceWriter.getInstance(LoginActivity.this).getString(GlobalVariables.deviceToken));
         Call<Login> call=api_service.forecasterLogin(login);
         call.enqueue(new Callback<Login>() {
             @Override
             public void onResponse(Call<Login> call, Response<Login> response) {
                 if(response.isSuccessful())
                 {
                     Login server_response=response.body();
                     if(server_response.getStatus().equalsIgnoreCase("SUCCESS"))
                     {
                         dailogHelper.dismissDailog();
                         Toast.makeText(LoginActivity.this,server_response.getResponseMessage(),Toast.LENGTH_LONG).show();
                         setPreferences(server_response);
                         Intent intent=new Intent(LoginActivity.this,CategorySelectionActivity.class);
                         finish();
                         startActivity(intent);



                     }else if(server_response.getStatus().equalsIgnoreCase("FAILURE"))
                     {
                         if(server_response.getResponseMessage().equalsIgnoreCase("Please complete your profile first"))
                         {
                             Intent intent=new Intent(LoginActivity.this,ProfileSetupActivity.class);
                             SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(GlobalVariables.forcaster_name,server_response.getData().getName());
                             SharedPreferenceWriter.getInstance(LoginActivity.this).writeStringValue(GlobalVariables._id,server_response.getData().getId());
                             finish();
                             startActivity(intent);
                         }
                         dailogHelper.dismissDailog();
                         Toast.makeText(LoginActivity.this,server_response.getResponseMessage(),Toast.LENGTH_LONG).show();

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

        if(!Validation.hasText(username_ed)) ret=false;
        if(!Validation.hasText(pass_ed)) ret=false;




        return ret;
    }
}
