package com.road.quzhibathhousemqtt.service;

import com.road.quzhibathhousemqtt.bo.WorkDataBO;
import com.road.quzhibathhousemqtt.util.HttpResult;

/**
 * @author wenqi
 * @Title: ClockService
 * @ProjectName quzhi-bathhouse-mqtt
 * @Description: 时钟服务
 * @date 2019/2/21上午11:18
 */
public interface ClockService {

    HttpResult queryClock(String key,String addr,String proID,Long cmdId);


    HttpResult setClock(String key,String addr,String proID,Long cmdId);

    /**
     * 设置时钟
     * @param workData
     * @param message
     */
    void setClock(WorkDataBO workData ,String message);
}
