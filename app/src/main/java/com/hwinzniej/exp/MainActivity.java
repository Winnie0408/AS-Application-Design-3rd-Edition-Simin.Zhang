package com.hwinzniej.exp;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.method.ScrollingMovementMethod;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    Button send, confirm;
    EditText input, phone;
    TextView content;
    private static final int MY_PERMISSIONS_REQUEST_READ_SMS = 0;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;
    private static final int MY_PERMISSIONS_REQUEST_RECEIVE_SMS = 0;
    String phoneNumber;
    ArrayList<Long> date = new ArrayList<>();
    Map<Long, String> smsContent = new HashMap<>();
    public static MainActivity instance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        send = findViewById(R.id.send);
        input = findViewById(R.id.input);
        phone = findViewById(R.id.phone);
        content = findViewById(R.id.content);
        content.setMovementMethod(new ScrollingMovementMethod());
        confirm = findViewById(R.id.confirm);
        instance = this;
        registerReceiver(new SmsReceiver(), new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
        requestPermission();
        confirm.setOnClickListener(v -> showSms());
        send.setOnClickListener(v -> sendSms(String.valueOf(input.getText())));
    }
    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_SMS},
                    MY_PERMISSIONS_REQUEST_READ_SMS);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS},
                    MY_PERMISSIONS_REQUEST_SEND_SMS);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECEIVE_SMS},
                    MY_PERMISSIONS_REQUEST_RECEIVE_SMS);
        }
    } //请求短信相关权限

    private void sendSms(String message) {
        String SENT = "action.send.sms";
        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), PendingIntent.FLAG_IMMUTABLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            registerReceiver(new SmsSender(), new IntentFilter(SENT), Context.RECEIVER_EXPORTED);
        }
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber, null, message, sentPI, null);
        input.setText("");
    }  //发送短信

    public void showSms() {
        phoneNumber = String.valueOf(phone.getText());
        readSms();
        if (date.size() == 0)
            Toast.makeText(this, "没有与" + phoneNumber + "的短信记录", Toast.LENGTH_SHORT).show();
        else {
            for (Long d : date) {
                content.append(convertTsToTime(d) + "\n" + smsContent.get(d) + "\n----------------------------------------\n\n");
            }
            int scrollAmount = content.getLayout().getLineTop(content.getLineCount()) - content.getHeight();
            // if there is no need to scroll, scrollAmount will be <=0
            if (scrollAmount > 0)
                content.scrollTo(0, scrollAmount);
            else
                content.scrollTo(0, 0);
        }
    }  //显示短信

    public void readSms() {
        date.clear();
        smsContent.clear();
        content.setText("");

        String phoneNumber = instance.phoneNumber;
        Uri uri = Uri.parse("content://sms/inbox");
        String[] projection = new String[]{"_id", "address", "person", "body", "date", "type"};
        String where = "address = ?";
        String[] whereValue = {phoneNumber};
        Cursor cursor = getContentResolver().query(uri, projection, where, whereValue, "date asc");
        try {
            if (cursor.moveToFirst()) {
                date.add(cursor.getLong(cursor.getColumnIndex("date")));
                smsContent.put(cursor.getLong(cursor.getColumnIndex("date")), "对方说：\n" + cursor.getString(cursor.getColumnIndex("body")));

                while (cursor.moveToNext()) {
                    date.add(cursor.getLong(cursor.getColumnIndex("date")));
                    smsContent.put(cursor.getLong(cursor.getColumnIndex("date")), "对方说：\n" + cursor.getString(cursor.getColumnIndex("body")));
                }
//                String address = cursor.getString(cursor.getColumnIndex("address"));
//                StringBuilder body = new StringBuilder(convertTs(cursor.getString(cursor.getColumnIndex("date"))));
//                body.append("\n").append(cursor.getString(cursor.getColumnIndex("body"))).append("\n----------------------------------------");
//                phone.setText(address);
//                while (cursor.moveToNext()) {
//                    body.append("\n\n").append(convertTs(cursor.getString(cursor.getColumnIndex("date")))).append("\n").append(cursor.getString(cursor.getColumnIndex("body"))).append("\n----------------------------------------");
//                }
//                content.setText(body.toString());
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        uri = Uri.parse("content://sms/sent");
        cursor = getContentResolver().query(uri, projection, where, whereValue, "date asc");
        try {
            if (cursor.moveToFirst()) {
                date.add(cursor.getLong(cursor.getColumnIndex("date")));
                smsContent.put(cursor.getLong(cursor.getColumnIndex("date")), "您说：\n" + cursor.getString(cursor.getColumnIndex("body")));

                while (cursor.moveToNext()) {
                    date.add(cursor.getLong(cursor.getColumnIndex("date")));
                    smsContent.put(cursor.getLong(cursor.getColumnIndex("date")), "您说：\n" + cursor.getString(cursor.getColumnIndex("body")));
                }
//                String address = cursor.getString(cursor.getColumnIndex("address"));
//                StringBuilder body = new StringBuilder(convertTs(cursor.getString(cursor.getColumnIndex("date"))));
//                body.append("\n").append(cursor.getString(cursor.getColumnIndex("body"))).append("\n----------------------------------------");
//                phone.setText(address);
//                while (cursor.moveToNext()) {
//                    body.append("\n\n").append(convertTs(cursor.getString(cursor.getColumnIndex("date")))).append("\n").append(cursor.getString(cursor.getColumnIndex("body"))).append("\n----------------------------------------");
//                }
//                content.setText(body.toString());
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        date.sort(Long::compareTo);
    }  //读取短信

    private String convertTsToTime(Long timestamp) {
        Date date = new Date(timestamp);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(date);
    }  //时间戳转时间

//    class mClick implements View.OnClickListener {
//        @Override
//        public void onClick(View v) {
//            String service = Context.NOTIFICATION_SERVICE;
//            n_Manager = (NotificationManager)
//                    MainActivity.this.getSystemService(service);
//            if (v == btn1) {
//                showNotification();
//            } else if (v == btn2) {
//                n_Manager.cancelAll();
//            }
//        }
//
//        private void showNotification() {
//            int icon1 = R.drawable.shield;  //事先准备好图标文件
//            int icon2 = R.drawable.notice;
//            CharSequence tickerText = "紧急通知,程序已启动";
//            long when = System.currentTimeMillis();
//            String CHANNEL_ID = "zsm_id";//应用频道Id唯一值，
//            String CHANNEL_NAME = "zsm_name";
//
//            Intent intent = new Intent(MainActivity.this, MainActivity.class);
//            PendingIntent pintent = PendingIntent.getActivity(
//                    MainActivity.this,
//                    1001,
//                    intent,
//                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
//            notification = new NotificationCompat.Builder(MainActivity.this, CHANNEL_ID)
//                    .setSmallIcon(icon2)//设置小图标
//                    .setContentTitle("通知")
//                    .setContentText(tickerText)   //("通知内容")
//                    .setContentIntent(pintent)
//                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), icon1))
//                    .setPriority(Notification.PRIORITY_HIGH)
//                    .setFullScreenIntent(pintent, true)
//                    .build();
//            //Android 8.0 以上需添加渠道
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,
//                        CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW);
//                n_Manager.createNotificationChannel(notificationChannel);
//            }
//            n_Manager.notify(NOTIFY_ID, notification);
//        }
//    }
}
