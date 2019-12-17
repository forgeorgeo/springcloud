package com.road.quzhibathhousemqtt.dao;

import com.road.quzhibathhousemqtt.entity.Account;
import com.road.quzhibathhousemqtt.entity.ProjectInfo;
import org.apache.ibatis.annotations.Param;

/**
 * @author wenqi
 * @title: AccountDao
 * @projectName quzhi-bathhouse-mqtt
 * @description:
 * @date 2019/4/11下午12:07
 */
public interface AccountDao {

    /**
     * 根据澡堂码获取用户信息
     * @param projectId
     * @param bathPassword
     * @return
     */
    Account getAccountByBathCode(@Param("projectId") int projectId,
                                 @Param("bathPassword") String bathPassword);

    /**
     * 根据ID 查询用户信息
     * @param projectId
     * @param accountId
     * @return
     */
    Account getAccountById (@Param("projectId") int projectId,
                            @Param("accountId") int accountId);

    /**
     * 修改账户金额
     * @param projectId
     * @param accountId
     * @param realMoney
     * @param givenMoney
     * @return
     */
    int updateAccountMoney(@Param("projectId") int projectId,@Param("accountId") int accountId,
                           @Param("realMoney") long realMoney,@Param("givenMoney") long givenMoney);

    /**
     * 根据项目Id 获取项目信息
     * @param projectId
     * @return
     */
    ProjectInfo getProjectInfo(@Param("projectId") int projectId);
}
