package com.forecaster_app.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.forecaster_app.Adapter.RequestManagementAdapter;
import com.forecaster_app.Modal.Data;
import com.forecaster_app.Modal.RequestManagement;
import com.forecaster_app.R;
import com.forecaster_app.Retrofit.RetroInterface;
import com.forecaster_app.Retrofit.RetrofitInit;
import com.forecaster_app.Utility.GlobalVariables;
import com.forecaster_app.Utility.InternetCheck;
import com.forecaster_app.Utility.ProgressDailogHelper;
import com.forecaster_app.Utility.SharedPreferenceWriter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RequestManagementActivity extends AppCompatActivity {
    @BindView(R.id.requestmtRV)
    RecyclerView requestmtRV;

    @BindView(R.id.drawer)
    DrawerLayout drawerLayout;

    @BindView(R.id.menu_ll)
    LinearLayout menu_ll;

    List<Data> managementList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_management);
        ButterKnife.bind(this);

        new CategorySelectionActivity(this,drawerLayout);

        getRequestListApi();


    }

    private void getRequestListApi() {
        if(new InternetCheck(this).isConnect())
        {
            ProgressDailogHelper dailogHelper=new ProgressDailogHelper(this,"Getting Request List...");
            dailogHelper.showDailog();
            RetroInterface api_service=RetrofitInit.getConnect().createConnection();
            RequestManagement management=new RequestManagement();
            management.setForecasterId(SharedPreferenceWriter.getInstance(this).getString(GlobalVariables._id));
            management.setLangCode("en");
            Call<RequestManagement> call=api_service.getRequestList(management,SharedPreferenceWriter.getInstance(this).getString(GlobalVariables.jwtToken));
            call.enqueue(new Callback<RequestManagement>() {
                @Override
                public void onResponse(Call<RequestManagement> call, Response<RequestManagement> response) {
                    if(response.isSuccessful())
                    {
                        dailogHelper.dismissDailog();
                        RequestManagement server_response=response.body();
                        if(server_response.getStatus().equalsIgnoreCase(GlobalVariables.SUCCESS))
                        {
                           managementList=server_response.getData();
                            requestmtRV.setLayoutManager(new LinearLayoutManager(RequestManagementActivity.this));
                            RequestManagementAdapter adapter=new RequestManagementAdapter(RequestManagementActivity.this,managementList);
                            requestmtRV.setAdapter(adapter);


                        }
                        else if(server_response.getStatus().equalsIgnoreCase(GlobalVariables.FAILURE))
                        {
                            Toast toast=Toast.makeText(RequestManagementActivity.this,server_response.getResponseMessage(),Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER,0,0);
                            toast.show();

                        }




                    }
                }

                @Override
                public void onFailure(Call<RequestManagement> call, Throwable t) {

                }
            });


        }
        else
        {

        }


    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(RequestManagementActivity.this,CategorySelectionActivity.class);
        finish();
        startActivity(intent);
    }

    @OnClick({R.id.menu_ll,R.id.drawer})
    void OnClick(View view)
    {
        switch (view.getId())
        {
            case R.id.menu_ll:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
        }

    }

}
