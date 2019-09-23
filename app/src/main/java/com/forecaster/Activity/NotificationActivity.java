package com.forecaster.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.forecaster.Adapter.NotificationAdapter;
import com.forecaster.Modal.Data;
import com.forecaster.Modal.Notification;
import com.forecaster.R;
import com.forecaster.Retrofit.RetroInterface;
import com.forecaster.Retrofit.RetrofitInit;
import com.forecaster.Utility.GlobalVariables;
import com.forecaster.Utility.InternetCheck;
import com.forecaster.Utility.ProgressDailogHelper;
import com.forecaster.Utility.SharedPreferenceWriter;

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
