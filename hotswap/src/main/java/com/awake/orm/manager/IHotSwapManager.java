package com.awake.orm.manager;

import com.awake.orm.model.ClassFileDef;

import java.util.Map;

/**
 * @version : 1.0
 * @ClassName: IHotSwapManager
 * @Description: TODO
 * @Auther: awake
 * @Date: 2023/11/8 18:02
 **/
public interface IHotSwapManager {
    Map<String, ClassFileDef> getClassFileDefMap();
}
