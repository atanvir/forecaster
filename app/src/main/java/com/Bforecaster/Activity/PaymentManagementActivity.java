package com.Bforecaster.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.Bforecaster.Adapter.PaymentManagementAdapter;
import com.Bforecaster.Modal.Data;
import com.Bforecaster.Modal.PaymentManagement;
import com.Bforecaster.R;
import com.Bforecaster.Retrofit.RetroInterface;
import com.Bforecaster.Retrofit.RetrofitInit;
import com.Bforecaster.Utility.GlobalVariables;
import com.Bforecaster.Utility.InternetCheck;
import com.Bforecaster.Utility.ProgressDailogHelper;
import com.Bforecaster.Utility.SharedPreferenceWriter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentManagementActivity extends AppCompatActivity {

    @BindView(R.id.paymentRV)
    RecyclerView paymentRV;
    List<Data> managementList;
    ProgressDailogHelper dailogHelper;

    @BindView(R.id.drawer)
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_management);
        ButterKnife.bind(this);
        new CategorySelectionActivity(this,drawer);
        dailogHelper=new ProgressDailogHelper(this,"");
        getForecasterTransactionListApi();



    }

    private void getForecasterTransactionListApi() {
        if(new InternetCheck(this).isConnect())
        {
            dailogHelper.showDailog();
            PaymentManagement paymentManagement=new PaymentManagement();
            paymentManagement.setForecasterId(SharedPreferenceWriter.getInstance(this).getString(GlobalVariables._id));
            paymentManagement.setLangCode(SharedPreferenceWriter.getInstance(PaymentManagementActivity.this).getString(GlobalVariables.langCode));
            RetroInterface api_service= RetrofitInit.getConnect().createConnection();
            Call<PaymentManagement> call=api_service.getForecasterTransactionList(paymentManagement,SharedPreferenceWriter.getInstance(PaymentManagementActivity.this).getString(GlobalVariables.jwtToken));
            call.enqueue(new Callback<PaymentManagement>() {
                @Override
                public void onResponse(Call<PaymentManagement> call, Response<PaymentManagement> response) {
                    if(response.isSuccessful())
                    {
                        dailogHelper.dismissDailog();
                        if(response.body().getStatus().equalsIgnoreCase(GlobalVariables.SUCCESS))
                        {
                            managementList=response.body().getData();
                            paymentRV.setLayoutManager(new LinearLayoutManager(PaymentManagementActivity.this));
                            PaymentManagementAdapter adapter=new PaymentManagementAdapter(PaymentManagementActivity.this,managementList);
                            paymentRV.setAdapter(adapter);

                        }
                        else if(response.body().getStatus().equalsIgnoreCase(GlobalVariables.FAILURE))
                        {
                            if(response.body().getResponseMessage().equalsIgnoreCase("Invalid Token"))
                            {
                                Intent intent=new Intent(PaymentManagementActivity.this,LoginActivity.class);
                                finish();
                                startActivity(intent);
                                Toast.makeText(PaymentManagementActivity.this, getString(R.string.other_device_logged_in), Toast.LENGTH_SHORT).show();

                            }
                            else {
                                Toast.makeText(PaymentManagementActivity.this, response.body().getResponseMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }


                    }
                }

                @Override
                public void onFailure(Call<PaymentManagement> call, Throwable t) {
                    dailogHelper.dismissDailog();
                    Toast.makeText(PaymentManagementActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();


                }
            });



        }else
        {
            Toast toast=Toast.makeText(this, getString(R.string.check_internet), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();

        }


    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(PaymentManagementActivity.this,CategorySelectionActivity.class);
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
