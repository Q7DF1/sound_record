package com.example.soundrecord.service.impl;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.util.Log;
import com.example.soundrecord.service.AudioMedia;
import java.io.File;
import java.io.IOException;

public class AudioRecordImpl implements AudioMedia {
    // 文件暂存名字
    private MediaPlayer mediaPlayer = null;
    private MediaRecorder mediaRecorder = null;
    public AudioRecordImpl() {
    }
    private static final String TMP_NAME = "record_tmp.mp3";

    @Override
    public void startRecord(File filePath) {
        if (mediaRecorder == null) {
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setOnInfoListener(new MediaRecorder.OnInfoListener() {
                @Override
                public void onInfo(MediaRecorder mr, int what, int extra) {
                    Log.v("mr--" + mr.toString(),"what--" + what + "--extra--" + extra);
                }
            });
        }
        File file = new File(filePath,TMP_NAME);
        Log.v("path",file.getAbsolutePath());
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFile(file);
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

    public void setOnRecordListener(MediaRecorder.OnInfoListener listener) {
        mediaRecorder.setOnInfoListener(listener);
    }

    public void setOnPlayListener(MediaPlayer.OnInfoListener listener) {
        mediaPlayer.setOnInfoListener(listener);
    }
}
