package com.hello.gamemodule.mission;

import com.awake.net2.session.Session;
import com.awake.orm.anno.EntityCacheAutowired;
import com.awake.orm.cache.EntityCache;
import com.awake.storage.anno.StorageAutowired;
import com.awake.storage.model.IStorage;
import com.hello.gamemodule.mission.progresscondition.ProgressConditionTypeEnum;
import com.hello.gamemodule.mission.entity.MissionEntity;
import com.hello.gamemodule.mission.missiongroup.MissionGroupEnum;
import com.hello.gamemodule.mission.missiontype.MissionTypeEnum;
import com.hello.gamemodule.mission.struct.Mission;
import com.hello.resource.MissionResource;
import com.hello.resource.model.Reward;
import org.apache.commons.compress.utils.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author：lqh
 * @Date：2024/5/28 19:45
 */
@Service
public class MissionService {


    private static final Logger logger = LoggerFactory.getLogger(MissionService.class);
    @EntityCacheAutowired
    private EntityCache<Long, MissionEntity> missionEntityCache;
    @StorageAutowired
    public IStorage<Integer, MissionResource> missionResources;

    public EntityCache<Long, MissionEntity> getMissionEntityCache() {
        return missionEntityCache;
    }

    public void acceptMission(Session session, int missionConfId) {

    }

    public void doMission(Session session, int missionConfId) {

    }

    public void completeMission(Session session, int missionConfId) {

    }

    public void giveUpMission(Session session, int missionConfId) {

    }


    /**
     * 初始化任务组
     *
     * @param roleId
     * @param groupId
     */
    public void initMissionGroup(long roleId, int groupId) {
        MissionGroupEnum missionGroupEnum = MissionGroupEnum.getMissionGroupEnum(groupId);
        if (missionGroupEnum == null) {
            logger.error("mission initMissionGroup missionGroupEnum not exit. roleId:{},groupId:{}", roleId, groupId);
            return;
        }
        MissionEntity missionEntity = missionEntityCache.load(roleId);
        List<Mission> missions = missionGroupEnum.initMissionGroup(roleId);
        missionEntityCache.update(missionEntity);
        //TODO 通知新增任务
    }

    /**
     * 完成任务
     */
    public List<Reward> completedMission(long roleId, Mission mission) {
        MissionResource missionResource = missionResources.get(mission.getConfId());
        int missionType = missionResource.getMissionType();
        MissionTypeEnum missionTypeEnum = MissionTypeEnum.getMissionTypeEnum(missionType);
        if (missionTypeEnum == null) {
            logger.error("mission completedMission missionTypeEnum not exit. roleId:{},missionId:{}", roleId, missionResource.getConfId());
            return null;
        }
        if (!missionTypeEnum.canCompleteMission(roleId, mission, missionResource)) {
            return Lists.newArrayList();
        }
        List<Reward> rewards = missionTypeEnum.completeMission(roleId, mission, missionResource);
        MissionEntity missionEntity = missionEntityCache.load(roleId);
        //完成后是否需要保留任务
        if (missionTypeEnum.isCompleteDeleteMission()) {
            missionEntity.removeMission(missionResource.getGroupId(), mission);
            //TODO 通知任务删除
        }
        //是否触发下一个任务
        if (missionTypeEnum.isTriggerNextMission(missionResource)) {
            if (!missionTypeEnum.verityCanAccept(roleId, missionResource)) {
                return null;
            }
            Mission nextMission = initMission(roleId, missionResource.getNextMissionId());
            if (nextMission == null) {
                return rewards;
            }
            missionEntity.addMission(missionResource.getGroupId(), nextMission);
            //TODO 通知新增任务
        }
        return rewards;
    }


    /**
     * 初始化任务
     *
     * @param roleId
     * @param missionConfId
     * @return
     */
    public Mission initMission(long roleId, int missionConfId) {
        MissionResource missionResource = missionResources.get(missionConfId);
        int missionType = missionResource.getMissionType();
        MissionTypeEnum missionTypeEnum = MissionTypeEnum.getMissionTypeEnum(missionType);
        if (missionTypeEnum == null) {
            logger.error("mission initMission missionTypeEnum not exit. roleId:{},missionId:{}", roleId, missionResource.getConfId());
            return null;
        }
        if (!missionTypeEnum.verityCanAccept(roleId, missionResource)) {
            return null;
        }
        return missionTypeEnum.initMission(roleId, missionResource);
    }


    /**
     * 更新指定条件类型的进度
     *
     * @param roleId
     * @param progressConditionTypeEnum
     * @param params
     */
    public void updateMission(long roleId, ProgressConditionTypeEnum progressConditionTypeEnum, Object... params) {
        int progressConditionType = progressConditionTypeEnum.getProgressConditionType();
        MissionEntity missionEntity = missionEntityCache.load(roleId);
        List<Mission> missions = missionEntity.getAllMissions();
        for (Mission mission : missions) {
            int confId = mission.getConfId();
            MissionResource missionResource = missionResources.get(confId);
            if (progressConditionType != missionResource.getProgressConditionType()) {
                continue;
            }
            int missionType = missionResource.getMissionType();
            MissionTypeEnum missionTypeEnum = MissionTypeEnum.getMissionTypeEnum(missionType);
            if (missionTypeEnum == null) {
                logger.error("mission updateMission missionTypeEnum not exit. roleId:{},missionId:{}", roleId, missionResource.getConfId());
                continue;
            }
            missionTypeEnum.updateMission(roleId, mission, missionResource, progressConditionTypeEnum.valueOfPrams(params));
        }
        //TODO 通知任务变化
        missionEntityCache.update(missionEntity);
    }

}
