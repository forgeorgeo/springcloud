package com.road.quzhibathhousemqtt.service.impl;

import com.road.quzhibathhousemqtt.bo.WorkDataBO;
import com.road.quzhibathhousemqtt.constant.UsedConstant;
import com.road.quzhibathhousemqtt.enums.CMDEnum;
import com.road.quzhibathhousemqtt.enums.DeviceTypeEnum;
import com.road.quzhibathhousemqtt.enums.FuncEnum;
import com.road.quzhibathhousemqtt.enums.WorkModeEnum;
import com.road.quzhibathhousemqtt.mqtt.MQTTPushClient;
import com.road.quzhibathhousemqtt.service.CMDService;
import com.road.quzhibathhousemqtt.service.CMDWithService;
import com.road.quzhibathhousemqtt.service.ComReponseService;
import com.road.quzhibathhousemqtt.util.ServerUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wenqi
 * @Title: ComReponseServiceImpl
 * @ProjectName quzhi-bathhouse-mqtt
 * @Description:
 * @date 2019/2/25上午11:38
 */
@Service
@Slf4j
public class ComReponseServiceImpl implements ComReponseService {

    @Autowired
    private CMDService cmdService;


    @Autowired
    private CMDWithService cmdWithService;




    @Override
    public boolean processHeadStatus(WorkDataBO workData, int dataLen) {

        if (workData.getWorkModeEnum().equals(WorkModeEnum.WORK_MODE_SEND)) {
            log.info(getCmdString(workData));
            return false;
        }

         boolean  verifyCmd = true;
        String data = workData.getData();

        String dataTag = data.substring(0, 2);
        String val = data.length() > 2 ? data.substring(2) : "";

        String s1 = ServerUtil.intToHex(CMDEnum.CMD_SUCCESS.getCode(), 2);
        String s2 = ServerUtil.intToHex(CMDEnum.CMD_SUCCESS_SEC.getCode(), 2);
        if ((!dataTag.equals(s1)) && (!dataTag.equals(s2))) {
            //命令执行失败81
//            CmdDao.updateCmdRltData(workData, workData.getAddr(), data);//更新命令状态
            log.error(getCmdString(workData));
            verifyCmd =  false;
        }

        if (val.length() < dataLen) {
            workData.setAnalyRes(CMDEnum.CMD_ERROR_DATA);
            log.error(getCmdString(workData) + "error datalen:  " + val.length() + "<" + dataLen);
            verifyCmd = false;
        }


        return verifyCmd;
    }

    @Override
    public String getCmdString(WorkDataBO workData) {
        return workData.getWorkModeEnum() + ";设备地址:" + workData.getAddr() + ";设备类型:"
                + DeviceTypeEnum.getEnum(workData.getStart()) + ";命令功能码:" + ServerUtil.intToHex(workData.getFunc(), 2)
                + ";计数器:" + workData.getCmdId() + ";解析类型:" + workData.getAnalyRes() + ";数据:" + workData.getData() + ";";
    }

    @Override
    public void responseError(WorkDataBO workData, String message, CMDEnum errCmdEnum) {
        String downData = ServerUtil.intToHex(CMDEnum.CMD_FALSE.getCode(), 2);
        if (null!=message&& !message.equals("")){
            downData += message;
        }
        downData = downData + ServerUtil.intToHex(errCmdEnum.getCode(), 2);

        String downCmd = cmdWithService.CMDCom(workData.getKey(), workData.getAddr(), workData.getCmdId(),
                FuncEnum.getEnum(workData.getFunc()), downData);


        MQTTPushClient client = MQTTPushClient.getInstance();

        client.publish(workData.getAddr() + UsedConstant.DEVICE_TOPIC_MARK, downCmd);

    }

    public void responseAllError(WorkDataBO workData, String message, CMDEnum errCmdEnum) {
        String downData = ServerUtil.intToHex(CMDEnum.CMD_FALSE.getCode(), 2);
        downData = downData + ServerUtil.intToHex(errCmdEnum.getCode(), 2);
        downData += message;

        String downCmd = cmdWithService.CMDCom(workData.getKey(), workData.getAddr(), workData.getCmdId(),
                FuncEnum.getEnum(workData.getFunc()), downData);


        MQTTPushClient client = MQTTPushClient.getInstance();

        client.publish(workData.getAddr() + UsedConstant.DEVICE_TOPIC_MARK, downCmd);

    }



    @Override
    public void responseOK(WorkDataBO workData, String message) {
        String downData = ServerUtil.intToHex(CMDEnum.CMD_SUCCESS.getCode(), 2) + "00";
        String downCmd = cmdWithService.CMDCom(workData.getKey(), workData.getAddr(), workData.getCmdId(),
                FuncEnum.getEnum(workData.getFunc()), downData);

        MQTTPushClient mqttPushClient = MQTTPushClient.getInstance();

        mqttPushClient.publish(workData.getAddr() + UsedConstant.DEVICE_TOPIC_MARK, downCmd);

//        client.publish(workData.getAddr() + UsedConstant.DEVICE_TOPIC_MARK, downCmd);
    }

    @Override
    public void orderResponseOK(WorkDataBO workData, String timeIds) {
        String downData = ServerUtil.intToHex(CMDEnum.CMD_SUCCESS.getCode(), 2);
            downData += timeIds;
        String downCmd = cmdWithService.CMDCom(workData.getKey(), workData.getAddr(), workData.getCmdId(),
                FuncEnum.getEnum(workData.getFunc()), downData);

        MQTTPushClient mqttPushClient = MQTTPushClient.getInstance();

        mqttPushClient.publish(workData.getAddr() + UsedConstant.DEVICE_TOPIC_MARK, downCmd);

    }

    @Override
    public void orderResponseError(WorkDataBO workData, String timeIds, CMDEnum errCmdEnum) {
        String downData = ServerUtil.intToHex(CMDEnum.CMD_FALSE.getCode(), 2);
        downData = downData + ServerUtil.intToHex(errCmdEnum.getCode(), 2);
        downData += timeIds;
        String downCmd = cmdWithService.CMDCom(workData.getKey(), workData.getAddr(), workData.getCmdId(),
                FuncEnum.getEnum(workData.getFunc()), downData);


        MQTTPushClient client = MQTTPushClient.getInstance();

        client.publish(workData.getAddr() + UsedConstant.DEVICE_TOPIC_MARK, downCmd);
    }

    @Override
    public void useResponseError(WorkDataBO workData, int deviceId, int waterId, CMDEnum errCmdEnum) {
        String downData = ServerUtil.intToHex(CMDEnum.CMD_FALSE.getCode(), 2);
        downData += ServerUtil.intToHex(deviceId,8);
        downData += ServerUtil.intToHex(waterId,4);
        downData = downData + ServerUtil.intToHex(errCmdEnum.getCode(), 2);
        String downCmd = cmdWithService.CMDCom(workData.getKey(), workData.getAddr(), workData.getCmdId(),
                FuncEnum.getEnum(workData.getFunc()), downData);


        MQTTPushClient client = MQTTPushClient.getInstance();

        client.publish(workData.getAddr() + UsedConstant.DEVICE_TOPIC_MARK, downCmd);
    }

    @Override
    public void processEndCommon(WorkDataBO workData, String data, String terminal) {
        log.info(getCmdString(workData));
        cmdService.updateCmdResult(workData, data, terminal);
    }

    @Override
    public void processSetCommon(WorkDataBO workData) {
        if (!processHeadStatus(workData, 0)) {
            return;
        }
        String data = workData.getData();
        String terminal = workData.getAddr();

        processEndCommon(workData, data, terminal);
    }

}
