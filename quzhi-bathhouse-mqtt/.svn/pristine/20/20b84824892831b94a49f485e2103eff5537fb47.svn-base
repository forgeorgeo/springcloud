package com.road.quzhibathhousemqtt.service;

import com.road.quzhibathhousemqtt.bo.WorkDataBO;
import com.road.quzhibathhousemqtt.enums.CMDEnum;

/**
 * @author wenqi
 * @Title: ComReponseService
 * @ProjectName quzhi-bathhouse-mqtt
 * @Description:
 * @date 2019/2/25上午10:49
 */
public interface ComReponseService {

    /**
     *  检验Head 是否正确(80 成功/ 81 失败)
     * @param workData
     * @param dataLen
     * @return
     */
    boolean processHeadStatus(WorkDataBO workData, int dataLen);


    /**
     * 收到消息更新消息状态
     * @param workData
     * @param data
     * @param terminal
     */
    void processEndCommon(WorkDataBO workData, String data, String terminal);


    /**
     * 设置 下发处理
     * @param workData
     */
   void processSetCommon(WorkDataBO workData);

    /**
     * 获取命令字日志字符串
     * @param workData
     * @return
     */
    String getCmdString(WorkDataBO workData);


    /**
     * 错误接收处理
     * @param workData
     * @param message
     * @param errCmdEnum
     */
    void responseError(WorkDataBO workData,String message,
                CMDEnum errCmdEnum);

    /**
     * 数据成功处理
     * @param workData
     * @param message
     */
    void responseOK(WorkDataBO workData, String message);

    /**
     * 订单处理成功
     * @param workData
     * @param timeIds
     */
    void orderResponseOK(WorkDataBO workData,String timeIds);

    /**
     * 订单处理失败
     * @param workData
     * @param timeIds
     * @param errCmdEnum
     */
    void orderResponseError(WorkDataBO workData,String timeIds,
                            CMDEnum errCmdEnum);


    /**
     * 使用码使用失败返还错误
     * @param workData
     * @param deviceId
     * @param waterId
     * @param errCmdEnum
     */
    void useResponseError(WorkDataBO workData,int deviceId,int waterId,
                          CMDEnum errCmdEnum);


}
