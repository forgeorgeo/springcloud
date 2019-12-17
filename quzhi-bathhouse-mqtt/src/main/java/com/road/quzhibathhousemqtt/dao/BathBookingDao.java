package com.road.quzhibathhousemqtt.dao;

import com.road.quzhibathhousemqtt.entity.BathBooking;
import org.apache.ibatis.annotations.Param;

/**
 * @author wenqi
 * @title: BathBookingDao
 * @projectName quzhi-bathhouse-mqtt
 * @description:
 * @date 2019/4/11下午2:13
 */
public interface BathBookingDao {

    /**
     * 获取澡堂下面个人有效的预约
     * @param projectId
     * @param bathroomId
     * @param accountId
     * @return
     */
    BathBooking getBookingByStop(@Param("projectId") int projectId,
                                  @Param("bathroomId") long bathroomId,
                                  @Param("accountId") int accountId );

    /**
     * 把预约状态改为预约中 并调整预约预约有效时长
     * @param projectId
     * @param bathroomId
     * @param accountId
     * @param orderId
     * @return
     */
    int updateStopTimeAndStatus(@Param("projectId") int projectId,
                               @Param("bathroomId") long bathroomId,
                               @Param("accountId") int accountId,
                               @Param("orderId") String orderId);

    /**
     * 订单开始 修改订单开始状态并且 存入订单号
     * @param projectId
     * @param bathroomId
     * @param accountId
     * @param consumeTime
     * @param orderId
     * @return
     */
    int updateOrderStart(@Param("projectId") int projectId,
                         @Param("bathroomId") long bathroomId,
                         @Param("accountId") int accountId,
                         @Param("consumeTime") String consumeTime,
                         @Param("orderId") String orderId);


    /**
     * 修改预约状态为已结束
     * @param projectId
     * @param orderId
     * @param accountId
     * @return
     */
    int updateEndStatus(@Param("projectId") int projectId,
                           @Param("orderId") String orderId,
                           @Param("accountId") int accountId);

}
