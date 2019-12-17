package com.road.quzhibathhousemqtt.service;

import com.road.quzhibathhousemqtt.bo.WorkDataBO;
import com.road.quzhibathhousemqtt.dto.*;
import com.road.quzhibathhousemqtt.util.HttpResult;

/**
 * @author wenqi
 * @Title: DeviceService
 * @ProjectName quzhi-bathhouse-mqtt
 * @Description:
 * @date 2019/2/22上午10:28
 */
public interface DeviceService {

    /**
     * @param password
     * @param addr
     * @param proID
     * @param cmdId
     * @return
     */
    HttpResult setPassword(String password, String addr,
                           String proID, String cmdId);


    /**
     * 查询水表状态
     *
     * @param key
     * @param addr
     * @param waterId
     * @param cmdId
     * @return
     */
    HttpResult queryWaterStatus(String key, String addr,String ProID,
                                String waterId, String cmdId);



    /**
     * 发送水表费率
     * @param rateDTO
     * @return
     */
    HttpResult sendWaterRate(WaterRateDTO rateDTO);

    /**
     * 设置费率 洗衣机 or 吹风机
     * @param rateDTO
     * @return
     */
    HttpResult sendRateWashAndBlower(WashBlowerRateDTO rateDTO);


    /**
     * 设置水表机号
     *
     * @param Key
     * @param Addr
     * @param ProID
     * @param SetMode
     * @param cmdId
     * @return
     */
    HttpResult setWaterID(String Key, String Addr,
                          String ProID, String SetMode,
                          String cmdId);


    /**
     * 设置水表列表
     * @param waterListDTO
     * @return
     */
    HttpResult setWaterList(SetWaterListDTO waterListDTO);


    /**
     * 设备上传异常
     * @param workData
     * @param message
     */
    void deviceFail(WorkDataBO workData,String message);


    /**
     * 删除设备回调
     * @param workDataBO
     * @param message
     */
    void deleteDevice(WorkDataBO workDataBO,String message);


    /**
     * 设备登录
     * @param workData
     * @param message
     */
    void deviceLogin(WorkDataBO workData,String message);


    /**
     * 设备心跳
     * @param workData
     * @param message
     */
    void deviceHeartbeat(WorkDataBO workData,String message);


    /**
     * 初始化集中器
     * @param key
     * @param addr
     * @param projectId
     * @param cmdId
     * @return
     */
    HttpResult initDevice(String key, String addr,
                          String projectId, String cmdId);


    /**
     * 设置设备信息
     * @param dataBO
     * @param message
     * @return
     */
    void setDeviceInfo(WorkDataBO dataBO,String message);


    /**
     * 删除设备
     * @param deviceDTO
     * @return
     */
    HttpResult deleteDevice(DeleteDeviceDTO deviceDTO);


    /**
     * 获取设备信息
     * @param dataBO
     * @param message
     */
    void getDeviceInfo(WorkDataBO dataBO,String message);


    /**
     * 更新采集器
     * @param key
     * @param projectId
     * @param addr
     * @param ip
     * @param port
     * @param username
     * @param password
     * @param fileName
     * @param cmdId
     * @return
     */
    HttpResult updateCollector( String key,String projectId,  String addr,
                                String ip,int port,
                               String username,String password,
                                int fileName, String cmdId);

}
