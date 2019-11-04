package com.Bforecaster.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.Bforecaster.R;
import com.Bforecaster.Utility.GlobalVariables;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WebviewAcitivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.webView) WebView webView;
    @BindView(R.id.back_ll) LinearLayout back_ll;
    @BindView(R.id.title_txt) TextView title_txt;
    @BindView(R.id.back_im) ImageView back_im;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        ButterKnife.bind(this);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(getIntent().getStringExtra(GlobalVariables.url));
        title_txt.setText(getIntent().getStringExtra(GlobalVariables.title));
        back_ll.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.back_ll:
                Intent intent=new Intent(WebviewAcitivity.this,CategorySelectionActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("Activity","Yes");
                startActivity(intent);
                break;

        }


    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(WebviewAcitivity.this,CategorySelectionActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("Activity","Yes");
        startActivity(intent);
    }
}
