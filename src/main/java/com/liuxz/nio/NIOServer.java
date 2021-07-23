package com.liuxz.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NIOServer {
    public static void main(String[] args) throws Exception {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        Selector selector = Selector.open();

        serverSocketChannel.socket().bind(new InetSocketAddress(6666));
        serverSocketChannel.configureBlocking(false);

        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            if (selector.select(1000) == 0) {
                System.out.println("服务器等待了1秒，无连接");
                continue;
            }

            // 如果返回的 > 0,表示已经获取到了关注的事件了
            // 通过selector.selectedKeys()返回关注事件的集合
            // 通过selectionKeys反向获取通道
            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
            while (keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next();
                // 根据key对应通道发生的相应的处理
                if (key.isAcceptable()) { // 如果是OP_ACCEPT，有新的客户端链接
                    // 该客户端生成一个socketChannel
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    System.out.println("客户端链接成功，生成了一个socketChannel " + socketChannel.hashCode());
                    // 将socketChannel设置为非阻塞
                    socketChannel.configureBlocking(false);
                    // 将当前socketChannel注册到selector，关注事件为OP_READ，同时给socketChannel关联一个buffer
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                }

                if (key.isReadable()) {// 发生OP_READ
                    // 通过key，反向获取对应的channel
                    SocketChannel channel = (SocketChannel) key.channel();
                    // 获取到该channel关联的buffer
                    ByteBuffer buffer = (ByteBuffer) key.attachment();
                    channel.read(buffer);
                    System.out.println("from 客户端 " + new String(buffer.array()));
                }

                // 手动从集合中移除当前的selectionKey，防止重复操作
                keyIterator.remove();
            }
        }
    }
}
