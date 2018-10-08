package com.zcy.nettylearn.EchoServer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * 实现服务器的逻辑
 * channelRead()-每个信息入站都会被调用
 * channelReadComplete()-通知处理器最后的channelread()是当前批处理中的最后一条消息时调用
 * exceptionCaught()-读操作时捕获到异常时调用
 */

@ChannelHandler.Sharable //1 标识这类的实例之间可以在channel里面共享
public class EchoServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx,
                            Object msg) {
        ByteBuf in = (ByteBuf) msg;
        System.out.println("Server received: " + in.toString(CharsetUtil.UTF_8));        //2 日志消息输出到控制台
        ctx.write(in);                            //3 将所接收的消息返回给发送者。注意，这里还没沖刷数据
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception{
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);//4 冲刷所有待审消息到远程节点。关闭通道后，操作晚餐
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause){
        cause.printStackTrace();//5 打印异常堆栈跟踪
        ctx.close();//6 关闭通道
    }



}