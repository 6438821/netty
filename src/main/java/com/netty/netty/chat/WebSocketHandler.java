package com.netty.netty.chat;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
import org.springframework.stereotype.Component;

@Component
@ChannelHandler.Sharable
public class WebSocketHandler extends ChannelInboundHandlerAdapter {

    WebSocketServerHandshaker handshaker;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest) {
            FullHttpRequest request = (FullHttpRequest) msg;
            if (!request.decoderResult().isSuccess() || !"websocket".equals(request.headers().get("Upgrade"))) {
                DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST);
                if (response.status().code() != 200) {
                    ByteBuf byteBuf = Unpooled.copiedBuffer("请求异常", CharsetUtil.UTF_8);
                    response.content().writeBytes(byteBuf);
                    byteBuf.release();
                }
                ctx.writeAndFlush(response);
                return;
            }

            WebSocketServerHandshakerFactory webSocketServerHandshakerFactory = new WebSocketServerHandshakerFactory("ws://localhost:8888/websocket", null, false);
            handshaker = webSocketServerHandshakerFactory.newHandshaker(request);
            if (handshaker == null) {
                WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
            } else {
                handshaker.handshake(ctx.channel(), request);
            }
        } else if (msg instanceof WebSocketFrame) {
            if (msg instanceof CloseWebSocketFrame) {
                handshaker.close(ctx.channel(), (CloseWebSocketFrame) msg);
            }
            if (msg instanceof TextWebSocketFrame) {
                String content = ((TextWebSocketFrame) msg).text();
                ctx.writeAndFlush(new TextWebSocketFrame("serverSocket:" + msg));
            }
        }
    }
}
