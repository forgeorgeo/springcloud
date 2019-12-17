package com.road.quzhibathhousemqtt.enums;

/**
 * @author wenqi
 * @Title: WorkModeEnum
 * @ProjectName quzhi-bathhouse-mqtt
 * @Description:
 * @date 2019/2/20上午11:42
 */
public enum WorkModeEnum {

    WORK_MODE_NONE(-1,"未知操作"),
    WORK_MODE_RECEIVE(0x81,"接收"),
    WORK_MODE_SEND(0x80,"发送"),

    ;

    private Integer code;

    private String msg;

    WorkModeEnum(Integer code,String msg){
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode(){
        return code;
    }

    public String getMsg(){
        return msg;
    }

    public static WorkModeEnum getEnum(int value) {
        for (WorkModeEnum wm : WorkModeEnum.values()) {
            if (wm.getCode() == value) {
                return wm;
            }
        }
        return WorkModeEnum.WORK_MODE_NONE;
    }
}
