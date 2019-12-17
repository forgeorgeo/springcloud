package com.road.quzhibathhousemqtt.enums;

/**
 * @author wenqi
 * @title: UseModeEnum
 * @projectName quzhi-bathhouse-mqtt
 * @description: 使用码使用模式
 * @date 2019/4/11上午10:11
 */
public enum UseModeEnum {

    UNKNOWN_MODE(-1,"未知模式"),

    WATER_MODE(0,"水表模式"),
    NOE_MODE (1,"吹风或洗衣机选择的模式1"),
    TWO_MODE(2,"吹风或洗衣机选择的模式2"),
    THREE_MODE(3,"吹风或洗衣机选择的模式3"),
    FOUR_MODE(4,"吹风或洗衣机选择的模式4")
    ;

    private Integer code;

    private String msg;

    UseModeEnum(Integer code,String msg){
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode(){
        return code;
    }

    public String getMsg(){
        return msg;
    }

    public static UseModeEnum getEnum(int code) {
        for (UseModeEnum ce : UseModeEnum.values()) {
            if (ce.getCode() == code) {
                return ce;
            }
        }
        return UseModeEnum.UNKNOWN_MODE;
    }
}
