package com.liuxz.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannel03 {
    public static void main(String[] args) throws IOException {
        FileInputStream fileInputStream = new FileInputStream("copy-source.txt");
        FileChannel fileInputStreamChannel = fileInputStream.getChannel();

        FileOutputStream fileOutputStream = new FileOutputStream("copy-target.txt");
        FileChannel fileOutputStreamChannel = fileOutputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(2048);

        while (true) {
            // clear操作非常重要，如果position没有复位，会造成read=0，从而造成死循环
            byteBuffer.clear();
            int read = fileInputStreamChannel.read(byteBuffer);
            System.out.println("read = " + read);
            if (read == -1) {
                // 读完了
                break;
            }
            // 将buffer中的数据写入到fileOutputStreamChannel中（copy-target.txt）
            byteBuffer.flip();
            fileOutputStreamChannel.write(byteBuffer);
        }
        fileOutputStream.close();
        fileInputStream.close();

    }
}
