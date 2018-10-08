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
        //1 设置端口值（抛出一个 NumberFormatException 如果端口参数格式不正确）
        int port=Integer.parseInt(args[0]);
        //2 呼叫服务器的start()方法
        new EchoServer(port).start();
    }

    private void start() throws InterruptedException {
        //3 创建 EventLoopGroup
        NioEventLoopGroup group=new NioEventLoopGroup();
        try {
            //4 创建ServerBootstrap
            ServerBootstrap b=new ServerBootstrap();
            b.group(group)
                    //5 制定使用NIO的传输Channel
                    .channel(NioServerSocketChannel.class)
                    //6 设置socket地址使用所选的端口
                    .localAddress(new InetSocketAddress(port))
                    //7 添加EchoServerHandler到Channel的ChannelPipeline
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new EchoServerHandler());
                        }
                    });
            //8 绑定的服务器;sync等待服务器关闭
            ChannelFuture f=b.bind().sync();
            System.out.println(EchoServer.class.getName() +" started and " +
                    "listen on "+f.channel().localAddress());
            //9 关闭channel和块，直到它被关闭
            f.channel().closeFuture().sync();

        } finally {
            //10 关机的EventLoopGroup，释放所有资源。
            group.shutdownGracefully().sync();

        }

    }
}
