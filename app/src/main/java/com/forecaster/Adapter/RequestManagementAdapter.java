package com.forecaster.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.forecaster.Activity.DetailActivity;
import com.forecaster.Modal.Data;
import com.forecaster.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class RequestManagementAdapter extends RecyclerView.Adapter<RequestManagementAdapter.MyViewHolder> {
    private Context context;
    private List<Data> managementList;

    public RequestManagementAdapter(Context context, List<Data> managementList)
    {
        this.context=context;
        this.managementList=managementList;
    }

    @NonNull
    @Override
    public RequestManagementAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(context).inflate(R.layout.adapter_request_management,parent,false);
        return new RequestManagementAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestManagementAdapter.MyViewHolder holder, int position) {
        holder.nametxt.setText(managementList.get(position).getDreamerData().getName());
        Glide.with(context).load(managementList.get(position).getDreamerData().getProfilePic()).into(holder.image_im);


        String getDate = managementList.get(position).getCreatedAt();
        String server_format = getDate;    //server comes format ?
        String server_format1 = "2019-04-04T13:27:36.591Z";    //server comes format ?
        String myFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));

        try {
            Date date = sdf.parse(server_format);
            System.out.println(date);
            String your_format = new SimpleDateFormat("dd-MM-yy hh:mm aa").format(date);
            System.out.println(your_format);
            String[] splitted = your_format.split(" ");


            System.out.println(splitted[1]);
            holder.datetxt.setText(your_format);
        } catch (Exception e) {
            System.out.println(e.toString()); //date format error
        }
        holder.category_name_txt.setText(managementList.get(position).getCategoryName());




    }

    @Override
    public int getItemCount() {
        return managementList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.nametxt) TextView nametxt;
        @BindView(R.id.datetext) TextView datetxt;
        @BindView(R.id.image_im) CircleImageView image_im;
        @BindView(R.id.constraintLayout) ConstraintLayout constraintLayout;
        @BindView(R.id.category_name_txt) TextView category_name_txt;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            constraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, DetailActivity.class);
                    intent.putParcelableArrayListExtra("RequestManagement", (ArrayList<? extends Parcelable>) managementList);
                    context.startActivity(intent);

                }
            });

        }
    }
}
