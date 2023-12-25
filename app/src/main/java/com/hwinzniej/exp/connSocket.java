package com.hwinzniej.exp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.concurrent.Callable;

public class connSocket implements Callable<String> {
    static DataInputStream datain;
    static DataOutputStream dataout;
    static Socket ss;
    String IP = "192.168.91.43";
    String runLog = "";
    public String call() throws Exception {
        try {
            ss = new Socket();
            SocketAddress socketAddress = new InetSocketAddress(IP, 8888);
            ss.connect(socketAddress, 5000); //设置超时时间
            datain = new DataInputStream(ss.getInputStream());//创建数据输入流
            dataout = new DataOutputStream(ss.getOutputStream());//创建数据输出流
            dataout.writeUTF("客户端发来的信息: Socket 我来了 !! ");
            this.runLog = datain.readUTF();
            Thread.sleep(500);
            dataout.writeUTF("客户端发来的信息: 我已经收到服务器的信息 !! ");
            this.runLog = datain.readUTF();
        } catch (Exception e) {
        }
        return this.runLog;
    }

    public static void disConnet() {
        if (datain != null)
            try {
                datain.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        if (dataout != null)
            try {
                dataout.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        if (ss != null)
            try {
                ss.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
    }
}
