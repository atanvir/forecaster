package com.Bforecaster.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Bforecaster.Adapter.NotificationAdapter;
import com.Bforecaster.Modal.Data;
import com.Bforecaster.Modal.Notification;
import com.Bforecaster.R;
import com.Bforecaster.Retrofit.RetroInterface;
import com.Bforecaster.Retrofit.RetrofitInit;
import com.Bforecaster.Utility.GlobalVariables;
import com.Bforecaster.Utility.InternetCheck;
import com.Bforecaster.Utility.NotificationUtils;
import com.Bforecaster.Utility.ProgressDailogHelper;
import com.Bforecaster.Utility.SharedPreferenceWriter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationActivity extends AppCompatActivity {

    @BindView(R.id.drawer)
    DrawerLayout drawer;
    private ProgressDailogHelper dailogHelper;
    @BindView(R.id.notification_rv) RecyclerView notification_rv;
    List<Data> dataList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        ButterKnife.bind(this);
        NotificationUtils.clearNotifications(this);
        new CategorySelectionActivity(this,drawer);
        dailogHelper=new ProgressDailogHelper(this,"");
        getNotificationListApi();

    }

    private void getNotificationListApi() {
        if(new InternetCheck(this).isConnect())
        {
            dailogHelper.showDailog();
            Notification notification=new Notification();
            notification.setForecasterId(SharedPreferenceWriter.getInstance(this).getString(GlobalVariables._id));
            notification.setLangCode(SharedPreferenceWriter.getInstance(NotificationActivity.this).getString(GlobalVariables.langCode));
            RetroInterface api_service= RetrofitInit.getConnect().createConnection();
            Call<Notification> call=api_service.getNotificationList(notification);
            call.enqueue(new Callback<Notification>() {
                @Override
                public void onResponse(Call<Notification> call, Response<Notification> response) {
                    if(response.isSuccessful())
                    {
                       dailogHelper.dismissDailog();
                       Notification server_response=response.body();
                       if(server_response.getStatus().equalsIgnoreCase(GlobalVariables.SUCCESS))
                       {
                           dataList=server_response.getData();
                           settingAdapter();



                       }else if(server_response.getStatus().equalsIgnoreCase(GlobalVariables.FAILURE))
                       {
                           if(server_response.getResponseMessage().equalsIgnoreCase(GlobalVariables.invalidoken))
                           {
                               Toast.makeText(NotificationActivity.this,getString(R.string.other_device_logged_in),Toast.LENGTH_LONG).show();
                               finish();
                               startActivity(new Intent(NotificationActivity.this,LoginActivity.class));
                               SharedPreferenceWriter.getInstance(NotificationActivity.this).clearPreferenceValues();
                               FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                   @Override
                                   public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                       if (!task.isSuccessful()) {
                                           // Log.w(TAG, "getInstanceId failed", task.getException());
                                           return;
                                       }

                                       String auth_token = task.getResult().getToken();
                                       Log.w("firebaese","token: "+auth_token);
                                       SharedPreferenceWriter.getInstance(NotificationActivity.this).writeStringValue(GlobalVariables.deviceToken,auth_token);
                                   }
                               });
                           }


                       }
                    }
                }

                @Override
                public void onFailure(Call<Notification> call, Throwable t) {

                }
            });

        }
        else
        {

        }



    }

    private void settingAdapter() {
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        notification_rv.setLayoutManager(layoutManager);
        notification_rv.hasFixedSize();
        NotificationAdapter adapter=new NotificationAdapter(this,dataList);
        notification_rv.setAdapter(adapter);

    }


    @Override
    public void onBackPressed() {
        Intent intent=new Intent(NotificationActivity.this,CategorySelectionActivity.class);
        finish();
        startActivity(intent);
    }

    @OnClick(R.id.menu_ll)
    void OnClick(View view)
    {
        switch (view.getId())
        {
            case R.id.menu_ll:
                drawer.openDrawer(GravityCompat.START);

                break;
        }

    }
}
