package com.liuxz.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannel02 {

    /**
     * 读取本地文件内容到控制台中
     */
    public static void main(String[] args) throws IOException {
        // 创建文件输入流
        File file = new File("C:\\temp\\file01.txt");
        FileInputStream fileInputStream = new FileInputStream(file);
        // 通过fileInputStream获取对应的FileChannel（实际类型FileChannelImpl）
        FileChannel fileChannel = fileInputStream.getChannel();
        // 创建缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate((int) file.length());
        // 将通道的数据读取到缓冲区中
        fileChannel.read(byteBuffer);
        // 将byteBuffer的字节数据转换成string
        System.out.println(new String(byteBuffer.array()));
        fileInputStream.close();
    }
}
