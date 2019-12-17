package com.road.quzhibathhousemqtt.dao;

import com.road.quzhibathhousemqtt.entity.RateType;
import org.apache.ibatis.annotations.Param;

/**
 * @author wenqi
 * @title: RateDao
 * @projectName quzhi-bathhouse-mqtt
 * @description:
 * @date 2019/4/10上午10:23
 */
public interface RateDao {

    /**
     * 根据小类ID查询费率
     * @param projectId
     * @param typeId
     * @return
     */
    RateType getRateInfo(@Param("projectId") int projectId, @Param("typeId") int typeId);
}
