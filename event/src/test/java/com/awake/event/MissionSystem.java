package com.awake.event;

import com.awakeyo.event.anno.EventReceiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @version : 1.0
 * @ClassName: MissionSystem
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/3/3 16:50
 **/
@Service
public class MissionSystem {

    private static final Logger logger = LoggerFactory.getLogger(MissionSystem.class);

    /**
     * 同一个事件可以被重复注册和接受
     *
     * 异步事件会被不会立刻执行，注意日志打印的线程号
     */
    @EventReceiver
    public void onLoginEvent(LoginEvent event) {
        logger.info("BagSystem:同步执行登录事件：玩家id:{}, 登录时间:{}", event.getRoleId(), event.getLoginTime());
    }
}
