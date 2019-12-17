package com.road.quzhibathhousemqtt.enums;

/**
 * @author wenqi
 * @Title: FuncEnum
 * @ProjectName quzhi-bathhouse-mqtt
 * @Description:
 * @date 2019/2/20下午3:59
 */
public enum FuncEnum {

    FUNC_UNKNOWN(0x00,"未找到功能码"),
    FUNC_GET_DEVICE_TIME(0x21,"获取时间"),
    FUNC_SET_DEVICE_TIME(0x22,"设置时间"),
    FUNC_GET_COMM_PASSWORD(0x23,"获取连接密码"),
    FUNC_SET_COMM_PASSWORD(0x24,"设置连接密码"),
    FUNC_SET_DEVICE_PARAMETER(0x2A,"设置设备参数"),
    FUNC_GET_DEVICE_PARAMETER(0x2B,"获取设备参数"),
    FUNC_GET_HEART_BEAT(0x2E,"查询设备心跳时间"),
    FUNC_SET_HEART_BEAT(0x2F,"设置设备心跳时间"),
    FUNC_SET_BLACK_MORE(0x2C,"设置多条黑名单"),
    FUNC_GET_BLACK_MORE(0x2D,"读取多条黑名单"),
    FUNC_GET_BLACK_VERSION(0x4E,"读取黑名单版本号"),

    FUNC_GET_DEVICE_DATA_ONE(0x34,"采集设备数据"),
    FUNC_SYSTEM_NET(0x4F,"组网"),
    FUNC_GET_CONCENTRATOR_CHANNEL(0x50,"查询集中器信道"),
    FUNC_SET_CONCENTRATOR_CHANNEL(0x51,"设置集中器信道"),
    FUNC_SET_DEVICE_LIST_AND_CHANNEL(0x52,"下发设备列表和信道"),
    // TODO 不知道词条命令作用
    FUNC_SET_SINGGLER_DEVICE_LIST_AND_CHANNEL(0x57,"下发"),
    FUNC_GET_REPEATER_STATUS_AND_CHANNEL(0x53,"查询集中器状态和信道"),
    FUNC_SET_REPEATER_CHANNEL(0x54,"设置集中器信道"),
    FUNC_GET_DEVICE_STATUS(0x62,"获取设备状态"),

    FUNC_DEVICE_RENEW_COLLECT_DATA(0x25,"采集指定数据(补采)"),

    FUNC_GET_DEVICE_TYPE_AND_VERSION(0x3E,"读取设备类型和版本号"),
    FUNC_SET_M1OrCPU(0x56,"设置M1/CPU启停指标"),

    FUNC_DEVICE_LOGIN(0x82,"设备登录"),
    FUNC_DEVICE_HEART_BEAT(0x83,"设备心跳"),
    FUNC_DEVICE_COLLECT_DATA(0x85,"设备上报数据"),
    FUNC_DEVICE_UP_LOAD_STATUS(0x87,"设备上报在线状态"),
    FUNC_SET_DEVICE_REQUEST_BLACK_LIST(0x95,"设置设备请求黑名单列表"),

    FUNC_SEND_WATER_RETA(0x60,"下发水表费率"),
    FUNC_SEND_WASH_OR_BLOWER_RETA(0x61,"下发洗衣机或者吹风机费率"),

    FUNC_REMOTE_CLOSE_ORDER(0x63,"远程关闭订单"),
    FUNC_SET_WATER_ID(0x64,"设置水表机号"),

    FUNC_DELETE_DEVICE(0x65,"删除设备"),
    FUNC_CANCEL_RESERVATION_CODE(0x66,"取消预约码"),
    FUNC_INIT_DEVICE(0x67,"初始化集中器"),
//    FUNC_DEVICE_ORDER_START(0x6A,"订单开始"),
    FUNC_DEVICE_USE_CODE(0x6A,"开始使用使用码"),

    FUNC_DEVICE_ORDER_END(0x6B,"订单结束上传交易数据"),
    FUNC_SET_DEVICE_INFO(0x6C,"设置设备信息"),
    FUNC_DEVICE_REPORT_ERROR_STATUS(0x6D,"上传设备异常"),
    FUNC_UPDATE_CONTROLLER(0x68,"更新采集器"),

    FUNC_GET_DEVICE_INFO(0x6F,"获取设备信息"),
    FUNC_UPLOAD_UNUSUAL_ORDER(0x7A,"上传异常订单数据"),



    ;



    private Integer code;

    private String msg;

    FuncEnum(Integer code,String msg){
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode(){
        return code;
    }

    public String getMsg(){
        return msg;
    }

    public static FuncEnum getEnum(int code) {
        for (FuncEnum dt : FuncEnum.values()) {
            if (dt.getCode() == code) {
                return dt;
            }
        }
        return FuncEnum.FUNC_UNKNOWN;
    }
}
