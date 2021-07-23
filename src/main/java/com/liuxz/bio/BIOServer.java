package com.liuxz.bio;


import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BIOServer {

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(6666);
        ExecutorService newCachedThreadPool = Executors.newCachedThreadPool();
        System.out.println("服务器启动了");
        while (true) {
            System.out.println("线程信息：id=" + Thread.currentThread().getId() +
                    "，名字=" + Thread.currentThread().getName());
            System.out.println("等待连接......");
            final Socket socket = serverSocket.accept();
            System.out.println("连接到一个客户端");
            newCachedThreadPool.execute(new Runnable() {
                public void run() {
                    handler(socket);
                }
            });
        }

    }

    private static void handler(Socket socket) {
        try {
            System.out.println("线程信息：id=" + Thread.currentThread().getId() +
                    "，名字=" + Thread.currentThread().getName());
            byte[] bytes = new byte[1024];
            InputStream inputStream = socket.getInputStream();
            while (true) {
                System.out.println("线程信息：id=" + Thread.currentThread().getId() +
                        "，名字=" + Thread.currentThread().getName());
                System.out.println("read......");
                int read = inputStream.read(bytes);
                if (read != -1) {
                    System.out.println(new String(bytes, 0, read));
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("关闭和client的连接");
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
