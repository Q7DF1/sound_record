package com.example.soundrecord.service.impl;

import android.app.Service;
import android.app.job.JobService;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.util.Log;

import androidx.core.app.JobIntentService;

import com.example.soundrecord.common.Configuration;
import com.example.soundrecord.service.AudioMedia;
import java.io.File;
import java.io.IOException;

public class AudioRecordImpl implements AudioMedia {
    // 文件暂存名字
    private MediaPlayer mediaPlayer = null;
    private MediaRecorder mediaRecorder = null;
    private final Configuration mConfig = Configuration.newInstance();
    public AudioRecordImpl() {
    }
    private static final String TMP_NAME_MP3 = "record_tmp.mp3";
    private static final String TMP_NAME_ACC = "record_tmp.acc";

    @Override
    public void startRecord(File filePath) {
        if (mediaRecorder == null) {
            mediaRecorder = new MediaRecorder();

        }
        File file = new File(filePath,TMP_NAME_ACC);
        Log.v("path",file.getAbsolutePath());
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFile(file);
        mediaRecorder.setAudioSamplingRate(44100);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.HE_AAC);
        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        mediaRecorder.start();
    }

    @Override
    public void stopRecord() {
        mediaRecorder.stop();
        releaseRecorder();
    }

    @Override
    public void pauseRecord() {
        mediaRecorder.pause();
    }

    @Override
    public void resumeRecord() {
        mediaRecorder.resume();
    }

    @Override
    public void startPlay() {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        }
    }
    @Override
    public void stopPlay() {

    }

    @Override
    public void pausePlay() {

    }

    @Override
    public void resumePlay() {

    }
    @Override
    public void releaseRecorder() {
        if (mediaRecorder != null) {
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }
    @Override
    public void releasePlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }

    }

    @Override
    public int getMaxAmplitude() {
        return mediaRecorder.getMaxAmplitude();
    }

    public void setOnRecordListener(MediaRecorder.OnInfoListener listener) {
        mediaRecorder.setOnInfoListener(listener);
    }

    public void setOnPlayListener(MediaPlayer.OnInfoListener listener) {
        mediaPlayer.setOnInfoListener(listener);
    }

}
