package com.awake.protocol;

import com.awake.NetContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @version : 1.0
 * @ClassName: ProtocolTest
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/8/4 16:13
 **/


@SpringBootTest(classes = {ProtocolConfiguration.class})
public class ProtocolTest {

    @Autowired
    private NetContext netContext;
    @Test
    public void testProtocol() {
        System.out.println(netContext.toString());
    }

}
