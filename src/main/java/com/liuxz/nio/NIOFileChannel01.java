package com.liuxz.nio;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannel01 {

    /**
     * 将一个字符串通过缓存区和通道写入到本地文件中
     * 字符串--》缓冲区--》通道--》本地文件
     *
     * @param args
     */
    public static void main(String[] args) throws IOException {
        String str = "hello,liuxz";

        // 创建一个输出流--》channel
        FileOutputStream fileOutputStream = new FileOutputStream("C:\\temp\\file01.txt");
        // 通过fileOutputStream 获取对应的FileChannel
        // FileChannel真实类型是FileChannelImpl
        FileChannel fileChannel = fileOutputStream.getChannel();
        // 创建一个缓冲区 ByteBuffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        // 将str放入byteBuffer中
        byteBuffer.put(str.getBytes());

        // 将byteBuffer 进行flip
        byteBuffer.flip();

        // 将byteBuffer 数据写入到fileChannel
        fileChannel.write(byteBuffer);
        fileOutputStream.close();
    }
}
