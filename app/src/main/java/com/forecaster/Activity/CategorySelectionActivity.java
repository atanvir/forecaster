package com.forecaster.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.Toolbar;


import com.forecaster.Modal.Data;
import com.forecaster.Modal.Setting;
import com.forecaster.Modal.UpdateStatus;
import com.forecaster.R;
import com.forecaster.Retrofit.RetroInterface;
import com.forecaster.Retrofit.RetrofitInit;
import com.forecaster.Utility.GlobalVariables;
import com.forecaster.Utility.InternetCheck;
import com.forecaster.Utility.ProgressDailogHelper;
import com.forecaster.Utility.SharedPreferenceWriter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.logging.SocketHandler;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategorySelectionActivity extends AppCompatActivity implements View.OnClickListener {

    Toolbar toolbar;
    LinearLayout  itemMyChats,itemHomes,itemAboutApp,itemContactUs,itemSettings,itemTermConditions,itemShareApp;
    LinearLayout burger;
    LinearLayout requestmtLL,paymentmtLL,profilemtLL,notificationLL;
    AppCompatImageView onlineStatus_iv;

    ProgressDialog dailog;

    private Context context;
    private DrawerLayout mDrawerLayout;
    private Activity activity;
    boolean onlineStatus;
    private ProgressDailogHelper dailogHelper;

    private void init(Activity activity) {
        if(activity == null)
        {
            NavigationView navigationView=  findViewById(R.id.nav_view);
            mDrawerLayout= findViewById(R.id.drawer);
            toolbar= findViewById(R.id.toolbar);
            burger= findViewById(R.id.burger);
            onlineStatus_iv=findViewById(R.id.onlineStatus_iv);
            itemHomes=findViewById(R.id.itemHomes);
            itemMyChats=findViewById(R.id.itemMyChats);
            itemAboutApp=findViewById(R.id.itemAboutApp);
            itemContactUs=findViewById(R.id.itemContactUs);
            itemSettings=findViewById(R.id.itemSettings);
            itemTermConditions=findViewById(R.id.itemTermConditions);
            itemShareApp=findViewById(R.id.itemShareApp);
            requestmtLL=findViewById(R.id.requestmtLL);
            paymentmtLL=findViewById(R.id.paymentmtLL);
            profilemtLL=findViewById(R.id.profilemtLL);
            notificationLL=findViewById(R.id.notificationLL);
            itemHomes.setOnClickListener(this);
            itemMyChats.setOnClickListener(this);
            itemAboutApp.setOnClickListener(this);
            itemContactUs.setOnClickListener(this);
            itemSettings.setOnClickListener(this);
            itemTermConditions.setOnClickListener(this);
            itemShareApp.setOnClickListener(this);
            burger.setOnClickListener(this);
            requestmtLL.setOnClickListener(this);
            paymentmtLL.setOnClickListener(this);
            profilemtLL.setOnClickListener(this);
            mDrawerLayout.setOnClickListener(this);
            notificationLL.setOnClickListener(this);
            onlineStatus_iv.setOnClickListener(this);
            dailogHelper=new ProgressDailogHelper(this,"");


        }
        else
        {
            NavigationView navigationView=  activity.findViewById(R.id.nav_view);
            mDrawerLayout= activity.findViewById(R.id.drawer);
            burger= activity.findViewById(R.id.burger);
            itemHomes=activity.findViewById(R.id.itemHomes);
            itemMyChats=activity.findViewById(R.id.itemMyChats);
            itemAboutApp=activity.findViewById(R.id.itemAboutApp);
            itemContactUs=activity.findViewById(R.id.itemContactUs);
            itemSettings=activity.findViewById(R.id.itemSettings);
            itemTermConditions=activity.findViewById(R.id.itemTermConditions);
            itemShareApp=activity.findViewById(R.id.itemShareApp);
            requestmtLL=activity.findViewById(R.id.requestmtLL);
            paymentmtLL=activity.findViewById(R.id.paymentmtLL);
            profilemtLL=activity.findViewById(R.id.profilemtLL);
            notificationLL=activity.findViewById(R.id.notificationLL);
            itemHomes.setOnClickListener(this);
            itemMyChats.setOnClickListener(this);
            itemAboutApp.setOnClickListener(this);
            itemContactUs.setOnClickListener(this);
            itemSettings.setOnClickListener(this);
            itemTermConditions.setOnClickListener(this);
            itemShareApp.setOnClickListener(this);
        }

    }

    public CategorySelectionActivity()
    {
        context=this;
    }


    public CategorySelectionActivity(Context context,DrawerLayout drawerLayout)
    {
        this.context=context;
        activity=(Activity)context;
        this.mDrawerLayout=drawerLayout;
        init(activity);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferenceWriter.getInstance(CategorySelectionActivity.this).writeStringValue(GlobalVariables.islogin, "Yes");
        init(activity);
        settingDailog();
        onlineStatus = SharedPreferenceWriter.getInstance(CategorySelectionActivity.this).getBoolean(GlobalVariables.onlineStatus);
        if (onlineStatus) {
            onlineStatus_iv.setImageDrawable(getDrawable(R.drawable.on));
        } else {
            onlineStatus_iv.setImageDrawable(getDrawable(R.drawable.off));
        }
    }

    private void settingDailog() {

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View view) {

         if (view== itemHomes){

             Intent intent=new Intent(context,CategorySelectionActivity.class);
             finish();
             context.startActivity(intent);

         }else if (view == burger){
             if(!mDrawerLayout.isDrawerOpen(GravityCompat.START))
                 mDrawerLayout.openDrawer(GravityCompat.START);
         }

         else if(view == itemSettings)
         {
             getForecasterSettingsApi();
         }

         else if(view == itemContactUs)
         {
             Intent intent=new Intent(context,ContactUsActivity.class);
             context.startActivity(intent);

         }
         else if(view ==requestmtLL)
         {

             Intent intent=new Intent(CategorySelectionActivity.this, RequestManagementActivity.class);
             finish();
             startActivity(intent);
         }
         else if(view ==itemAboutApp)
         {
             Intent intent=new Intent(CategorySelectionActivity.this, WebviewAcitivity.class);
             intent.putExtra(GlobalVariables.url,"http://18.218.65.12:4002/aboutUs");
             startActivity(intent);

         }

         else if(view==itemTermConditions)
         {
             Intent intent=new Intent(CategorySelectionActivity.this,WebviewAcitivity.class);
             intent.putExtra(GlobalVariables.url,"http://18.218.65.12:4002/terms");
             startActivity(intent);

         }
         else if(view ==paymentmtLL)
         {
            Intent intent=new Intent(CategorySelectionActivity.this, PaymentManagementActivity.class);
            finish();
            startActivity(intent);
         }
         else if(view == profilemtLL)
         {
             Intent intent=new Intent(CategorySelectionActivity.this,ProfileManagementActivity.class);
             finish();
             startActivity(intent);
         }
         else if(view == notificationLL)
         {
            Intent intent=new Intent(CategorySelectionActivity.this,NotificationActivity.class);
            finish();
            startActivity(intent);
         }

         else if(view == itemMyChats)
         {
             Intent intent=new Intent(context, ChatListingActivity.class);
             context.startActivity(intent);
         }

         else if(view == mDrawerLayout)
         {
             if(!mDrawerLayout.isDrawerOpen(GravityCompat.START))
                 mDrawerLayout.openDrawer(GravityCompat.START);
         }
         else if(view==onlineStatus_iv) {
             onlineStatus = SharedPreferenceWriter.getInstance(CategorySelectionActivity.this).getBoolean(GlobalVariables.onlineStatus);
             if (onlineStatus) {
                 onlineStatus_iv.setImageDrawable(getDrawable(R.drawable.off));
                 updateOnlineStatusApi(false);
             } else
             {
                 onlineStatus_iv.setImageDrawable(getDrawable(R.drawable.on));
                 updateOnlineStatusApi(true);
             }

         }

    }

    private void updateOnlineStatusApi(boolean onlineStatus) {
        if(new InternetCheck(this).isConnect())
        {
            RetroInterface api_service=RetrofitInit.getConnect().createConnection();
            UpdateStatus status=new UpdateStatus();
            status.setForecasterId(SharedPreferenceWriter.getInstance(CategorySelectionActivity.this).getString(GlobalVariables._id));
            status.setOnlineStatus(onlineStatus);
            status.setLangCode("en");
            Call<UpdateStatus> call=api_service.updateOnlineStatus(status,SharedPreferenceWriter.getInstance(CategorySelectionActivity.this).getString(GlobalVariables.jwtToken));
            call.enqueue(new Callback<UpdateStatus>() {
                @Override
                public void onResponse(Call<UpdateStatus> call, Response<UpdateStatus> response) {
                    if(response.isSuccessful())
                    {
                        UpdateStatus server_resposne=response.body();
                        if(server_resposne.getStatus().equalsIgnoreCase(GlobalVariables.SUCCESS))
                        {
                            setPrefrences(server_resposne.getData());
                        }
                        else if(server_resposne.getStatus().equalsIgnoreCase(GlobalVariables.FAILURE))
                        {
                            if(server_resposne.getResponseMessage().equalsIgnoreCase(GlobalVariables.invalidoken))
                            {
                                Toast.makeText(CategorySelectionActivity.this,getString(R.string.other_device_logged_in),Toast.LENGTH_LONG).show();
                                finish();
                                startActivity(new Intent(CategorySelectionActivity.this,LoginActivity.class));
                            }
                            else {
                                Toast toast = Toast.makeText(CategorySelectionActivity.this, server_resposne.getResponseMessage(), Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            }
                        }
                    }
                }


                @Override
                public void onFailure(Call<UpdateStatus> call, Throwable t) {

                }
            });

        }
        else
        {
            Toast toast= Toast.makeText(this,getString(R.string.check_internet),Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
        }


    }

    private void setPrefrences(Data data) {
//        SharedPreferenceWriter.getInstance(CategorySelectionActivity.this).writeStringValue(GlobalVariables.onlineStatus,data.getOnlineStatus());
        SharedPreferenceWriter.getInstance(CategorySelectionActivity.this).writeBooleanValue(GlobalVariables.onlineStatus,data.getOnlineStatus());


    }

    private void getForecasterSettingsApi() {
        dailogHelper=new ProgressDailogHelper(this,"");
        dailogHelper.showDailog();
        RetroInterface api_service= RetrofitInit.getConnect().createConnection();
        Setting setting=new Setting();
        setting.setForecasterId(SharedPreferenceWriter.getInstance(CategorySelectionActivity.this).getString(GlobalVariables._id));
        setting.setLangCode("en");
        Call<Setting> call=api_service.getForecasterSettings(setting,SharedPreferenceWriter.getInstance(CategorySelectionActivity.this).getString(GlobalVariables.jwtToken));
        call.enqueue(new Callback<Setting>() {
            @Override
            public void onResponse(Call<Setting> call, Response<Setting> response) {
                if(response.isSuccessful())
                {

                    Setting server_response=response.body();
                    if(server_response.getStatus().equalsIgnoreCase("SUCCESS"))
                    {
                        dailogHelper.dismissDailog();
                        Intent intent=new Intent(context,SettingActivity.class);
                        context.startActivity(intent);
                    }
                    else if(server_response.getStatus().equalsIgnoreCase("FAILURE"))
                    {
                        if (server_response.getResponseMessage().equalsIgnoreCase(GlobalVariables.invalidoken)) {
                            Toast.makeText(CategorySelectionActivity.this, getString(R.string.other_device_logged_in), Toast.LENGTH_LONG).show();
                            finish();
                            startActivity(new Intent(CategorySelectionActivity.this, LoginActivity.class));
                            SharedPreferenceWriter.getInstance(CategorySelectionActivity.this).clearPreferenceValues();
                            FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                @Override
                                public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                    if (!task.isSuccessful()) {
                                        return;
                                    }

                                    String auth_token = task.getResult().getToken();
                                    Log.w("firebaese", "token: " + auth_token);
                                    SharedPreferenceWriter.getInstance(CategorySelectionActivity.this).writeStringValue(GlobalVariables.deviceToken, auth_token);
                                }
                            });
                        }
                        else {
                            dailogHelper.dismissDailog();
                            Toast.makeText(CategorySelectionActivity.this, server_response.getResponseMessage(), Toast.LENGTH_LONG).show();
                        }}

                }
            }

            @Override
            public void onFailure(Call<Setting> call, Throwable t) {
                Toast.makeText(CategorySelectionActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
