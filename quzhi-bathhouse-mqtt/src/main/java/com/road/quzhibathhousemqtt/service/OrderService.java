package com.road.quzhibathhousemqtt.service;

import com.road.quzhibathhousemqtt.bo.UseCodeBO;
import com.road.quzhibathhousemqtt.bo.WorkDataBO;
import com.road.quzhibathhousemqtt.dto.IssuedReservationCodeDTO;
import com.road.quzhibathhousemqtt.dto.OrderStartDTO;
import com.road.quzhibathhousemqtt.dto.RemoteCloseOrderDTO;
import com.road.quzhibathhousemqtt.dto.UploadOrderDTO;
import com.road.quzhibathhousemqtt.entity.Account;
import com.road.quzhibathhousemqtt.util.HttpResult;

/**
 * @author wenqi
 * @Title: OrderService
 * @ProjectName quzhi-bathhouse-mqtt
 * @Description:
 * @date 2019/2/22下午4:55
 */
public interface OrderService {

    /**
     * 远程关闭订单
     *
     * @param closeOrderDTO
     * @return
     */
    HttpResult remoteCloseOrder(RemoteCloseOrderDTO closeOrderDTO);



    /**
     * 订单开始
     * @param workData
     * @param message
     * @param useCodeBO
     * @param account
     */
    void orderStart(WorkDataBO workData, String message, UseCodeBO useCodeBO, Account account);




    /**
     * 上传订单数据
     * @param workData
     * @param message
     */
    void uploadOrderData(WorkDataBO workData, String message);


    /**
     * 上传异常订单数据
     * @param workData
     * @param message
     */
    void uploadUnusualOrderData(WorkDataBO workData, String message);



    /**
     * 取消预约码硬件回复
     * @param workData
     * @param message
     */
    void cancelReponse(WorkDataBO workData, String message);


}
