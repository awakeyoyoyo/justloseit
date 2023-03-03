package com.awake.event;

import com.awakeyo.event.anno.EventReceiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @version : 1.0
 * @ClassName: BagServer
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/3/3 16:49
 **/
@Service
public class BagSystem {
    private static final Logger logger = LoggerFactory.getLogger(BagSystem.class);

    // 事件会被当前线程立刻执行，注意日志打印的线程号
    @EventReceiver
    public void handleLogin(LoginEvent event) {
        logger.info("BagSystem:同步执行登录事件：玩家id:{}, 登录时间:{}" , event.getRoleId(),event.getLoginTime());
    }
}
