package com.road.quzhibathhousemqtt.enums;

/**
 * @author wenqi
 * @title: DeviceBigTypeEnum
 * @projectName quzhi-bathhouse-mqtt
 * @description:
 * @date 2019/4/10上午9:34
 */
public enum DeviceBigTypeEnum {

    NONE(0,"未知设备"),
    HOT_WATER(4,"热水表"),
    BOILING_WATER(5,"开水器"),
    WASH(6,"洗衣机"),
    BLOWER(7,"吹风机"),
    CHARGING(8,"充电"),

    AIR_CONDITIONING(9,"空调"),

    ;

    private Integer code;

    private String msg;

    DeviceBigTypeEnum(Integer code,String msg){
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode(){
        return code;
    }

    public String getMsg(){
        return msg;
    }

    public static DeviceBigTypeEnum getEnum(int code) {
        for (DeviceBigTypeEnum dt : DeviceBigTypeEnum.values()) {
            if (dt.getCode() == code) {
                return dt;
            }
        }
        return DeviceBigTypeEnum.NONE;
    }
}
