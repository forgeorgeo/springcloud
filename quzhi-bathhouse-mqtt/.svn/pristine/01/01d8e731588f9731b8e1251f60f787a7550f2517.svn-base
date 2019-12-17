package com.road.quzhibathhousemqtt.enums;

/**
 * @author wenqi
 * @Title: DeviceTypeEnum
 * @ProjectName quzhi-bathhouse-mqtt
 * @Description:
 * @date 2019/2/20下午1:51
 */
public enum  DeviceTypeEnum {
    DEVICE_TYPE_NONE(0,"未知设备"),
    DEVICE_TYPE_CONSUME(0x64,"消费机"),
    DEVICE_TYPE_TRANSFER(0x65,"转账机"),
    DEVICE_TYPE_SAVE_MONEY(0x65,"空中充值"),
    DEVICE_TYPE_AIR_CTRL(0x67,"空调控制"),
    DEVICE_TYPE_BATHHOUSE_WATER(0x71,"澡堂水表"),

    DEVICE_TYPE_HEAT_CTRL(0x69,"热泵控制"),
    DEVICE_TYPE_NET_WATER(0x70,"物联网水表"),

            ;

    private Integer code;

    private String msg;

    DeviceTypeEnum(Integer code,String msg){
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode(){
        return code;
    }

    public String getMsg(){
        return msg;
    }

    public static DeviceTypeEnum getEnum(int code) {
        for (DeviceTypeEnum dt : DeviceTypeEnum.values()) {
            if (dt.getCode() == code) {
                return dt;
            }
        }
        return DeviceTypeEnum.DEVICE_TYPE_NONE;
    }
}

