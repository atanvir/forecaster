package com.forecaster_app.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.forecaster_app.Activity.ChatDetailsActivity;
import com.forecaster_app.Modal.Chat;
import com.forecaster_app.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {
    private Context context;
    private List<Chat> chatList;

    public ChatAdapter(Context context,List<Chat> chatList)
    {
        this.context=context;
        this.chatList=chatList;
    }


    @NonNull
    @Override
    public ChatAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.adapter_chat,parent,false);
        return new ChatAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.MyViewHolder holder, int position) {
        holder.image_im.setImageResource(chatList.get(position).getImage());

    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image_im)
        ImageView image_im;

        @BindView(R.id.cardViewChat)
        CardView cardViewChat;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

            cardViewChat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, ChatDetailsActivity.class);
                    context.startActivity(intent);


                }
            });

        }
    }
}
