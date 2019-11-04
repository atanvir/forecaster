package com.Bforecaster.Adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.Bforecaster.Modal.Data;
import com.Bforecaster.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PaymentManagementAdapter extends RecyclerView.Adapter<PaymentManagementAdapter.MyViewHolder> {

    private Context context;
    private List<Data> managementList;

    public PaymentManagementAdapter(Context context,List<Data> managementList)
    {
      this.context=context;
      this.managementList=managementList;
    }

    @NonNull
    @Override
    public PaymentManagementAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.adapter_payment_management,parent,false);
        return new PaymentManagementAdapter.MyViewHolder(view);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(@NonNull PaymentManagementAdapter.MyViewHolder holder, int position) {

        if(position % 2 == 0) {
            holder.linearLayout.setBackgroundColor(context.getColor(R.color.white));
        }

        else
        {
            holder.linearLayout.setBackgroundColor(context.getColor(R.color.grey));
        }
        holder.nametxt.setText(managementList.get(position).getDreamerData().getName());
        String getDate = managementList.get(position).getCreatedAt();
        String server_format = getDate;    //server comes format ?
        String server_format1 = "2019-04-04T13:27:36.591Z";    //server comes format ?
        String myFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));

        try {
            Date date = sdf.parse(server_format);
            System.out.println(date);
            String your_format = new SimpleDateFormat("dd-MMM-yy").format(date);
            System.out.println(your_format);
            holder.datetext.setText(your_format);
        } catch (Exception e) {
            System.out.println(e.toString()); //date format error
        }
        holder.categorytxt.setText(managementList.get(position).getCategoryName());
        holder.pricetxt.setText(managementList.get(position).getAmount()+" SAR");
    }

    @Override
    public int getItemCount() {
        return managementList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.datetext) TextView datetext;
        @BindView(R.id.categorytxt) TextView categorytxt;
        @BindView(R.id.linearLayout) LinearLayout linearLayout;
        @BindView(R.id.pricetxt) TextView pricetxt;
        @BindView(R.id.nametxt) TextView nametxt;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
