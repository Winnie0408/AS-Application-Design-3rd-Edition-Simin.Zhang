package com.hwinzniej.exp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AudioReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action != null) {
            String str = "正在播放音乐";
            System.out.println(str);
        }
    }
}
