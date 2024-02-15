package com.example.soundrecord.service;

import java.io.File;

public interface AudioMedia {
    void startRecord(File filePath);
    void stopRecord();
    void pauseRecord();
    void resumeRecord();

    void startPlay();

    void stopPlay();
    void pausePlay();
    void resumePlay();
    void release();
}
