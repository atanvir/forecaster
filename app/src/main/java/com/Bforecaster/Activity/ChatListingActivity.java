package com.Bforecaster.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.Bforecaster.Adapter.ChatListingAdapter;
import com.Bforecaster.Modal.ChatList;
import com.Bforecaster.Modal.Data;
import com.Bforecaster.R;
import com.Bforecaster.Retrofit.RetroInterface;
import com.Bforecaster.Retrofit.RetrofitInit;
import com.Bforecaster.Utility.GlobalVariables;
import com.Bforecaster.Utility.InternetCheck;
import com.Bforecaster.Utility.ProgressDailogHelper;
import com.Bforecaster.Utility.SharedPreferenceWriter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatListingActivity extends AppCompatActivity {
    @BindView(R.id.chatRV)
    RecyclerView chatRV;

    List<Data> chatList;
    ProgressDailogHelper dailogHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        dailogHelper=new ProgressDailogHelper(this,"");
        getchatListApi();

    }

    private void getchatListApi() {
        if(new InternetCheck(this).isConnect())
        {
            dailogHelper.showDailog();
            ChatList chatlist=new ChatList();
            chatlist.setForecasterId(SharedPreferenceWriter.getInstance(ChatListingActivity.this).getString(GlobalVariables._id));
            chatlist.setLangCode(SharedPreferenceWriter.getInstance(this).getString(GlobalVariables.langCode));
            RetroInterface api_service= RetrofitInit.getConnect().createConnection();
            Call<ChatList> call= api_service.getchatList(chatlist);
            call.enqueue(new Callback<ChatList>() {
                @Override
                public void onResponse(Call<ChatList> call, Response<ChatList> response) {
                    if(response.isSuccessful())
                    {
                        dailogHelper.dismissDailog();
                        if(response.body().getStatus().equalsIgnoreCase(GlobalVariables.SUCCESS))
                        {
                            chatList= response.body().getData();
                            chatRV.setLayoutManager(new LinearLayoutManager(ChatListingActivity.this));
                            ChatListingAdapter adapter=new ChatListingAdapter(ChatListingActivity.this,chatList);
                            chatRV.setAdapter(adapter);

                        }else if(response.body().getStatus().equalsIgnoreCase(GlobalVariables.FAILURE))
                        {
                            Toast.makeText(ChatListingActivity.this, response.body().getResponseMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ChatList> call, Throwable t) {

                }
            });


        }
        else
        {
            Toast toast=Toast.makeText(this, getString(R.string.check_internet), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
        }

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

}
