package com.forecaster.Activity;

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
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ScrollView;
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
import com.forecaster.Modal.ForcasterSetupProfile;
import com.forecaster.R;
import com.forecaster.Retrofit.RetroInterface;
import com.forecaster.Retrofit.RetrofitInit;
import com.forecaster.Utility.AddServiceBody;
import com.forecaster.Utility.GlobalVariables;
import com.forecaster.Utility.ProgressDailogHelper;
import com.forecaster.Utility.SharedPreferenceWriter;
import com.forecaster.Utility.TakeImage;
import com.forecaster.Utility.Validation;
import com.forecaster.Utility.VideoCompressor.Util;
import com.forecaster.Utility.VideoCompressor.VideoCompress;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
import static com.forecaster.BuildConfig.DEBUG;

public class ProfileSetupActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

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
    @BindView(R.id.dob_ed) EditText dob_ed;
    @BindView(R.id.pause_ll) LinearLayout pause_ll;
    @BindView(R.id.timer_txt) TextView timer_txt;
    @BindView(R.id.scrollView2) ScrollView scrollView2;
    final int PERMISSION_REQUEST_CODE = 200;
    final int PERMISSION_REQUEST_CODE2 = 400;
    public static final String DOCUMENTS_DIR = "documents";
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
    Boolean recording=false,playing=false;
    @BindView(R.id.upload_video_btn) Button upload_video_btn;
    @BindView(R.id.forcaster_name_txt) TextView forcaster_name_txt;
    ProgressDialog  dialog;
    private String outputDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
    int lastplay_position=0;
    boolean pause=false;
    private ProgressDailogHelper dailogHelper;
    @BindView(R.id.main_cl) ConstraintLayout main_cl;
    @BindView(R.id.full_screen_video) ImageView full_screen_video_iv;
    MediaController mediacontroller;
    SeekBar media_seekbar;
    boolean mediacontroller_seekbar=false;

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
        scrollView2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()){
                    case MotionEvent.ACTION_DOWN: {
                        if (mediacontroller != null) {
                            videoview.stopPlayback();
                            play_im.setVisibility(View.VISIBLE);
                            videoview.clearFocus();
                            mediacontroller.hide();

                        }
                    }
                    case MotionEvent.ACTION_UP: {
                        if (mediacontroller != null) {
                            videoview.stopPlayback();
                            play_im.setVisibility(View.VISIBLE);
                            videoview.clearFocus();
                            mediacontroller.hide();

                        }
                    }

                }

                return false;
            }
        });




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
        seekbar.setOnSeekBarChangeListener(this);
        full_screen_video_iv.setOnClickListener(this::OnClick);
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
                    if(documenttype_txt.getText().toString().equalsIgnoreCase(getString(R.string.document_type)))
                    {
                       Toast toast= Toast.makeText(ProfileSetupActivity.this,getString(R.string.select_document_type),Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER,0,0);
                        toast.show();
                    }
                    else
                    {
                        intentForAttachDocument(documenttype_txt.getText().toString());
                    }

                }

                break;
            case R.id.dob_ed:
                Date date= Calendar.getInstance().getTime();
                DatePickerDialog dialog=new DatePickerDialog(ProfileSetupActivity.this, AlertDialog.THEME_HOLO_LIGHT,datePickerListener,Integer.parseInt(new SimpleDateFormat("yyyy").format(date)),
                        Integer.parseInt(new SimpleDateFormat("MM").format(date))-1,Integer.parseInt(new SimpleDateFormat("dd").format(date)));
                dialog.getDatePicker().setMaxDate(date.getTime());
                dialog.show();

                break;

            case R.id.pause_ll:
                pauseAudio();
                break;

            case R.id.full_screen_video:
