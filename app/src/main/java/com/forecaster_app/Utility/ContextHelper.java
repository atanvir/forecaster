package com.forecaster_app.Utility;

import android.content.Context;

public class ContextHelper {
    private static ContextHelper contextHelper=null;
    private static Context context;

    private ContextHelper(Context context)
    {
        this.context=context;

    }
    public static ContextHelper setInstance(Context context)
    {
        if(contextHelper==null)
        {
            contextHelper=new ContextHelper(context);
        }
        return contextHelper;
    }


    public static Context getInstance()
    {
        return context;
    }
}
