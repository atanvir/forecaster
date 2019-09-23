package com.forecaster.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.forecaster.Modal.ContactUs;
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
        contactUs.setLangCode("en");
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
                        dailogHelper.dismissDailog();
                        Toast.makeText(ContactUsActivity.this,server_resposne.getResponseMessage(),Toast.LENGTH_LONG).show();


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
        if(!Validation.email(email_ed)) ret=false;
        if(!Validation.hasText(concern_ed)) ret=false;
        if(!Validation.hasText(message_ed)) ret=false;
        return ret;

    }


}
