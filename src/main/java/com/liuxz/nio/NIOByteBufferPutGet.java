package com.liuxz.nio;

        import java.nio.ByteBuffer;

public class NIOByteBufferPutGet {
    public static void main(String[] args) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(64);
        byteBuffer.putInt(1205);
        byteBuffer.putLong(44522119);
        byteBuffer.putChar('学');
        byteBuffer.putShort((short) 66);

        byteBuffer.flip();
        // System.out.println("正确示例");
        // System.out.println(byteBuffer.getInt());
        // System.out.println(byteBuffer.getLong());
        // System.out.println(byteBuffer.getChar());
        // System.out.println(byteBuffer.getShort());

        System.out.println("错误示例");

        System.out.println(byteBuffer.getInt());
        System.out.println(byteBuffer.getLong());
        System.out.println(byteBuffer.getChar());
        System.out.println(byteBuffer.getLong());
    }
}
