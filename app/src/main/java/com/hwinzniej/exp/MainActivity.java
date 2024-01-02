package com.hwinzniej.exp;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    TextView txt, txt2;
    ListView musicView;   //定义列表组件，用于显示音乐信息
    ArrayAdapter<String> adapter;   //定义适配器
    Button readBtn, stopBtn;
    String[] list = new String[5];
    Map<Integer, MusicInfo> musicMap = new HashMap<>();
    String musicUrl = "http://192.168.41.128/";
    MediaPlayer mMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txt = findViewById(R.id.textView3);
        txt2 = findViewById(R.id.textView4);
        musicView = findViewById(R.id.listView);
        readBtn = findViewById(R.id.button);
        stopBtn = findViewById(R.id.button3);
        getServerData();
        readBtn.setOnClickListener(v -> {
            for (int i = 0; i < musicMap.size(); i++) {
                list[i] = musicMap.get(i).getName() + " - " + musicMap.get(i).getSinger();
            }
            adapter = new ArrayAdapter<>(
                    MainActivity.this,
                    android.R.layout.simple_list_item_1,
                    list);
            musicView.setAdapter(adapter);
        });
        stopBtn.setOnClickListener(v -> {
            if (mMediaPlayer != null)
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.stop();
                    mMediaPlayer.reset();
                    mMediaPlayer.release();
                    txt2.setText("");
                    txt.setText("音乐播放信息");
                }
        });
        musicView.setOnItemClickListener((parent, view, position, id) -> {
            if (mMediaPlayer != null) {
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.stop();
                    mMediaPlayer.reset();
                    mMediaPlayer.release();
                    txt2.setText("");
                    txt.setText("音乐播放信息");
                }
            } //如果正在播放音乐，先停止播放
            MusicInfo clickedMusicInfo = musicMap.get(position);
            String mp3 = clickedMusicInfo.getMp3();
            try {
                String path = musicUrl + mp3;
                txt.setText(path);
                mMediaPlayer = new MediaPlayer();
                mMediaPlayer.reset();
                mMediaPlayer.setDataSource(path);
                mMediaPlayer.prepare();
                mMediaPlayer.start();
                txt2.setText("总时长：" + new SimpleDateFormat("mm:ss.SSS").format(mMediaPlayer.getDuration()));
            } catch (IOException ignored) {}
        });
    }
    public void getServerData() {
        String jsonUrl = musicUrl + "music_info.json";
        RequestQueue mQueue = Volley.newRequestQueue(MainActivity.this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(jsonUrl, response -> {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            MusicInfo musicinfo = new MusicInfo();
                            String sname = response.getJSONObject(i).getString("name");
                            String ssinger = response.getJSONObject(i).getString("singer");
                            String smp3 = response.getJSONObject(i).getString("mp3");
                            musicinfo.setName(sname);
                            musicinfo.setSinger(ssinger);
                            musicinfo.setMp3(smp3);
                            musicMap.put(i, musicinfo);
                        }
                    } catch (JSONException e) {
                        txt.setText("list错误");
                        Log.e("json错误", e.getMessage(), e);
                    }
                },
                error -> Log.e("TAG错误", "")) {
            @Override
            protected Response<JSONArray> parseNetworkResponse(
                    NetworkResponse response) {
                try {
                    String jsonString = new String(response.data,
                            StandardCharsets.UTF_8);
                    return Response.success(new JSONArray(jsonString),
                            HttpHeaderParser.parseCacheHeaders(response));
                } catch (JSONException e) {
                    return Response.error(new ParseError(e));
                }
            }
        };
        mQueue.add(jsonArrayRequest);
    }

}
