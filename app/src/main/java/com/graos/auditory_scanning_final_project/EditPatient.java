package com.graos.auditory_scanning_final_project;
/**
 * Created by GG on 13/01/2017.
 */
import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EditPatient extends AppCompatActivity {

    static final int REQUEST_VIDEO_CAPTURE = 1;
    private static final int REQUEST_RECORD_AUDIO = 1;
    private static final int REQUEST_WRITE_STORAGE = 112;
    private static final int CAMERA_REQUEST = 1888;

    private boolean btn_rec_yes_mode = false;
    public Uri UriYesVideo;
    public String abs_path;
    public String audio_path;
    private VideoView patient_video;
    private RelativeLayout VideoContainer;
    private global_variables mApp;
    private Context thisContext;
    private Button rec_yes_btn;
    AssignmentsDBHelper dbHelper;
    Cursor cursor;
    MyListAdapter mla;

    ListView _my_list;
    TextView _patient;
    EditText _item;

    String my_patient;
    String add_item;
    String [] projection = new String[]{Constants.Items._ID, Constants.Items.ITEM};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_patient);
        setTitle(R.string.nameActivity_edit_pattient);
        thisContext = this;
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) this,
                    Manifest.permission.RECORD_AUDIO)) {
            } else {// No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions((Activity) this,
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        REQUEST_RECORD_AUDIO);
            }
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            } else {// No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions((Activity) this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_WRITE_STORAGE);
            }
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) this,
                    Manifest.permission.CAMERA)) {
            } else {// No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions((Activity) this,
                        new String[]{Manifest.permission.CAMERA},
                        CAMERA_REQUEST);
            }
        }

        mApp = ((global_variables)getApplicationContext());

        patient_video =  (VideoView) findViewById(R.id.PatientVideoView);
        VideoContainer = (RelativeLayout) findViewById(R.id.VideoContainer);
        patient_video.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
            @Override
            public void onCompletion(MediaPlayer mp){
                // invoke your activity here
                Button record_yes = (Button) findViewById(R.id.button4);
                Button record_no = (Button) findViewById(R.id.button5);
                record_yes.setVisibility(View.VISIBLE);
                record_no.setVisibility(View.VISIBLE);
                VideoContainer.setVisibility(View.INVISIBLE);
            }
        });


        // DB
        dbHelper = new AssignmentsDBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase(); //lock to write and read

        _patient = (TextView) findViewById(R.id.textViewHiPatient);
        _my_list = (ListView) findViewById(R.id.listItems);
        _item = (EditText) findViewById(R.id.editText);

        Intent i_result_patient;
        i_result_patient = getIntent();
        if(i_result_patient.getStringExtra("PATIENT") != null){
            my_patient = i_result_patient.getStringExtra("PATIENT");
        }

        _patient.setText(my_patient);

        // Cursor approaching to the TableDB-Contacts
        cursor = db.query(Constants.Items.TABLE_NAME, null, null, null, null, null, null);
        //doron changes:
        _my_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                Log.d("############","Items "+ arg2);
            }

        });
        //-----
        mla = new MyListAdapter(this, cursor);
        _my_list.setAdapter(mla);

        _my_list.invalidateViews();
        db.close();
    }



    // --------------- Add Item ----------------------------------------
    //-----------------------------------------------------------------
    public void press_addItem(View view){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values;

        if(_item.getText()!= null){
            add_item = _item.getText().toString();
        }
        else
            Toast.makeText(getApplicationContext(),"Please insert item" , Toast.LENGTH_LONG).show();

        if( !add_item.equals("") ){
            values = new ContentValues();
            values.put(Constants.Items.ITEM, add_item);
            db.insert(Constants.Items.TABLE_NAME, null, values);
            cursor = db.query(Constants.Items.TABLE_NAME, projection, null,null, null, null, null);
            mla = new MyListAdapter(this, cursor);
            _my_list.setAdapter(mla);
            _item.setText("");
        }
        else
            Toast.makeText(this, "Insert new item", Toast.LENGTH_SHORT).show();
    }


    // --------------- Delete Item ----------------------------------------
    //--------------------------------------------------------------------
    public void press_delete(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(EditPatient.this);
        builder.setMessage("Are you sure?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                        Toast.makeText(getApplicationContext(), "Delete the item" , Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
//                        Toast.makeText(getApplicationContext(), "NO Delete the item" , Toast.LENGTH_LONG).show();
                    }
                });
        // Create the AlertDialog object and return it
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            UriYesVideo = intent.getData();
            //convert from video to audio
            abs_path = getRealPathFromURI(this,UriYesVideo);
            audio_path = abs_path.substring(0,abs_path.lastIndexOf(".mp4")+1) + "flac";
            //String[] cmd = {"-i",abs_path,"-ab","128k","-ac","2","-ar","44100","-vn","my_audio.mp3"};
            //command with filter
            //String[] cmd = {"-i",abs_path,"-acodec","flac","-sample_fmt","s16","-bits_per_raw_sample","16","-ar","44100","-ac","1","-af","highpass=f=200, lowpass=f=3000",audio_path};
            String[] cmd = {"-i",abs_path,"-acodec","flac","-sample_fmt","s16","-bits_per_raw_sample","16","-ar","44100","-ac","1",audio_path};
            final FFmpeg ffmpeg = FFmpeg.getInstance(this);
            try {
                //Load the binary
                ffmpeg.loadBinary(new LoadBinaryResponseHandler() {
                    @Override
                    public void onStart() {}
                    @Override
                    public void onFailure() {}
                    @Override
                    public void onSuccess() {}
                    @Override
                    public void onFinish() {}
                });
            } catch (FFmpegNotSupportedException e) {
                // Handle if FFmpeg is not supported by device
            }
            try {
                ffmpeg.execute(cmd, new ExecuteBinaryResponseHandler() {
                    @Override
                    public void onStart() {
                    }
                    @Override
                    public void onProgress(String message) {}
                    @Override
                    public void onFailure(String message) {
                        Log.d("error", "FFmpeg cmd failure");
                    }
                    @Override
                    public void onSuccess(String message) {
                        Log.d("success", "FFmpeg cmd success");
                        ffmpeg.killRunningProcesses();
                        Log.d("kill running process", "FFmpeg kill running process: " + ffmpeg.killRunningProcesses());

                        //get google speech transcription from audio
                        //---------------------------------------------
                        //audio_path = "/storage/emulated/0/DCIM/Camera/mono.flac";
                        //audio_path = "/storage/emulated/0/DCIM/Camera/last.flac";
                        //audio_path = "/storage/emulated/0/DCIM/Camera/test1.flac";
                        //audio_path = "/storage/emulated/0/DCIM/Camera/roy.flac";
                        //audio_path = "/storage/emulated/0/DCIM/Camera/boom.flac";
                        STT_external_api STT = new STT_external_api(audio_path);
                        ArrayList<String> matches = STT.transcript();
                        if(matches==null) {
                            mApp.alertMessage(thisContext,"Please try again","can't recognize this voice ,please try again.");
                            File file = new File(abs_path);
                            boolean deleted = file.delete();
                            file = new File(audio_path);
                            deleted = file.delete();
                            btn_rec_yes_mode = false;
                            rec_yes_btn.setText(R.string.button_yes);
                            return;
                        }
                        if(matches.size()==0) {
                            mApp.alertMessage(thisContext,"Please try again","can't recognize this voice ,please try again.");
                            File file = new File(abs_path);
                            boolean deleted = file.delete();
                            file = new File(audio_path);
                            deleted = file.delete();
                            btn_rec_yes_mode = false;
                            rec_yes_btn.setText(R.string.button_yes);
                            return;
                        }
                        mApp.setUriYesVideo(UriYesVideo);
                        mApp.setAudioPath(audio_path);
                        mApp.setMatchesList(matches);
                    }
                    @Override
                    public void onFinish() {
                        Log.d("finished", "FFmpeg cmd finished: is FFmpeg process running: " + ffmpeg.isFFmpegCommandRunning());
                    }
                });
            } catch (FFmpegCommandAlreadyRunningException e) {
                // Handle if FFmpeg is already running
                Log.d("exception", "FFmpeg exception: " + e);
            }
        }
    }

    public void onClick_record_yes(View view){
        rec_yes_btn = (Button) view;
        if(!btn_rec_yes_mode) {
            btn_rec_yes_mode = true;
            Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
            }
            rec_yes_btn.setText(R.string.button_watch_record);
        }else{
            start_video_and_hide_button(UriYesVideo);
        }
    }

    public void onClick_delete_record_yes(View view){
        //rec_yes_btn.setText("Recorder YES");
        //btn_rec_yes_mode = false;
    }

    private void start_video_and_hide_button(Uri video_uri){
        VideoContainer.setVisibility(View.VISIBLE);
        DisplayMetrics metrics = new DisplayMetrics(); getWindowManager().getDefaultDisplay().getMetrics(metrics);
        android.widget.RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) VideoContainer.getLayoutParams();
        params.width =  metrics.widthPixels;
        params.height = metrics.heightPixels;
        params.leftMargin = 0;
        VideoContainer.setLayoutParams(params);

        Button record_yes = (Button) findViewById(R.id.button4);
        Button record_no = (Button) findViewById(R.id.button5);
        record_yes.setVisibility(View.INVISIBLE);
        record_no.setVisibility(View.INVISIBLE);

        patient_video.setVideoURI(video_uri);
        patient_video.requestFocus();
        patient_video.start();
    }
    private String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Video.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}

