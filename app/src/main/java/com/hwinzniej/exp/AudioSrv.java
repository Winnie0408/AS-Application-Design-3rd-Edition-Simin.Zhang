package com.hwinzniej.exp;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.widget.Toast;

public class AudioSrv extends Service {
    public MediaPlayer play;
    private AudioReceiver recevier;  //新增----------
    private IntentFilter intentFilter;  //新增--------
    private final int[] musicList = {R.raw.fb, R.raw.xzh, R.raw.irememberu, R.raw.wmdmyc};
    private final String[] musicName = {"风波", "雪之华", "I Remember U", "我们都没有错"};
    private int currentNo = 0;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

//    public void onCreate() //创建后台服务
//    {
//        super.onCreate();
//        Toast.makeText(this, "创建后台服务...", Toast.LENGTH_LONG).show();
//    }

    void on_Start() {    //启动广播
        recevier = new AudioReceiver();
        intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(recevier, intentFilter);
    }

//    public int onStartCommand(Intent intent, int flags, int startId) {//启动后台服务
//        System.out.println("(4)启动后台服务......");
//        super.onStartCommand(intent, flags, startId);
//
//        switch (intent.getIntExtra("play", -1)) {
//            case 1:
//                if (play == null) {
//                    play = MediaPlayer.create(this, musicList[currentNo]);
//                    MainActivity.txt.setText("正在播放：" + musicName[currentNo]);
//                }
//                if (play.getCurrentPosition() == 0) {
//                    play.start();  //开始
//                    on_Start();    //启动广播
//                    Toast.makeText(this, "启动后台服务，播放音乐...", Toast.LENGTH_SHORT).show();//提示
//                } else {
//                    play.start();  //开始
//                    MainActivity.txt.setText("正在播放：" + musicName[currentNo]);
//                    Toast.makeText(this, "继续...", Toast.LENGTH_SHORT).show();//提示
//                }
//                break;
//            case 2:
//                play.pause();//暂停
//                MainActivity.txt.setText("暂停播放：" + musicName[currentNo]);
//                Toast.makeText(this, "暂停...", Toast.LENGTH_SHORT).show();//提示框
//                break;
//            case 3:
//                play.stop();//停止
//                play.release();//释放内存
//                play = null;
//                MainActivity.txt.setText("播放停止");
//                Toast.makeText(this, "销毁后台服务...", Toast.LENGTH_SHORT).show();//提示框
//                break;
//            case 4:
//                play.stop();//停止
//                play.release();//释放内存
//                currentNo = (currentNo - 1 + musicList.length) % musicList.length;
//                play = MediaPlayer.create(this, musicList[currentNo]);
//                play.start();
//                MainActivity.txt.setText("正在播放：" + musicName[currentNo]);
//                Toast.makeText(this, "上一曲...", Toast.LENGTH_SHORT).show();//提示框
//                break;
//            case 5:
//                play.stop();//停止
//                play.release();//释放内存
//                currentNo = (currentNo + 1) % musicList.length;
//                play = MediaPlayer.create(this, musicList[currentNo]);
//                play.start();
//                MainActivity.txt.setText("正在播放：" + musicName[currentNo]);
//                Toast.makeText(this, "下一曲...", Toast.LENGTH_SHORT).show();//提示框
//                break;
//        }
//        return START_STICKY;
//    }

    public void onDestroy() {
        play.release();
        super.onDestroy();
        Toast.makeText(this, "销毁后台服务！", Toast.LENGTH_LONG).show();
    }

}
