package com.forecaster.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.forecaster.Adapter.PaymentManagementAdapter;
import com.forecaster.Modal.PaymentManagement;
import com.forecaster.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PaymentManagementActivity extends AppCompatActivity {

    @BindView(R.id.paymentRV)
    RecyclerView paymentRV;
    List<PaymentManagement> managementList;

    @BindView(R.id.drawer)
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_management);
        ButterKnife.bind(this);
        new CategorySelectionActivity(this,drawer);
        SettingValues();
        paymentRV.setLayoutManager(new LinearLayoutManager(this));
        PaymentManagementAdapter adapter=new PaymentManagementAdapter(this,managementList);
        paymentRV.setAdapter(adapter);


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


    private void SettingValues() {
        managementList=new ArrayList<>();
        managementList.add(new PaymentManagement("26 July","Dream"));
        managementList.add(new PaymentManagement("25 July","Dream"));
        managementList.add(new PaymentManagement("23 July","Psychological Counseling"));
        managementList.add(new PaymentManagement("20 July","Dream"));
        managementList.add(new PaymentManagement("20 July","Dream"));
        managementList.add(new PaymentManagement("30 June","Psychological Counseling"));
        managementList.add(new PaymentManagement("30 June","Dream"));
        managementList.add(new PaymentManagement("29 June","Psychological Counseling"));
        managementList.add(new PaymentManagement("25 June","Dream"));




    }


}
