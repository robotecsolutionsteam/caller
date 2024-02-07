package com.robotec.temi.bluetooth;

import java.util.ArrayDeque;

interface SerialListener {
    void onSerialConnect      ();
    void onSerialConnectError (Exception e);
    void onSerialRead         (byte[] data);
    void onSerialRead         (ArrayDeque<byte[]> datas);
    void onSerialIoError      (Exception e);
}
