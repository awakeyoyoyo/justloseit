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
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author：lqh
 * @Date：2024/5/28 19:45
 */
@Service
public class MissionService {
    @Resource
    private MissionManager missionManager;
    @EntityCacheAutowired
    private EntityCache<Long, MissionEntity> missionEntityCache;
    @StorageAutowired
    public IStorage<Integer, MissionResource> missionResources;

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
     *
     * @param roleId
     * @param groupId
     */
    public void initMissionGroup(long roleId, int groupId) {
        MissionGroupEnum missionGroupEnum = MissionGroupEnum.getMissionGroupEnum(groupId);
        if (missionGroupEnum == null) {
            throw new RuntimeException("mission initMissionGroup error. roleId:" + roleId + "groupId:" + groupId);
        }
        MissionEntity missionEntity = missionEntityCache.load(roleId);
        missionGroupEnum.initMissionGroup(roleId, groupId, missionEntity);
        missionEntityCache.update(missionEntity);
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
        MissionTypeEnum missionGroupEnum = MissionTypeEnum.getMissionGroupEnum(missionType);
        if (missionGroupEnum == null) {
            throw new RuntimeException("mission init Mission error. roleId:" + roleId + "missionConfigId:" + missionResource.getConfId());
        }
        return missionGroupEnum.initMission(roleId,missionResource);
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
        List<Mission> missions = missionEntity.getConditionType2Mission().get(progressConditionType);
        for (Mission mission : missions) {
            int confId = mission.getConfId();
            MissionResource missionResource = missionResources.get(confId);
            int missionType = missionResource.getMissionType();
            MissionTypeEnum missionGroupEnum = MissionTypeEnum.getMissionGroupEnum(missionType);
            if (missionGroupEnum == null) {
                throw new RuntimeException("mission update Mission error. roleId:" + roleId + "missionConfigId:" + missionResource.getConfId());
            }
            missionGroupEnum.updateMission(roleId, mission, missionResource, progressConditionTypeEnum.valueOfPrams(params));
        }
        missionEntityCache.update(missionEntity);
    }

}
