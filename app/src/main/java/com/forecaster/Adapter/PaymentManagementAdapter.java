package com.forecaster.Adapter;

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

import com.forecaster.Modal.PaymentManagement;
import com.forecaster.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PaymentManagementAdapter extends RecyclerView.Adapter<PaymentManagementAdapter.MyViewHolder> {

    private Context context;
    private List<PaymentManagement> managementList;

    public PaymentManagementAdapter(Context context,List<PaymentManagement> managementList)
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
        holder.datetext.setText(managementList.get(position).getDate());
        holder.categorytxt.setText(managementList.get(position).getCategory_type());
    }

    @Override
    public int getItemCount() {
        return managementList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.datetext)
        TextView datetext;

        @BindView(R.id.categorytxt)
        TextView categorytxt;

        @BindView(R.id.linearLayout)
        LinearLayout linearLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
