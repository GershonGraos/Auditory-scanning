package com.graos.auditory_scanning_final_project;
/**
 * Created by GG on 13/01/2017.
 */
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Edit_Rama_1 extends AppCompatActivity {

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



    DBHelper_Requests my_dbHelper_requests;
    MyListAdapter my_list_adapter;
    ListView _my_list_view;

    TextView _patient;
    EditText _item;
    EditText a;
    int flag_edit_delete = 0;

    String id_patient, request;
    String request_click;
    String id_parent_send;
    ImageView help;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__rama_1);
        setTitle(R.string.nameActivity_edit_pattient);
        thisContext = this;

        //help = (ImageView) findViewById(R.id.imageView8);
        //help.bringToFront();

        // ----- DB -------
        my_dbHelper_requests = new DBHelper_Requests(Edit_Rama_1.this);

        _patient = (TextView) findViewById(R.id.textViewHiPatient);
        _item = (EditText) findViewById(R.id.editText);
        _my_list_view = (ListView) findViewById(R.id.listItems);

        Intent i_result_patient;
        i_result_patient = getIntent();
        id_patient = i_result_patient.getStringExtra("ID_PATIENT");

        _patient.setText(id_patient);

        // SHOW THE LIST OF REQUESTS
        populateListViews();

        // ONE-CLICK TO ENTRY TO INTERIOR LIST
        _my_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // ListView Clicked item value
                request_click = (String)_my_list_view.getItemAtPosition(position);

                Cursor cursor = my_dbHelper_requests.show_requests();
                while(cursor.moveToNext()){
                    if(cursor.getString(3).equals(request_click)){
                        id_parent_send = cursor.getString(4);
                        break;
                    }
                }

                Intent i;
                i = new Intent(Edit_Rama_1.this, Edit_Rama_2.class);
                i.putExtra("THE_REQUEST",request_click);
                i.putExtra("ID_PT",id_patient);
                i.putExtra("MY_I_PARENT",id_parent_send);
                startActivity(i);
            }
        });


        // LONG-CLICK TO DELETE SPECIFIC REQUEST
        _my_list_view.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l){
                request_click = (String)_my_list_view.getItemAtPosition(position);
                View view1 = LayoutInflater.from(Edit_Rama_1.this).inflate(R.layout.edit_or_delete_request, null);
                final AlertDialog.Builder builder_buttons = new AlertDialog.Builder(Edit_Rama_1.this);
                builder_buttons.setView(view1);
                builder_buttons.setTitle(R.string.edit_tittle_buttons);
                builder_buttons.setIcon(R.mipmap.ic_alert);
                builder_buttons.setNegativeButton(R.string.edit_exit_option, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                builder_buttons.show();
                return true;
            }
        });


        // ----------- VIDEO RECORD ------------

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
    }



    // EDIT REQUEST
    public void button_edit_request(View view){
        final AlertDialog.Builder builder = new AlertDialog.Builder(Edit_Rama_1.this);
        builder.setTitle(R.string.edit_tittle);
        builder.setIcon(R.mipmap.ic_edit_req);
        a = new EditText(Edit_Rama_1.this);
        builder.setView(a);
        builder.setMessage(R.string.edit_message)

                .setPositiveButton(R.string.button_get_yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String in = a.getText().toString();
                        if( !in.equals("")){
                            Cursor cursor = my_dbHelper_requests.show_requests();
                            while(cursor.moveToNext()) {
                                if(cursor.getString(3).equals(request_click)){
                                    boolean edit_row = my_dbHelper_requests.update_data(in , cursor.getString(4));
                                    if(edit_row == true){
                                        flag_edit_delete = 1;
                                        populateListViews();
                                        Toast.makeText(Edit_Rama_1.this, R.string.edit_successfully, Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                        Toast.makeText(Edit_Rama_1.this, R.string.edit_not_successfully, Toast.LENGTH_SHORT).show();
                                    break;
                                }
                            }
                        }
                        else
                            Toast.makeText(Edit_Rama_1.this, R.string.edit_enter_request, Toast.LENGTH_SHORT).show();
                    }
                })

                .setNegativeButton(R.string.button_get_no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        builder.show();
    }


    // DELETE REQUEST
    public void button_delete_request(View view){
        final AlertDialog.Builder builder = new AlertDialog.Builder(Edit_Rama_1.this);
        builder.setTitle(R.string.delete_item_tittle);
        builder.setIcon(R.mipmap.ic_remove);
        builder.setMessage(R.string.delete_item_quetion)

                .setPositiveButton(R.string.button_get_yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Cursor cursor = my_dbHelper_requests.show_requests();
                        if(cursor.getCount() != 0){
                            while(cursor.moveToNext()) {
                                if(cursor.getString(3).equals(request_click)){
                                    Integer delete_row = my_dbHelper_requests.delete_data(cursor.getString(4));
                                    if(delete_row > 0)
                                        populateListViews();
                                    Toast.makeText(getApplicationContext(), R.string.delete_the_item , Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    }
                })

                .setNegativeButton(R.string.button_get_no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        builder.show();
    }



    // ADD REQUEST TO DB
    public void press_addItem(View view){
        request= _item.getText().toString();
        int count = 0;
        int flag = 0;

        if(!request.equals("")){
            Cursor cursor = my_dbHelper_requests.show_requests();
            while (cursor.moveToNext()){
                if(cursor.getString(3). equals(request)){
                    Toast.makeText(this, R.string.same_request, Toast.LENGTH_SHORT).show();
                    flag = 1;
                }

            }

            if(flag == 0){
                long temp = my_dbHelper_requests.add_request(id_patient, "-1", "1", request, getDateTime(), count);
                if (temp == -1)
                    Toast.makeText(this, R.string.sign_up_error, Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, R.string.sign_up_successful, Toast.LENGTH_SHORT).show();

                _item.setText("");
                populateListViews();
            }

        }
        else
            Toast.makeText(this, R.string.insert_item, Toast.LENGTH_SHORT).show();
    }



    // SHOW DATABASE
    public void show_db_requests(View view){
        Cursor res = my_dbHelper_requests.show_requests();
        if(res.getCount() == 0) {
            showMessage("Error", "No data found");
            return;
        }
        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()){
            buffer.append("Id-patient: " + res.getString(0) + "\n");
            buffer.append("I_parent: " + res.getString(1) + "\n");
            buffer.append("Stage: " + res.getString(2) + "\n");
            buffer.append("Request: " + res.getString(3)+ "\n");
            buffer.append("I: " + res.getString(4) + "\n");
            buffer.append("time: " + res.getString(5) + "\n");
            buffer.append("count: " + String.valueOf(res.getInt(6)) + "\n\n");
        }
        showMessage("Data", buffer.toString());
    }

    public void showMessage(String tittle, String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(tittle);
        builder.setMessage(Message);
        builder.show();
    }

    // SHOW THE STRING ARRAY REQUESTS INTO LAYOUT //
    public void populateListViews(){
        Cursor cursor = my_dbHelper_requests.show_requests();
        if(cursor.getCount() != 0) {
            ArrayList<String> listRequests = new ArrayList<String >();
            while (cursor.moveToNext()) {
                if(cursor.getString(0).equals(id_patient) && cursor.getString(1).equals("-1") && cursor.getString(2).equals("1"))
                    listRequests.add(cursor.getString(3));
            }
            my_list_adapter = new MyListAdapter(this, listRequests);
            _my_list_view.setAdapter(my_list_adapter);
        }
        else
            _my_list_view.setAdapter(null);
    }

    // GET DATE TIME
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    // ----------- VIDEO FUNCTIONS ------------
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
        }    }

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
