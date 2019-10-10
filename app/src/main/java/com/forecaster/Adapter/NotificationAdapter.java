package com.forecaster.Adapter;

import android.content.Context;
import android.os.Build;
import android.telecom.Call;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.forecaster.Modal.Data;
import com.forecaster.R;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder>{
    private Context context;
    private List<Data> data;

    public NotificationAdapter(Context context,List<Data> data)
    {
        this.context=context;
        this.data=data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.notification_adapter,parent,false);
        return new NotificationAdapter.MyViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String  notification_txt[]=data.get(position).getNotiMessage().split("[.]");

        if(notification_txt.length>1)
        {
            holder.notificationtxt.setText(notification_txt[0]+notification_txt[1].trim());
        }
        else {
            holder.notificationtxt.setText(data.get(position).getNotiMessage());
        }

        String getDate = data.get(position).getCreatedAt();
        String server_format = getDate;    //server comes format ?
        String server_format1 = "2019-04-04T13:27:36.591Z";    //server comes format ?
        String myFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));

        try {
            Date date = sdf.parse(server_format);
            String your_format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(date);
            String[] splitted = your_format.split(" ");
            SimpleDateFormat dateFormat=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            Date endDate=dateFormat.parse(your_format);
            Log.e("endDate", String.valueOf(endDate));
            Date startDate=Calendar.getInstance().getTime();

            long differenceDate=startDate.getTime()-endDate.getTime();
            Log.e("differnce", String.valueOf(differenceDate));
            String[] completeDate=splitted[0].split("-");
            String date1=completeDate[0];
            String month=completeDate[1];
            String year=completeDate[2];


            int days_in_months=new GregorianCalendar(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(date1)).getActualMaximum(Calendar.DAY_OF_MONTH);

            long secounds=1000;     // 1 secound
            long min=60*secounds;   // 1 min
            long hour=3600000;      // 1 hour
            long day=86400000;      // 1 days


            long monthdifference=differenceDate/(days_in_months*day);
            long daysDifference=differenceDate/day;
            long hourdifference=differenceDate/hour;
            long mindifference=differenceDate/min;
            long secoundsDiffer=differenceDate/secounds;

            Log.e("monthdifference", String.valueOf(monthdifference));
            Log.e("daysDifference", String.valueOf(daysDifference));
            Log.e("hourdifference", String.valueOf(hourdifference));
            Log.e("mindifference", String.valueOf(mindifference));
            Log.e("secoundsDiffer", String.valueOf(secoundsDiffer));

            if(monthdifference>0)
            {
                holder.timing_txt.setText(monthdifference+" months ago");
            }
            else if(daysDifference>0)
            {
                holder.timing_txt.setText(daysDifference+" days ago");
            }
            else if(hourdifference>0)
            {
                holder.timing_txt.setText(hourdifference+" hour ago");
            }
            else if(mindifference>0)
            {
                holder.timing_txt.setText(mindifference+" min ago");
            }
            else if(secoundsDiffer>0)
            {
                holder.timing_txt.setText(secoundsDiffer+" sec ago");

            }

        } catch (Exception e) {
            System.out.println(e.toString()); //date format error
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.notificationtxt) TextView notificationtxt;
        @BindView(R.id.timing_txt) TextView timing_txt;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

        }
    }
}
