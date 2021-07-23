package com.liuxz.nio;

import java.nio.ByteBuffer;

public class NIOByteBufferReadOnly {
    public static void main(String[] args) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(64);
        for (int i = 0; i < 64; i++) {
            byteBuffer.put((byte) i);
        }

        byteBuffer.flip();
        // 将byteBuffer转换为readOnlyBuffer（仅支持读取操作）
        ByteBuffer readOnlyBuffer = byteBuffer.asReadOnlyBuffer();
        // readOnlyBuffer class：class java.nio.HeapByteBufferR
        System.out.println("readOnlyBuffer class：" + readOnlyBuffer.getClass());
        while (byteBuffer.hasRemaining()) {
            System.out.println(readOnlyBuffer.get());
        }

        // 对只读byteBuffer写数据，会提示异常：java.nio.BufferUnderflowException
        readOnlyBuffer.put((byte) 1);

    }
}
