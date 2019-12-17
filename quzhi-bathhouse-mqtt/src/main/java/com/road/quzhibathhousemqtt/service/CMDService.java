package com.road.quzhibathhousemqtt.service;

import com.road.quzhibathhousemqtt.bo.WorkDataBO;

/**
 * @author wenqi
 * @Title: CMDService
 * @ProjectName quzhi-bathhouse-mqtt
 * @Description:
 * @date 2019/2/25上午11:58
 */
public interface CMDService {

    /**
     * 更新设备处理命令返回结果 更新下发命令状态
     * PrjID int, --项目ID
     * DevAddr bigint, --控制器地址
     * CmdType int, --功能码
     * CmdCount int, --计数器
     * DealType int, --参数下发处理状态
     * Value varchar(1000) --参数处理结果值
     * @param workData
     * @return
     */
    boolean updateDeviceStatus(WorkDataBO workData);

    /**
     * 更新命令结果
     * @param workData
     * PrjID int, --项目ID
     * CmdType int, --功能码
     * DevType int, --设备类型(消费机64 转账机65 空中充值66 空调控制67 无线水表68 热泵控制69 联网水表70)
     * CtrlAddr bigint, --客户端地址
     * DealAddr bigint, --终端地址
     * Value varchar(1000) --处理值
     * @param terminal
     * @param value
     */
    void updateCmdResult(WorkDataBO workData, String terminal, String value);

    /**
     * 获取通讯密码
     * @param projectId
     * @param addr
     * @param deviceTypeId
     * @return
     */
    String  getCommPassword(int projectId, String addr, int deviceTypeId);

    /**
     * 更新命令结果
     * @param projectId
     * @param count
     * @param func
     * @param type
     * @param addr
     * @param terminal
     * @param value
     */
    void updateCmdResult(int projectId, long count,int func, int type, String addr, String terminal, String value);
}
