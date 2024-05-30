package com.hello.gamemodule.mission;

import com.awake.storage.anno.StorageAutowired;
import com.awake.storage.model.IStorage;
import com.hello.resource.MissionResource;
import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 配置管理 or 模块公共数据
 * @Author：lqh
 * @Date：2024/5/28 20:26
 */
@Component
public class MissionManager {

    @StorageAutowired
    public IStorage<Integer, MissionResource> missionResources;

    /**
     * 任务组id-任务id集合
     */
    private Map<Integer, List<Integer>> groupId2MissionIds = new HashMap<>();

    /**
     * 功能id-任务组id
     */
    private Map<Integer, Integer> functionId2GroupId = new HashMap<>();

    public List<Integer> getMissionGroupIds() {
        return Lists.newArrayList(groupId2MissionIds.keySet().iterator());
    }

    public List<Integer> getMissionsByGroupId(int groupId) {
        return groupId2MissionIds.get(groupId);
    }

    public MissionResource getMissionResource(Integer missionId) {
        return missionResources.get(missionId);
    }
}
