package com.liuxz.nio.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

public class GroupChatServer {
    private static final int PORT = 6667;
    private Selector selector;
    private ServerSocketChannel listenChannel;

    public GroupChatServer() {
        try {
            // 得到选择器
            selector = Selector.open();
            // 得到serverSocketChannel
            listenChannel = ServerSocketChannel.open();
            // 绑定端口
            listenChannel.socket().bind(new InetSocketAddress(PORT));
            // 设置非阻塞模式
            listenChannel.configureBlocking(false);
            // 将该listenChannel注册到selector
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 监听
    public void listen() {
        try {
            while (true) {
                int count = selector.select();
                if (count > 0) {
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        if (key.isAcceptable()) {
                            SocketChannel sc = listenChannel.accept();
                            sc.configureBlocking(false);
                            // 将该sc注册到selector
                            sc.register(selector, SelectionKey.OP_READ);
                            System.out.println(sc.getRemoteAddress() + " 上线了");
                        }

                        // 通道发送read时间，即是通道是可读状态
                        if (key.isReadable()) {
                            // 处理读
                            readData(key);
                        }
                        // 当前的key删除，防止重复处理
                    }
                    // 手动从集合中移除当前的selectionKey，防止重复操作
                    iterator.remove();
                } else {
                    System.out.println("等待......");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    /**
     * 读取客户端消息
     */
    private void readData(SelectionKey key) {
        // d定义一个socketchannel
        SocketChannel channel = null;
        try {
            // 从selectionKey中获取关联的channel
            channel = (SocketChannel) key.channel();
            // 创建buffer
            ByteBuffer buffer = ByteBuffer.allocate(1024);

            int count = channel.read(buffer);
            // 根据count值处理了
            if (count > 0) {
                // 把缓冲区的数据转换成字符串
                String msg = new String(buffer.array());
                // 输出该消息
                System.out.println("from 客户端： " + msg);
                // 向其他客户端转发消息
                sendInfoToOtherClient(msg, channel);
            }
        } catch (IOException e) {
            try {
                System.out.println(channel.getRemoteAddress() + " 离线了....");
                // 取消注册
                key.cancel();
                // 关闭通道
                channel.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    /**
     * 转发消息给其他客户端（通道）
     */
    private void sendInfoToOtherClient(String msg, SocketChannel self) throws IOException {
        System.out.println("服务器转发消息......");
        // 遍历所有注册到selector上饿socketchannel，并排除自己self
        for (SelectionKey key : selector.keys()) {
            // 通过key取出对应的socketchannel
            SelectableChannel targetChannel = key.channel();
            // 排除自己
            if (targetChannel instanceof SocketChannel && targetChannel != self) {
                // 转型
                SocketChannel dest = (SocketChannel) targetChannel;
                // 将msg存储到buffer
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                // 将buffer数据写入到通道中
                dest.write(buffer);
            }
        }
    }

    public static void main(String[] args) {
        GroupChatServer server = new GroupChatServer();
        server.listen();
    }
}
