package com.example.soundrecord;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends Activity {

    private boolean mChecked = true;
    private boolean mIsSave = false;
    @Override
    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button brake = findViewById(R.id.brake);
        Button saveOrDir = findViewById(R.id.save_or_dir);
        brake.setOnClickListener(view -> {
            mChecked = !mChecked;
            mIsSave = true;
            int playResId = mChecked ? R.drawable.ic_start_btn_background_combin : R.drawable.ic_pause_foreground_combin;
            saveOrDir.setBackgroundResource(R.drawable.ic_save_btn_combin);
            brake.setBackgroundResource(playResId);
        });
    }
}