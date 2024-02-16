package com.example.soundrecord.common;
/*
    stop() -> 0
    start() / resume() -> 1
    pause() -> 2
    stop  <==> 0
    going <==> 1
    pause <==> 2

* */
public enum RecordStatus {
    STOP((byte) 0),
    RECORDING((byte) 1),
    PAUSE((byte) 2);

    final byte value;
    RecordStatus(byte value) {
        this.value = value;
    }

}
