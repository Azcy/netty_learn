package com.zcy.nettylearn.EchoServer;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * 引导服务器
 * 1,监听和接收进来的连接轻轻
 * 2,配置Channel来通知一个关于入站消息的EchoServerHandler实例
 */
public class EchoServer {
    private final int port;


    public EchoServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
        if (args.length!=1){
            System.err.println("Usage:"+EchoServer.class.getSimpleName()+"<port>");
            return;
        }
        int port=Integer.parseInt(args[0]);  //1 设置端口值（抛出一个 NumberFormatException 如果端口参数格式不正确）
        new EchoServer(port).start();        //2 呼叫服务器的start()方法
    }

    private void start() throws InterruptedException {
        NioEventLoopGroup group=new NioEventLoopGroup(); //3 创建 EventLoopGroup
        try {
            ServerBootstrap b=new ServerBootstrap();//4 创建ServerBootstrap
            b.group(group)
                    .channel(NioServerSocketChannel.class)  //5 制定使用NIO的传输Channel
                    .localAddress(new InetSocketAddress(port))   //6 设置socket地址使用所选的端口
                    .childHandler(new ChannelInitializer<SocketChannel>() { //7 添加EchoServerHandler到Channel的ChannelPipeline
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new EchoServerHandler());
                        }
                    });

            ChannelFuture f=b.bind().sync();   //8 绑定的服务器;sync等待服务器关闭
            System.out.println(EchoServer.class.getName() +" started and " +
                    "listen on "+f.channel().localAddress());
            f.channel().closeFuture().sync();  //9 关闭channel和块，直到它被关闭

        } finally {
            group.shutdownGracefully().sync(); //10 关机的EventLoopGroup，释放所有资源。

        }

    }
}
