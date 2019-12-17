package com.road.quzhibathhousemqtt.bo;

import lombok.Data;

/**
 * @author wenqi
 * @title: UploadUnusualOrderBO
 * @projectName quzhi-bathhouse-mqtt
 * @description:
 * @date 2019/5/6下午2:45
 */
@Data
public class UploadUnusualOrderBO {
    private int projectId;


    private String orderId;

    private int accountId;

    private int deviceId;




    private int waterId;


    private long amount;

    private String endTime;


    private long pulseNum;

    private long flowRate;

    private int useTime;


    private int pauseCnt;


    private int errState;

    private long UpMoney;

    private long UpLeadMoney;

    private long UpLeadMoneyReal;

    private long UpLeadMoneyGiven;
}
