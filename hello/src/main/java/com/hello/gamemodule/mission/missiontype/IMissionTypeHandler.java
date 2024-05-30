package com.hello.gamemodule.mission.missiontype;

/**
 * 任务类型 处理器
 * @Author：lqh
 * @Date：2024/5/30 15:39
 */
public interface IMissionTypeHandler {

    /**
     * 初始化任务
     */
    void initMission();

    /**
     * 执行-任务  采集/采矿等
     */
    void doMission();

    /**
     * 完成任务
     */
    void completeMission();

    /**
     * 触发下一个任务
     */
    void triggerNextMission();
}
