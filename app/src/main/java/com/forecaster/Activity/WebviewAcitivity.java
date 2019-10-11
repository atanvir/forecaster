package com.forecaster.Activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.forecaster.R;
import com.forecaster.Utility.GlobalVariables;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WebviewAcitivity extends AppCompatActivity {
    @BindView(R.id.webView) WebView webView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        ButterKnife.bind(this);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(getIntent().getStringExtra(GlobalVariables.url));







    }


}
