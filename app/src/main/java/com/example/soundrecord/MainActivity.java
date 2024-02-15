package com.example.soundrecord;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.example.soundrecord.common.Configuration;
import com.example.soundrecord.service.AudioMedia;
import com.example.soundrecord.service.impl.AudioRecordImpl;

import java.io.File;
import java.util.Objects;

public class MainActivity extends Activity {

    private AudioMedia audioMedia;
    private File musicFile = null;
    private Button mBrake;
    private Button mDirOrSave;
    private Button mFlag;
    private final Configuration config = new Configuration();
    private static final int AUDIO_PERMISSION_GRANTED = 0x123;
    private static final int STORY_PERMISSION_GRANTED = 0x124;
    private static final String[] PERMISSIONS = {Manifest.permission.RECORD_AUDIO};
    @Override
    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        musicFile = this.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        mBrake = findViewById(R.id.brake);
        mDirOrSave = findViewById(R.id.dir_save);
        mFlag = findViewById(R.id.flag);
        audioMedia = new AudioRecordImpl();
        mBrake.setOnClickListener(view -> {
            config.setPlay(!config.isPlay());
            config.setSaveStatus(true);
            config.onChangeListener(mBrake, mDirOrSave);
            if (config.isPlay()) {
                audioMedia.startRecord(musicFile);
            }else {
                audioMedia.pauseRecord();
            }
        });


        mDirOrSave.setOnClickListener(view -> {
            if (config.isSaveStatus()) {
                saveAudio();
            }else {
                showDir();
            }
        });

    }


    private void saveAudio() {
        Log.v("save","save");
        audioMedia.stopRecord();
    }

    private void showDir() {

        Log.v("showDir","showDir");
    }


    @Override
    protected void onStop() {
        super.onStop();
        audioMedia.release();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,PERMISSIONS,AUDIO_PERMISSION_GRANTED);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.v("permission","granted");
            return;
        }
        Log.v("permission","reject");
    }
}