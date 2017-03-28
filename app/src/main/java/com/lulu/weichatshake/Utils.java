package com.lulu.weichatshake;

import android.bluetooth.BluetoothAdapter;

/**
 * Created by lin on 2017/3/18.
 */

public class Utils {

    public static String getBLEState() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter == null) {
            return "该手机没有蓝牙";
        }
        if (adapter.isEnabled()) {
            return "true";
        }
        return "请先开启手机蓝牙";
    }
}
