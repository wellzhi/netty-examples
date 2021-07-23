package com.liuxz.nio;

import java.nio.ByteBuffer;

public class test {

    public static void main(String[] args) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(10);

        while (byteBuffer.hasRemaining()){
            System.out.println(byteBuffer.get());
        }
    }
}
