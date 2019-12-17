package com.road.quzhibathhousemqtt.dao;

import com.road.quzhibathhousemqtt.bo.*;
import com.road.quzhibathhousemqtt.dto.BalanceDTO;
import com.road.quzhibathhousemqtt.dto.OrderStartDTO;
import com.road.quzhibathhousemqtt.dto.UploadOrderDTO;
import com.road.quzhibathhousemqtt.entity.Account;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wenqi
 * @Title: OrderDao
 * @ProjectName quzhi-bathhouse-mqtt
 * @Description:
 * @date 2019/2/26上午10:59
 */
public interface OrderDao {

    /**
     * 保存订单数据 （用户）
     *
     * @param orderStartDTO
     * @return
     */
    int saveOrderStartData(OrderStartDTO orderStartDTO);

    /**
     * 保存订单数据 （管理员）
     *
     * @param orderStartDTO
     * @return
     */
    int saveOrderStartDataOperator(OrderStartDTO orderStartDTO);

    /**
     * 修改预约状态
     *
     * @param orderStartDTO
     * @return
     */
    int updateBathBookingStatus(OrderStartDTO orderStartDTO);

    /**
     * 预约状态改为使用中
     * @param orderStartDTO
     * @return
     */
    int updateBathBookingStatusStart(OrderStartDTO orderStartDTO);

    /**
     * 预约状态改为结束
     * @param orderStartDTO
     * @return
     */
    int updateBathBookingStatusEnd(OrderStartDTO orderStartDTO);

    /**
     * 修改水表状态
     *
     * @param orderStartDTO
     * @return
     */
    int updateBathWaterStatus(OrderStartDTO orderStartDTO);

    /**
     * 查询普通用户金额
     * @param projectId
     * @param accountId
     * @return
     */
    BalanceDTO getAccountBalance(@Param("projectId") long projectId,
                                 @Param("accountId") int accountId);

    /**
     * 查询管理员用户余额
     * @param accountId
     * @return
     */
    BalanceDTO getAccountBalanceOperator(@Param("accountId") int accountId);

    /**
     * 修改用户金额
     * @param orderDTO
     * @return
     */
    int updateAccountMoney(UploadOrderDTO orderDTO);

    /**
     * 修改管理员金额
     * @param orderDTO
     * @return
     */
    int updateOperatorMoney(UploadOrderDTO orderDTO);

    /**
     * 上传交易数据
     * @param orderDTO
     * @return
     */
    int updateConsumeData(UploadOrderDTO orderDTO);

    /**
     * 根据预约码查询订单
     * @param projectId
     * @param reservationCode
     * @return
     */
    HashMap<String,Object> getOrderByReservationCode(@Param("projectId") long projectId,
                                                     @Param("reservationCode") int reservationCode);

    /**
     * 取消预约码
     * @param orderStartDTO
     * @return
     */
    int cancelCode(OrderStartDTO orderStartDTO);


    /**
     * 订单开始，创建订单
     * @param orderBO
     * @return
     */
    int createOrder(CreateOrderBO orderBO);

    /**
     * 获取订单信息
     * @param projectId
     * @param accountId
     * @param orderId
     * @return
     */
    CreateOrderBO getOrderInfo(@Param("projectId") int projectId,
                               @Param("accountId") int accountId,
                               @Param("orderId") String orderId);

    /**
     * 根据设备ID 获取最后一条订单
     * @param projectId
     * @param deviceId
     * @return
     */
    CreateOrderBO getOrderInfoByDeviceId(@Param("projectId") int projectId,
                                         @Param("deviceId") int deviceId);

    /**
     * 上传订单数据
     */
    int uploadOrder(UploadOrderBO orderBO);

    /**
     * 上传异常订单的数据
     * @param orderBO
     * @return
     */
    int uploadUnusualOrder(UploadUnusualOrderBO orderBO);
}
