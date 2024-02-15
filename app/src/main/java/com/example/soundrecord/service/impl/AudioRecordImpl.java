package com.example.soundrecord.service.impl;

import android.media.MediaPlayer;
import android.media.MediaRecorder;

import com.example.soundrecord.service.AudioMedia;

import java.io.File;
import java.io.IOException;

public class AudioRecordImpl implements AudioMedia {
    // 文件暂存名字
    private MediaPlayer mediaPlayer;
    private MediaRecorder mediaRecorder;

    public AudioRecordImpl() {
        mediaRecorder = new MediaRecorder();
        mediaPlayer = new MediaPlayer();
    }
    private static final String TMP_NAME = "record.tmp";

    @Override
    public void startRecord(File filePath) {
        File file = new File(filePath,TMP_NAME);
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFile(file);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);
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
        release();
    }

    @Override
    public void pauseRecord() {

    }

    @Override
    public void resumeRecord() {

    }

    @Override
    public void startPlay() {

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
    public void release() {
        if (mediaRecorder != null) {
            mediaRecorder.release();
        }
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }
}
