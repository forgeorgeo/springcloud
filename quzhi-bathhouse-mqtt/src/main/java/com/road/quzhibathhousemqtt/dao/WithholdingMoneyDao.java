package com.road.quzhibathhousemqtt.dao;

import com.road.quzhibathhousemqtt.entity.WithholdingMoney;
import org.apache.ibatis.annotations.Param;

/**
 * @author wenqi
 * @title: WithholdingMoneyDao
 * @projectName quzhi-bathhouse-mqtt
 * @description: 预扣金额
 * @date 2019/4/11下午7:29
 */
public interface WithholdingMoneyDao {

    /**
     * 获取预扣金额
     * @param projectId
     * @param deviceBigTypeId
     * @return
     */
    WithholdingMoney getWithholdingMoney(@Param("projectId") int projectId,
                                         @Param("deviceBigTypeId") int deviceBigTypeId);
}
