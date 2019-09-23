package com.netty;

import com.netty.netty.chat.ChatServiceApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class NettychatApplication {

    public static void main(String[] args) throws Exception{
       ConfigurableApplicationContext context =  SpringApplication.run(NettychatApplication.class, args);
        ChatServiceApplication bean = context.getBean(ChatServiceApplication.class);
        bean.start();
    }
}
