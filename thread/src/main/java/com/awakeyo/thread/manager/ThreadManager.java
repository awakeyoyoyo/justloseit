package com.awakeyo.thread.manager;


import com.awakeyo.thread.pool.AbstractThreadPool;

import java.util.Map;

/**
 * @version : 1.0
 * @ClassName: ThreadManager
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/4/24 10:41
 **/
public class ThreadManager implements IThreadManager {

    private Map<ThreadPoolNameEnum, AbstractThreadPool> poolNameEnum2ThreadPoolMap;
}
