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

public class WebviewAcitivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.webView) WebView webView;
    @BindView(R.id.back_ll) LinearLayout back_ll;
    @BindView(R.id.title_txt) TextView title_txt;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        ButterKnife.bind(this);
        title_txt.setText(getIntent().getStringExtra(GlobalVariables.title));

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(getIntent().getStringExtra(GlobalVariables.url));
        back_ll.setOnClickListener(this);







    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.back_ll:
                finish();
                break;

        }


    }

}
