package com.road.quzhibathhousemqtt.service.impl;

import com.road.quzhibathhousemqtt.bo.CMDBaseBO;
import com.road.quzhibathhousemqtt.bo.WorkDataBO;
import com.road.quzhibathhousemqtt.constant.UsedConstant;
import com.road.quzhibathhousemqtt.enums.DeviceTypeEnum;
import com.road.quzhibathhousemqtt.enums.FuncEnum;
import com.road.quzhibathhousemqtt.enums.WorkModeEnum;
import com.road.quzhibathhousemqtt.service.*;
import com.road.quzhibathhousemqtt.util.ServerUtil;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wenqi
 * @Title: CMDDispatcherServiceImpl
 * @ProjectName quzhi-bathhouse-mqtt
 * @Description:
 * @date 2019/2/20上午11:49
 */
@Service
@Slf4j
public class CMDDispatcherServiceImpl implements CMDDispatcherService {



    @Autowired
    private CMDWithService cmdWithService;


    @Autowired
    private ComReponseService comReponseService;


    @Autowired
    private OrderService orderService;


    @Autowired
    private DeviceService deviceService;

    @Autowired
    private UseCodeService useCodeService;

    @Override
    public void execute(WorkModeEnum modeEnum, MqttMessage message) {
        String msg = message.toString();

        if (ServerUtil.isEmpty(msg)) {
            log.error("接收到空包消息: " + message.getId());
            return;
        }

        // 去头# 不用去尾,尾对后期解析不影响
        String msgsub = msg.substring(1);

        // 取当前协议的设备类型
        DeviceTypeEnum deviceType = DeviceTypeEnum.getEnum(CMDBaseBO.getHeader(msgsub));

        // 协议解析数据
        WorkDataBO wd = null;
        //默认密码(需要解密报文时，暂时用不着)
        String cmdPassword = "000000";

        String data = "";
        String terminal = "";
        // 分发处理
        switch (deviceType) {
            // WiWater(0x71), // 澡堂水表
            case DEVICE_TYPE_BATHHOUSE_WATER:

                wd = cmdWithService.CMDSplitByMode(cmdPassword, modeEnum, msg);

                if (!wd.result()) {
                    log.error("CMDSplitByMode err: " + wd.getAnalyRes());
                    return;
                }
                FuncEnum funcEnum = FuncEnum.getEnum(wd.getFunc());

                switch (funcEnum) {
                    // 获取时钟
                    case FUNC_GET_DEVICE_TIME:
                        if (!comReponseService.processHeadStatus(wd, UsedConstant.USED_DATA_LEN)) {
                            return;
                        }
                        data = wd.getData().substring(0, 12);
                        data = ServerUtil.dateFormat(data);
                        String datalog = "读取完成;设备时间:" + data;
                        wd.setData(datalog);
                        terminal = wd.getAddr();

                        comReponseService.processEndCommon(wd, data, terminal);
                        break;
                    // 设置时钟
                    case FUNC_SET_DEVICE_TIME:
                        if (!comReponseService.processHeadStatus(wd, 0)) {
                            return;
                        }
                        data = wd.getData();
                        terminal = wd.getAddr();

                        comReponseService.processEndCommon(wd, data, terminal);
                        break;
                    // 获取密码
                    case FUNC_GET_COMM_PASSWORD:
                        if (!comReponseService.processHeadStatus(wd, UsedConstant.PASSWORD_DATA_LEN)) {
                            return;
                        }

                        data = wd.getData();
                        if (ServerUtil.formatPassword(Integer.valueOf(wd.getKey()).intValue()).equals(data)) {
                            data = wd.getKey();
                        }

                        datalog = "读取完成;通讯密码:" + data;
                        wd.setData(datalog);
                        terminal = wd.getAddr();

                        comReponseService.processEndCommon(wd, data, terminal);

                        break;
                    // 设置密码
                    case FUNC_SET_COMM_PASSWORD:
                        comReponseService.processSetCommon(wd);
                        break;
                    // 下发水表费率
                    case FUNC_SEND_WATER_RETA:
                        comReponseService.processSetCommon(wd);
                        break;

                    // 下发洗衣机或者吹风机费率
                    case FUNC_SEND_WASH_OR_BLOWER_RETA:
                      comReponseService.processSetCommon(wd);
                        break;
                    // 获取设备状态
                    case FUNC_GET_DEVICE_STATUS:
                        if (!comReponseService.processHeadStatus(wd, UsedConstant.DEVICE_STATUS_DATA_LEN)) {
                            return;
                        }

                        data = wd.getData().substring(0, 12);
                        data = ServerUtil.dateFormat(data);
                        datalog = "读取水表状态完成;水表状态:" + data;
                        wd.setData(datalog);
                        terminal = wd.getAddr();

                        comReponseService.processEndCommon(wd, data, terminal);

                        break;
                    // 远程关闭订单
                    case FUNC_REMOTE_CLOSE_ORDER:
                        comReponseService.processSetCommon(wd);
                        break;


                    // 设置水表机号
                    case FUNC_SET_WATER_ID:
                        comReponseService.processSetCommon(wd);
                        break;

                    // 设置或删除设备列表
                    case FUNC_DELETE_DEVICE:
                      deviceService.deleteDevice(wd,msg);

                        break;

                    // 取消预约码
                    case FUNC_CANCEL_RESERVATION_CODE:
//                        orderService.cancelReponse(wd,msg);

                        break;
                    // 设置时钟
                    case FUNC_INIT_DEVICE:
                        if (!comReponseService.processHeadStatus(wd, 0)) {
                            return;
                        }
                        data = wd.getData();
                        terminal = wd.getAddr();

                        comReponseService.processEndCommon(wd, data, terminal);
                        break;

                     // 使用使用码
                    case FUNC_DEVICE_USE_CODE:

                        useCodeService.useCode(wd,msg);
                        break;
                    // 订单结束
                    case FUNC_DEVICE_ORDER_END:
                        orderService.uploadOrderData(wd,msg);
                        break;
                    // 设备设备信息
                    case FUNC_SET_DEVICE_INFO:
//                        orderService.initiativeOrderData(wd,msg);
                        deviceService.setDeviceInfo(wd,msg);
                        break;
                    // 上传设备异常状态
                    case FUNC_DEVICE_REPORT_ERROR_STATUS:
                        deviceService.deviceFail(wd,msg);
                        break;
                    // 设备登录
                    case FUNC_DEVICE_LOGIN:
                        deviceService.deviceLogin(wd,msg);
                        break;
                    // 设备心跳
                    case FUNC_DEVICE_HEART_BEAT:
                        deviceService.deviceHeartbeat(wd,msg);
                        break;
                    case FUNC_GET_DEVICE_INFO:
                        deviceService.getDeviceInfo(wd,msg);
                        break;
                    case FUNC_UPDATE_CONTROLLER:
                        comReponseService.processSetCommon(wd);
                        break;
                    case FUNC_UPLOAD_UNUSUAL_ORDER:
                        orderService.uploadUnusualOrderData(wd,msg);
                        break;
                    default:
                        log.error("not find func, this func is" + funcEnum.getCode());
                        return;
                }


                break;

            default: // dtNone(0), // //如果是未知设备
                log.error("not find dev, this type is " + deviceType.getCode());
                return;
        }


    }
}
