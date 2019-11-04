package com.Bforecaster.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.Bforecaster.Activity.ChatDetailsActivity;
import com.Bforecaster.Modal.Data;
import com.Bforecaster.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChatListingAdapter extends RecyclerView.Adapter<ChatListingAdapter.MyViewHolder> {
    private Context context;
    private List<Data> chatList;

    public ChatListingAdapter(Context context, List<Data> chatList)
    {
        this.context=context;
        this.chatList=chatList;
    }


    @NonNull
    @Override
    public ChatListingAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.adapter_chat,parent,false);
        return new ChatListingAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListingAdapter.MyViewHolder holder, int position) {
        holder.nametxt.setText(chatList.get(position).getDreamerData().getName());
        Glide.with(context).load(chatList.get(position).getDreamerData().getProfilePic()).into(holder.profile_iv);
        holder.desc_txt.setText(chatList.get(position).getLastMessage());

        String getDate = chatList.get(position).getCreatedAt();
        String server_format = getDate;    //server comes format ?
        String server_format1 = "2019-04-04T13:27:36.591Z";    //server comes format ?
        String myFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));


        try {
            Date date = sdf.parse(server_format);
            String server_date_complete = new SimpleDateFormat("dd/MM/yyyy hh:mm aa").format(date);

            Date currrent_date=Calendar.getInstance().getTime();

            long difference=currrent_date.getTime()-date.getTime();
            int differ=currrent_date.compareTo(date);
            Log.e("difference", String.valueOf(difference));

            if(difference<=60000)
            {
                holder.timer_txt.setText(context.getString(R.string.just_now));
            }
            else if(difference>60000 && difference<86400000-currrent_date.getTime()/60000)
            {
                holder.timer_txt.setText(context.getString(R.string.today)+" "+server_date_complete.split(" ")[1]+" "+server_date_complete.split(" ")[2]);
            }
            else if(difference>(86400000-currrent_date.getTime()/60000) && difference<86400000*2) {

                holder.timer_txt.setText(context.getString(R.string.yesterday)+" " + server_date_complete.split(" ")[1]+" "+server_date_complete.split(" ")[2]);
            }
            else
            {
                holder.timer_txt.setText(server_date_complete.split(" ")[0]);
            }



        }catch (Exception e)
        {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.timer_txt) TextView timer_txt;
        @BindView(R.id.nametxt) TextView nametxt;
        @BindView(R.id.desc_txt) TextView desc_txt;
        @BindView(R.id.profile_iv) CircleImageView profile_iv;
        @BindView(R.id.cardViewChat) CardView cardViewChat;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            cardViewChat.setOnClickListener(MyViewHolder.this);



        }

        @Override
        public void onClick(View view) {
            switch (view.getId())
            {

                case R.id.cardViewChat:
                    Intent intent=new Intent(context, ChatDetailsActivity.class);
                    intent.putExtra("chat_details", chatList.get(getAdapterPosition()));
                    context.startActivity(intent);
                    break;

            }

        }
    }

}
