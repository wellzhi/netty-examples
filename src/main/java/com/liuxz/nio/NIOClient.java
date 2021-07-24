package com.liuxz.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NIOClient {
    public static void main(String[] args) throws Exception {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 6666);

        // 连接服务器
        if (!socketChannel.connect(inetSocketAddress)) {
            while (!socketChannel.finishConnect()) {
                System.out.println("因为连接需要时间，客户端不会阻塞，可以做其他工作");
            }
        }
        // 如果连接成功，就发送数据
        String content = "hello better";
        ByteBuffer byteBuffer = ByteBuffer.wrap(content.getBytes());
        // 发送数据，将buffer数据写入channel
        socketChannel.write(byteBuffer);
        System.in.read();
    }
}
