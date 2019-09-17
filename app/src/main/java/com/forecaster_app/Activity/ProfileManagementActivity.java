package com.forecaster_app.Activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.forecaster_app.Modal.ForcasterDetail;
import com.forecaster_app.Modal.ForcasterSetupProfile;
import com.forecaster_app.R;
import com.forecaster_app.Retrofit.RetroInterface;
import com.forecaster_app.Retrofit.RetrofitInit;
import com.forecaster_app.Utility.GlobalVariables;
import com.forecaster_app.Utility.InternetCheck;
import com.forecaster_app.Utility.ProgressDailogHelper;
import com.forecaster_app.Utility.SharedPreferenceWriter;
import com.forecaster_app.Utility.TakeImage;
import com.forecaster_app.Utility.UpdateForcasterBody;
import com.forecaster_app.Utility.Validation;
import com.forecaster_app.Utility.VideoCompressor.Util;
import com.forecaster_app.Utility.VideoCompressor.VideoCompress;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class ProfileManagementActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    @BindView(R.id.drawer) DrawerLayout drawer;
    @BindView(R.id.gender_spn) Spinner gender_spn;
    @BindView(R.id.categorytype_spn) Spinner categorytype_spn;
    @BindView(R.id.profile_iv) ImageView profile_iv;
    @BindView(R.id.camera_iv) ImageView camera_iv;
    @BindView(R.id.full_name_ed) EditText full_name_ed;
    @BindView(R.id.username_ed) EditText username_ed;
    @BindView(R.id.email_ed) EditText email_ed;
    @BindView(R.id.phone_ed) EditText phone_ed;
    @BindView(R.id.gender_txt) TextView gender_txt;
    @BindView(R.id.dob_ed) EditText dob_ed;
    @BindView(R.id.categorytype_txt) TextView categorytype_txt;
    @BindView(R.id.record_audio_iv) ImageView record_audio_iv;
    @BindView(R.id.stop_iv) ImageView stop_iv;
    @BindView(R.id.playaudio_iv) ImageView playaudio_iv;
    @BindView(R.id.timer_txt) TextView timer_txt;
    @BindView(R.id.upload_btn) Button upload_btn;
    @BindView(R.id.about_us_ed) EditText about_us_ed;
    @BindView(R.id.price_per_ed) EditText price_per_ed;
    @BindView(R.id.selectbank_txt) TextView selectbank_txt;
    @BindView(R.id.selectbank_spn) Spinner selectbank_spn;
    @BindView(R.id.account_holder_ed) EditText account_holder_ed;
    @BindView(R.id.bank_number_ed) EditText bank_number_ed;
    @BindView(R.id.image_thumnail) ImageView image_thumnail;
    @BindView(R.id.upload_iv) ImageView upload_iv;
    @BindView(R.id.seekBar) SeekBar seekBar;
    @BindView(R.id.progressbar) ProgressBar progressbar;
    @BindView(R.id.play_iv) ImageView play_iv;
    @BindView(R.id.videoview) VideoView videoview;
    @BindView(R.id.psychological_txt) TextView psychological_txt;
    @BindView(R.id.psychological_spn) Spinner psychological_spn;
    @BindView(R.id.psychological_cv) CardView psychological_cv;
    @BindView(R.id.save_btn) Button save_btn;
    @BindView(R.id.pause_iv) ImageView pause_iv;
    private final int CAMERA_PIC_REQUEST = 11, REQ_CODE_PICK_IMAGE = 1;
    private DatePickerDialog.OnDateSetListener datePickerListener;
    private Handler mHandler = new Handler();

    private static final int PICKFILE_REQUEST_CODE =18 ;
    String videoFile;
    private String outputDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
    Uri audio_uri,video_uri;
    MediaPlayer mediaPlayer;
    boolean playing;
    private Handler handler=new Handler();
    final int PERMISSION_REQUEST_CODE = 200;
    File video;
    ProgressDialog dialog;
    final int PERMISSION_REQUEST_CODE2 = 400;
    MediaRecorder mRecorder;
    File audio;
    int lastProgress,lastposition=0;
    CountDownTimer timer;
    final int REQUEST_VIDEO_CAPTURE = 132;
    private int START_VERIFICATION = 1001;
    String imagePath;
    File profile_image;
    boolean recording;
    List<String> genderlist;
    List<String> categoryList;
    List<String> bankList;
    List<String> psychologicalList;
    String audioFile;
    int lastplay_position=0;
    boolean pause=false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_management);
        ButterKnife.bind(this);
        new CategorySelectionActivity(this,drawer);
        init();



        getForecasterDetailsApi();
        dialog=new ProgressDialog(ProfileManagementActivity.this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(false);
        dialog.setTitle("Please wait ...!");
        dialog.setMessage("Compressing video");


        datePickerClick();
        spinnerClick();




    }

    private void datePickerClick() {

        datePickerListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MM-yyyy");
                Date date= null;
                try {
                    date = simpleDateFormat.parse(dayOfMonth+"-"+(month+1)+"-"+year);

                    dob_ed.setText(simpleDateFormat.format(date));

                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        };
    }

    private void spinnerClick() {
        gender_spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gender_txt.setText(genderlist.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        categorytype_spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 2) {
                    psychological_cv.setVisibility(View.VISIBLE);
                } else {
                    psychological_cv.setVisibility(View.GONE);
                }

                categorytype_txt.setText(categoryList.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });

        selectbank_spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectbank_txt.setText(bankList.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        psychological_spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                psychological_txt.setText(psychologicalList.get(position));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void init() {
        playaudio_iv.setOnClickListener(this::OnClick);
        stop_iv.setOnClickListener(this::OnClick);
        play_iv.setOnClickListener(this::OnClick);
        videoview.setOnClickListener(this::OnClick);
        camera_iv.setOnClickListener(this::OnClick);
        upload_btn.setOnClickListener(this::OnClick);
        record_audio_iv.setOnClickListener(this::OnClick);
        gender_txt.setOnClickListener(this::OnClick);
        categorytype_txt.setOnClickListener(this::OnClick);
        selectbank_txt.setOnClickListener(this::OnClick);
        dob_ed.setOnClickListener(this::OnClick);
        psychological_txt.setOnClickListener(this::OnClick);
        save_btn.setOnClickListener(this::OnClick);
        seekBar.setOnSeekBarChangeListener(this);
        pause_iv.setOnClickListener(this::OnClick);

    }

    private void getForecasterDetailsApi() {
        if(new InternetCheck(this).isConnect())
        {
            ProgressDailogHelper dailogHelper=new ProgressDailogHelper(this,"Setting Profile");
            dailogHelper.showDailog();
            RetroInterface api_service= RetrofitInit.getConnect().createConnection();
            ForcasterDetail forcasterDetail=new ForcasterDetail();
            forcasterDetail.setForecasterId(SharedPreferenceWriter.getInstance(this).getString(GlobalVariables._id));
            forcasterDetail.setLangCode("en");
            Call<ForcasterDetail> call=api_service.getForecasterDetails(forcasterDetail,SharedPreferenceWriter.getInstance(this).getString(GlobalVariables.jwtToken));
            call.enqueue(new Callback<ForcasterDetail>() {
                @Override
                public void onResponse(Call<ForcasterDetail> call, Response<ForcasterDetail> response) {
                    if(response.isSuccessful())
                    {

                        ForcasterDetail server_response=response.body();
                        if(server_response.getStatus().equalsIgnoreCase(GlobalVariables.SUCCESS))
                        {

                            Glide.with(ProfileManagementActivity.this).load(server_response.getData().getProfilePic()).into(profile_iv);
                            full_name_ed.setText(server_response.getData().getName());
                            username_ed.setText(server_response.getData().getUsername());
                            email_ed.setText(server_response.getData().getEmail());
                            phone_ed.setText(server_response.getData().getMobileNumber());
                            gender_txt.setText(server_response.getData().getGender());
                            dob_ed.setText(server_response.getData().getDob());
                            if(server_response.getData().getCategoryName().equalsIgnoreCase("Dreamer"))
                            {
                                psychological_cv.setVisibility(View.GONE);
                                categorytype_txt.setText(server_response.getData().getCategoryName());

                            }
                            else
                            {
                                psychological_cv.setVisibility(View.VISIBLE);
                                categorytype_txt.setText(getString(R.string.psychological_counselling));
                                psychological_txt.setText(server_response.getData().getCategoryName());

                            }
                            about_us_ed.setText(server_response.getData().getAboutUs());
                            price_per_ed.setText(server_response.getData().getPricePerQues());
                            selectbank_txt.setText(server_response.getData().getBankName());
                            account_holder_ed.setText(server_response.getData().getAccountHolderName());
                            bank_number_ed.setText(server_response.getData().getAccountNumber());



                            audio_uri=Uri.parse(server_response.getData().getVoiceRecording());
                            video_uri=Uri.parse(server_response.getData().getUploadedVideo());

                            settingSeekbar();

                            try {
                                upload_iv.setVisibility(View.GONE);
                                Bitmap bitmap_thumbnail=retriveVideoFrameFromVideo(server_response.getData().getUploadedVideo());
                                image_thumnail.setImageBitmap(bitmap_thumbnail);


                            } catch (Throwable throwable) {
                                throwable.printStackTrace();
                            }
                            dailogHelper.dismissDailog();





                        }
                        else if(server_response.getStatus().equalsIgnoreCase(GlobalVariables.FAILURE))
                        {
                            dailogHelper.dismissDailog();
                            Toast.makeText(ProfileManagementActivity.this,server_response.getResponseMessage(),Toast.LENGTH_LONG).show();

                        }
                    }
                }

                @Override
                public void onFailure(Call<ForcasterDetail> call, Throwable t) {

                }
            });

        }
        else
        {
            Toast.makeText(this,"Please check your internet connection",Toast.LENGTH_LONG).show();


        }
    }



    public Bitmap retriveVideoFrameFromVideo(String videoPath) throws Throwable
    {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try
        {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            if (Build.VERSION.SDK_INT >= 14)
                mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            else
                mediaMetadataRetriever.setDataSource(videoPath);
            //   mediaMetadataRetriever.setDataSource(videoPath);
            bitmap = mediaMetadataRetriever.getFrameAtTime();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Throwable("Exception in retriveVideoFrameFromVideo(String videoPath)" + e.getMessage());

        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }

    private void settingSeekbar() {
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(this, audio_uri);
            mediaPlayer.prepare();

        } catch (IOException e) {
            e.printStackTrace();
        }
        seekBar.setMax(300);

        seekBar.setProgress(mediaPlayer.getDuration() / 1000);
        if (mediaPlayer.getDuration() / 1000 < 10) {
            timer_txt.setText("0:0" + mediaPlayer.getDuration() / 1000);
        } else {
            timer_txt.setText("0:" + mediaPlayer.getDuration() / 1000 );
        }



    }


    @Override
    public void onBackPressed() {
        Intent intent=new Intent(ProfileManagementActivity.this,CategorySelectionActivity.class);
        finish();
        startActivity(intent);
    }

    @OnClick({R.id.menu_ll})
    void OnClick(View view)
    {
        switch (view.getId())
        {
            case R.id.menu_ll:
                drawer.openDrawer(GravityCompat.START);
                break;

            case R.id.stop_iv:
                stoppingAudio();
                break;


            case R.id.playaudio_iv:
                playingAudio();
                break;

            case R.id.play_iv:
                videoview.performClick();

                break;
            case R.id.videoview:
                videoview.setVisibility(View.VISIBLE);
                playingVideo();
                break;

            case R.id.camera_iv:
                SharedPreferenceWriter.getInstance(ProfileManagementActivity.this).writeStringValue("Camera","Yes");
                if (checkingPermission()) {
                    profileBottomLayout();
                }
                break;

            case R.id.record_audio_iv:
                if (checkingPermissionAudio()) {
                    recordingAudio();
                }
                break;

            case R.id.upload_btn:
                SharedPreferenceWriter.getInstance(ProfileManagementActivity.this).writeStringValue("Video","Yes");
                if (checkingPermission()) {
                    dispatchTakeVideoIntent();

                }
                break;
            case R.id.gender_txt:
                GenderSpinner();
                break;


            case R.id.categorytype_txt:
                CategorySpinner();
                break;

            case R.id.selectbank_txt:
                BankSpinner();
                break;

            case R.id.dob_ed:
                Date date= Calendar.getInstance().getTime();
                new DatePickerDialog(ProfileManagementActivity.this, AlertDialog.THEME_HOLO_LIGHT,datePickerListener,Integer.parseInt(new SimpleDateFormat("yyyy").format(date)),
                        Integer.parseInt(new SimpleDateFormat("MM").format(date))-1,Integer.parseInt(new SimpleDateFormat("dd").format(date))).show();


                break;
            case R.id.psychological_txt:
                PsychologicalSpinner();
                break;

            case R.id.save_btn:
                if(checkValidation())
                {
                    updateForcasterDetailApi();

                }


                break;

            case R.id.pause_iv:
                pauseAudio();
                break;




        }

    }

    private void pauseAudio() {
        pause=true;
        try
        {
            playaudio_iv.setVisibility(View.VISIBLE);
            pause_iv.setVisibility(View.GONE);
            if(mediaPlayer.isPlaying())
            {
                mediaPlayer.pause();
                lastplay_position=mediaPlayer.getCurrentPosition();
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }


    }

    private void updateForcasterDetailApi() {
        if(new InternetCheck(this).isConnect())
        {
            ProgressDailogHelper dailogHelper=new ProgressDailogHelper(this,"Updating Profile ...");
            dailogHelper.showDailog();
            RetroInterface api_service=RetrofitInit.getConnect().createConnection();
            ForcasterSetupProfile profile=new ForcasterSetupProfile();
            profile.setForecasterId(SharedPreferenceWriter.getInstance(this).getString(GlobalVariables._id));
            profile.setName(full_name_ed.getText().toString().trim());
            profile.setEmail(email_ed.getText().toString().trim());
            profile.setGender(gender_txt.getText().toString().trim());
            profile.setDob(dob_ed.getText().toString().trim());
            profile.setAboutUs(about_us_ed.getText().toString().trim());
            if(categorytype_txt.getText().toString().equalsIgnoreCase("Dreamer"))
            {
                profile.setCategoryName(categorytype_txt.getText().toString());
            }
            else
            {
                profile.setCategoryName(psychological_txt.getText().toString());
            }

            profile.setPricePerQues(Integer.valueOf(price_per_ed.getText().toString()));
//            profile.setBankName(selectbank_txt.getText().toString());
//            profile.setAccountHolderName(account_holder_ed.getText().toString());
//            profile.setAccountNumber(bank_number_ed.getText().toString());

            UpdateForcasterBody body=new UpdateForcasterBody(profile);

            RequestBody profile_body;
            MultipartBody.Part prfpic=null;
            if (imagePath != null) {
                File file = new File(imagePath);
                profile_body=RequestBody.create(MediaType.parse("image/*"), file);
                prfpic=MultipartBody.Part.createFormData("profilePic", file.getName(), profile_body);
            }

            RequestBody video_body;
            MultipartBody.Part videoattached=null;
            if(videoFile!=null)
            {
                File file=new File(videoFile);
                video_body=RequestBody.create(MediaType.parse("*/*"),file);
                videoattached=MultipartBody.Part.createFormData("uploadedVideo",file.getName(),video_body);

            }

            RequestBody voice_body;
            MultipartBody.Part voiceAttached=null;
            if(audioFile!=null)
            {
                File file=new File(audioFile);
                voice_body=RequestBody.create(MediaType.parse("*/*"),file);
                voiceAttached=MultipartBody.Part.createFormData("voiceRecording",file.getName(),voice_body);

            }

            Call<ForcasterSetupProfile> call=api_service.forecasterUpdateProfile(prfpic,videoattached,voiceAttached,body.getBody());
            call.enqueue(new Callback<ForcasterSetupProfile>() {
                @Override
                public void onResponse(Call<ForcasterSetupProfile> call, Response<ForcasterSetupProfile> response) {
                    if(response.isSuccessful())
                    {
                        dailogHelper.dismissDailog();
                        ForcasterSetupProfile server_response=response.body();
                        if(server_response.getStatus().equalsIgnoreCase(GlobalVariables.SUCCESS))
                        {
                            Toast toast=Toast.makeText(ProfileManagementActivity.this,server_response.getResponseMessage(),Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER,0,0);
                            toast.show();
                            Intent intent=new Intent(ProfileManagementActivity.this,CategorySelectionActivity.class);
                            finish();
                            startActivity(intent);

                        }
                        else if(server_response.getStatus().equalsIgnoreCase(GlobalVariables.FAILURE))
                        {
                            Toast toast=Toast.makeText(ProfileManagementActivity.this,server_response.getResponseMessage(),Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER,0,0);
                            toast.show();

                        }

                    }
                }

                @Override
                public void onFailure(Call<ForcasterSetupProfile> call, Throwable t) {

                }
            });


        }
        else
        {
            Toast toast=Toast.makeText(this,"Please check your internet connection",Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();

        }



    }

    private boolean checkValidation() {
        boolean ret=true;

        if(gender_txt.getText().toString().equalsIgnoreCase("Gender"))
        {
            ret=false;
            gender_txt.setError("Please Select");
            gender_txt.setFocusable(true);
            gender_txt.requestFocus();
        }
        if(!Validation.hasText(dob_ed)) ret=false;
        if(categorytype_txt.getText().toString().equalsIgnoreCase("Category Type"))
        {
            ret=false;
            categorytype_txt.setError("Please Select");
            categorytype_txt.setFocusable(true);
            categorytype_txt.requestFocus();
        }
        if(categorytype_txt.getText().toString().equalsIgnoreCase("Psychological Counselling")) {
            if (psychological_txt.getText().toString().equalsIgnoreCase("Please select")) {
                ret = false;
                psychological_txt.setError("Please Select");
                psychological_txt.setFocusable(true);
                psychological_txt.requestFocus();
            }
        }



        if(selectbank_txt.getText().toString().equalsIgnoreCase("Select Bank"))
        {
            ret=false;
            selectbank_txt.setError("Please select bank");
            selectbank_txt.setFocusable(true);
            selectbank_txt.requestFocus();
        }

        if(!Validation.hasText(price_per_ed)) ret=false;
        if(!Validation.hasText(account_holder_ed)) ret=false;
        if(bank_number_ed.getText().toString().isEmpty() || bank_number_ed.getText().toString().length()!=16)
        {
            if(!bank_number_ed.getText().toString().isEmpty())
            {
                ret=false;
                bank_number_ed.setError("Please enter account number");
                bank_number_ed.setFocusable(true);
                bank_number_ed.requestFocus();

            }
            else if(bank_number_ed.getText().toString().length()!=16)
            {
                ret=false;
                bank_number_ed.setError("Please enter valid account number");
                bank_number_ed.setFocusable(true);
                bank_number_ed.requestFocus();

            }

        }

        if(!Validation.email(email_ed)) ret=false;

        if(!Validation.hasText(about_us_ed)) ret=false;

        return ret;

    }


    private void PsychologicalSpinner() {
        psychologicalList = new ArrayList<>();
        psychologicalList.add("Please select");
        psychologicalList.add("Self Development Counselling");
        psychologicalList.add("Family Counselling");
        psychologicalList.add("Psychological Counselling");
        psychologicalList.add("Parent Counselling");


        ArrayAdapter genderArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, psychologicalList) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0)
                    return false;
                else
                    return true;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                return super.getDropDownView(position, convertView, parent);
            }

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                TextView tv = (TextView) super.getView(position, convertView, parent);

                // Set the text color of spinner item
                tv.setTextColor(Color.TRANSPARENT);

                // Return the view
                return tv;
            }
        };

        genderArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        psychological_spn.setAdapter(genderArrayAdapter);
        psychological_spn.performClick();
    }

    private void BankSpinner() {
        bankList = new ArrayList<>();
        bankList.add("Please select");
        bankList.add("State Bank Of India");
        bankList.add("Punjab National Bank");
        bankList.add("ICIC Bank");


        ArrayAdapter genderArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, bankList) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0)
                    return false;
                else
                    return true;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                return super.getDropDownView(position, convertView, parent);
            }

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                TextView tv = (TextView) super.getView(position, convertView, parent);

                // Set the text color of spinner item
                tv.setTextColor(Color.TRANSPARENT);

                // Return the view
                return tv;
            }
        };

        genderArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectbank_spn.setAdapter(genderArrayAdapter);
        selectbank_spn.performClick();

    }

    private void CategorySpinner() {
        categoryList = new ArrayList<>();
        categoryList.add("Please select");
        categoryList.add("Dreamer");
        categoryList.add("Psychological Counselling");



        ArrayAdapter genderArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categoryList) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0)
                    return false;
                else
                    return true;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                return super.getDropDownView(position, convertView, parent);
            }

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                TextView tv = (TextView) super.getView(position, convertView, parent);

                // Set the text color of spinner item
                tv.setTextColor(Color.TRANSPARENT);

                // Return the view
                return tv;
            }
        };
        genderArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorytype_spn.setAdapter(genderArrayAdapter);
        categorytype_spn.performClick();


    }


    private void GenderSpinner() {
        genderlist = new ArrayList<>();
        genderlist.add("Please select");
        genderlist.add("Male");
        genderlist.add("Female");

        ArrayAdapter genderArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, genderlist) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0)
                    return false;
                else
                    return true;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                return super.getDropDownView(position, convertView, parent);
            }

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                TextView tv = (TextView) super.getView(position, convertView, parent);

                // Set the text color of spinner item
                tv.setTextColor(Color.TRANSPARENT);

                // Return the view
                return tv;
            }
        };

        genderArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender_spn.setAdapter(genderArrayAdapter);
        gender_spn.performClick();
    }

    private void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);



    }


    private void recordingAudio() {
        stop_iv.setVisibility(View.VISIBLE);
        recording=true;
        playing=false;
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        /**In the lines below, we create a directory VoiceRecorderSimplifiedCoding/Audios in the phone storage
         * and the audios are being stored in the Audios folder **/
        File root = android.os.Environment.getExternalStorageDirectory();
        File file = new File(root.getAbsolutePath() + "/VoiceRecorderSimplifiedCoding/Audios");
        if (!file.exists()) {
            file.mkdirs();
        }

        audioFile =  root.getAbsolutePath() + "/VoiceRecorderSimplifiedCoding/Audios/" +
                String.valueOf(System.currentTimeMillis() + ".mp3");
        Log.d("filename",audioFile);
        mRecorder.setOutputFile(audioFile);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
            mRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        stopPlaying();
        lastProgress = 0;
        seekBar.setProgress(0);
        seekBar.setMax(300);
        lastposition=0;
        seekUpdation2();
        timer=new CountDownTimer(31500, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {


            }

            @Override
            public void onFinish() {
                stopPlaying();
                Toast toast=Toast.makeText(ProfileManagementActivity.this,"You can record upto 30 secounds",Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
            }
        }.start();



    }
    Runnable newthread=new Runnable() {
        @Override
        public void run() {
            if(recording) {
                seekUpdation2();
            }
        }
    };

    private void seekUpdation2() {
        lastposition++;
        seekBar.setProgress(lastposition);
        mHandler.postDelayed(newthread,100);
    }

    private void stopPlaying() {
        try{
            if(mediaPlayer !=null) {
                mediaPlayer.release();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        mediaPlayer = null;
        //showing the play button

    }


    private boolean checkingPermissionAudio() {
        boolean ret = true;
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
            ) {
                ret = false;


                requestPermissions(new String[]
                        {
                                Manifest.permission.RECORD_AUDIO,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                        }, PERMISSION_REQUEST_CODE2);

            } else {

                ret = true;
            }
        }
        return ret;

    }


    private void profileBottomLayout() {
        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.update_pic_layout, null);
        ImageView camera = (ImageView) popupView.findViewById(R.id.camera);
        ImageView gallery = (ImageView) popupView.findViewById(R.id.gallery);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Upload photo");
        alertDialog.setView(popupView);
        final AlertDialog dialog = alertDialog.show();
        alertDialog.setCancelable(true);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    dialog.dismiss();
                    Intent intent = new Intent(ProfileManagementActivity.this, TakeImage.class);
                    intent.putExtra("from", "camera");
                    startActivityForResult(intent, CAMERA_PIC_REQUEST);
                } catch (Exception ex) {
                    Log.d("exp_result:", ex.getMessage().toString());
                }
            }


        });
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileManagementActivity.this, TakeImage.class);
                intent.putExtra("from", "gallery");
                startActivityForResult(intent, REQ_CODE_PICK_IMAGE);
                dialog.dismiss();
            }
        });

    }


    private boolean checkingPermission() {
        boolean ret=true;
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, CAMERA) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
            ) {
                ret=false;


                requestPermissions(new String[]{CAMERA, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);




            } else {

                ret=true;
            }
        }
        return ret;
    }


    private void playingVideo() {
        try {
            progressbar.setVisibility(View.VISIBLE);
            play_iv.setVisibility(View.GONE);
            MediaController mediacontroller = new MediaController(ProfileManagementActivity.this);
            mediacontroller.setAnchorView(videoview);
            videoview.setMediaController(mediacontroller);
            videoview.setVideoURI(video_uri);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();

        }

        videoview.requestFocus();
        videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            public void onPrepared(MediaPlayer mp) {
                videoview.start();
                play_iv.setVisibility(View.GONE);
                progressbar.setVisibility(View.GONE);



            }
        });

        videoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            public void onCompletion(MediaPlayer mp) {
                play_iv.setVisibility(View.VISIBLE);

            }
        });

    }


    private void playingAudio() {
        try {
            stop_iv.setVisibility(View.VISIBLE);
            pause_iv.setVisibility(View.VISIBLE);
            playaudio_iv.setVisibility(View.GONE);
            if(timer!=null) {
                timer.cancel();

            }
            recording=false;
            playing=true;
            mediaPlayer = new MediaPlayer();
            if(audioFile!=null) {
                mediaPlayer.setDataSource(audioFile);
            }else
            {
                mediaPlayer.setDataSource(this,audio_uri);
            }
            mediaPlayer.prepare();
            if(pause)
            {
                mediaPlayer.seekTo(lastplay_position);
            }
            mediaPlayer.start();
            seekBar.setProgress(0);
            seekBar.setMax(mediaPlayer.getDuration());
            seekBar.setClickable(false);
            seekUpdation();

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    playaudio_iv.setVisibility(View.VISIBLE);
                    pause_iv.setVisibility(View.GONE);
                    pause=false;

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            seekUpdation();
        }
    };

    private void seekUpdation() {
        if(playing) {
            if (mediaPlayer != null) {
                int mCurrentPosition = mediaPlayer.getCurrentPosition();
                seekBar.setProgress(mCurrentPosition);
            }
            handler.postDelayed(runnable, 100);
        }
    }

    private void stoppingAudio() {

        try {
            mRecorder.stop();
            mRecorder.release();

        } catch (Exception e) {
            e.printStackTrace();
        }
        try
        {
            recording=false;
            playing=false;
//                mediaPlayer = new MediaPlayer();
//                mediaPlayer.setDataSource(this, audio_uri);
//                mediaPlayer.prepare();
//                if (mediaPlayer.getDuration() / 1000 < 10) {
//                    timer_txt.setText("0:0" + mediaPlayer.getDuration() / 1000);
//                } else {
//                    timer_txt.setText("" + mediaPlayer.getDuration() / 1000 + ":00");
//                }
            mediaPlayer.release();
            if(timer!=null) {
                timer.cancel();
            }

        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            String selectedVideoPath = getPath3(data.getData());
            try
            {
                if(selectedVideoPath == null) {
                    Log.e("no video","Selected null");
                } else {
                    video_uri=Uri.parse(selectedVideoPath);
                    videoview.setVideoURI(video_uri);
                    video=new File(video_uri.getPath());
                    videoFile = outputDir+File.separator + "VID_" + new SimpleDateFormat("yyyyMMdd_HHmmss", getLocale()).format(new Date()) + ".mp4";
                    VideoCompress.compressVideoLow(video.getPath(),videoFile, new VideoCompress.CompressListener() {
                        @Override
                        public void onStart() {
                            Util.writeFile(ProfileManagementActivity.this, "Start at: " + new SimpleDateFormat("HH:mm:ss", getLocale()).format(new Date()) + "\n");
                        }

                        @Override
                        public void onSuccess() {
                            if(dialog.isShowing())
                            {
                                dialog.dismiss();
                            }

                            Util.writeFile(ProfileManagementActivity.this, "End at: " + new SimpleDateFormat("HH:mm:ss", getLocale()).format(new Date()) + "\n");
                            //  Util.writeFile(ProfileSetupActivity.this, "Total: " + ((endTime - startTime)/1000) + "s" + "\n");
                            Util.writeFile(ProfileManagementActivity.this);

                        }

                        @Override
                        public void onFail() {
                            Util.writeFile(ProfileManagementActivity.this, "Failed Compress!!!" + new SimpleDateFormat("HH:mm:ss", getLocale()).format(new Date()));
                        }

                        @Override
                        public void onProgress(float percent) {
//                           tv_progress.setText(String.valueOf(percent) + "%");
                            dialog.setMax((int) percent);
                            dialog.show();



                        }
                    });

//                    upload_im.setVisibility(View.GONE);
                    play_iv.setVisibility(View.VISIBLE);
                    videoview.seekTo(1);

                }


            } catch (Exception e)
            {
                e.printStackTrace();
            }



        }

        else if (requestCode == START_VERIFICATION) {
            if (resultCode == RESULT_OK) {
                setResult(RESULT_OK);
                finish();
            }
        } else if (resultCode == RESULT_OK) {
            if (data.getStringExtra("filePath") != null) {
                imagePath = data.getStringExtra("filePath");
                profile_image = new File(data.getStringExtra("filePath"));

                if (profile_image.exists() && profile_image != null) {
                    profile_iv.setImageURI(Uri.fromFile(profile_image));
                }
            }
        } else if (requestCode == 1 && resultCode == RESULT_CANCELED) {
            finish();


        }


        super.onActivityResult(requestCode, resultCode, data);
    }




    private String getPath3(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if(cursor!=null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        else return null;
    }

    @SuppressWarnings("deprecation")
    public static Locale getSystemLocaleLegacy(Configuration config){
        return config.locale;
    }

    @TargetApi(Build.VERSION_CODES.N)
    public static Locale getSystemLocale(Configuration config){
        return config.getLocales().get(0);
    }
    private Locale getLocale() {
        Configuration config = getResources().getConfiguration();
        Locale sysLocale = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            sysLocale = getSystemLocale(config);
        } else {
            sysLocale = getSystemLocaleLegacy(config);
        }

        return sysLocale;
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean readAccepted = grantResults[2] == PackageManager.PERMISSION_GRANTED;

                    if (cameraAccepted && writeAccepted && readAccepted) {
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_LONG).show();
                        if(SharedPreferenceWriter.getInstance(ProfileManagementActivity.this).getString("Camera").equalsIgnoreCase("Yes"))
                        {
                            profileBottomLayout();
                        }
                        else if(SharedPreferenceWriter.getInstance(ProfileManagementActivity.this).getString("Video").equalsIgnoreCase("Yes"))
                        {
                            dispatchTakeVideoIntent();
                        }



                    }else {

                        Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show();
                    }
                }
                break;

            case PERMISSION_REQUEST_CODE2:
                if(grantResults.length>0)
                {
                    boolean audioaccepted=grantResults[0]==PackageManager.PERMISSION_GRANTED;
                    boolean readAccepted=grantResults[1]==PackageManager.PERMISSION_GRANTED;
                    boolean writeAccepted=grantResults[2]==PackageManager.PERMISSION_GRANTED;

                    if(audioaccepted && readAccepted && writeAccepted)
                    {
                        Toast.makeText(ProfileManagementActivity.this,"Recording Started",Toast.LENGTH_LONG).show();
                        recordingAudio();
                    }
                    else
                    {
                        Toast.makeText(ProfileManagementActivity.this,"Permission Denied",Toast.LENGTH_LONG).show();

                    }
                }


                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if(playing) {
            if (progress / 1000 < 10) {
                timer_txt.setText("0:0" + progress / 1000);
            } else {
                timer_txt.setText("0:" + progress / 1000);
            }
        }
        else
        {
            if (progress / 10 < 10) {
                timer_txt.setText("0:0" + progress / 10);
            } else {
                timer_txt.setText("0:" + progress / 10);
            }

        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
