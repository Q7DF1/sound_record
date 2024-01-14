package com.example.soundrecord;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.example.soundrecord.common.Configuration;
import com.example.soundrecord.view.WaveformView;

import java.util.Arrays;

public class MainActivity extends Activity {

    @Override
    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button brake = findViewById(R.id.brake);
        Button saveOrDir = findViewById(R.id.save_or_dir);
        brake.setOnClickListener(view -> {
            Configuration.IS_PLAY = !Configuration.IS_PLAY;
            Configuration.IS_SAVE_STATE = true;
            int playResId = !Configuration.IS_PLAY ? R.drawable.ic_start_btn_background_combin : R.drawable.ic_pause_foreground_combin;
            saveOrDir.setBackgroundResource(R.drawable.ic_save_btn_combin);
            brake.setBackgroundResource(playResId);
            showWave();
        });
    }


    private void showWave() {
        @SuppressLint("WrongViewCast")
        WaveformView waveformView = (WaveformView) findViewById(R.id.surface_view);
        for (short i = 0; i < 100; i++) {
            waveformView.updateAudioData(generateAudioData(44100,10000));
            waveformView.drawWaveform();
        }
    }
    public static short[] generateAudioData(int numSamples, int amplitude) {
        short[] audioData = new short[numSamples];

        for (int i = 1; i < numSamples; i++) {
            // 生成简单的正弦波形数据，你可以根据需要替换为其他生成逻辑
            double angle = 2.0 * Math.PI * i / (44100.0 / 440.0); // 440Hz 正弦波
            audioData[i] = (short) (amplitude * Math.sin(angle));
            if (i == 1) {
                break;
            }
        }
        Log.d("data", "generateAudioData: " + Arrays.toString(audioData));
        return audioData;
    }
}