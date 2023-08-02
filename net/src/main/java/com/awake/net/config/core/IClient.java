package com.awake.net.config.core;

import com.awake.net.config.session.Session;

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
