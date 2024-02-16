package com.example.soundrecord.common;

import android.os.Environment;
import android.view.View;

import com.example.soundrecord.R;

import java.io.File;

public class Configuration implements ConfigurationListener {

    private int mPlayId;
    private int mDirSaveId;
    // 定义录音状态 0 stop, 1 playing, 2 pause
    private RecordStatus recordStatus = RecordStatus.STOP;

    public Configuration() {}

    // 确定播放状态
    private boolean isPlay = false;
    // 确定是否可以点击保存状态、 默认开启录音后就不再显示目录可以直接保存
    private  boolean isSaveStatus = false;
    // 拓展存储目录文件
    public static final File STORY_FILE = Environment.getExternalStorageDirectory();
    // 拓展存储目录文件路径
    public static final String STORY_FILE_PATH = STORY_FILE.getAbsolutePath();

    public int getPlayId() {
        return mPlayId;
    }

    public void setPlayId(int mPlayId) {
        this.mPlayId = mPlayId;
    }

    public int getDirSaveId() {
        return mDirSaveId;
    }

    public void setDirSaveId(int mDirSaveId) {
        this.mDirSaveId = mDirSaveId;
    }

    public boolean isPlay() {
        return isPlay;
    }

    public void setPlay(boolean play) {
        isPlay = play;
    }

    public boolean isSaveStatus() {
        return isSaveStatus;
    }

    public void setSaveStatus(boolean saveStatus) {
        isSaveStatus = saveStatus;
    }
    public void setRecordStatus(RecordStatus status) {
        this.recordStatus = status;
    }

    public RecordStatus getRecordStatus() {
        return recordStatus;
    }

    @Override
    public void onChangeListener(View ...view) {
        changeRestId(view);
    }

    private void changeRestId(View ...view) {
        for (View viewI : view) {

            if (viewI.getId() == R.id.brake) {
                if (isPlay) {
                    viewI.setBackgroundResource(R.drawable.ic_pause_foreground_combin);
                } else {
                    viewI.setBackgroundResource(R.drawable.ic_start_btn_background_combin);
                }
            }
            if (viewI.getId() == R.id.dir_save) {
                if (isSaveStatus) {
                    viewI.setBackgroundResource(R.drawable.ic_save_btn_combin);
                }else {
                    viewI.setBackgroundResource(R.drawable.ic_dir_btn_combin);
                }
            }
        }
    }
}
