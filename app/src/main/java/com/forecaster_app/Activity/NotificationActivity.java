package com.forecaster_app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.forecaster_app.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NotificationActivity extends AppCompatActivity {

    @BindView(R.id.drawer)
    DrawerLayout drawer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        ButterKnife.bind(this);
        new CategorySelectionActivity(this,drawer);

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
