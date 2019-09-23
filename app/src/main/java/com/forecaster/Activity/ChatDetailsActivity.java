package com.forecaster.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.forecaster.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChatDetailsActivity extends AppCompatActivity {
    private String DetailActivity;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_detail);
        ButterKnife.bind(this);
        DetailActivity=getIntent().getStringExtra("DetailActivity");
    }

    @OnClick(R.id.back_ll)
    void OnClick(View view)
    {
        switch (view.getId())
        {
            case R.id.back_ll:
                if(DetailActivity!=null) {
                    if (DetailActivity.equalsIgnoreCase("Yes")) {
                        Intent intent = new Intent(ChatDetailsActivity.this, DetailActivity.class);
                        finish();
                        startActivity(intent);
                    }
                }
                else {
                    Intent intent = new Intent(ChatDetailsActivity.this, ChatActivity.class);
                    finish();
                    startActivity(intent);
                }

                break;
        }
    }

    @Override
    public void onBackPressed() {
        if(DetailActivity!=null) {
            if (DetailActivity.equalsIgnoreCase("Yes")) {
                Intent intent = new Intent(ChatDetailsActivity.this, DetailActivity.class);
                finish();
                startActivity(intent);
            }
        }
        else {
            Intent intent = new Intent(ChatDetailsActivity.this, ChatActivity.class);
            finish();
            startActivity(intent);
        }
    }
}
