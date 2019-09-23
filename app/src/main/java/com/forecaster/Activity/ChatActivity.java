package com.forecaster.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.forecaster.Adapter.ChatAdapter;
import com.forecaster.Modal.Chat;
import com.forecaster.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChatActivity extends AppCompatActivity {
    @BindView(R.id.chatRV)
    RecyclerView chatRV;

    List<Chat> chatList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        SettingListValues();

        chatRV.setLayoutManager(new LinearLayoutManager(this));
        ChatAdapter adapter=new ChatAdapter(this,chatList);
        chatRV.setAdapter(adapter);
    }

    @OnClick(R.id.back_ll)
    void OnClick(View view)
    {
        switch (view.getId())
        {
            case R.id.back_ll:
                finish();
                break;
        }

    }

    @Override
    public void onBackPressed() {
       finish();
    }

    private void SettingListValues() {
        chatList=new ArrayList<>();
        chatList.add(new Chat(R.drawable.img_one));
        chatList.add(new Chat(R.drawable.img_two));
        chatList.add(new Chat(R.drawable.img_three));
        chatList.add(new Chat(R.drawable.img_four));
        chatList.add(new Chat(R.drawable.img_five));

    }
}
