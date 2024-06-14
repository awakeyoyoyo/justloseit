package com.hello.gamemodule.mission.entity;

import com.awake.orm.anno.EntityCache;
import com.awake.orm.anno.Id;
import com.awake.orm.model.IEntity;
import com.hello.gamemodule.mission.struct.Mission;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author：lqh
 * @Date：2024/5/28 19:37
 */
@EntityCache
@Data
public class MissionEntity implements IEntity<Long> {
    @Id
    private long id;

    /**
     * 任务组id 对应任务
     */
    private Map<Integer, Map<Integer, Mission>> groupId2MissionGroup=new HashMap<>();

    /**
     * 条件Type 对应任务
     */
    private Map<Integer, List<Mission>> conditionType2Mission = new HashMap<>();

    @Override
    public Long id() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id =id;
    }


    /**
     * 添加任务
     * @param groupId
     * @param conditionType
     * @param mission
     */
    public void addMission(int groupId, int conditionType, Mission mission) {
        groupId2MissionGroup.computeIfAbsent(groupId, k -> new HashMap<>()).put(mission.getConfId(), mission);
        conditionType2Mission.computeIfAbsent(conditionType, k -> new ArrayList<>()).add(mission);
    }

    /**
     * 删除任务
     * @param groupId
     * @param conditionType
     * @param mission
     */
    public void removeMission(int groupId, int conditionType, Mission mission) {
        groupId2MissionGroup.computeIfAbsent(groupId, k -> new HashMap<>()).remove(mission.getConfId());
        conditionType2Mission.computeIfAbsent(conditionType, k -> new ArrayList<>()).remove(mission);
    }
}
