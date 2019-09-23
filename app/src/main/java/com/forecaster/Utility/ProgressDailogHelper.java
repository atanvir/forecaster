package com.forecaster.Utility;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.view.WindowManager;
import com.forecaster.R;

public class ProgressDailogHelper {
    private String message;
    private Context context;
    private Dialog dialog;


    public ProgressDailogHelper(Context context, String message)
    {
        this.context=context;
        this.message=message;
        dialog=new ProgressDialog(context);
    }



    public void showDailog()
    {
        dialog=new Dialog(context,android.R.style.Theme_Black);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.loading_animation);
        dialog.setCancelable(false);
        dialog.show();
    }

    public void dismissDailog()
    {
        if(dialog.isShowing())
        {
            dialog.dismiss();
        }

    }




}
