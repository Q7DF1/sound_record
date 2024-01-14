package com.example.soundrecord.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

import androidx.core.app.ActivityCompat;

import com.example.soundrecord.MainActivity;

public class AudioRecorder {
    private AudioRecord audioRecord;
    private int bufferSize;

    public interface AmplitudeListener {
        void onAmplitudeChanged(int amplitude);
    }

    private AmplitudeListener amplitudeListener;

    public void setAmplitudeListener(AmplitudeListener amplitudeListener) {
        this.amplitudeListener = amplitudeListener;
    }

    public void startRecording() {
        bufferSize = AudioRecord.getMinBufferSize(
                44100
                , AudioFormat.CHANNEL_IN_MONO
                , AudioFormat.ENCODING_PCM_16BIT);
        if (ActivityCompat.checkSelfPermission(new MainActivity(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC
                , 44100, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize);
        short[] buffer = new short[bufferSize];
        audioRecord.startRecording();
        while (true) {
            int byteRead = audioRecord.read(buffer,0,bufferSize);
            int amplitude = calculateAmplitude(buffer,byteRead);
            if (amplitudeListener != null) {
                amplitudeListener.onAmplitudeChanged(amplitude);
            }
        }
    }



    public void stopRecording() {
        if (audioRecord != null) {
            audioRecord.stop();
            audioRecord.release();
            audioRecord = null;
        }
    }

    private int calculateAmplitude(short[] buffer, int byteRead) {
        int sum = 0;
        for (int i = 0; i < byteRead; i++) {
            sum += Math.abs(buffer[i]);
        }
        return sum / byteRead;
    }
}
