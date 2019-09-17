package com.forecaster_app.Activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.ThumbnailUtils;
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
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.forecaster_app.Modal.Data;
import com.forecaster_app.Modal.ForcasterSetupProfile;
import com.forecaster_app.Modal.Login;
import com.forecaster_app.Modal.Signup;
import com.forecaster_app.R;
import com.forecaster_app.Retrofit.RetroInterface;
import com.forecaster_app.Retrofit.RetrofitInit;
import com.forecaster_app.Utility.AddServiceBody;
import com.forecaster_app.Utility.ContextHelper;
import com.forecaster_app.Utility.DailogBox;
import com.forecaster_app.Utility.GlobalVariables;
import com.forecaster_app.Utility.MyVideoView;
import com.forecaster_app.Utility.ProgressDailogHelper;
import com.forecaster_app.Utility.SharedPreferenceWriter;
import com.forecaster_app.Utility.TakeImage;
import com.forecaster_app.Utility.Validation;
import com.forecaster_app.Utility.VideoCompressor.Util;
import com.forecaster_app.Utility.VideoCompressor.VideoCompress;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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

public class ProfileSetupActivity extends AppCompatActivity {

    private static final int PICKFILE_REQUEST_CODE =18 ;
    @BindView(R.id.gender_spn) Spinner gender_spn;
    @BindView(R.id.categorytype_spn) Spinner categorytype_spn;
    @BindView(R.id.documenttype_spn) Spinner documenttype_spn;
    @BindView(R.id.selectbank_spn) Spinner selectbank_spn;
    @BindView(R.id.gender_txt) TextView gender_txt;
    @BindView(R.id.categorytype_txt) TextView categorytype_txt;
    @BindView(R.id.documenttype_txt) TextView documenttype_txt;
    @BindView(R.id.selectbank_txt) TextView selectbank_txt;
    @BindView(R.id.back_ll) LinearLayout back_ll;
    @BindView(R.id.save_btn) Button save_btn;
    @BindView(R.id.profile_iv) ImageView profile_iv;
    @BindView(R.id.camera_iv) ImageView camera_iv;
    @BindView(R.id.psychological_cv) CardView psychological_cv;
    @BindView(R.id.psychological_spn) Spinner psychological_spn;
    @BindView(R.id.psychological_txt) TextView psychological_txt;
    @BindView(R.id.upload_im) ImageView upload_im;
    @BindView(R.id.play_im) ImageView play_im;
    @BindView(R.id.videoview) VideoView videoview;
    @BindView(R.id.mic_im) LottieAnimationView mic_im;
    @BindView(R.id.seekbar) SeekBar seekbar;
    @BindView(R.id.stop_ll) LinearLayout stop_ll;
    @BindView(R.id.playaudio_ll) LinearLayout playaudio_ll;
    @BindView(R.id.chromometer) Chronometer chromometer;
    @BindView(R.id.dob_ed) EditText dob_ed;
    @BindView(R.id.pause_ll) LinearLayout pause_ll;
    final int PERMISSION_REQUEST_CODE = 200;
    final int PERMISSION_REQUEST_CODE2 = 400;

