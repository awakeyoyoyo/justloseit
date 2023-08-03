package com.awake.net.server;

import com.awake.net.session.Session;

/**
 * @version : 1.0
 * @ClassName: IClient
 * @Description: 客户端接口
 * @Auther: awake
 * @Date: 2023/8/2 20:12
 **/
public interface IClient {

    Session start();
}
