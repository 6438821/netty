package com.netty;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NettychatApplicationTests {

    @Test
    public void contextLoads() {

        Boolean bool = "22" instanceof String;
        System.out.println(bool);
    }

}