    List<String> genderlist;
    List<String> categoryList;
    List<String> documentlist;
    List<String> bankList;
    List<String> psychologicalList;
    private Context context1;
    private final int CAMERA_PIC_REQUEST = 11, REQ_CODE_PICK_IMAGE = 1;
    private File fileFlyer;
    private String imagePath = null;
    private int START_VERIFICATION = 1001;
    final int REQUEST_VIDEO_CAPTURE = 132;
    String videoFile;
    Uri videoUri;
    private MediaRecorder mRecorder;
    private MediaPlayer mPlayer;
    private String fileName = null;
    private int lastProgress = 0;
    private Handler mHandler = new Handler();
    private Handler mHandler2=new Handler();
    private boolean isPlaying = false;
    @BindView(R.id.attach_doc_im) ImageView attach_doc_im;
    private DatePickerDialog.OnDateSetListener datePickerListener;
    File attach_document,audio,video,profile_image;
    @BindView(R.id.attach_doc_txt) TextView attach_doc_txt;
    @BindView(R.id.record_audio_txt) TextView record_audio_txt;
    @BindView(R.id.price_per_ed) EditText price_per_ed;
    @BindView(R.id.account_holder_ed) EditText account_holder_ed;
    @BindView(R.id.bank_number_ed) EditText bank_number_ed;
    @BindView(R.id.about_us_ed) EditText about_us_ed;
    int lastposition=0;
    CountDownTimer timer;
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    Boolean recording,playing;
    @BindView(R.id.upload_video_btn) Button upload_video_btn;
    @BindView(R.id.forcaster_name_txt) TextView forcaster_name_txt;
    ProgressDialog  dialog;
    private String outputDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
    int lastplay_position=0;
    boolean pause=false;
    private ProgressDailogHelper dailogHelper;





    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setup);
        ButterKnife.bind(this);
        init();
        spinnerClick();
        dialog=new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle(getString(R.string.please_wait));
        dialog.setMessage(getString(R.string.compressing_video));
        dialog.setCancelable(false);




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

        documenttype_spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                documenttype_txt.setText(documentlist.get(position));
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
        back_ll.setOnClickListener(this::OnClick);
        save_btn.setOnClickListener(this::OnClick);
        gender_txt.setOnClickListener(this::OnClick);
        categorytype_txt.setOnClickListener(this::OnClick);
        documenttype_txt.setOnClickListener(this::OnClick);
        selectbank_txt.setOnClickListener(this::OnClick);
        camera_iv.setOnClickListener(this::OnClick);
        psychological_txt.setOnClickListener(this::OnClick);
        upload_im.setOnClickListener(this::OnClick);
        play_im.setOnClickListener(this::OnClick);
        mic_im.setOnClickListener(this::OnClick);
        stop_ll.setOnClickListener(this::OnClick);
        playaudio_ll.setOnClickListener(this::OnClick);
        attach_doc_im.setOnClickListener(this::OnClick);
        dob_ed.setOnClickListener(this::OnClick);
        upload_video_btn.setOnClickListener(this::OnClick);
        pause_ll.setOnClickListener(this::OnClick);
        forcaster_name_txt.setText(SharedPreferenceWriter.getInstance(ProfileSetupActivity.this).getString(GlobalVariables.forcaster_name));
        dailogHelper=new ProgressDailogHelper(this,"");
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ProfileSetupActivity.this, SignupActivity.class);
        finish();
        startActivity(intent);
    }

    @OnClick()
    void OnClick(View view) {
        switch (view.getId()) {
            case R.id.back_ll:
                Intent intent = new Intent(ProfileSetupActivity.this, SignupActivity.class);
                finish();
                startActivity(intent);
                break;


            case R.id.save_btn:
                if(checkValidation())
                {
                    forecasterSetupProfileApi();
                }

                break;
            case R.id.gender_txt:
                GenderSpinner();

                break;
            case R.id.categorytype_txt:
                CategorySpinner();

                break;
            case R.id.documenttype_txt:
                DocumentSpinner();

                break;
            case R.id.selectbank_txt:
                BankSpinner();

                break;
            case R.id.camera_iv:
                SharedPreferenceWriter.getInstance(ProfileSetupActivity.this).writeStringValue("Camera","Yes");
                if (checkingPermission()) {
                    profileBottomLayout();
                }

                break;


            case R.id.psychological_txt:
                PsychologicalSpinner();


                break;
            case R.id.upload_video_btn:
                SharedPreferenceWriter.getInstance(ProfileSetupActivity.this).writeStringValue("Video","Yes");

                if (checkingPermission()) {
                    dispatchTakeVideoIntent();

                }

                break;

            case R.id.play_im:
                settingThumbnail();
                break;
            case R.id.mic_im:
                if (checkingPermissionAudio()) {
                    recordingAudio();
                }
                break;

            case R.id.stop_ll:
                 stoppingAudio();
                break;

            case R.id.playaudio_ll:
                playingAudio();
                break;

            case R.id.attach_doc_im:
                SharedPreferenceWriter.getInstance(ProfileSetupActivity.this).writeStringValue("Recording","Yes");

                if(checkingPermission()) {
                    intentForAttachDocument();
                }

                break;
            case R.id.dob_ed:
                Date date= Calendar.getInstance().getTime();
                new DatePickerDialog(ProfileSetupActivity.this, AlertDialog.THEME_HOLO_LIGHT,datePickerListener,Integer.parseInt(new SimpleDateFormat("yyyy").format(date)),
                        Integer.parseInt(new SimpleDateFormat("MM").format(date))-1,Integer.parseInt(new SimpleDateFormat("dd").format(date))).show();

                break;

            case R.id.pause_ll:
                pauseAudio();
                break;
        }

    }

    private void pauseAudio() {
        try
        {
            pause=true;
            playaudio_ll.setVisibility(View.VISIBLE);
            pause_ll.setVisibility(View.GONE);
            if(mPlayer.isPlaying())
            {
                mPlayer.pause();
                lastplay_position=mPlayer.getCurrentPosition();
            }

        }catch (Exception e)
        {

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
        if(documenttype_txt.getText().toString().equalsIgnoreCase("Document Type"))
        {
            ret=false;
            documenttype_txt.setError("Please Select");
            documenttype_txt.setFocusable(true);
            documenttype_txt.requestFocus();
        }
        if(attach_document==null)
        {
            ret=false;
            attach_doc_txt.setError("Please attach document");
            attach_doc_txt.setFocusable(true);
            attach_doc_txt.requestFocus();
        }
        if(audio==null)
        {
            ret=false;
            record_audio_txt.setError("Please record you voice");
            record_audio_txt.setFocusable(true);
            record_audio_txt.requestFocus();

        }
        if(video==null)
        {
            ret=false;
            Toast.makeText(ProfileSetupActivity.this,"Please upload video",Toast.LENGTH_LONG).show();
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


        if(profile_image==null)
        {
            ret=false;
            Toast.makeText(ProfileSetupActivity.this,"Please Upload Profile Photo",Toast.LENGTH_LONG).show();
        }
        if(!Validation.hasText(about_us_ed)) ret=false;

        return ret;

    }

    private void forecasterSetupProfileApi() {
        dailogHelper.showDailog();
        RetroInterface api_service= RetrofitInit.getConnect().createConnection();
        ForcasterSetupProfile profile=new ForcasterSetupProfile();
        profile.setForecasterId(SharedPreferenceWriter.getInstance(ProfileSetupActivity.this).getString(GlobalVariables._id));
        //profile.setForecasterId("5d6e0ec0e9ee7170b6ce2e6b");
        profile.setGender(gender_txt.getText().toString());
        profile.setDob(dob_ed.getText().toString());
        if(categorytype_txt.getText().toString().equalsIgnoreCase(getString(R.string.psychological_counselling)))
        {
            profile.setCategoryName(psychological_txt.getText().toString());

        }
        else
        {
            profile.setCategoryName(categorytype_txt.getText().toString());

        }
        profile.setAboutUs(about_us_ed.getText().toString().trim());
        profile.setPricePerQues(Integer.valueOf(price_per_ed.getText().toString().trim()));
        profile.setBankName(selectbank_txt.getText().toString());
        profile.setAccountHolderName(account_holder_ed.getText().toString().trim());
        profile.setAccountNumber(bank_number_ed.getText().toString());
        profile.setDocumentType(documenttype_txt.getText().toString());
        AddServiceBody body=new AddServiceBody(profile);

        RequestBody profile_body;
        MultipartBody.Part prfpic = null;

        if (imagePath != null) {
            File file = new File(imagePath);
            profile_body = RequestBody.create(MediaType.parse("image/*"), file);
            prfpic = MultipartBody.Part.createFormData("profilePic", file.getName(), profile_body);
        }
        File video=new File(videoFile);
        RequestBody video_body=RequestBody.create(MediaType.parse("*/*"),video);
        MultipartBody.Part uploadVideo=MultipartBody.Part.createFormData("uploadedVideo",video.getName(),video_body);

        RequestBody audio_body=RequestBody.create(MediaType.parse("*/*"),audio);
        MultipartBody.Part voiceRecording=MultipartBody.Part.createFormData("voiceRecording",audio.getName(),audio_body);

        RequestBody attachDocumentBody=RequestBody.create(MediaType.parse("application/pdf"),attach_document);
        MultipartBody.Part attachedDocument=MultipartBody.Part.createFormData("attachedDocument",attach_document.getName(),attachDocumentBody);


        Call<ForcasterSetupProfile> call=api_service.forecasterSetupProfile(prfpic,attachedDocument,voiceRecording,uploadVideo,body.getBody());
        call.enqueue(new Callback<ForcasterSetupProfile>() {
            @Override
            public void onResponse(Call<ForcasterSetupProfile> call, Response<ForcasterSetupProfile> response) {
                if(response.isSuccessful())
                {
                    dailogHelper.dismissDailog();
                    ForcasterSetupProfile server_response=response.body();
                    if(server_response.getStatus().equalsIgnoreCase("SUCCESS"))
                    {
                        Intent intent=new Intent(ProfileSetupActivity.this,CategorySelectionActivity.class);

                        Toast.makeText(ProfileSetupActivity.this,server_response.getResponseMessage(),Toast.LENGTH_LONG).show();
                        setPreferences(server_response);
                        startActivity(intent);
                        finish();
                    }
                    else if(server_response.getStatus().equalsIgnoreCase("FAILURE"))
                    {
                        Toast.makeText(ProfileSetupActivity.this,server_response.getResponseMessage(),Toast.LENGTH_LONG).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<ForcasterSetupProfile> call, Throwable t) {

            }
        });


    }

    private void setPreferences(ForcasterSetupProfile server_response) {
        SharedPreferenceWriter.getInstance(ProfileSetupActivity.this).writeStringValue(GlobalVariables.profilePic,server_response.getData().getProfilePic());
        SharedPreferenceWriter.getInstance(ProfileSetupActivity.this).writeBooleanValue(GlobalVariables.notificationStatus,server_response.getData().getNotificationStatus());
        SharedPreferenceWriter.getInstance(ProfileSetupActivity.this).writeBooleanValue(GlobalVariables.profileSetup,server_response.getData().getProfileSetup());
        SharedPreferenceWriter.getInstance(ProfileSetupActivity.this).writeIntValue(GlobalVariables.totalRating,server_response.getData().getTotalRating());
        SharedPreferenceWriter.getInstance(ProfileSetupActivity.this).writeIntValue(GlobalVariables.avgRating,server_response.getData().getAvgRating());
        SharedPreferenceWriter.getInstance(ProfileSetupActivity.this).writeBooleanValue(GlobalVariables.onlineStatus,server_response.getData().getOnlineStatus());
        SharedPreferenceWriter.getInstance(ProfileSetupActivity.this).writeStringValue(GlobalVariables.responseTime,server_response.getData().getResponseTime());
        SharedPreferenceWriter.getInstance(ProfileSetupActivity.this).writeIntValue(GlobalVariables.pendingQueue,server_response.getData().getPendingQueue());
//        SharedPreferenceWriter.getInstance(ProfileSetupActivity.this).writeIntValue(GlobalVariables.totalPoints,server_response.getData().getTotalPoints());
        SharedPreferenceWriter.getInstance(ProfileSetupActivity.this).writeBooleanValue(GlobalVariables.profileComplete,server_response.getData().getProfileComplete());
        SharedPreferenceWriter.getInstance(ProfileSetupActivity.this).writeStringValue(GlobalVariables._id,server_response.getData().getId());
        SharedPreferenceWriter.getInstance(ProfileSetupActivity.this).writeStringValue(GlobalVariables.name,server_response.getData().getName());
        SharedPreferenceWriter.getInstance(ProfileSetupActivity.this).writeStringValue(GlobalVariables.username,server_response.getData().getUsername());
        SharedPreferenceWriter.getInstance(ProfileSetupActivity.this).writeStringValue(GlobalVariables.email,server_response.getData().getEmail());
        SharedPreferenceWriter.getInstance(ProfileSetupActivity.this).writeStringValue(GlobalVariables.countryCode,server_response.getData().getCountryCode());
        SharedPreferenceWriter.getInstance(ProfileSetupActivity.this).writeStringValue(GlobalVariables.mobileNumber,server_response.getData().getMobileNumber());
        SharedPreferenceWriter.getInstance(ProfileSetupActivity.this).writeStringValue(GlobalVariables.jwtToken,server_response.getData().getJwtToken());
        SharedPreferenceWriter.getInstance(ProfileSetupActivity.this).writeStringValue(GlobalVariables.createdAt,server_response.getData().getCreatedAt());
        SharedPreferenceWriter.getInstance(ProfileSetupActivity.this).writeStringValue(GlobalVariables.updatedAt,server_response.getData().getUpdatedAt());
        SharedPreferenceWriter.getInstance(ProfileSetupActivity.this).writeIntValue(GlobalVariables.__v,server_response.getData().getV());
        SharedPreferenceWriter.getInstance(ProfileSetupActivity.this).writeStringValue(GlobalVariables.aboutUs,server_response.getData().getAboutUs());
        SharedPreferenceWriter.getInstance(ProfileSetupActivity.this).writeStringValue(GlobalVariables.accountHolderName,server_response.getData().getAccountHolderName());
        SharedPreferenceWriter.getInstance(ProfileSetupActivity.this).writeStringValue(GlobalVariables.accountNumber,server_response.getData().getAccountNumber());
        SharedPreferenceWriter.getInstance(ProfileSetupActivity.this).writeStringValue(GlobalVariables.bankName,server_response.getData().getBankName());
        SharedPreferenceWriter.getInstance(ProfileSetupActivity.this).writeStringValue(GlobalVariables.categoryName,server_response.getData().getCategoryName());
        SharedPreferenceWriter.getInstance(ProfileSetupActivity.this).writeStringValue(GlobalVariables.dob,server_response.getData().getDob());
        SharedPreferenceWriter.getInstance(ProfileSetupActivity.this).writeStringValue(GlobalVariables.gender,server_response.getData().getGender());
        SharedPreferenceWriter.getInstance(ProfileSetupActivity.this).writeStringValue(GlobalVariables.pricePerQues,server_response.getData().getPricePerQues());

    }

    private void intentForAttachDocument() {
        String[] mimeTypes ={"image/*", "application/pdf"};
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        startActivityForResult(Intent.createChooser(intent,"ChooseFile"), PICKFILE_REQUEST_CODE);

    }

    private void stoppingAudio() {
        try {
            mRecorder.stop();
            mRecorder.release();
            //chromometer.stop();

        } catch (Exception e) {
            e.printStackTrace();
        }
        mRecorder = null;

        chromometer.stop();
        seekbar.setProgress(0);
        seekbar.setMax(0);
        timer.cancel();


//        chromometer.setBase(SystemClock.elapsedRealtime());
        mPlayer=null;
        mic_im.pauseAnimation();
        Toast.makeText(this, "Recording stopped successfully.", Toast.LENGTH_SHORT).show();

    }

    private void playingAudio() {
        recording=false;
        playing=true;
        playaudio_ll.setVisibility(View.GONE);
        pause_ll.setVisibility(View.VISIBLE);
        mPlayer = new MediaPlayer();
        try {
            if(fileName!=null) {
//fileName is global string. it contains the Uri to the recently recorded audio.
                mPlayer.setDataSource(fileName);
                mPlayer.prepare();
                if(pause) {
                    mPlayer.seekTo(lastplay_position);
                }
                else
                {
                    mPlayer.seekTo(0);
                }

                mPlayer.start();
                seekbar.setMax(mPlayer.getDuration());
            }
            else
            {
                Toast toast=Toast.makeText(ProfileSetupActivity.this,"Please record audio first",Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
                seekbar.setMax(0);

            }
        } catch (IOException e) {
            Log.e("LOG_TAG", "prepare() failed");
        }
        //making the imageview pause button

        seekbar.setProgress(0);
        //mPlayer.seekTo(lastProgress);

        seekUpdation();
        chromometer.setBase(SystemClock.elapsedRealtime());
        chromometer.start();

        /** once the audio is complete, timer is stopped here**/

        mPlayerCompleteListner();


        /** moving the track as per the seekBar's position**/
        updatingSeekbar();


    }

    private void mPlayerCompleteListner() {
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                isPlaying = false;
                chromometer.stop();
                playaudio_ll.setVisibility(View.VISIBLE);
                pause_ll.setVisibility(View.GONE);
                pause=false;
            }
        });
    }

    private void updatingSeekbar() {
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if( mPlayer!=null && fromUser){
                    //here the track's progress is being changed as per the progress bar
                    mPlayer.seekTo(progress);
                    //timer is being updated as per the progress of the seekbar
                    chromometer.setBase(SystemClock.elapsedRealtime() - mPlayer.getCurrentPosition());
                    lastProgress = progress;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if(playing) {
                seekUpdation();
            }
        }
    };

    private void seekUpdation() {
        if(mPlayer != null){
            int mCurrentPosition = mPlayer.getCurrentPosition() ;
            seekbar.setProgress(mCurrentPosition);
            lastProgress = mCurrentPosition;
        }
        mHandler2.postDelayed(runnable, 100);
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

    private void recordingAudio() {

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

        fileName =  root.getAbsolutePath() + "/VoiceRecorderSimplifiedCoding/Audios/" +
                String.valueOf(System.currentTimeMillis() + ".mp3");
        Log.d("filename",fileName);
        mRecorder.setOutputFile(fileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        audio=new File(fileName);

        try {
            mRecorder.prepare();
            mRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        stopPlaying();
        lastProgress = 0;
        seekbar.setProgress(lastProgress);
        seekbar.setMax(300);
        lastposition=0;
        seekUpdation2();
        chromometer.setBase(SystemClock.elapsedRealtime());
        chromometer.start();
        mic_im.setProgress(0);
        mic_im.playAnimation();




        timer=new CountDownTimer(31500, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {


            }

            @Override
            public void onFinish() {
                stopPlaying();
                Toast.makeText(ProfileSetupActivity.this,"You can record upto 30 secounds",Toast.LENGTH_LONG).show();
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
         seekbar.setProgress(lastposition);
        mHandler.postDelayed(newthread,100);
    }

    private void stopPlaying() {
        try{
            mPlayer.release();
        }catch (Exception e){
            e.printStackTrace();
        }
        mPlayer = null;
        //showing the play button

        chromometer.stop();
    }


    private void settingThumbnail() {

        try {
            MediaController mediacontroller = new MediaController(ProfileSetupActivity.this);
            mediacontroller.setAnchorView(videoview);
            videoview.setMediaController(mediacontroller);
            videoview.setVideoURI(videoUri);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();

        }

        videoview.requestFocus();
        videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            public void onPrepared(MediaPlayer mp) {
                videoview.start();

            }
        });

        videoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            public void onCompletion(MediaPlayer mp) {

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

    private void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);



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
                    Intent intent = new Intent(ProfileSetupActivity.this, TakeImage.class);
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
                Intent intent = new Intent(ProfileSetupActivity.this, TakeImage.class);
                intent.putExtra("from", "gallery");
                startActivityForResult(intent, REQ_CODE_PICK_IMAGE);
                dialog.dismiss();
            }
        });

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

    private void DocumentSpinner() {
        documentlist = new ArrayList<>();
        documentlist.add("Please select");
        documentlist.add("Any");

        ArrayAdapter genderArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, documentlist) {
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
        documenttype_spn.setAdapter(genderArrayAdapter);
        documenttype_spn.performClick();

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
                   videoUri=Uri.parse(selectedVideoPath);
                   videoview.setVideoURI(videoUri);
                   video=new File(videoUri.getPath());
                   videoFile = outputDir+File.separator + "VID_" + new SimpleDateFormat("yyyyMMdd_HHmmss", getLocale()).format(new Date()) + ".mp4";
                   VideoCompress.compressVideoLow(video.getPath(),videoFile, new VideoCompress.CompressListener() {
                       @Override
                       public void onStart() {
                           Util.writeFile(ProfileSetupActivity.this, "Start at: " + new SimpleDateFormat("HH:mm:ss", getLocale()).format(new Date()) + "\n");
                       }

                       @Override
                       public void onSuccess() {
                           if(dialog.isShowing())
                           {
                               dialog.dismiss();
                           }

                           Util.writeFile(ProfileSetupActivity.this, "End at: " + new SimpleDateFormat("HH:mm:ss", getLocale()).format(new Date()) + "\n");
                         //  Util.writeFile(ProfileSetupActivity.this, "Total: " + ((endTime - startTime)/1000) + "s" + "\n");
                           Util.writeFile(ProfileSetupActivity.this);

                       }

                       @Override
                       public void onFail() {
                           Util.writeFile(ProfileSetupActivity.this, "Failed Compress!!!" + new SimpleDateFormat("HH:mm:ss", getLocale()).format(new Date()));
                       }

                       @Override
                       public void onProgress(float percent) {
//                           tv_progress.setText(String.valueOf(percent) + "%");
                           dialog.setMax((int) percent);
                           dialog.show();



                       }
                   });

                   upload_im.setVisibility(View.GONE);
                   play_im.setVisibility(View.VISIBLE);
                   videoview.seekTo(1);

               }


               } catch (Exception e)
                {
                    e.printStackTrace();
                }



        }
        else if(requestCode == PICKFILE_REQUEST_CODE)
        {
            try
            {
                Uri fileUri=getPath2(ProfileSetupActivity.this,data.getData());
                attach_document=new File(fileUri.getPath());
                attach_doc_im.setImageURI(fileUri);



            }catch (Exception e)
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




    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private Uri getPath2(Context context , Uri uri) {
        //check here to KITKAT or new version
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {

            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Uri.parse(Environment.getExternalStorageDirectory() + "/" + split[1]);
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return Uri.parse(getDataColumn(context, contentUri, null, null));
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return Uri.parse(getDataColumn(context, contentUri, selection, selectionArgs));
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return Uri.parse(uri.getLastPathSegment());

            return Uri.parse(getDataColumn(context, uri, null, null));
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return Uri.parse(uri.getPath());
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return
                "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return
                "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }


    public static String getPath(Context context, Uri uri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = { "_data" };
            Cursor cursor = null;

            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it
            }
        }
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;

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
                        if(SharedPreferenceWriter.getInstance(ProfileSetupActivity.this).getString("Camera").equalsIgnoreCase("Yes"))
                        {
                            profileBottomLayout();
                        }
                        else if(SharedPreferenceWriter.getInstance(ProfileSetupActivity.this).getString("Video").equalsIgnoreCase("Yes"))
                        {
                            dispatchTakeVideoIntent();
                        }
                        else if(SharedPreferenceWriter.getInstance(ProfileSetupActivity.this).getString("Recording").equalsIgnoreCase("Yes"))
                        {
                            intentForAttachDocument();
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
                        Toast.makeText(ProfileSetupActivity.this,"Recording Started",Toast.LENGTH_LONG).show();
                        recordingAudio();
                    }
                    else
                    {
                        Toast.makeText(ProfileSetupActivity.this,"Permission Denied",Toast.LENGTH_LONG).show();

                    }
                }


                break;
        }
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

@SuppressWarnings("deprecation")
public static Locale getSystemLocaleLegacy(Configuration config){
        return config.locale;
        }

@TargetApi(Build.VERSION_CODES.N)
public static Locale getSystemLocale(Configuration config){
        return config.getLocales().get(0);
        }





}
