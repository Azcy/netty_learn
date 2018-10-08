package com.zcy.nettylearn.EchoClient;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * 实现客户端逻辑
 * @author zcy
 */
@ChannelHandler.Sharable   //1 标志这个类的实例可以在channel里共享
public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {


    /**
     * 服务器的连接被建立后调用
     * @param ctx
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx){
        ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!", //2 当被通知该channel三活动的时候就发送信息
                CharsetUtil.UTF_8));
    }

    /**
     * 数据后从服务器接收到调用
     * @param ctx
     * @param in
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        System.out.println("Client received: "+in.toString(CharsetUtil.UTF_8)); //3 记录收到的消息
    }

    /**
     * 捕获一个异常时调用
     * @param ctx
     * @param cause
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause){ //4记录日志错误并关闭channel
        cause.printStackTrace();
        ctx.close();
    }

}
