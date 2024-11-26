package com.hello.gamemodule.mission.entity;

import com.awake.orm.anno.EntityCache;
import com.awake.orm.anno.Id;
import com.awake.orm.model.IEntity;
import com.hello.gamemodule.mission.struct.Mission;
import lombok.Data;

import java.util.*;

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

    @Override
    public Long id() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id =id;
    }


    public List<Mission> getAllMissions(){
        List<Mission> missions=new ArrayList<>();
        for (Map<Integer, Mission> map : groupId2MissionGroup.values()) {
            missions.addAll(map.values());
        }
        return missions;
    }
    /**
     * 添加任务
     * @param groupId
     * @param mission
     */
    public void addMission(int groupId, Mission mission) {
        groupId2MissionGroup.computeIfAbsent(groupId, k -> new HashMap<>()).put(mission.getConfId(), mission);
    }

    /**
     * 删除任务
     * @param groupId
     * @param mission
     */
    public void removeMission(int groupId, Mission mission) {
        groupId2MissionGroup.computeIfAbsent(groupId, k -> new HashMap<>()).remove(mission.getConfId());
    }

    /**
     * 获取任务组任务列表
     * @param groupId
     * @return
     */
    public List<Mission> getGroupMissionList(int groupId) {
        List<Mission> ret=new ArrayList<>();
        Map<Integer, Mission> groupMission = groupId2MissionGroup.get(groupId);
        if (groupMission==null||groupMission.isEmpty()){
            return ret;
        }
        ret.addAll(groupMission.values());
        return ret;
    }
}
