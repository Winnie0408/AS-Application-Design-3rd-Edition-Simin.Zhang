package com.hwinzniej.exp;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

public class MainActivity extends AppCompatActivity {
    Button IPBtn;
    EditText hostName;
    ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IPBtn = findViewById(R.id.button);
        hostName = findViewById(R.id.editTextText);
        IPBtn.setOnClickListener(v -> {
            try {
                getLocalIPAddress(hostName.getText().toString());
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });
    }

    public void getLocalIPAddress(String hostName) {
        try {
            if (hostName.equals("")) {
                AtomicReference<String> ipv4 = new AtomicReference<>("");
                ArrayList<NetworkInterface> nilist = Collections.list(NetworkInterface.getNetworkInterfaces());
                for (NetworkInterface ni : nilist) {
                    ArrayList<InetAddress> ialist = Collections.list(ni.getInetAddresses());
                    for (InetAddress address : ialist) {
                        ipv4.set(address.getHostAddress());
                        if (!address.isLoopbackAddress() && address instanceof Inet4Address) {
                            runOnUiThread(() -> {
                                Toast.makeText(MainActivity.this, "本机IP地址：" + ipv4, Toast.LENGTH_LONG).show();
                            });
                        }
                    }
                }
            } else {
                executorService = Executors.newSingleThreadExecutor();
                AtomicReference<String> address = new AtomicReference<>("");
                executorService.execute(() -> {
                    try {
                        address.set(InetAddress.getByName(hostName).getHostAddress());
                        runOnUiThread(() -> {
                            Toast.makeText(MainActivity.this, hostName + " 对应的IP地址：" + address.get(), Toast.LENGTH_LONG).show();
                        });
                    } catch (UnknownHostException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        } catch (SocketException ex) {
            Log.e("localip", ex.toString());
        }
    }
}
