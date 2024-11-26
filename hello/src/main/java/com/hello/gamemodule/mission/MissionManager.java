package com.hello.gamemodule.mission;

import com.awake.orm.anno.EntityCacheAutowired;
import com.awake.orm.cache.EntityCache;
import com.awake.storage.anno.StorageAutowired;
import com.awake.storage.model.IStorage;
import com.hello.define.ManagerOrder;
import com.hello.frame.manger.IModuleManager;
import com.hello.gamemodule.mission.entity.MissionEntity;
import com.hello.resource.MissionResource;
import org.apache.commons.compress.utils.Lists;
import org.checkerframework.checker.units.qual.A;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 配置管理 or 模块公共数据
 *
 * @Author：lqh
 * @Date：2024/5/28 20:26
 */
@Component
public class MissionManager implements IModuleManager {

    @StorageAutowired
    public IStorage<Integer, MissionResource> missionResources;

    /**
     * 任务组id-任务id集合
     */
    private Map<Integer, List<Integer>> groupId2MissionIds = new HashMap<>();

    public List<Integer> getMissionsByGroupId(int groupId) {
        return groupId2MissionIds.get(groupId);
    }


    @Override
    public int order() {
        return ManagerOrder.HIGH_LEVEL;
    }

    @Override
    public void init() {
        groupId2MissionIds.clear();
        for (MissionResource missionResource : missionResources.getAll()) {
            groupId2MissionIds.computeIfAbsent(missionResource.getGroupId(), k -> new ArrayList<>())
                    .add(missionResource.getConfId());
        }
    }

    @Override
    public void shutDown() {

    }
}
