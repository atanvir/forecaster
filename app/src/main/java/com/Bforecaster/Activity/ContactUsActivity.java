package com.Bforecaster.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.Bforecaster.Modal.ContactUs;
import com.Bforecaster.R;
import com.Bforecaster.Retrofit.RetroInterface;
import com.Bforecaster.Retrofit.RetrofitInit;
import com.Bforecaster.Utility.GlobalVariables;
import com.Bforecaster.Utility.ProgressDailogHelper;
import com.Bforecaster.Utility.SharedPreferenceWriter;
import com.Bforecaster.Utility.Validation;
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

public class ContactUsActivity extends AppCompatActivity {
    @BindView(R.id.email_ed) EditText email_ed;
    @BindView(R.id.concern_ed) EditText concern_ed;
    @BindView(R.id.message_ed) EditText message_ed;
    @BindView(R.id.submitBtn) Button submitBtn;
    private ProgressDailogHelper dailogHelper;





    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        ButterKnife.bind(this);
        init();





    }

    private void init() {
        submitBtn.setOnClickListener(this::OnClick);
        dailogHelper=new ProgressDailogHelper(this,"");
    }


    @Override
    public void onBackPressed() {
        finish();
    }



    @OnClick(R.id.back_ll)
    void OnClick(View view)
    {
        switch (view.getId())
        {
            case R.id.back_ll:
                finish();
                break;

            case R.id.submitBtn:
                if(checkValidation())
                {
                    contactUsApi();

                }


                break;

        }
    }

    private void contactUsApi() {
        dailogHelper.showDailog();
        ContactUs contactUs=new ContactUs();
        contactUs.setForecasterId(SharedPreferenceWriter.getInstance(ContactUsActivity.this).getString(GlobalVariables._id));
        contactUs.setEmail(email_ed.getText().toString().trim());
        contactUs.setSelectedConcern(concern_ed.getText().toString().trim());
        contactUs.setMessage(message_ed.getText().toString().trim());
        contactUs.setLangCode(SharedPreferenceWriter.getInstance(ContactUsActivity.this).getString(GlobalVariables.langCode));
        RetroInterface api_service=RetrofitInit.getConnect().createConnection();
        Call<ContactUs> call=api_service.contactUs(contactUs,SharedPreferenceWriter.getInstance(ContactUsActivity.this).getString(GlobalVariables.jwtToken));
        call.enqueue(new Callback<ContactUs>() {
            @Override
            public void onResponse(Call<ContactUs> call, Response<ContactUs> response) {
                if(response.isSuccessful())
                {
                    ContactUs server_resposne=response.body();
                    if(server_resposne.getStatus().equalsIgnoreCase("SUCCESS"))
                    {
                        dailogHelper.dismissDailog();
                        Toast.makeText(ContactUsActivity.this,server_resposne.getResponseMessage(),Toast.LENGTH_LONG).show();
                        Intent intent=new Intent(ContactUsActivity.this,SettingActivity.class);
                        startActivity(intent);


                    }else if(server_resposne.getStatus().equalsIgnoreCase("FAILURE"))
                    {
                        if(server_resposne.getResponseMessage().equalsIgnoreCase(GlobalVariables.invalidoken))
                        {
                            Toast.makeText(ContactUsActivity.this,getString(R.string.other_device_logged_in),Toast.LENGTH_LONG).show();
                            finish();
                            startActivity(new Intent(ContactUsActivity.this,LoginActivity.class));
                            SharedPreferenceWriter.getInstance(ContactUsActivity.this).clearPreferenceValues();
                            FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                @Override
                                public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                    if (!task.isSuccessful()) {
                                        // Log.w(TAG, "getInstanceId failed", task.getException());
                                        return;
                                    }

                                    String auth_token = task.getResult().getToken();
                                    Log.w("firebaese","token: "+auth_token);
                                    SharedPreferenceWriter.getInstance(ContactUsActivity.this).writeStringValue(GlobalVariables.deviceToken,auth_token);
                                }
                            });
                        }
                        else
                        {
                            dailogHelper.dismissDailog();
                            Toast.makeText(ContactUsActivity.this,server_resposne.getResponseMessage(),Toast.LENGTH_LONG).show();

                        }


                    }
                }
            }

            @Override
            public void onFailure(Call<ContactUs> call, Throwable t) {

            }
        });


    }

    private boolean checkValidation() {
        boolean ret=true;
        Validation validation=new Validation(this);
        if(!validation.email(email_ed,getString(R.string.not_valid_email))
        || !validation.hasText(concern_ed,getString(R.string.enter_your_concern))
        || !validation.hasText(message_ed,getString(R.string.enter_your_message))
        )
        {
            if(!validation.email(email_ed,getString(R.string.not_valid_email)))
            {

                ret=false;
                email_ed.requestFocus();

            }
            else if(!validation.hasText(concern_ed,getString(R.string.enter_your_concern)))
            {
                ret=false;
                concern_ed.requestFocus();

            }
            else if(!validation.hasText(message_ed,getString(R.string.enter_your_message)))
            {
                ret=false;
                message_ed.requestFocus();

            }


        }


        return ret;

    }


}
