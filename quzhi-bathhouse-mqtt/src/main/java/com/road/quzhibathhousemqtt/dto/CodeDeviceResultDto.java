package com.road.quzhibathhousemqtt.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author wenqi
 * @Title: CodeDeviceResultDto
 * @ProjectName quzhi-bathhouse-mqtt
 * @Description:
 * @date 2019/2/28下午8:01
 */
@Data
public class CodeDeviceResultDto implements Serializable {
    private static final long serialVersionUID = -8882957855346255724L;
    /**
     * 项目ID
     */
    private long projectId;
    /**
     * 账号ID
     */
    private long accountId;
    /**
     * 类型 1.下发预约码  2.取消预约码
     */
    private int type;
    /**
     * 结果
     */
    private boolean result;

    /**
     * 创建时间 yyyyMMddHHmmss
     */
    private long createTime;
}
