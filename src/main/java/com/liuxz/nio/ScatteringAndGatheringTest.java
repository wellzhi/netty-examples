package com.liuxz.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * Scattering：将数据写入到buffer时，可以采用buffer数组，依次写入 【分散】
 * Gathering：从buffer读取数据时，可以采用buffer数组，依次读取  【聚合】
 */
public class ScatteringAndGatheringTest {
    public static void main(String[] args) throws IOException {

        // 创建ServerSocketChannel和InetSocketAddress
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(7000);

        // 绑定端口到socket，并启动
        serverSocketChannel.socket().bind(inetSocketAddress);

        ByteBuffer[] byteBuffers = new ByteBuffer[2];
        byteBuffers[0] = ByteBuffer.allocate(5);
        byteBuffers[1] = ByteBuffer.allocate(3);

        // 等待客户端链接
        SocketChannel socketChannel = serverSocketChannel.accept();

        // 循环读取
        int msgLen = 8;
        while (true) {
            int byteRead = 0;
            while (byteRead < msgLen) {
                long r = socketChannel.read(byteBuffers);
                // 累计读取的字节数
                byteRead += r;
                System.out.println("byteRead=" + byteRead);

                Arrays.asList(byteBuffers)
                        .stream()
                        .map(buffer -> "position=" + buffer.position() + ",limit=" + buffer.limit())
                        .forEach(System.out::println);
            }

            // 将所有的buffer进行flip
            Arrays.asList(byteBuffers).forEach(buffer -> buffer.flip());

            int byteWrite = 0;
            while (byteWrite < msgLen) {
                long w = socketChannel.write(byteBuffers);
                byteWrite += w;
            }
            // 将所有的buffer进行clear
            Arrays.asList(byteBuffers).forEach(buffer -> buffer.clear());
            System.out.println("byteRead=" + byteRead + ",byteWrite=" + byteWrite + ",msgLen=" + msgLen);
        }
    }
}
