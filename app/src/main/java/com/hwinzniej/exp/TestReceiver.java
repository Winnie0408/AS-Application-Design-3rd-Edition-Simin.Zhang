package com.hwinzniej.exp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class TestReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        String str = "后台广播已经启动,\n action=" + action;
        System.out.println(str);


    }
}
