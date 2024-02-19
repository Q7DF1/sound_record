package com.example.soundrecord.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import androidx.annotation.Nullable;
import com.example.soundrecord.common.RecordStatus;
import java.io.File;
import java.io.IOException;

public class RecordingService extends Service {
    private MediaRecorder mRecorder;
    private final RecordBinder recordBinder = new RecordBinder();
    // recording config class
    private RecordStatus status = null; // record status
    public static final int NO_AMPLITUDE_VALUE = -1;
    private File mRestoreFolder;
    public class RecordBinder extends Binder{
        public int getMaxAmplitude(){
            int maxAmplitude = NO_AMPLITUDE_VALUE;
            // 获取最大振幅
            if (mRecorder != null) {
                //the maximum absolute amplitude measured since the last call, or
                 //       * 0 when called for the first time
                maxAmplitude = mRecorder.getMaxAmplitude();
            }
            return maxAmplitude;
        }
        public RecordStatus getRecordStatus() {
            return status;
        }
        public void start(){
            prepareToRecord();
            startRecording();
        }
        public void pause() {
            pauseRecording();
        }
        public void stop() {
            stopRecording();
        }
        public void resume() {
            resumeRecording();
        }

    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        // 开始录音
        return recordBinder;

    }

    @Override
    public void onCreate() {
        super.onCreate();
        mRestoreFolder = this.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 结束录音
        stopRecording();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        // 结束录音
        stopRecording();
        return true;
    }

    /*
        api method from MediaRecorder
         */
    private void prepareToRecord() {
        if (mRecorder == null) {
            mRecorder = new MediaRecorder();
        }
        // 重置所有设定
        mRecorder.reset();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        File file = new File(mRestoreFolder,"tmp.mp3");
        mRecorder.setOutputFile(file);
        mRecorder.setAudioSamplingRate(44100);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.HE_AAC);
        try {
            mRecorder.prepare();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // call it before prepareRecording
    private void startRecording() {

        if (status == RecordStatus.STOP || status == null) {
            mRecorder.start();
            status = RecordStatus.RECORDING;
        }

    }
    private void pauseRecording() {
        if (status == RecordStatus.RECORDING) {
            mRecorder.pause();
            status = RecordStatus.PAUSE;
        }
    }
    private void stopRecording() {
        if (status != RecordStatus.STOP) {
            mRecorder.stop();
            status = RecordStatus.STOP;
            releaseRecord();
        }
    }
    private void resumeRecording() {
        if (status == RecordStatus.PAUSE) {
            mRecorder.resume();
            status = RecordStatus.RECORDING;
        }
    }
    private void releaseRecord() {
        if (mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
        }
    }
}
