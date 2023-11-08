package com.awake;

import com.awake.manager.HotSwapManager;
import com.awake.server.HotSwapServiceMBean;

/**
 * @version : 1.0
 * @ClassName: HotSwapContext
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/11/8 10:31
 **/
public class HotSwapContext {
    private static final HotSwapContext HOT_SWAP_CONTEXT = new HotSwapContext();

    private HotSwapContext() {

    }

    public static HotSwapServiceMBean getHotSwapService() {
        return HotSwapServiceMBean.getSingleInstance();
    }

    public static HotSwapManager getHotSwapManager() {
        return HotSwapManager.getInstance();
    }
}
