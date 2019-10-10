package com.forecaster.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.forecaster.Adapter.ChatListingAdapter;
import com.forecaster.Modal.Chatlist;
import com.forecaster.Modal.Data;
import com.forecaster.R;
import com.forecaster.Retrofit.RetroInterface;
import com.forecaster.Retrofit.RetrofitInit;
import com.forecaster.Utility.GlobalVariables;
import com.forecaster.Utility.InternetCheck;
import com.forecaster.Utility.ProgressDailogHelper;
import com.forecaster.Utility.SharedPreferenceWriter;

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
            Chatlist chatlist=new Chatlist();
            chatlist.setForecasterId(SharedPreferenceWriter.getInstance(ChatListingActivity.this).getString(GlobalVariables._id));
            RetroInterface api_service= RetrofitInit.getConnect().createConnection();
            Call<Chatlist> call= api_service.getchatList(chatlist);
            call.enqueue(new Callback<Chatlist>() {
                @Override
                public void onResponse(Call<Chatlist> call, Response<Chatlist> response) {
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
                public void onFailure(Call<Chatlist> call, Throwable t) {

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
