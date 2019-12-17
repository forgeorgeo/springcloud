package com.road.quzhibathhousemqtt.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author wenqi
 * @Title: UploadOrderDTO
 * @ProjectName quzhi-bathhouse-mqtt
 * @Description:
 * @date 2019/2/27上午9:23
 */
@Data
public class UploadOrderDTO implements Serializable {


    private static final long serialVersionUID = -2590215405521589371L;
    /**
     * 项目ID
     */
    private long projectId;
    /**
     * 水表ID
     */
    private int waterId;


    /**
     * 订单ID
     */
    private String orderId;
    /**
     * 账号
     */
    private int accountId;
    /**
     *  账户类别（1：管理员2普通用户）
     */
    private int accountType;
    /**
     * 消费金额
     */
    private long amount;
    /**
     * 订单开始时间,20181225010101(yyyy-mm-dd-hh-mm-ss)
     */
    private String startTime;
    /**
     * 结束时间(yyyy-mm-dd-hh-mm-ss)
     */
    private String endTime;
    /**
     *  消费脉冲数
     */
    private long pulseNum;
    /**
     * 水流速
     */
    private int flowRate;
    /**
     * 使用时长（分钟）
     */
    private int useTime;
    /**
     * 暂停次数
     */
    private int pauseCnt;
    /**
     * 异常状态
     */
    private int errorState;
    /**
     *
     */
    private long addr;

    /**
     * 充值金额
     */
    private long realAccMoney;

    /**
     * 赠送金额
     */
    private long givenAccMoney;

    /**
     * 扣现金金额
     */
    private long perMoneyReal;

    /**
     * 扣赠送金额
     */
    private long perMoneyGiven;

    /**
     * 剩余合计金额
     */
    private long upLeadMoney;

    /**
     * 剩余充值金额
     */
    private long upLeadMoneyReal;

    /**
     * 剩余赠送金额
     */
    private long upLeadMoneyGiven;
}
