package com.Bforecaster.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
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
import android.view.Gravity;
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

import com.Bforecaster.Modal.Signup;
import com.Bforecaster.R;
import com.Bforecaster.Retrofit.RetroInterface;
import com.Bforecaster.Retrofit.RetrofitInit;
import com.Bforecaster.Utility.GlobalVariables;
import com.Bforecaster.Utility.ProgressDailogHelper;
import com.Bforecaster.Utility.SharedPreferenceWriter;
import com.Bforecaster.Utility.Validation;
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

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity implements CountryCodePicker.OnCountryChangeListener {
    @BindView(R.id.back_ll) LinearLayout back_ll;
    @BindView(R.id.signuptext) TextView signuptext;
    @BindView(R.id.check_im) ImageView check_im;
    @BindView(R.id.full_name_ed) EditText full_name_ed;
    @BindView(R.id.username_ed) EditText username_ed;
    @BindView(R.id.email_ed) EditText email_ed;
    @BindView(R.id.phone_ed) EditText phone_ed;
    @BindView(R.id.pass_ed) EditText pass_ed;
    @BindView(R.id.conpass_ed) EditText conpass_ed;
    @BindView(R.id.countrycode_txt) TextView countrycode_txt;
    @BindView(R.id.ccode)
    CountryCodePicker ccode;
    EditText first_ed,secound_ed,third_ed,fourth_ed,fifth_ed,sixth_ed;
    long clickcount11=0;
    int clickcount=0;
    private String verificationCode;
    FirebaseAuth auth;
    String countrycode="";
    private ProgressDailogHelper dailogHelper;
    String langCode="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
        init();
        auth=FirebaseAuth.getInstance();
        settingPasswordVisibility();
        confirmPasswordVisibility();


    }

    private void confirmPasswordVisibility() {
        conpass_ed.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;


                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(langCode.equalsIgnoreCase("ar"))
                    {
                        if (event.getRawX()  <= (pass_ed.getCompoundDrawables()[DRAWABLE_LEFT].getBounds().width())+conpass_ed.getPaddingLeft()+conpass_ed.getPaddingRight()+conpass_ed.getPaddingBottom()+conpass_ed.getPaddingTop()+conpass_ed.getPaddingEnd()) {
                            if(clickcount % 2 ==0)
                            {
                                clickcount=clickcount+1;
                                conpass_ed.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                                conpass_ed.setCompoundDrawablesWithIntrinsicBounds(R.drawable.view_icon, 0, 0, 0);
                                return true;
                            }
                            else
                            {
                                clickcount=clickcount+1;
                                conpass_ed.setTransformationMethod(PasswordTransformationMethod.getInstance());
                                conpass_ed.setCompoundDrawablesWithIntrinsicBounds(R.drawable.un_view_icon, 0, 0, 0);
                                return true;


                            }

                        }

                    }
                    else
                    {
                        if (event.getRawX() >= (conpass_ed.getRight() - conpass_ed.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                            if(clickcount % 2 ==0)
                            {
                                clickcount=clickcount+1;
                                conpass_ed.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                                conpass_ed.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.view_icon, 0);
                                return true;
                            }
                            else
                            {
                                clickcount=clickcount+1;
                                conpass_ed.setTransformationMethod(PasswordTransformationMethod.getInstance());
                                conpass_ed.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.un_view_icon, 0);
                                return true;


                            }

                        }
                    }


                }


                return false;
            }
        });
    }

    private void settingPasswordVisibility() {
        pass_ed.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;


                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if (langCode.equalsIgnoreCase("ar")) {
                        if (event.getRawX() <= (pass_ed.getCompoundDrawables()[DRAWABLE_LEFT].getBounds().width()) + pass_ed.getPaddingLeft() + pass_ed.getPaddingRight() + pass_ed.getPaddingBottom() + pass_ed.getPaddingTop() + pass_ed.getPaddingEnd()) {
                            if (clickcount % 2 == 0) {
                                clickcount = clickcount + 1;
                                pass_ed.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                                pass_ed.setCompoundDrawablesWithIntrinsicBounds(R.drawable.view_icon, 0, 0, 0);
                                return true;
                            } else {
                                clickcount = clickcount + 1;
                                pass_ed.setTransformationMethod(PasswordTransformationMethod.getInstance());
                                pass_ed.setCompoundDrawablesWithIntrinsicBounds(R.drawable.un_view_icon, 0, 0, 0);
                                return true;


                            }

                        }
                    } else {
                        if (event.getRawX() >= (pass_ed.getRight() - pass_ed.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                            if (clickcount % 2 == 0) {
                                clickcount = clickcount + 1;
                                pass_ed.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                                pass_ed.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.view_icon, 0);
                                return true;
                            } else {
                                clickcount = clickcount + 1;
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
        back_ll.setOnClickListener(this::OnClick);
        signuptext.setOnClickListener(this::OnClick);
        check_im.setOnClickListener(this::OnClick);
        countrycode_txt.setOnClickListener(this::OnClick);
        dailogHelper=new ProgressDailogHelper(this,"");
        langCode=SharedPreferenceWriter.getInstance(SignupActivity.this).getString(GlobalVariables.langCode);
        ccode.setOnCountryChangeListener(this);

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @OnClick()
    void OnClick(View view)
    {
        switch (view.getId())
        {
            case R.id.back_ll:
                Intent intent=new Intent(SignupActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
                break;


            case R.id.signuptext:
                if(checkValidation()) {
                    SharedPreferenceWriter.getInstance(SignupActivity.this).writeStringValue(GlobalVariables.forcaster_name,full_name_ed.getText().toString().trim());
                    forcasterSignupApi();
                }


                break;
            case R.id.check_im:
                if(clickcount % 2 ==0)
                {
                    clickcount=clickcount+1;
                    check_im.setBackgroundResource(R.drawable.checked);

                }
                else
                {
                    clickcount=clickcount+1;
                    check_im.setBackgroundResource(R.drawable.unchecked);

                }


                break;

            case R.id.countrycode_txt:
                ccode.performClick();
//
//                CountryPickerDialog countryPicker = new CountryPickerDialog(SignupActivity.this, new CountryPickerCallbacks() {
//                    @Override
//                    public void onCountrySelected(Country country, int flagResId) {
//                        //country.toString();
//                        countrycode_txt.setText("+"+country.getDialingCode());
//                        countrycode=countrycode_txt.getText().toString();
//
//
//                        // TODO handle callback
//                    }
//                });
//                countryPicker.show();


                break;


        }
    }

    private void forcasterSignupApi() {
        dailogHelper.showDailog();
        RetroInterface api_service=RetrofitInit.getConnect().createConnection();
        Signup signup=new Signup();
        signup.setEmail(email_ed.getText().toString().trim());
        signup.setUsername(username_ed.getText().toString().trim());
        signup.setLangCode(SharedPreferenceWriter.getInstance(SignupActivity.this).getString(GlobalVariables.langCode));
        if(countrycode.equalsIgnoreCase("")) {
            signup.setCountryCode(ccode.getDefaultCountryCodeWithPlus());
        }
        else {
            signup.setCountryCode(countrycode);
        }
        signup.setMobileNumber(phone_ed.getText().toString().trim());
        Call<Signup> call=api_service.checkForecasterUsername(signup);
        call.enqueue(new Callback<Signup>() {
            @Override
            public void onResponse(Call<Signup> call, Response<Signup> response) {
                if(response.isSuccessful())
                {
                    Signup server_response=response.body();
                    if(server_response.getStatus().equalsIgnoreCase("SUCCESS"))
                    {
                        dailogHelper.dismissDailog();
                        sendVerificationCode(phone_ed.getText().toString().trim());
                        OtpDailogPopup();



                    }else if(server_response.getStatus().equalsIgnoreCase("FAILURE"))
                    {
                   if(server_response.getResponseMessage().equalsIgnoreCase(GlobalVariables.invalidoken))
                    {
                        Toast.makeText(SignupActivity.this,getString(R.string.other_device_logged_in),Toast.LENGTH_LONG).show();
                        finish();
                        startActivity(new Intent(SignupActivity.this,LoginActivity.class));
                        SharedPreferenceWriter.getInstance(SignupActivity.this).clearPreferenceValues();
                        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                            @Override
                            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                if (!task.isSuccessful()) {
                                    // Log.w(TAG, "getInstanceId failed", task.getException());
                                    return;
                                }

                                String auth_token = task.getResult().getToken();
                                Log.w("firebaese","token: "+auth_token);
                                SharedPreferenceWriter.getInstance(SignupActivity.this).writeStringValue(GlobalVariables.deviceToken,auth_token);
                            }
                        });
                    }
                    else {


                        dailogHelper.dismissDailog();
                        Toast.makeText(SignupActivity.this, server_response.getResponseMessage(), Toast.LENGTH_LONG).show();
                        if (server_response.getResponseMessage().equalsIgnoreCase("Username already exist")) {
                            username_ed.setError(getString(R.string.username_already_exists));
                            username_ed.setFocusable(true);
                            username_ed.requestFocus();
                        } else if (server_response.getResponseMessage().equalsIgnoreCase("Email already exist")) {
                            email_ed.setError(getString(R.string.email_already_exists));
                            email_ed.setFocusable(true);
                            email_ed.requestFocus();

                        } else if (server_response.getResponseMessage().equalsIgnoreCase("Mobile number already exist")) {
                            phone_ed.setError(getString(R.string.mobile_number_already_exists));
                            phone_ed.requestFocus();
                            phone_ed.requestFocus();
                        }
                    }



                    }

                }
            }

            @Override
            public void onFailure(Call<Signup> call, Throwable t) {

            }
        });



    }

    private void OtpDailogPopup() {
        final Dialog dialog1=new Dialog(SignupActivity.this,android.R.style.Theme_Black);
        dialog1.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog1.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog1.setContentView(R.layout.activity_otp_verify);
        LinearLayout close_ll=dialog1.findViewById(R.id.close_ll);
        first_ed=dialog1.findViewById(R.id.first_ed);
        secound_ed=dialog1.findViewById(R.id.secound_ed);
        third_ed=dialog1.findViewById(R.id.third_ed);
        fourth_ed=dialog1.findViewById(R.id.fourth_ed);
        fifth_ed=dialog1.findViewById(R.id.fifth_ed);
        sixth_ed=dialog1.findViewById(R.id.sixth_ed);
        Button verifyBtn=dialog1.findViewById(R.id.verifyBtn);
        close_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });




        addTextChangeListner(dialog1);

        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkValidation2())
                {
                    dailogHelper.showDailog();
                    verifyVerificationCode(first_ed.getText().toString()+secound_ed.getText().toString()+third_ed.getText().toString()+fourth_ed.getText().toString()+fifth_ed.getText().toString()+sixth_ed.getText().toString().trim());


                }
                else
                {
                    Toast toast=Toast.makeText(SignupActivity.this,getString(R.string.please_enter_otp),Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }
            }
        });


        dialog1.show();






    }

    private void addTextChangeListner(Dialog dialog1) {
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
                if(s!=null && s.length()==1) {
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


    private void sendVerificationCode(String phone_number) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber("+91"+phone_number, 60, TimeUnit.SECONDS, SignupActivity.this, mCallback);


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
            //Toast.makeText(SignupActivity.this,"Otp send successfully",Toast.LENGTH_LONG).show();
        }
    };



    private void verifyVerificationCode(String otp) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, otp);
        signInWithPhoneAuthCredential(credential);

    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //Log.e("aa gya","yes");
                            RetroInterface api_service=RetrofitInit.getConnect().createConnection();
                            Signup signup=new Signup();
                            signup.setUsername(username_ed.getText().toString().trim());
                            signup.setEmail(email_ed.getText().toString().trim());
                            signup.setMobileNumber(phone_ed.getText().toString().trim());
                            signup.setCountryCode("+91");
                            signup.setPassword(pass_ed.getText().toString().trim());
                            signup.setName(full_name_ed.getText().toString().trim());
                            signup.setDeviceType(GlobalVariables.device_type);
                            signup.setDeviceToken(SharedPreferenceWriter.getInstance(SignupActivity.this).getString(GlobalVariables.deviceToken));
                            signup.setLangCode(SharedPreferenceWriter.getInstance(SignupActivity.this).getString(GlobalVariables.langCode));
                            Call<Signup> call=api_service.forecasterSignup(signup);
                            call.enqueue(new Callback<Signup>() {
                                @Override
                                public void onResponse(Call<Signup> call, Response<Signup> response) {
                                    if(response.isSuccessful())
                                    {
                                        Signup server_response=response.body();
                                        if(server_response.getStatus().equalsIgnoreCase("SUCCESS"))
                                        {
                                            dailogHelper.dismissDailog();
                                            setPreferences(server_response);
                                           // Toast.makeText(SignupActivity.this,server_response.getResponseMessage(),Toast.LENGTH_LONG).show();
                                            Intent intent=new Intent(SignupActivity.this,ProfileSetupActivity.class);
                                            startActivity(intent);

                                        }
                                        else if(server_response.getStatus().equalsIgnoreCase("FAILURE"))
                                        {
                                            if(server_response.getResponseMessage().equalsIgnoreCase(GlobalVariables.invalidoken))
                                            {
                                                Toast.makeText(SignupActivity.this,getString(R.string.other_device_logged_in),Toast.LENGTH_LONG).show();
                                                finish();
                                                startActivity(new Intent(SignupActivity.this,LoginActivity.class));
                                                SharedPreferenceWriter.getInstance(SignupActivity.this).clearPreferenceValues();
                                                FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                                        if (!task.isSuccessful()) {
                                                            // Log.w(TAG, "getInstanceId failed", task.getException());
                                                            return;
                                                        }

                                                        String auth_token = task.getResult().getToken();
                                                        Log.w("firebaese","token: "+auth_token);
                                                        SharedPreferenceWriter.getInstance(SignupActivity.this).writeStringValue(GlobalVariables.deviceToken,auth_token);
                                                    }
                                                });
                                            }
                                            else {
                                                dailogHelper.dismissDailog();
                                                Toast.makeText(SignupActivity.this, server_response.getResponseMessage(), Toast.LENGTH_LONG).show();
                                            }

                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<Signup> call, Throwable t) {

                                }
                            });

                        }
                        else {
                            dailogHelper.dismissDailog();

                            //verification unsuccessful.. display an error message

                            String message = "Somthing is wrong, we will fix it soon...";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                            }
                            Toast.makeText(SignupActivity.this,message,Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }

    private void setPreferences(Signup server_response) {
        SharedPreferenceWriter.getInstance(SignupActivity.this).writeStringValue(GlobalVariables.profilePic,server_response.getData().getProfilePic());
        SharedPreferenceWriter.getInstance(SignupActivity.this).writeBooleanValue(GlobalVariables.notificationStatus,server_response.getData().getNotificationStatus());
        SharedPreferenceWriter.getInstance(SignupActivity.this).writeStringValue(GlobalVariables.language,server_response.getData().getLanguage());
        SharedPreferenceWriter.getInstance(SignupActivity.this).writeBooleanValue(GlobalVariables.profileSetup,server_response.getData().getProfileSetup());
        SharedPreferenceWriter.getInstance(SignupActivity.this).writeStringValue(GlobalVariables.totalRating, String.valueOf(server_response.getData().getTotalRating()));
        SharedPreferenceWriter.getInstance(SignupActivity.this).writeStringValue(GlobalVariables.avgRating, String.valueOf(server_response.getData().getAvgRating()));
        SharedPreferenceWriter.getInstance(SignupActivity.this).writeBooleanValue(GlobalVariables.onlineStatus,server_response.getData().getOnlineStatus());
        SharedPreferenceWriter.getInstance(SignupActivity.this).writeStringValue(GlobalVariables.responseTime,server_response.getData().getResponseTime());
        SharedPreferenceWriter.getInstance(SignupActivity.this).writeIntValue(GlobalVariables.pendingQueue,server_response.getData().getPendingQueue());
        SharedPreferenceWriter.getInstance(SignupActivity.this).writeBooleanValue(GlobalVariables.profileComplete,server_response.getData().getProfileComplete());
        SharedPreferenceWriter.getInstance(SignupActivity.this).writeStringValue(GlobalVariables.status,server_response.getData().getStatus());
        SharedPreferenceWriter.getInstance(SignupActivity.this).writeIntValue(GlobalVariables.bookingCount,server_response.getData().getBookingCount());
        SharedPreferenceWriter.getInstance(SignupActivity.this).writeStringValue(GlobalVariables._id,server_response.getData().getId());
        SharedPreferenceWriter.getInstance(SignupActivity.this).writeStringValue(GlobalVariables.name,server_response.getData().getName());
        SharedPreferenceWriter.getInstance(SignupActivity.this).writeStringValue(GlobalVariables.username,server_response.getData().getUsername());
        SharedPreferenceWriter.getInstance(SignupActivity.this).writeStringValue(GlobalVariables.email,server_response.getData().getEmail());
        SharedPreferenceWriter.getInstance(SignupActivity.this).writeStringValue(GlobalVariables.countryCode,server_response.getData().getCountryCode());
        SharedPreferenceWriter.getInstance(SignupActivity.this).writeStringValue(GlobalVariables.mobileNumber,server_response.getData().getMobileNumber());
        SharedPreferenceWriter.getInstance(SignupActivity.this).writeStringValue(GlobalVariables.createdAt,server_response.getData().getCreatedAt());
        SharedPreferenceWriter.getInstance(SignupActivity.this).writeStringValue(GlobalVariables.updatedAt,server_response.getData().getUpdatedAt());
        SharedPreferenceWriter.getInstance(SignupActivity.this).writeIntValue(GlobalVariables.__v,server_response.getData().getV());
        SharedPreferenceWriter.getInstance(SignupActivity.this).writeStringValue(GlobalVariables.jwtToken,server_response.getData().getJwtToken());
        SharedPreferenceWriter.getInstance(SignupActivity.this).writeStringValue(GlobalVariables.deviceToken,server_response.getData().getDeviceToken());
        SharedPreferenceWriter.getInstance(SignupActivity.this).writeStringValue(GlobalVariables.device_type,server_response.getData().getDeviceType());

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

    private boolean checkValidation() {
        boolean ret=true;
        Validation validation=new Validation(this);
        if(!validation.hasText(full_name_ed,getString(R.string.please_enter_fullname))
        || !validation.hasText(username_ed,getString(R.string.pls_enter_username))
        || !validation.email(email_ed,getString(R.string.please_enter_email))
        || !validation.isPhoneNumber(phone_ed,true)
        || !validation.hasText(pass_ed,getString(R.string.pls_enter_pass))
        || !validation.hasText(conpass_ed,getString(R.string.please_enter_confirm_password))
        || !conpass_ed.getText().toString().equalsIgnoreCase(pass_ed.getText().toString().trim())
        || clickcount%2==0)
        {
            if(!validation.hasText(full_name_ed,getString(R.string.please_enter_fullname)))
            {
                ret=false;
                full_name_ed.requestFocus();
            }
            else if(!validation.hasText(username_ed,getString(R.string.pls_enter_username)))
            {
                ret=false;
                username_ed.requestFocus();
            }
            else if(!validation.email(email_ed,getString(R.string.please_enter_email)))
            {
                ret=false;
                email_ed.requestFocus();
            }
            else if(!validation.isPhoneNumber(phone_ed,true))
            {
                ret=false;
                phone_ed.requestFocus();
            }
            else if(!validation.hasText(pass_ed,getString(R.string.pls_enter_pass)))
            {
                ret=false;
                pass_ed.requestFocus();
            }
            else if(!validation.hasText(conpass_ed,getString(R.string.please_enter_confirm_password)))
            {
                ret=false;
                conpass_ed.requestFocus();

            }
            else if(!conpass_ed.getText().toString().equalsIgnoreCase(pass_ed.getText().toString().trim()))
            {
                ret=false;
                conpass_ed.setError(getString(R.string.confirm_password_not_match));
                conpass_ed.requestFocus();
            }

            else if(clickcount%2==0)
            {
                ret=false;
                Toast.makeText(SignupActivity.this,getString(R.string.please_check_terms_conditions),Toast.LENGTH_LONG).show();

            }
        }
        return ret;
    }


    @Override
    public void onBackPressed() {
        Intent intent=new Intent(SignupActivity.this,LoginActivity.class);
        finish();
        startActivity(intent);
    }

    @Override
    public void onCountrySelected() {
        countrycode_txt.setText(ccode.getSelectedCountryCodeWithPlus());
        countrycode=ccode.getSelectedCountryCodeWithPlus();


    }
}
