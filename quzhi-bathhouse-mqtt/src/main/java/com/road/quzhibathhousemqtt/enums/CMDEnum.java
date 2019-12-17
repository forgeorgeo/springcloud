package com.road.quzhibathhousemqtt.enums;

/**
 * @author wenqi
 * @Title: CMDEnum
 * @ProjectName quzhi-bathhouse-mqtt
 * @Description:
 * @date 2019/2/20下午2:30
 */
public enum CMDEnum {

    //协议常用值
    CMD_SUCCESS(0x80,"成功,一次性回复"),
    CMD_SUCCESS_SEC(0x88,"成功,二次性回复"),
    CMD_FALSE(0x81,"失败,协议错误返还代码"),

    CMD_ERROR_START(0x01,"包头错误"),
    CMD_ERROR_LEN(0x02,"长度错误"),
    CMD_ERROR_ADDR(0x03,"地址错误"),
    CMD_ERROR_FUNC(0x04,"功能码错误"),
    CMD_ERROR_DATA(0x05,"数据格式错误"),
    CMD_ERROR_CHECK(0x06,"校验格式错误"),
    CMD_ERROR_END(0x07,"结束码错误"),
    CMD_ERROR_CTRL(0x08,"来源错误"),
    CMD_ERROR_KEY(0x09,"密码校验错误"),
    CMD_ERROR_MARK(0x0A,"标识符错误"),
    CMD_ERROR_PARAMETER(0x10,"参数校验错误"),
    CMD_ERROR_DEVICE_TYPE(0x11,"设备类型错误"),
    CMD_ERROR_PROJECT_SN(0x12,"项目ID和SN不匹配"),
    CMD_ERROR_WATER_NO_REPEAT(0x22,"机号重复"),
    CMD_ERROR_TIME_OUT(0x23,"超时"),
    CMD_ERROR_NOT_PROJECT(0x24,"未找到项目"),
    CMD_ERROR_USE_CODE(0x25,"使用码错误"),
    CMD_ERROR_UNOPENED_USE_CODE(0x26,"未开启使用码"),
    CMD_ERROR_RETA_VERSION(0x27,"费率版本错误"),
    CMD_ERROR_USE_MODE(0x28,"使用模式错误"),
    CMD_ERROR_NOT_BOOKING(0x29,"没有预约信息"),
    CMD_ERROR_MONEY_NOT_ENOUGH(0x30,"金额不足"),
    CMD_ERROR_NOT_ORDER(0x31,"未找到订单"),
    CMD_ERROR_ORDER_UPLOADED(0x32,"订单已上传"),
    CMD_ERROR_YK_LT_AMOUNT(0x33,"消费金额大于预扣金额"),
    CMD_ERROR_NOT_DEVICE(0x34,"未找到设备"),
    CMD_ERROR_DEVICE_BATHROOM(0x35,"设备信息和澡堂信息不匹配"),


    //非十六进制字符数据
    CMD_ERROR_CHAR(0x0B,"非法数据"),

    CMD_ERROR_UNKNOWN(0xFF,"未知错误"),


    ;

    private Integer code;

    private String msg;

    CMDEnum(Integer code,String msg){
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode(){
        return code;
    }

    public String getMsg(){
        return msg;
    }

    public static CMDEnum getEnum(int code) {
        for (CMDEnum ce : CMDEnum.values()) {
            if (ce.getCode() == code) {
                return ce;
            }
        }
        return CMDEnum.CMD_ERROR_UNKNOWN;
    }
}
