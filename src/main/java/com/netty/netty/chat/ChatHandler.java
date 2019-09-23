package com.netty.netty.chat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.springframework.stereotype.Component;

@Component
@ChannelHandler.Sharable  //在多线程环境下
public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private static final ChannelGroup CHANNEL_GROUP = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    /**
     * 广播方法
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        String content = msg.text();
        Channel channel = ctx.channel();
        for (Channel channels : CHANNEL_GROUP) {
            if (channels != channel) {
                channel.writeAndFlush(new TextWebSocketFrame(ctx.channel().remoteAddress() + content));
            } else {
                channel.writeAndFlush(new TextWebSocketFrame("我自己："+ content));
            }
        }
    }

    /**
     * 进入方法
     *
     * @param ctx
     * @throws Exception
     */

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        for (Channel channel : CHANNEL_GROUP) {
            channel.writeAndFlush(new TextWebSocketFrame(ctx.channel().remoteAddress() + "" + "进入聊天室"));
        }
        CHANNEL_GROUP.add(ctx.channel());
    }

    /**
     * 退出方法
     *
     * @param ctx
     * @throws Exception
     */

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        for (Channel channel : CHANNEL_GROUP) {
            channel.writeAndFlush(new TextWebSocketFrame(ctx.channel().remoteAddress() + "退出聊天室"));
        }
        CHANNEL_GROUP.remove(ctx.channel());
    }
}
