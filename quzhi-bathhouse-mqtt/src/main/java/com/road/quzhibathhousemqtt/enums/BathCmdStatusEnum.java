package com.road.quzhibathhousemqtt.enums;

/**
 * @author wenqi
 * @Title: BathCmdStatusEnum
 * @ProjectName quzhi-bathhouse-mqtt
 * @Description: 澡堂设备命令状态枚举
 * @date 2019/2/25下午4:12
 */
public enum BathCmdStatusEnum {

    UNKNOWN_STATUS(0,"未知状态"),

    BUILD_FAILED(1,"生成失败"),

    HAS_BEEN_SENT(2,"已发送"),

    OPERATION_COMPLETED(5,"操作完成"),

    TIME_OUT(8,"超时")
    ;

    private Integer code;

    private String msg;

    BathCmdStatusEnum(Integer code,String msg){
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode(){
        return code;
    }

    public String getMsg(){
        return msg;
    }

    public static BathCmdStatusEnum getEnum(int code) {
        for (BathCmdStatusEnum ce : BathCmdStatusEnum.values()) {
            if (ce.getCode() == code) {
                return ce;
            }
        }
        return BathCmdStatusEnum.UNKNOWN_STATUS;
    }
}
