package com.road.quzhibathhousemqtt.constant;

/**
 * @author wenqi
 * @Title: UsedConstant
 * @ProjectName quzhi-bathhouse-mqtt
 * @Description: 常用常量
 * @date 2019/2/21下午8:30
 */
public class UsedConstant {

    public final static String USE_CODE_KEY  = "TheProductIsStupid";

    /**
     * CMD redis 标记
     */
    public final static String CMD_REDIS_MARK = "mqttCmdStatus:";


    /**
     * 设备Topic标记
     */
    public final static String DEVICE_TOPIC_MARK = "_serverCMD";

    /**
     * 设备状态 redis标记
     */
    public final static String DEVICE_REDIS_MARK ="mqttDevStatus:";
    /**
     * 清除 预约码 redis 标记
     */
    public final static String  CANCEL_CODE_PHONE_REDIS_MARK ="cancelCodePhone:account";

    /**
     * 使用码有效时间(秒)
     */
    public final static long INTERVAL_TIME =  10;

    /**
     * 手机客户端 MQTT标记
     */
    public final static String PHONE_CLIENT_MQTT_MARK="phoneClient_";


    /**
     * 常用数据长度
     */
    public final static int USED_DATA_LEN = 2;

    /**
     * 密码数据长度
     */
    public final static int PASSWORD_DATA_LEN = 12;

    /**
     * 请求使用码数据长度= 使用码 + 设备ID + 项目ID + 水表编码 + 时间戳 + 设备类型 + 费率版本 + 使用模式
     */
    public final static int USE_CODE_DATA_LEN = 8 + 8 + 8 + 4 + 14 + 4 + 8 + 2 ;

    /**
     * 设备状态数据长度
     */
    public final static int DEVICE_STATUS_DATA_LEN = 12;

    /**
     * 删除设备返还数据长度
     */
    public final static int DELETE_DEVICE_DATA_LEN = 2 + 8 +4 +8 +2;

    /**
     * 订单开始数据长度
     */
    public final static int ORDER_START_DATA_LEN = 25 * 2;

    /**
     * 上传订单数据长度： 项目ID 水表号 订单ID 设备ID 账户ID 账户类型 ，消费金额 开始时间  结束时间 脉冲数 水流速 使用时长 暂停次数 异常状态
     */
    public final static int UPLOAD_ORDER_DATA_LEN = 8 + 4 +20 + 8 + 8 + 2 + 8 + 14 + 14 + 8 +4 + 4 + 2 + 2;

    /**
     * 上传异常状态的订单
     */
    public final static int  UNUSUAL_UPLOAD_ORDER_DATA_LEN = 8 + 4 +8 +8+ 14 + 8 + 4 + 4 +2 +2;

    /**
     * 主动获取订单数据长度
     */
    public final static int INITIATIVE_ORDER_DATA_LEN = 16;

    /**
     * 设备异常数据长度 = 项目ID +  设备ID + 状态
     */
    public final static int DEVICE_FAIL_DATA_LEN = 8+ 8 +2 ;

    /**
     * 登录包数据长度 32分别为12位通信密码和8位项目ID和12位设备SN + 版本密码
     */
    public final static int LOGIN_DATA_LEN = 12 + 8 + 12 +8;

    /**
     * 取消预约码数据长度
     */
    public final static int CANCEL_CODE_DATA_LEN = 2 + 20 + 8 + 8  +12;

    /**
     * 下发预约码返回数据长度
     */
    public final static int SET_CODE_DATA_LEN = 2 + 20 + 8 +8 +12 + 14;

    /**
     *  6C 设置设备信息 项目ID 8  集中器设备编号4   设备计费类型4  是否强制 (0 否 \1 是)
     */
    public final static int SET_DEVICE_INFO_DATA_LEN = 8 + 4 + 4 + 2;


    /**
     * 操作成功标记
     */
    public final static String OPERATION_SUCCESS_MARK = "80";


    /**
     * 操作失败标记
     */
    public final static String OPERATION_FAILED_MARK = "81";

    /**
     * 普通用户
     */
    public final static int ORDINARY_USER = 2;

    /**
     * 管理员用户
     */
    public final static int OPERATOR_USER = 1;


    /**
     * 下发预约码通讯时间
     */
    public final static int SET_CODE_COMMUNICARION_TIME = 12;


    /**
     * 获取设备信息数据长度
     */
    public final static int GET_DEVICE_DATA_LEN = 8 + 8+ 4 + 4;




}
