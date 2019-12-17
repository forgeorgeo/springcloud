package com.road.quzhibathhousemqtt.dao;

import com.road.quzhibathhousemqtt.entity.BathWater;
import com.road.quzhibathhousemqtt.entity.DeviceInfo;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

/**
 * @author wenqi
 * @Title: DeviceDao
 * @ProjectName quzhi-bathhouse-mqtt
 * @Description: 设备dao
 * @date 2019/2/25下午2:35
 */
public interface DeviceDao {

    /**
     * 更新设备状态
     * @param status
     * @param addr
     * @return
     */
    int updateDeviceStatus(@Param("status") int status, @Param("addr") String addr);

    /**
     * 获取设备通讯密码
     * @param projectId
     * @return
     */
    String getCommPassword(@Param("projectId") int projectId);


    /**
     * 上传设备异常数据
     * @param projectId
     * @param deviceId
     * @param waterState
     * @return
     */
    int updateDeviceStatu(@Param("projectId") long projectId,@Param("deviceId") int deviceId,
                         @Param("waterState") int waterState);

    /**
     * 根据sn码查询设备状态
     * @param addr
     * @return
     */
    HashMap<String,Object> queryDevStatus(@Param("addr") String addr);

    /**
     * 修改集中器下面所有水表状态
     * @param projectId
     * @param waterStatus
     * @param addr
     * @return
     */
    int updateConcentratorWaterStatus(@Param("projectId") String projectId,@Param("waterStatus") int waterStatus,@Param("addr") String addr);

    /**
     * 根据状态获取水表列表
     * @param status
     * @return
     */
    List<BathWater> getListByStatus(@Param("projectId") long projectId, @Param("status") int status);


    /**
     * 根据澡堂和机号获取设备信息
     * @param projectId
     * @param bathroomId
     * @param waterNo
     * @return
     */
    DeviceInfo getDeviceInfo(@Param("projectId") int projectId,
                             @Param("bathroomId") int bathroomId,
                             @Param("waterNo") int waterNo);



    /**
     * 根据设备ID获取设备信息
     * @param projectId
     * @param deviceId
     * @return
     */
    DeviceInfo getDeviceInfoById(@Param("projectId") int projectId,@Param("deviceId") int deviceId);


    /**
     * 登记设备信息
     * @param deviceInfo
     * @return
     */
    int createDeviceInfo(DeviceInfo deviceInfo);

    /**
     * 强制修改设备信息
     * @param deviceInfo
     * @return
     */
    int updateDeviceInfo(DeviceInfo deviceInfo);

    /**
     * 设备信息插入总表
     * @param macOne
     * @param macTwo
     * @param projectId
     * @param deviceId
     * @param typeStatus
     * @return
     */
    int createDeviceAll(@Param("macOne") long macOne,
                        @Param("macTwo") long macTwo,
                        @Param("projectId") long projectId,
                        @Param("deviceId") long deviceId,
                        @Param("typeStatus") int typeStatus);

    /**
     * 修改设备总表设备的项目ID和 deviceId
     * @param macOne
     * @param macTwo
     * @param projectId
     * @param deviceId
     * @return
     */
    int updateDeviceAll(@Param("macOne") long macOne,
                        @Param("macTwo") long macTwo,
                        @Param("projectId") long projectId,
                        @Param("deviceId") long deviceId);

    /**
     * 删除设备 -- 把设备的waterNo赋值空
     * @param projectId
     * @param deviceId
     * @return
     */
    int deleteDevice( @Param("projectId") long projectId,
                        @Param("deviceId") long deviceId);





}
