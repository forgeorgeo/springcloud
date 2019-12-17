package com.road.quzhibathhousemqtt.enums;

/**
 * @author wenqi
 * @Title: BookingStatusEnum
 * @ProjectName quzhi-bathhouse-mqtt
 * @Description:
 * @date 2019/2/26下午5:20
 */
public enum BookingStatusEnum {


    UNKNOWN_STATUS(-1,"未知状态"),

    BOOKING_IN_STATUS(0,"已预中"),

    EXPiRED_STATUS(1,"已失效"),

    CANCELLED_STATUS(2,"已取消"),

    USING_STATUS(3,"使用中"),

    USING_END_STATUS(4,"使用结束")
    ;

    private Integer code;

    private String msg;

    BookingStatusEnum(Integer code,String msg){
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode(){
        return code;
    }

    public String getMsg(){
        return msg;
    }

    public static BookingStatusEnum getEnum(int code) {
        for (BookingStatusEnum ce : BookingStatusEnum.values()) {
            if (ce.getCode() == code) {
                return ce;
            }
        }
        return BookingStatusEnum.UNKNOWN_STATUS;
    }
}
