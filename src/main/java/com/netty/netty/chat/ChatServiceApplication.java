package com.netty.netty.chat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

@Component
public class ChatServiceApplication {

    @Autowired
    @Qualifier(value = "bootstrap")
    private ServerBootstrap serverBootstrap;

    private Channel channel;

    public void start()throws Exception{
        System.out.println("netty启动");
        channel = serverBootstrap.bind(8888).sync().channel().closeFuture().sync().channel();
    }

    @PreDestroy  //执行完成销毁
    public void close(){
        channel.close();
        channel.parent().close();
    }
}