//                ForcasterSetupProfile setupProfile=new ForcasterSetupProfile();
//                setupProfile.setProfilePic(imagePath);
//                setupProfile.setGender(gender_txt.getText().toString());
//                setupProfile.setDob(dob_ed.getText().toString());
//                setupProfile.setCategoryName(categorytype_txt.getText().toString());
//                setupProfile.setPsychological_extra(psychological_txt.getText().toString());
//                setupProfile.setDocumentType(documenttype_txt.getText().toString());
//                setupProfile.setAttachedDocument(String.valueOf(attach_document));
//                setupProfile.setVoiceRecording(String.valueOf(audio));
//                setupProfile.setUploadedVideo(String.valueOf(videoUri));
//                setupProfile.setAboutUs(about_us_ed.getText().toString());
//                setupProfile.setPricePerQues(Integer.valueOf(price_per_ed.getText().toString()));
//                setupProfile.setBankName(selectbank_txt.getText().toString());
//                setupProfile.setAccountHolderName(account_holder_ed.getText().toString());
//                setupProfile.setAccountNumber(bank_number_ed.getText().toString());
                Intent intent1=new Intent(ProfileSetupActivity.this,FullScreenVideoActivity.class);
                intent1.putExtra(GlobalVariables.videouri,videoUri.toString());
                startActivity(intent1);

                break;
        }

    }

    private void pauseAudio() {
        try
        {
            pause=true;
            playaudio_ll.setVisibility(View.VISIBLE);
            pause_ll.setVisibility(View.GONE);
            mic_im.pauseAnimation();
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

        if(profile_image==null
        || gender_txt.getText().toString().equalsIgnoreCase(getString(R.string.gender))
        || !Validation.hasText(dob_ed,getString(R.string.please_enter_dob))
        || categorytype_txt.getText().toString().equalsIgnoreCase(getString(R.string.category_type))
        || categorytype_txt.getText().toString().equalsIgnoreCase(getString(R.string.psycho_counseling))
        || documenttype_txt.getText().toString().equalsIgnoreCase(getString(R.string.document_type))
        || attach_document==null
        || audio==null
        || video==null
        || !Validation.hasText(about_us_ed,getString(R.string.write_about_yourself))
        || !Validation.hasText(price_per_ed,getString(R.string.please_enter_ppq))
        || selectbank_txt.getText().toString().equalsIgnoreCase(getString(R.string.select_bank))
        || !Validation.hasText2(account_holder_ed,getString(R.string.please_enter_holder_name))
        || bank_number_ed.getText().toString().isEmpty() || bank_number_ed.getText().toString().length()!=16)
        {
             if(profile_image==null)
            {
            ret=false;
            Toast.makeText(ProfileSetupActivity.this,getString(R.string.enter_profile_photo),Toast.LENGTH_LONG).show();
            }
            else if(gender_txt.getText().toString().equalsIgnoreCase(getString(R.string.gender)))
            {
                ret=false;
                gender_txt.requestFocusFromTouch();
                gender_txt.setError(getString(R.string.please_select));
                gender_txt.setFocusable(true);
                gender_txt.requestFocus();
            }
            else if(!Validation.hasText(dob_ed,getString(R.string.please_enter_dob)))
            {
                gender_txt.setError(null);
                ret=false;
                dob_ed.requestFocusFromTouch();
                dob_ed.setFocusableInTouchMode(true);
                dob_ed.setFocusable(true);

            }
            else if(categorytype_txt.getText().toString().equalsIgnoreCase(getString(R.string.category_type)))
            {
                ret=false;
                categorytype_txt.requestFocusFromTouch();
                categorytype_txt.setError((getString(R.string.please_select)));
                categorytype_txt.setFocusable(true);
                categorytype_txt.requestFocus();
            }
            else if(categorytype_txt.getText().toString().equalsIgnoreCase(getString(R.string.psycho_counseling)))
            {
                ret=false;
                if (psychological_txt.getText().toString().equalsIgnoreCase(getString(R.string.please_select))) {
                    ret = false;
                    categorytype_txt.setError(null);
                    psychological_txt.requestFocusFromTouch();
                    psychological_txt.setError(getString(R.string.please_select));
                    psychological_txt.setFocusable(true);
                }
            }
            else if(documenttype_txt.getText().toString().equalsIgnoreCase(getString(R.string.document_type)))
            {
                categorytype_txt.setError(null);
                ret=false;
                documenttype_txt.requestFocusFromTouch();
                documenttype_txt.setError(getString(R.string.please_select));
                documenttype_txt.setFocusable(true);
            }
            else if(attach_document==null)
            {
                documenttype_txt.setError(null);
                ret=false;
                attach_doc_txt.requestFocusFromTouch();
                attach_doc_txt.setError(getString(R.string.pls_attach_doc));
                attach_doc_txt.setFocusable(true);
            }
            else if(audio==null)
            {
                attach_doc_txt.setError(null);
                ret=false;
                record_audio_txt.setError(getString(R.string.pls_record_your_voice));
                record_audio_txt.setFocusable(true);
                record_audio_txt.requestFocusFromTouch();

            }
            else if(video==null)
            {
                ret=false;
                Toast.makeText(ProfileSetupActivity.this,getString(R.string.pls_upload_video),Toast.LENGTH_LONG).show();
            }

            else if(!Validation.hasText(about_us_ed,getString(R.string.write_about_yourself)))
            {
                ret=false;
                about_us_ed.requestFocusFromTouch();

            }

            else if(!Validation.hasText(price_per_ed,getString(R.string.please_enter_ppq)))
            {
                ret=false;
                price_per_ed.requestFocusFromTouch();

            }
            else if(selectbank_txt.getText().toString().equalsIgnoreCase(getString(R.string.select_bank)))
            {
                ret=false;
                selectbank_txt.setError(getString(R.string.pls_select_bank));
                selectbank_txt.setFocusable(true);
                selectbank_txt.requestFocusFromTouch();
            }


            else if(!Validation.hasText(account_holder_ed,getString(R.string.please_enter_holder_name)))
            {
                ret=false;
                account_holder_ed.requestFocusFromTouch();

            }else if(bank_number_ed.getText().toString().isEmpty() || bank_number_ed.getText().toString().length()!=16)
            {
                if(bank_number_ed.getText().toString().isEmpty())
                {
                    ret=false;
                    bank_number_ed.setError(getString(R.string.plz_enter_accnt_nmbr));

                    bank_number_ed.setFocusable(true);
                    bank_number_ed.requestFocusFromTouch();

                }
                else if(bank_number_ed.getText().toString().length()!=16)
                {
                    ret=false;
                    bank_number_ed.setError(getString(R.string.pls_enter_valid_accnum));
                    bank_number_ed.setFocusable(true);
                    bank_number_ed.requestFocusFromTouch();

                }
            }


        }else
        {
            gender_txt.clearFocus();
            gender_txt.setError(null);
            categorytype_txt.setError(null);
            categorytype_txt.clearFocus();
            documenttype_txt.setError(null);



        }

        return ret;

    }

    private void forecasterSetupProfileApi() {
        dailogHelper.showDailog();
        RetroInterface api_service= RetrofitInit.getConnect().createConnection();
        ForcasterSetupProfile profile=new ForcasterSetupProfile();
        profile.setForecasterId(SharedPreferenceWriter.getInstance(ProfileSetupActivity.this).getString(GlobalVariables._id));
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
        profile.setPricePerQues(Float.valueOf(price_per_ed.getText().toString().trim()));
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

        RequestBody attachDocumentBody;
        MultipartBody.Part attachedDocument = null;


        if(documenttype_txt.getText().toString().equalsIgnoreCase(documentlist.get(1)))
       {
           attachDocumentBody=RequestBody.create(MediaType.parse("application/pdf"),attach_document);
           attachedDocument=MultipartBody.Part.createFormData("attachedDocument",attach_document.getName(),attachDocumentBody);
       }
        else if(documenttype_txt.getText().toString().equalsIgnoreCase(documentlist.get(2)))
        {
            attachDocumentBody=RequestBody.create(MediaType.parse("image/jpeg"),attach_document);
            attachedDocument=MultipartBody.Part.createFormData("attachedDocument",attach_document.getName(),attachDocumentBody);

        }
        else if(documenttype_txt.getText().toString().equalsIgnoreCase(documentlist.get(3)))
        {
            attachDocumentBody=RequestBody.create(MediaType.parse("*/*"),attach_document);
            attachedDocument=MultipartBody.Part.createFormData("attachedDocument",attach_document.getName(),attachDocumentBody);

        }



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
                        if(server_response.getResponseMessage().equalsIgnoreCase(GlobalVariables.invalidoken))
                        {
                            Toast.makeText(ProfileSetupActivity.this,getString(R.string.other_device_logged_in),Toast.LENGTH_LONG).show();
                            finish();
                            startActivity(new Intent(ProfileSetupActivity.this,LoginActivity.class));
                            SharedPreferenceWriter.getInstance(ProfileSetupActivity.this).clearPreferenceValues();
                            FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                @Override
                                public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                    if (!task.isSuccessful()) {
                                        // Log.w(TAG, "getInstanceId failed", task.getException());
                                        return;
                                    }

                                    String auth_token = task.getResult().getToken();
                                    Log.w("firebaese","token: "+auth_token);
                                    SharedPreferenceWriter.getInstance(ProfileSetupActivity.this).writeStringValue(GlobalVariables.deviceToken,auth_token);
                                }
                            });
                        }
                        else
                        {

                        Toast.makeText(ProfileSetupActivity.this,server_response.getResponseMessage(),Toast.LENGTH_LONG).show();
                    }}

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
        SharedPreferenceWriter.getInstance(ProfileSetupActivity.this).writeStringValue(GlobalVariables.totalRating, String.valueOf(server_response.getData().getTotalRating()));
        SharedPreferenceWriter.getInstance(ProfileSetupActivity.this).writeStringValue(GlobalVariables.avgRating, String.valueOf(server_response.getData().getAvgRating()));
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

    private void intentForAttachDocument(String document_type) {
        Intent intent = null;
        if(document_type.equalsIgnoreCase(documentlist.get(1)))
        {
           intent=new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("application/pdf");

        }
        else if(document_type.equalsIgnoreCase(documentlist.get(2))){
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/jpeg");

        }
        else if(document_type.equalsIgnoreCase(documentlist.get(3)))
        {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            String[] mimetypes = {"application/msword","application/vnd.openxmlformats-officedocument.wordprocessingml.document","application/vnd.google-apps.document","application/vnd.google-apps.spreadsheet","text/plain"};
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }

        startActivityForResult(Intent.createChooser(intent,"ChooseFile"), PICKFILE_REQUEST_CODE);



    }


    private void stoppingAudio() {
        if(playing) {
            try {
                playaudio_ll.setVisibility(View.VISIBLE);
                pause_ll.setVisibility(View.GONE);
                mRecorder.stop();
                mRecorder.release();

            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                recording = false;
                playing = false;
                timer_txt.setText(mPlayer.getDuration() / 100);
                mPlayer.release();

                if (timer != null) {
                    timer.cancel();
                    timer.onFinish();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            mic_im.pauseAnimation();
        }else
        {
            Toast.makeText(context1, getString(R.string.please_play_audio_first), Toast.LENGTH_SHORT).show();
        }
       // attach_doc_txt.setText(audio.getName());


    }

    private void playingAudio() {
        if(!recording)
        {

        if (fileName == null) {
            Toast toast = Toast.makeText(ProfileSetupActivity.this, getString(R.string.please_record_audio_first), Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else {
            try {


                mic_im.playAnimation();
//            stop_ll.setVisibility(View.VISIBLE);
                pause_ll.setVisibility(View.VISIBLE);
                playaudio_ll.setVisibility(View.GONE);
                if (timer != null) {
                    timer.cancel();

                }
                recording = false;
                playing = true;
                mPlayer = new MediaPlayer();
                if (fileName != null) {
                    mPlayer.setDataSource(fileName);
                    mPlayer.prepare();
                }

                if (pause) {
                    mPlayer.seekTo(lastplay_position);
                }
                mPlayer.start();
                seekbar.setProgress(0);
                seekbar.setMax(mPlayer.getDuration());
                seekbar.setClickable(false);
                seekUpdation();

                mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        playaudio_ll.setVisibility(View.VISIBLE);
                        pause_ll.setVisibility(View.GONE);
                        pause = false;
                        mic_im.pauseAnimation();
                        mic_im.setProgress(0);

                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        }else
        {
            Toast.makeText(ProfileSetupActivity.this,getString(R.string.please_stop_recording),Toast.LENGTH_LONG).show();
        }
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

    @Override
    protected void onResume() {
        super.onResume();
        main_cl.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
        videoview.setVideoURI(videoUri);
        videoview.seekTo(1);

        if(mediacontroller!=null) {
            videoview.stopPlayback();
            mediacontroller.hide();
        }
        Log.e("resumed","yes");
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
        if(timer!=null)
        {
            timer.cancel();
            timer.onFinish();
        }


        mHandler.removeCallbacks(newthread);


        recording=true;
        playing=false;
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);

        File root = android.os.Environment.getExternalStorageDirectory();
        File file = new File(root.getAbsolutePath() + "/VoiceRecorderSimplifiedCoding/Audios");

        if (!file.exists()) {
            file.mkdirs();
        }
        else
        {
            file.delete();
            file.mkdirs();
        }

        fileName =  root.getAbsolutePath() + "/VoiceRecorderSimplifiedCoding/Audios/" + String.valueOf(System.currentTimeMillis() + ".mp3");
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
        lastposition=0;
        mic_im.setProgress(0);
        mic_im.playAnimation();
        seekbar.setProgress(0);
        seekbar.setMax(300);
        seekUpdation2();





        timer=new CountDownTimer(31500, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
//                if(recording)
//                {
//
//
//                long sec=(millisUntilFinished/1000)+1;
//                timer_txt.setText("0:"+sec);
//                Log.e("timer_count", String.valueOf(millisUntilFinished));
//                }

            }

            @Override
            public void onFinish() {
                if(recording)
                {
                recording=false;
                mic_im.pauseAnimation();
                stopPlaying();
                try {
                    mRecorder.stop();
                    mRecorder.release();
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                Log.e("timer_stop","yes");
                Toast.makeText(ProfileSetupActivity.this,"You can record upto 30 secounds",Toast.LENGTH_LONG).show();
                }
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
    }


    private void settingThumbnail() {


            play_im.setVisibility(View.GONE);
            try {
                mediacontroller = new MediaController(ProfileSetupActivity.this);
                mediacontroller.setAnchorView(videoview);
                videoview.setMediaController(mediacontroller);
                videoview.setVideoURI(videoUri);
                videoview.seekTo(1);
                final int topContainerId1 = getResources().getIdentifier("mediacontroller_progress", "id", "android");
                media_seekbar = (SeekBar) mediacontroller.findViewById(topContainerId1);
                media_seekbar.setOnSeekBarChangeListener(this);
                mediacontroller_seekbar=true;

            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();

            }

            videoview.requestFocus();
            videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                public void onPrepared(MediaPlayer mp) {
//                mp.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT);

                    videoview.start();

                }
            });

            videoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                public void onCompletion(MediaPlayer mp) {
                    play_im.setVisibility(View.VISIBLE);

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
        psychologicalList.add(getString(R.string.please_select));
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
        bankList.add(getString(R.string.please_select));
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
        documentlist.add(getString(R.string.please_select));
        documentlist.add("PDF");
        documentlist.add("JPEG");
        documentlist.add("WORD");

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
        categoryList.add(getString(R.string.please_select));
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
        genderlist.add(getString(R.string.please_select));
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
                           full_screen_video_iv.setVisibility(View.VISIBLE);
                           videoview.setVideoURI(videoUri);

                           Util.writeFile(ProfileSetupActivity.this, "End at: " + new SimpleDateFormat("HH:mm:ss", getLocale()).format(new Date()) + "\n");
                         //  Util.writeFile(ProfileSetupActivity.this, "Total: " + ((endTime - startTime)/1000) + "s" + "\n");
                           Util.writeFile(ProfileSetupActivity.this);

                       }

                       @Override
                       public void onFail() {
                           if(dialog.isShowing())
                           {
                               dialog.dismiss();
                           }
                           Util.writeFile(ProfileSetupActivity.this, "Failed Compress!!!" + new SimpleDateFormat("HH:mm:ss", getLocale()).format(new Date()));
                       }

                       @Override
                       public void onProgress(float percent) {
//                           tv_progress.setText(String.valueOf(percent) + "%");
                           dialog.setMax((int) percent);
                           dialog.show();
                           full_screen_video_iv.setVisibility(View.VISIBLE);



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
                String selectedFilePath;
                if (isGoogleDriveUri(data.getData()))
                {
                    selectedFilePath =getDriveFilePath(data.getData(),this);
                    documenttype_txt.setError(null);
                }
                else
                {
                    selectedFilePath=getPath(ProfileSetupActivity.this,data.getData());
                }
                Log.e("path",selectedFilePath);
                File file=new File(selectedFilePath);
                attach_doc_txt.setText(file.getName());

                attach_document=new File(file.getPath());
                documenttype_txt.setError(null);
                Log.e("data", String.valueOf(data.getData()));


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
//            finish();


        }


        super.onActivityResult(requestCode, resultCode, data);
    }

    public static boolean isGoogleDriveUri(Uri uri) {
        return "com.google.android.apps.docs.storage".equals(uri.getAuthority()) || "com.google.android.apps.docs.storage.legacy".equals(uri.getAuthority());
    }


    public static String getDriveFilePath(Uri uri, Context context) {
        Uri returnUri = uri;
        Cursor returnCursor = context.getContentResolver().query(returnUri, null, null, null, null);

        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
        returnCursor.moveToFirst();
        String name = (returnCursor.getString(nameIndex));
        String size = (Long.toString(returnCursor.getLong(sizeIndex)));
        File file = new File(context.getCacheDir(), name);
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            FileOutputStream outputStream = new FileOutputStream(file);
            int read = 0;
            int maxBufferSize = 110241024;
            int bytesAvailable = inputStream.available();

            //int bufferSize = 1024;
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);

            final byte[] buffers = new byte[bufferSize];
            while ((read = inputStream.read(buffers)) != -1) {
                outputStream.write(buffers, 0, read);
            }
            Log.e("File Size", "Size " + file.length());
            inputStream.close();
            outputStream.close();
            Log.e("File Path", "Path " + file.getPath());
            Log.e("File Size", "Size " + file.length());
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }
        return file.getPath();
    }





    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = this.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (result == null) {
            result = uri.getLastPathSegment();
        }
        return result;
    }

    public  File getDocumentCacheDir(@NonNull Context context) {
        File dir = new File(context.getCacheDir(), DOCUMENTS_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        logDir(context.getCacheDir());
        logDir(dir);

        return dir;
    }

    private static void logDir(File dir) {
        if(!DEBUG) return;
        Log.d("Dir", "Dir=" + dir);
        File[] files = dir.listFiles();
        for (File file : files) {
            Log.d("File", "File=" + file.getPath());
        }
    }




    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public  String getPath(Context context, final Uri uri) {

        // check here to KITKAT or new version
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {

            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/"
                            + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);

                if (id != null && id.startsWith("raw:")) {
                    return id.substring(4);
                }

                String[] contentUriPrefixesToTry = new String[]{
                        "content://downloads/public_downloads",
                        "content://downloads/my_downloads",
                        "content://downloads/all_downloads"
                };

                for (String contentUriPrefix : contentUriPrefixesToTry) {
                    Uri contentUri = ContentUris.withAppendedId(Uri.parse(contentUriPrefix), Long.valueOf(id));
                    try {
                        String path = getDataColumn(context, contentUri, null, null);
                        if (path != null) {
                            return path;
                        }
                    } catch (Exception e) {}
                }

                // path could not be retrieved using ContentResolver, therefore copy file to accessible cache using streams
                String fileName = getFileName(uri);
                File cacheDir = getDocumentCacheDir(context);
                File file = generateFileName(fileName, cacheDir);

                String destinationPath = null;
                if (file != null) {
                    destinationPath = file.getAbsolutePath();
                    saveFileFromUri(context, uri, destinationPath);
                }

                return destinationPath;
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
                final String[] selectionArgs = new String[] { split[1] };

                return getDataColumn(context, contentUri, selection,
                        selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    @Nullable
    public static File generateFileName(@Nullable String name, File directory) {
        if (name == null) {
            return null;
        }

        File file = new File(directory, name);

        if (file.exists()) {
            String fileName = name;
            String extension = "";
            int dotIndex = name.lastIndexOf('.');
            if (dotIndex > 0) {
                fileName = name.substring(0, dotIndex);
                extension = name.substring(dotIndex);
            }

            int index = 0;

            while (file.exists()) {
                index++;
                name = fileName + '(' + index + ')' + extension;
                file = new File(directory, name);
            }
        }

        try {
            if (!file.createNewFile()) {
                return null;
            }
        } catch (IOException e) {
            Log.w("error", e);
            return null;
        }

        logDir(directory);

        return file;
    }

    private static void saveFileFromUri(Context context, Uri uri, String destinationPath) {
        InputStream is = null;
        BufferedOutputStream bos = null;
        try {
            is = context.getContentResolver().openInputStream(uri);
            bos = new BufferedOutputStream(new FileOutputStream(destinationPath, false));
            byte[] buf = new byte[1024];
            is.read(buf);
            do {
                bos.write(buf);
            } while (is.read(buf) != -1);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) is.close();
                if (bos != null) bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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


//    public static String getPath(Context context, Uri uri) throws URISyntaxException {
//        if ("content".equalsIgnoreCase(uri.getScheme())) {
//            String[] projection = { "_data" };
//            Cursor cursor = null;
//
//            try {
//                cursor = context.getContentResolver().query(uri, projection, null, null, null);
//                int column_index = cursor.getColumnIndexOrThrow("_data");
//                if (cursor.moveToFirst()) {
//                    return cursor.getString(column_index);
//                }
//            } catch (Exception e) {
//                // Eat it
//            }
//        }
//        else if ("file".equalsIgnoreCase(uri.getScheme())) {
//            return uri.getPath();
//        }
//
//        return null;
//
//    }

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
                        //Toast.makeText(this, "Permission Granted", Toast.LENGTH_LONG).show();
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
                            if(documenttype_txt.getText().toString().equalsIgnoreCase(getString(R.string.document_type)))
                            {
                                Toast toast= Toast.makeText(ProfileSetupActivity.this,getString(R.string.select_document_type),Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER,0,0);
                                toast.show();
                            }
                            else
                            {
                                intentForAttachDocument(documenttype_txt.getText().toString());
                            }
                        }



                    }else {

                       // Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show();
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
                        //Toast.makeText(ProfileSetupActivity.this,"Recording Started",Toast.LENGTH_LONG).show();
                        recordingAudio();
                    }
                    else
                    {
                       // Toast.makeText(ProfileSetupActivity.this,"Permission Denied",Toast.LENGTH_LONG).show();

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


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if(playing) {
            if (progress / 1000 < 10) {
                timer_txt.setText("0:0" + progress / 1000);
            } else {
                timer_txt.setText("0:" + progress / 1000);
            }
        }

        else if(mediacontroller_seekbar)
        {
            Log.e("Progress", String.valueOf(seekBar.getProgress()));
            if(seekBar.getMax()==progress)
            {
                if(mediacontroller.isShowing()) {
                    mediacontroller.hide();
                    Log.e("prepare", String.valueOf(progress));
                    play_im.setVisibility(View.VISIBLE);
                    videoview.stopPlayback();
                }
            }
            else {
                play_im.setVisibility(View.GONE);
            }
        }


        else
        {
            Log.e("timer", String.valueOf(progress));
            if (progress / 10 < 10) {
                timer_txt.setText("0:0" + progress / 10);
            } else {
                timer_txt.setText("0:" + progress / 10);
            }

        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        Log.e("Start", String.valueOf(seekBar.getProgress()));
        if(mediacontroller_seekbar)
        {
            media_seekbar.setProgress(seekBar.getProgress());
            videoview.seekTo(seekBar.getProgress());
            videoview.resume();
        }

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        Log.e("Stop", String.valueOf(seekBar.getProgress()));
        if(mediacontroller_seekbar)
        {
            media_seekbar.setProgress(seekBar.getProgress());
            videoview.seekTo(seekBar.getProgress());
            videoview.resume();
        }

    }
}
