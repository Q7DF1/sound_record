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
import com.example.soundrecord.common.RecordStatus;
import com.example.soundrecord.service.AudioMedia;
import com.example.soundrecord.service.impl.AudioRecordImpl;
import com.example.soundrecord.view.WaveformView;

import java.io.File;

@SuppressLint("RestrictedApi")
public class MainActivity extends Activity {

    private AudioMedia audioMedia;
    private File musicFile = null;
    private Button mBrake;
    private Button mDirOrSave;
    private Button mFlag;
    private final Configuration config = Configuration.newInstance();
    private static final int AUDIO_PERMISSION_GRANTED = 0x123;
    private static final int STORY_PERMISSION_GRANTED = 0x124;
    private static final String[] PERMISSIONS = {Manifest.permission.RECORD_AUDIO};
    private WaveformView mWaveformView;
    @Override
    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // init Resource stuff
        initResourceRefs();
    }

    private void saveAudio() {
        Log.v("save","save");
        if (config.getRecordStatus() == RecordStatus.RECORDING
                || config.getRecordStatus() == RecordStatus.PAUSE) {
            audioMedia.stopRecord();
            audioMedia.releaseRecorder();
            config.setRecordStatus(RecordStatus.STOP);
        }
        // 恢复
        config.setPlay(false);
        config.setSaveStatus(false);
        config.onChangeListener(mBrake,mDirOrSave);
    }

    private void showDir() {
        Log.v("showDir","showDir");
    }

    @Override
    protected void onStop() {
        super.onStop();
        audioMedia.releaseRecorder();
        audioMedia.releasePlayer();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // request permission
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

    private void updateAmplitude() {
        while (config.getRecordStatus() == RecordStatus.RECORDING) {
            runOnUiThread(()-> mWaveformView.updateAmplitude(audioMedia.getMaxAmplitude()));
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void initResourceRefs() {
        musicFile = this.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        mBrake = findViewById(R.id.brake);
        mDirOrSave = findViewById(R.id.dir_save);
        mFlag = findViewById(R.id.flag);
        audioMedia = new AudioRecordImpl();
        mWaveformView = findViewById(R.id.surface_view);
        // 制动按钮
        mBrake.setOnClickListener(view -> {
            switch (config.getRecordStatus()) {
                case STOP:
                    audioMedia.startRecord(musicFile);
                    config.setRecordStatus(RecordStatus.RECORDING);
                    // 记录音浪
                    new Thread(this::updateAmplitude);
                    break;
                case RECORDING:
                    audioMedia.pauseRecord();
                    config.setRecordStatus(RecordStatus.PAUSE);
                    break;
                case PAUSE:
                    audioMedia.resumeRecord();
                    config.setRecordStatus(RecordStatus.RECORDING);
                    new Thread(this::updateAmplitude);
                    break;
                default:
                    throw new RuntimeException("unknown status code");
            }
            // 视图变化
            config.setPlay(!config.isPlay());
            config.setSaveStatus(true);
            config.onChangeListener(mBrake, mDirOrSave);
        });
        // 保存 || 打开目录
        mDirOrSave.setOnClickListener(view -> {
            if (config.isSaveStatus()) {
                saveAudio();
            }else {
                showDir();
            }
        });
    }


}