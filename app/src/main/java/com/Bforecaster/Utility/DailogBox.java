package com.Bforecaster.Utility;

import android.app.ProgressDialog;
import android.content.Context;

public class DailogBox {
    public static ProgressDialog showDailog(Context context ,String message)
    {
        ProgressDialog dailog=new ProgressDialog(context);
        dailog.setCancelable(false);
        dailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dailog.setMessage(message);
        dailog.setTitle("Please wait!..");
        dailog.show();


        return dailog;
    }


}
