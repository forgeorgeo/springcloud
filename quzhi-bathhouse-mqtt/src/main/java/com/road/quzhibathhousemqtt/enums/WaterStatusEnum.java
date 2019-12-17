package com.road.quzhibathhousemqtt.enums;

/**
 * @author wenqi
 * @Title: WaterStatusEnum
 * @ProjectName quzhi-bathhouse-mqtt
 * @Description: 水表状态
 * @date 2019/2/26下午3:25
 */
public enum WaterStatusEnum {


    UNKNOWN_STATUS(-1,"未知状态"),

    ONLINE_STATUS(0,"在线"),

    OFFLINE_STATUS(1,"脱机"),

    FAULT_STATUS(2,"操作完成"),

    USING_STATUS(3,"使用中")
    ;

    private Integer code;

    private String msg;

    WaterStatusEnum(Integer code,String msg){
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode(){
        return code;
    }

    public String getMsg(){
        return msg;
    }

    public static WaterStatusEnum getEnum(int code) {
        for (WaterStatusEnum ce : WaterStatusEnum.values()) {
            if (ce.getCode() == code) {
                return ce;
            }
        }
        return WaterStatusEnum.UNKNOWN_STATUS;
    }
}
