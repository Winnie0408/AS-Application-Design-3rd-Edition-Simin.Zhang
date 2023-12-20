package com.hwinzniej.exp;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class SmsSender extends BroadcastReceiver {
    public static final String ACTION = "action.send.sms";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (ACTION.equals(action)) {
            if (getResultCode() == Activity.RESULT_OK) {
                MainActivity.instance.showSms();
                Toast.makeText(context, "发送成功", Toast.LENGTH_SHORT).show();
            }else
                Toast.makeText(context, "发送失败", Toast.LENGTH_SHORT).show();
        }
    }
}