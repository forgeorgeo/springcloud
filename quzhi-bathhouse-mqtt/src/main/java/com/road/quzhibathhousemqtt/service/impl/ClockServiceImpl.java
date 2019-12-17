package com.road.quzhibathhousemqtt.service.impl;

import com.road.quzhibathhousemqtt.bo.MQTTCMDStatus;
import com.road.quzhibathhousemqtt.bo.WorkDataBO;
import com.road.quzhibathhousemqtt.constant.UsedConstant;
import com.road.quzhibathhousemqtt.enums.FuncEnum;
import com.road.quzhibathhousemqtt.enums.ResultEnum;
import com.road.quzhibathhousemqtt.mqtt.MQTTPushClient;
import com.road.quzhibathhousemqtt.service.CMDWithService;
import com.road.quzhibathhousemqtt.service.ClockService;
import com.road.quzhibathhousemqtt.service.RedisService;
import com.road.quzhibathhousemqtt.util.DateUtil;
import com.road.quzhibathhousemqtt.util.HttpResult;
import com.road.quzhibathhousemqtt.util.MapConvertBaseUtil;
import com.road.quzhibathhousemqtt.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @author wenqi
 * @Title: ClockServiceImpl
 * @ProjectName quzhi-bathhouse-mqtt
 * @Description:
 * @date 2019/2/21上午11:37
 */
@Slf4j
@Service
public class ClockServiceImpl implements ClockService {


    @Autowired
    private CMDWithService cmdWithService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private MQTTPushClient mqttPushClient;


    @Override
    public HttpResult queryClock(String key, String addr, String proID, Long cmdId) {

        try {
            addr = String.format("%12s", addr).replace(' ', '0');

            String downData = "";
            String downCmd = cmdWithService.CMDCom(key, addr, cmdId, FuncEnum.FUNC_GET_DEVICE_TIME, downData);
            log.info("查询时钟项目 ProID:{} 命令:{}", proID, downCmd);
            MQTTCMDStatus cmdStatus = new MQTTCMDStatus();
            cmdStatus.setPrjID(proID);
            cmdStatus.setCmdTypeId(FuncEnum.FUNC_GET_DEVICE_TIME.getCode().toString());
            cmdStatus.setCmdId(cmdId.toString());
            cmdStatus.setCreateTime(DateUtil.getFormatTime());
            cmdStatus.setSncode(addr);

            redisService.hmSet(UsedConstant.CMD_REDIS_MARK + cmdStatus.getCmdId(),
                    MapConvertBaseUtil.object2Map(cmdStatus));

            WorkDataBO workData = new WorkDataBO();
            workData.setAddr(addr);
            workData.setKey(key);
            workData.setCmdId(Long.valueOf(cmdId));


            MQTTPushClient client = MQTTPushClient.getInstance();

            client.publish(workData.getAddr() + UsedConstant.DEVICE_TOPIC_MARK, downCmd);

            return ResultUtil.success();
        } catch (Exception e) {
            log.error("查询时钟异常" + e);
            e.printStackTrace();
        }
        return ResultUtil.error(ResultEnum.CMD_SEND_ERROR);
    }

    @Override
    public HttpResult setClock(String key, String addr, String proID, Long cmdId) {

        try {
            addr = String.format("%12s", addr).replace(' ', '0');

            String downData = "";

             downData = DateUtil.getFormatTimeTwelve();
            String downCmd = cmdWithService.CMDCom(key, addr, cmdId, FuncEnum.FUNC_SET_DEVICE_TIME, downData);
            log.info("设置时钟项目" + proID + "命令:" + downCmd);
            MQTTCMDStatus cmdStatus = new MQTTCMDStatus();
            cmdStatus.setPrjID(proID);
            cmdStatus.setCmdTypeId(FuncEnum.FUNC_SET_DEVICE_TIME.getCode().toString());
            cmdStatus.setCmdId(cmdId.toString());
            cmdStatus.setCreateTime(DateUtil.getFormatTime());
            cmdStatus.setSncode(addr);

            redisService.hmSet(UsedConstant.CMD_REDIS_MARK + cmdStatus.getCmdId(),
                    MapConvertBaseUtil.object2Map(cmdStatus));

            WorkDataBO workData = new WorkDataBO();
            workData.setAddr(addr);
            workData.setKey(key);
            workData.setCmdId(Long.valueOf(cmdId));


            MQTTPushClient client = MQTTPushClient.getInstance();

            client.publish(workData.getAddr() + UsedConstant.DEVICE_TOPIC_MARK, downCmd);

            return ResultUtil.success();
        } catch (Exception e) {
            log.error("设置时钟异常"+ e);
            e.printStackTrace();
        }

        return ResultUtil.error(ResultEnum.CMD_SEND_ERROR);
    }

    @Override
    public void setClock(WorkDataBO workData, String message) {
        MQTTPushClient client = MQTTPushClient.getInstance();


        client.publish(workData.getAddr() + UsedConstant.DEVICE_TOPIC_MARK, message);


    }
}
