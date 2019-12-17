package com.road.quzhibathhousemqtt.service.impl;

import com.road.quzhibathhousemqtt.bo.MQTTCMDStatus;
import com.road.quzhibathhousemqtt.bo.MQTTDeviceStatus;
import com.road.quzhibathhousemqtt.bo.WorkDataBO;
import com.road.quzhibathhousemqtt.constant.UsedConstant;
import com.road.quzhibathhousemqtt.dao.BathroomCollectDevDao;
import com.road.quzhibathhousemqtt.dao.DeviceDao;
import com.road.quzhibathhousemqtt.dao.RateDao;
import com.road.quzhibathhousemqtt.dto.*;
import com.road.quzhibathhousemqtt.entity.BathroomCollectDev;
import com.road.quzhibathhousemqtt.entity.DeviceInfo;
import com.road.quzhibathhousemqtt.entity.RateType;
import com.road.quzhibathhousemqtt.enums.*;
import com.road.quzhibathhousemqtt.mqtt.MQTTPushClient;
import com.road.quzhibathhousemqtt.service.*;
import com.road.quzhibathhousemqtt.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * @author wenqi
 * @Title: DeviceServiceImpl
 * @ProjectName quzhi-bathhouse-mqtt
 * @Description:
 * @date 2019/2/22下午2:09
 */
@Service
@Slf4j
public class DeviceServiceImpl implements DeviceService {

    @Autowired
    private CMDWithService cmdWithService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private ComReponseService reponseService;

    @Autowired
    private DeviceDao deviceDao;

    @Autowired
    private RateDao rateDao;

    @Autowired
    private BathroomCollectDevDao collectDevDao;


    @Autowired
    private ClockService clockService;

    @Override
    public HttpResult setPassword(String password, String addr, String proID, String cmdId) {

        StringBuffer sb = null;

        try {
            addr = String.format("%12s", addr).replace(' ', '0');
            String downData = ServerUtil.intToHex(Integer.valueOf(password), 12);
            downData += ServerUtil.intToHex(Integer.valueOf(proID),8);
            String downCmd = cmdWithService.CMDCom(password, addr, Long.valueOf(cmdId), FuncEnum.FUNC_SET_COMM_PASSWORD, downData);
            log.info("设置通讯密码项目" + proID + "命令:" + downCmd);
            MQTTCMDStatus cmdStatus = new MQTTCMDStatus();
            cmdStatus.setCmdTypeId(String.valueOf(FuncEnum.FUNC_SET_COMM_PASSWORD.getCode()));
            cmdStatus.setCmdId(cmdId);
            cmdStatus.setCreateTime(DateUtil.getFormatTime());
            cmdStatus.setPrjID(proID);
            cmdStatus.setSncode(addr);
            redisService.hmSet(UsedConstant.CMD_REDIS_MARK + cmdStatus.getCmdId(),
                    MapConvertBaseUtil.object2Map(cmdStatus));


            MQTTPushClient client = MQTTPushClient.getInstance();

            client.publish(addr + UsedConstant.DEVICE_TOPIC_MARK, downCmd);
            return ResultUtil.success();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("设置通讯密码异常", e);
        }
        return ResultUtil.error(ResultEnum.CMD_SEND_ERROR);
    }

    @Override
    public HttpResult queryWaterStatus(String key, String addr, String proID, String waterId, String cmdId) {

        try {
            addr = String.format("%12s", addr).replace(' ', '0');
            String downData = ServerUtil.intToHex(Integer.valueOf(proID), 8);
            downData += ServerUtil.intToHex(Integer.valueOf(waterId), 8);
            String downCmd = cmdWithService.CMDCom(key, addr, Long.valueOf(cmdId), FuncEnum.FUNC_GET_DEVICE_STATUS, downData);
            log.info("获取设备状态项目" + proID + "命令:" + downCmd);
            MQTTCMDStatus cmdStatus = new MQTTCMDStatus();
            cmdStatus.setCmdTypeId(String.valueOf(FuncEnum.FUNC_GET_DEVICE_STATUS.getCode()));
            cmdStatus.setCmdId(cmdId);
            cmdStatus.setCreateTime(DateUtil.getFormatTime());
            cmdStatus.setPrjID(proID);
            cmdStatus.setSncode(addr);
            redisService.hmSet(UsedConstant.CMD_REDIS_MARK + cmdStatus.getCmdId(),
                    MapConvertBaseUtil.object2Map(cmdStatus));


            MQTTPushClient client = MQTTPushClient.getInstance();

            client.publish(addr + UsedConstant.DEVICE_TOPIC_MARK, downCmd);
            return ResultUtil.success();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取设备状态异常", e);
        }
        return ResultUtil.error(ResultEnum.CMD_SEND_ERROR);
    }


    @Override
    public HttpResult sendWaterRate(WaterRateDTO rateDTO) {
        try {
            String addr = String.format("%12s", rateDTO.getAddr()).replace(' ', '0');
            StringBuilder downData = new StringBuilder();
            /**
             *  projectId 长度8
             *  usrMode 计费模式 0x00 0x11
             *  rate  费率
             *  unit 计费单位
             *  timeOut 无流量超时时间
             *  maxConsume 最大消费金额
             *  deviceType 设备小类
             *  rateVersion  费率版本号
             */
            downData.append(ServerUtil.intToHex(Integer.valueOf(rateDTO.getProjectId()), 8));
            downData.append(ServerUtil.intToHex(Integer.valueOf(rateDTO.getUsrMode()), 2));
            downData.append(ServerUtil.intToHex(Integer.valueOf(rateDTO.getRate()), 8));
            downData.append(ServerUtil.intToHex(Integer.valueOf(rateDTO.getUnit()), 2));
            downData.append(ServerUtil.intToHex(Integer.valueOf(rateDTO.getTimeOut()), 4));
            downData.append(ServerUtil.intToHex(Integer.valueOf(rateDTO.getMaxConsume()), 8));
            downData.append(ServerUtil.intToHex(Integer.valueOf(rateDTO.getDeviceType()), 4));
            downData.append(ServerUtil.intToHex(Integer.valueOf(rateDTO.getRateVersion()), 8));
            String downCmd = cmdWithService.CMDCom(rateDTO.getKey(), rateDTO.getAddr(), Long.valueOf(rateDTO.getCmdId()),
                    FuncEnum.FUNC_SEND_WATER_RETA, downData.toString());
            log.info("下发水表费率" + rateDTO.getProjectId() + " 命令:" + downCmd);
            MQTTCMDStatus cmdStatus = new MQTTCMDStatus();
            cmdStatus.setCmdTypeId(String.valueOf(FuncEnum.FUNC_SEND_WATER_RETA.getCode()));
            cmdStatus.setCmdId(rateDTO.getCmdId());
            cmdStatus.setCreateTime(DateUtil.getFormatTime());
            cmdStatus.setPrjID(rateDTO.getProjectId());
            cmdStatus.setSncode(addr);
            redisService.hmSet(UsedConstant.CMD_REDIS_MARK + cmdStatus.getCmdId(),
                    MapConvertBaseUtil.object2Map(cmdStatus));
            MQTTPushClient client = MQTTPushClient.getInstance();

            client.publish(addr + UsedConstant.DEVICE_TOPIC_MARK, downCmd);
            return ResultUtil.success();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("下发水表费率异常", e);
        }

        return ResultUtil.error(ResultEnum.CMD_SEND_ERROR);
    }

    @Override
    public HttpResult sendRateWashAndBlower(WashBlowerRateDTO rateDTO) {
        try {
            String addr = String.format("%12s", rateDTO.getAddr()).replace(' ', '0');
            StringBuilder downData = new StringBuilder();
            /**
             *  projectId 长度8
             *  rate1 费率
             *  time1 时间
             *  rate2
             *  time2
             *  rate3
             *  time3
             *  rate4
             *  time4
             *  deviceType 设备小类
             *  rateVersion  费率版本号
             */
            downData.append(ServerUtil.intToHex(Integer.valueOf(rateDTO.getProjectId()), 8));
            downData.append(ServerUtil.intToHex(Integer.valueOf(rateDTO.getRate1()), 8));
            downData.append(ServerUtil.intToHex(Integer.valueOf(rateDTO.getTime1()), 4));
            downData.append(ServerUtil.intToHex(Integer.valueOf(rateDTO.getRate2()), 8));
            downData.append(ServerUtil.intToHex(Integer.valueOf(rateDTO.getTime2()), 4));
            downData.append(ServerUtil.intToHex(Integer.valueOf(rateDTO.getRate3()), 8));
            downData.append(ServerUtil.intToHex(Integer.valueOf(rateDTO.getTime3()), 4));
            downData.append(ServerUtil.intToHex(Integer.valueOf(rateDTO.getRate4()), 8));
            downData.append(ServerUtil.intToHex(Integer.valueOf(rateDTO.getTime4()), 4));
            downData.append(ServerUtil.intToHex(Integer.valueOf(rateDTO.getDeviceType()), 4));
            downData.append(ServerUtil.intToHex(Integer.valueOf(rateDTO.getRateVersion()), 8));
            String downCmd = cmdWithService.CMDCom(rateDTO.getKey(), rateDTO.getAddr(), Long.valueOf(rateDTO.getCmdId()),
                    FuncEnum.FUNC_SEND_WASH_OR_BLOWER_RETA, downData.toString());
            log.info("下发洗衣 or 吹风机费率" + rateDTO.getProjectId() + " 命令:" + downCmd);
            MQTTCMDStatus cmdStatus = new MQTTCMDStatus();
            cmdStatus.setCmdTypeId(String.valueOf(FuncEnum.FUNC_SEND_WASH_OR_BLOWER_RETA.getCode()));
            cmdStatus.setCmdId(rateDTO.getCmdId());
            cmdStatus.setCreateTime(DateUtil.getFormatTime());
            cmdStatus.setPrjID(rateDTO.getProjectId());
            cmdStatus.setSncode(addr);
            redisService.hmSet(UsedConstant.CMD_REDIS_MARK + cmdStatus.getCmdId(),
                    MapConvertBaseUtil.object2Map(cmdStatus));
            MQTTPushClient client = MQTTPushClient.getInstance();

            client.publish(addr + UsedConstant.DEVICE_TOPIC_MARK, downCmd);
            return ResultUtil.success();

        } catch (Exception e) {
            e.printStackTrace();
            log.error("下发洗衣 or 吹风机费率异常", e);
        }

        return ResultUtil.error(ResultEnum.CMD_SEND_ERROR);
    }

    @Override
    public HttpResult setWaterID(String Key, String Addr, String ProID, String SetMode, String cmdId) {
        try {
            Addr = String.format("%12s", Addr).replace(' ', '0');
            String downData = ServerUtil.intToHex(Integer.valueOf(SetMode), 2);
            downData += ServerUtil.intToHex(Integer.valueOf(ProID), 8);
            downData += Addr;
            String time =  DateUtil.getFormatTimeTwelve();
            downData += time;
            String downCmd = cmdWithService.CMDCom(Key, Addr, Long.valueOf(cmdId), FuncEnum.FUNC_SET_WATER_ID, downData);
            log.info("设置水表机号项目" + ProID + "命令:" + downCmd);
            MQTTCMDStatus cmdStatus = new MQTTCMDStatus();
            cmdStatus.setCmdTypeId(String.valueOf(FuncEnum.FUNC_SET_WATER_ID.getCode()));
            cmdStatus.setCmdId(cmdId);
            cmdStatus.setCreateTime(DateUtil.getFormatTime());
            cmdStatus.setPrjID(ProID);
            cmdStatus.setSncode(Addr);
            redisService.hmSet(UsedConstant.CMD_REDIS_MARK + cmdStatus.getCmdId(),
                    MapConvertBaseUtil.object2Map(cmdStatus));


            MQTTPushClient client = MQTTPushClient.getInstance();

            client.publish(Addr + UsedConstant.DEVICE_TOPIC_MARK, downCmd);
            return ResultUtil.success();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("设置水表机号异常", e);
        }

        return ResultUtil.error(ResultEnum.CMD_SEND_ERROR);
    }

    @Override
    public HttpResult setWaterList(SetWaterListDTO waterListDTO) {

        try {
            String Addr = String.format("%12s", waterListDTO.getAddr()).replace(' ', '0');
            String[] array = waterListDTO.getDevAddrList().split(",");
            String downData = ServerUtil.intToHex(Integer.valueOf(1), 2);
            downData += ServerUtil.intToHex(Integer.valueOf(waterListDTO.getTotalDevCount()), 2);
            downData += ServerUtil.intToHex(Integer.valueOf(waterListDTO.getSetMode()), 2);
            for (int i = 0; i < Integer.valueOf(waterListDTO.getTotalDevCount()); i++) {
                downData += ServerUtil.intToHex(Integer.valueOf(array[i]), 4);
            }
            String downCmd = cmdWithService.CMDCom(waterListDTO.getKey(), Addr,
                    Long.valueOf(waterListDTO.getCmdId()), FuncEnum.FUNC_DELETE_DEVICE, downData);
            log.info("下发（删除）设备列表项目" + waterListDTO.getProID() + "命令:" + downCmd);

            MQTTCMDStatus cmdStatus = new MQTTCMDStatus();
            cmdStatus.setCmdTypeId(String.valueOf(FuncEnum.FUNC_DELETE_DEVICE.getCode()));
            cmdStatus.setCmdId(waterListDTO.getCmdId());
            cmdStatus.setCreateTime(DateUtil.getFormatTime());
            cmdStatus.setPrjID(waterListDTO.getProID());
            cmdStatus.setSncode(Addr);
            redisService.hmSet(UsedConstant.CMD_REDIS_MARK + cmdStatus.getCmdId(),
                    MapConvertBaseUtil.object2Map(cmdStatus));


            MQTTPushClient client = MQTTPushClient.getInstance();

            client.publish(Addr + UsedConstant.DEVICE_TOPIC_MARK, downCmd);
            return ResultUtil.success();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("下发（删除）设备列表异常", e);

        }
        return ResultUtil.error(ResultEnum.CMD_SEND_ERROR);
    }

    @Override
    public void deviceFail(WorkDataBO workData, String message) {
        if (workData.getWorkModeEnum().equals(WorkModeEnum.WORK_MODE_SEND)) {
            log.info(reponseService.getCmdString(workData));
            return;
        }
        String data = workData.getData();
        // 判断数据长度

        if (data.length() != UsedConstant.DEVICE_FAIL_DATA_LEN) {
            workData.setAnalyRes(CMDEnum.CMD_ERROR_DATA);
            log.info(reponseService.getCmdString(workData));
            reponseService.responseError(workData, null, workData.getAnalyRes());
            return;
        }

        try {
            long projectId = Integer.parseInt(data.substring(0, 8), 16);
            int deviceId = Integer.parseInt(data.substring(8, 16), 16);
            int waterState = Integer.parseInt(data.substring(16, 18), 16);
            int count = deviceDao.updateDeviceStatu(projectId, deviceId, waterState);
//            if (count < 1) {
//                workData.setAnalyRes(CMDEnum.CMD_ERROR_DATA);
//                log.error("设备异常上传数据失败  " + reponseService.getCmdString(workData));
//                reponseService.responseError(workData, null, workData.getAnalyRes());
//                return;
//            }
            //需要回复
            String downData = ServerUtil.intToHex(CMDEnum.CMD_SUCCESS.getCode(), 2);
            String downCmd = cmdWithService.CMDCom(workData.getKey(), workData.getAddr(), workData.getCmdId(), FuncEnum.getEnum(workData.getFunc()), downData);
            reponseService.responseOK(workData, downCmd);
            log.info(reponseService.getCmdString(workData));
        } catch (Exception e) {
            workData.setAnalyRes(CMDEnum.CMD_ERROR_DATA);
            log.error("设备异常上传数据异常   " + reponseService.getCmdString(workData));

            reponseService.responseError(workData, null, workData.getAnalyRes());
            e.printStackTrace();
            log.error("设备异常上传数据异常", e);
        }

    }

    @Override
    public void deleteDevice(WorkDataBO workData, String message) {
        if (workData.getWorkModeEnum().equals(WorkModeEnum.WORK_MODE_SEND)) {
            log.info(reponseService.getCmdString(workData));
            return;
        }
        String data = workData.getData();
        // 判断数据长度

        if (data.length() != UsedConstant.DELETE_DEVICE_DATA_LEN) {
            workData.setAnalyRes(CMDEnum.CMD_ERROR_DATA);
            log.info(reponseService.getCmdString(workData));
//            reponseService.responseError(workData, message, workData.getAnalyRes());
            return;
        }
        try {
            String result = data.substring(0, 2);


            long projectId = Integer.parseInt(data.substring(2, 10), 16);
            int waterId = Integer.parseInt(data.substring(10, 14), 16);
            int deviceId = Integer.parseInt(data.substring(14, 22), 16);
            int errState = Integer.parseInt(data.substring(22, 24), 16);
            if (UsedConstant.OPERATION_SUCCESS_MARK.equals(result)) {

                int count = deviceDao.deleteDevice(projectId, deviceId);
                if (count < 1) {
                    workData.setAnalyRes(CMDEnum.CMD_ERROR_DATA);
                    log.error("删除设备回复后修改数据库 " + reponseService.getCmdString(workData));
                    return;
                }
            } else {
                log.error("删除设备硬件失败：projectId:{},waterId:{},deviceId:{},errState:{} ", projectId, waterId, deviceId, errState);

            }


            reponseService.processSetCommon(workData);
        } catch (Exception e) {
            workData.setAnalyRes(CMDEnum.CMD_ERROR_DATA);
            log.error("删除设备回复   " + reponseService.getCmdString(workData));

//            reponseService.responseError(workData, message, workData.getAnalyRes());
            e.printStackTrace();
            log.error("删除设备回复", e);
        }
    }

    @Override
    public void deviceLogin(WorkDataBO workData, String message) {
        if (workData.getWorkModeEnum().equals(WorkModeEnum.WORK_MODE_SEND)) {
            log.info(reponseService.getCmdString(workData));
            return;
        }
        String data = workData.getData();
        // 判断数据长度

        if (data.length() < UsedConstant.LOGIN_DATA_LEN) {
            workData.setAnalyRes(CMDEnum.CMD_ERROR_DATA);
            log.error(reponseService.getCmdString(workData));
            reponseService.responseError(workData, null, workData.getAnalyRes());
            return;
        }

        // 获取密码
        String pwd = data.substring(0, 12);
        // 获取项目ID
        int pid = Integer.parseInt(data.substring(12, 20), 16);
        //获取项目SN
        String snCode = data.substring(20, 32);

        // 版本
        String softVer = data.substring(32,40);

        log.info(this.getClass().getSimpleName() + "WiLogin pid: " + pid + ";addr:"
                + workData.getAddr() + ";addrtype:"
                + DeviceTypeEnum.getEnum(workData.getStart()).getCode() + ";password:"
                + pwd);


        // 设置项目ID
        workData.setProjectId(pid);
        // 设置设备SN
        workData.setDeviceId(snCode);
        // 是正确的密码或是密码，作出正确的响应回复
//        reponseService.responseOK(workData, message);
        // 设置设备为在线状态
        deviceDao.updateDeviceStatus(WaterStatusEnum.ONLINE_STATUS.getCode(), workData.getAddr());

        // 第一次登录,密码全为F时，则下发密码
//			if (emptyPwd.equals(pwd)) {
//				responsePsw(workData, messagepub);
//			}

        StringBuilder downData = new StringBuilder();
        downData.append(ServerUtil.intToHex(CMDEnum.CMD_SUCCESS.getCode(), 2));
        downData.append(DateUtil.getFormatTimeTwelve());


        log.info("登录返回数据包：" +downData);


        String downCmd = cmdWithService.CMDCom(workData.getKey(), workData.getAddr(), workData.getCmdId(), FuncEnum.FUNC_DEVICE_LOGIN, downData.toString());
        MQTTPushClient client = MQTTPushClient.getInstance();

        client.publish(workData.getAddr() + UsedConstant.DEVICE_TOPIC_MARK, downCmd);
        log.info("WiLogin " + downData);


    }

    @Override
    public void deviceHeartbeat(WorkDataBO workData, String message) {
        if (workData.getWorkModeEnum().equals(WorkModeEnum.WORK_MODE_SEND)) {
            log.info(reponseService.getCmdString(workData));
            return;
        }
        String data = workData.getData();
        workData.setProjectId(Long.parseLong(data.substring(0, 8), 16));
        String keyId = "mqttDevStatus:" + workData.getAddr();
        Map object = redisService.hmGet(keyId);
        log.info(object.toString());
        MQTTDeviceStatus mqttDeviceStatus = null;
        if (object.size() > 0) {
            mqttDeviceStatus = (MQTTDeviceStatus) MapConvertBaseUtil.map2Object(object, MQTTDeviceStatus.class);
        }


        if (mqttDeviceStatus == null) {

            mqttDeviceStatus = new MQTTDeviceStatus();
            mqttDeviceStatus.setAddr(workData.getAddr());
            mqttDeviceStatus.setCreateTime(DateUtil.getFormatTime());
            mqttDeviceStatus.setKey(workData.getKey());
            mqttDeviceStatus.setPrjID(String.valueOf(workData.getProjectId()));
            mqttDeviceStatus.setStatus(WaterStatusEnum.ONLINE_STATUS.getCode());
            // 更新数据库状态为在线
            deviceDao.updateDeviceStatus(WaterStatusEnum.ONLINE_STATUS.getCode(), workData.getAddr());
        }
        // 更新redis最新在线时间
        mqttDeviceStatus.setUpTime(DateUtil.getFormatTime());
        Map map = MapConvertBaseUtil.object2Map(mqttDeviceStatus);
        redisService.hmSet(UsedConstant.DEVICE_REDIS_MARK + workData.getAddr(), map);

    }

    @Override
    public HttpResult initDevice(String key, String addr, String proID, String cmdId) {

        StringBuffer sb = null;

        try {
            addr = String.format("%12s", addr).replace(' ', '0');
            String downData = ServerUtil.intToHex(Integer.valueOf(proID), 8);
            String downCmd = cmdWithService.CMDCom(key, addr, Long.valueOf(cmdId), FuncEnum.FUNC_INIT_DEVICE, downData);
            log.info("初始化集中器" + proID + "命令:" + downCmd);
            MQTTCMDStatus cmdStatus = new MQTTCMDStatus();
            cmdStatus.setCmdTypeId(String.valueOf(FuncEnum.FUNC_INIT_DEVICE.getCode()));
            cmdStatus.setCmdId(cmdId);
            cmdStatus.setCreateTime(DateUtil.getFormatTime());
            cmdStatus.setPrjID(proID);
            cmdStatus.setSncode(addr);
            redisService.hmSet(UsedConstant.CMD_REDIS_MARK + cmdStatus.getCmdId(),
                    MapConvertBaseUtil.object2Map(cmdStatus));


            MQTTPushClient client = MQTTPushClient.getInstance();

            client.publish(addr + UsedConstant.DEVICE_TOPIC_MARK, downCmd);
            return ResultUtil.success();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("设置通讯密码异常", e);
        }
        return ResultUtil.error(ResultEnum.CMD_SEND_ERROR);
    }

    @Override
    public void setDeviceInfo(WorkDataBO dataBO, String message) {

        if (dataBO.getWorkModeEnum().equals(WorkModeEnum.WORK_MODE_SEND)) {
            log.info(reponseService.getCmdString(dataBO));
            return;
        }

        String data = dataBO.getData();
        // 判断数据长度

        if (data.length() != UsedConstant.SET_DEVICE_INFO_DATA_LEN) {
            dataBO.setAnalyRes(CMDEnum.CMD_ERROR_DATA);
            log.info(reponseService.getCmdString(dataBO));
            reponseService.responseError(dataBO, null, dataBO.getAnalyRes());
            return;
        }

        try {

            int projectId = Integer.parseInt(data.substring(0, 8), 16);
            int waterId = Integer.parseInt(data.substring(8, 12), 16);
            int deviceType = Integer.parseInt(data.substring(12, 16), 16);
            int state = Integer.parseInt(data.substring(16, 18), 16);
            message = data.substring(0, 8) +data.substring(8, 12);
            if (projectId == 0 || waterId == 0 || deviceType == 0) {
                dataBO.setAnalyRes(CMDEnum.CMD_ERROR_PARAMETER);
                log.error("设置设备信息数据校验异常  " + reponseService.getCmdString(dataBO));

                reponseService.responseError(dataBO, message, dataBO.getAnalyRes());
                return;
            }
            // 查看小类是否存在
            RateType rateType = rateDao.getRateInfo(projectId, deviceType);
            if (rateType == null) {
                dataBO.setAnalyRes(CMDEnum.CMD_ERROR_DEVICE_TYPE);
                log.error("设置设备信息设备类型错误: projectId: {} , deviceType: {}", projectId, deviceType);

                reponseService.responseError(dataBO, message, dataBO.getAnalyRes());
                return;
            }
            // 查看采集器 sn 是不是属于这个项目并且获取澡堂ID
            BathroomCollectDev collectDev = collectDevDao.getBathroomCollect(projectId, dataBO.getAddr());
            if (collectDev == null) {
                dataBO.setAnalyRes(CMDEnum.CMD_ERROR_PROJECT_SN);
                log.error("设置设备信息项目信息和SN不匹配错误: projectId: {} , sn: {}", projectId, dataBO.getAddr());
                reponseService.responseError(dataBO, message, dataBO.getAnalyRes());
                return;
            }

            DeviceInfo deviceInfo = deviceDao.getDeviceInfo(projectId, collectDev.getBathroomId(), waterId);
            // 判断机号是否被占用
            if (deviceInfo != null && state == 0) {
                dataBO.setAnalyRes(CMDEnum.CMD_ERROR_WATER_NO_REPEAT);
                log.error("设置设备信息机号重复: projectId: {} , sn: {} ,bathroomId: {}, waterNo: {}",
                        projectId, dataBO.getAddr(), collectDev.getBathroomId(), waterId);
                reponseService.responseError(dataBO, message, dataBO.getAnalyRes());
                return;
            }
            deviceInfo = new DeviceInfo();
            deviceInfo.setProjectId(projectId);
            deviceInfo.setAreaId(collectDev.getAreaId());
            deviceInfo.setBathroomId(collectDev.getBathroomId());
            deviceInfo.setDeviceType(rateType.getDeviceType());
            // 3 为澡堂设备
            deviceInfo.setTypeStatus(3);
            deviceInfo.setWaterNo(waterId);
            deviceInfo.setDescript(collectDev.getCollectName() + "-" + rateType.getName() + "-" + waterId + "号");
            deviceInfo.setName(collectDev.getBathroomName() + "-" + waterId + "号");
            log.info(deviceInfo.toString());
            int count = 0;
            if (state == 0) {
                count = deviceDao.createDeviceInfo(deviceInfo);
            } else {
                count = deviceDao.updateDeviceInfo(deviceInfo);
            }

            if (count <= 0) {
                dataBO.setAnalyRes(CMDEnum.CMD_FALSE);
                log.error("设置设备信息机号入库失败: " + deviceInfo.toString());
                reponseService.responseError(dataBO, message, dataBO.getAnalyRes());
                return;
            }
            deviceInfo = deviceDao.getDeviceInfo(projectId, collectDev.getBathroomId(), waterId);
            // 总表Mac = 集中器SN + 设备机号
            String macStr = dataBO.getAddr() + waterId;
            String macTwoStr = dataBO.getAddr().substring(0, 4);
            String macOneStr = macStr.substring(4);
            long macOne = Long.parseLong(macOneStr, 16);
            long macTwo = Long.parseLong(macTwoStr, 16);
            try {
                count = deviceDao.createDeviceAll(macOne, macTwo, projectId, deviceInfo.getDeviceId(), deviceInfo.getTypeStatus());
            } catch (Exception e) {
                log.info("修改设备登记信息 macOne:{},macTwo:{} ,projectId:{}", macOne, macTwo, projectId, deviceInfo.getDeviceId());
                deviceDao.updateDeviceAll(macOne, macTwo, projectId, deviceInfo.getDeviceId());
            }


            //需要回复 成功状态80 projectID  设备机号waterId 设备deviceId
            String downData = ServerUtil.intToHex(CMDEnum.CMD_SUCCESS.getCode(), 2);
            downData += ServerUtil.intToHex(projectId, 8);
            downData += ServerUtil.intToHex(waterId, 4);
            downData += ServerUtil.intToHex(deviceInfo.getDeviceId(), 8);

            String downCmd = cmdWithService.CMDCom(dataBO.getKey(), dataBO.getAddr(), dataBO.getCmdId(), FuncEnum.FUNC_SET_DEVICE_INFO, downData);

            MQTTPushClient client = MQTTPushClient.getInstance();

            client.publish(dataBO.getAddr() + UsedConstant.DEVICE_TOPIC_MARK, downCmd);
            log.info(reponseService.getCmdString(dataBO));
        } catch (Exception e) {
            dataBO.setAnalyRes(CMDEnum.CMD_ERROR_DATA);
            log.error("设置设备异常   " + reponseService.getCmdString(dataBO));
            reponseService.responseError(dataBO, message, dataBO.getAnalyRes());
            e.printStackTrace();
            log.error("设置设备异常", e);
        }

    }

    @Override
    public HttpResult deleteDevice(DeleteDeviceDTO deviceDTO) {


        try {
            String addr = String.format("%12s", deviceDTO.getAddr()).replace(' ', '0');
            String downData = ServerUtil.intToHex(Integer.valueOf(deviceDTO.getProID()), 8);
            downData += ServerUtil.intToHex(Integer.valueOf(deviceDTO.getWaterId()), 4);
            downData += ServerUtil.intToHex(Integer.valueOf(deviceDTO.getDeviceId()), 8);
            String downCmd = cmdWithService.CMDCom(deviceDTO.getKey(), addr,
                    Long.valueOf(deviceDTO.getCmdId()), FuncEnum.FUNC_DELETE_DEVICE, downData);
            log.info("删除设备" + deviceDTO.getDeviceId() + "命令:" + downCmd);
            MQTTCMDStatus cmdStatus = new MQTTCMDStatus();
            cmdStatus.setCmdTypeId(String.valueOf(FuncEnum.FUNC_DELETE_DEVICE.getCode()));
            cmdStatus.setCmdId(deviceDTO.getCmdId());
            cmdStatus.setCreateTime(DateUtil.getFormatTime());
            cmdStatus.setPrjID(deviceDTO.getProID());
            cmdStatus.setSncode(addr);
            redisService.hmSet(UsedConstant.CMD_REDIS_MARK + cmdStatus.getCmdId(),
                    MapConvertBaseUtil.object2Map(cmdStatus));


            MQTTPushClient client = MQTTPushClient.getInstance();

            client.publish(addr + UsedConstant.DEVICE_TOPIC_MARK, downCmd);
            return ResultUtil.success();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("删除设备异常", e);
        }
        return ResultUtil.error(ResultEnum.CMD_SEND_ERROR);
    }

    @Override
    public void getDeviceInfo(WorkDataBO dataBO, String message) {

        if (dataBO.getWorkModeEnum().equals(WorkModeEnum.WORK_MODE_SEND)) {
            log.info(reponseService.getCmdString(dataBO));
            return;
        }

        String data = dataBO.getData();
        // 判断数据长度

        if (data.length() != UsedConstant.GET_DEVICE_DATA_LEN) {
            dataBO.setAnalyRes(CMDEnum.CMD_ERROR_DATA);
            log.info(reponseService.getCmdString(dataBO));
            reponseService.responseError(dataBO, null, dataBO.getAnalyRes());
            return;
        }

        int projectId = Integer.parseInt(data.substring(0, 8), 16);
        int deviceId = Integer.parseInt(data.substring(8, 16), 16);
        int waterId = Integer.parseInt(data.substring(16, 20), 16);
        int deviceType = Integer.parseInt(data.substring(20, 24), 16);
        int state = 0;
        if (projectId != 0) {
            BathroomCollectDev  collectDev = collectDevDao.getBathroomCollect(projectId,dataBO.getAddr());
            if (collectDev ==null || collectDev.getIsSoftDelete() ==1){
                state = 1;
            }else {

                if (deviceId == 0) {
                    state = 3;
                } else {
                    DeviceInfo deviceInfo = deviceDao.getDeviceInfoById(projectId, deviceId);
                    if (deviceInfo == null) {
                        state = 3;
                    }else {
                        // 登记机号不匹配
                        if(deviceInfo.getWaterNo()!=waterId){
                            state =2;
                            waterId = deviceInfo.getWaterNo();
                        }else {
                            // 登记设备类型不匹配
                            if (deviceType!=deviceInfo.getDeviceType()){
                                state =4;
                                deviceType = deviceInfo.getDeviceType();
                            }
                        }
                        // 登记澡堂信息不匹配
//                        if (collectDev.getBathroomId()!=deviceInfo.getBathroomId()){
//                            state = 1;
//                        }
                    }
                }
            }

        }else {
            state =5;
        }

        //需要回复 成功状态80 projectID  设备机号waterId 设备deviceId
        String downData = ServerUtil.intToHex(CMDEnum.CMD_SUCCESS.getCode(), 2);
        downData += ServerUtil.intToHex(projectId, 8);
        downData += ServerUtil.intToHex(deviceId, 8);
        downData += ServerUtil.intToHex(waterId, 4);
        downData += ServerUtil.intToHex(deviceType, 4);
        downData += ServerUtil.intToHex(state,2);

        String downCmd = cmdWithService.CMDCom(dataBO.getKey(), dataBO.getAddr(), dataBO.getCmdId(), FuncEnum.FUNC_GET_DEVICE_INFO, downData);

        MQTTPushClient client = MQTTPushClient.getInstance();

        client.publish(dataBO.getAddr() + UsedConstant.DEVICE_TOPIC_MARK, downCmd);
        log.info(reponseService.getCmdString(dataBO));


    }

    @Override
    public HttpResult updateCollector(String key ,String projectId, String addr, String ip, int port, String username, String password, int fileName, String cmdId) {
        try {
            String downData = ServerUtil.intToHex(Integer.valueOf(projectId),8);
             downData  += ip;
            downData += ServerUtil.intToHex(port, 4);

            username = ServerUtil.strZeroFill(username,10);
            password = ServerUtil.strZeroFill(password,10);
            downData += ServerUtil.strToHex(username);
            downData +=ServerUtil.strToHex(password);

            downData += ServerUtil.intToHex(fileName,10);
            String downCmd = cmdWithService.CMDCom(key, addr,
                   Long.valueOf(cmdId), FuncEnum.FUNC_UPDATE_CONTROLLER, downData);
            log.info("更新设备" + addr + "命令:" + downCmd);
            MQTTCMDStatus cmdStatus = new MQTTCMDStatus();
            cmdStatus.setCmdTypeId(String.valueOf(FuncEnum.FUNC_UPDATE_CONTROLLER.getCode()));
            cmdStatus.setCmdId(cmdId);
            cmdStatus.setCreateTime(DateUtil.getFormatTime());
            cmdStatus.setPrjID(projectId);
            cmdStatus.setSncode(addr);
            redisService.hmSet(UsedConstant.CMD_REDIS_MARK + cmdStatus.getCmdId(),
                    MapConvertBaseUtil.object2Map(cmdStatus));


            MQTTPushClient client = MQTTPushClient.getInstance();

            client.publish(addr + UsedConstant.DEVICE_TOPIC_MARK, downCmd);
            return ResultUtil.success();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("删除设备异常", e);
        }
        return ResultUtil.error(ResultEnum.CMD_SEND_ERROR);
    }


}
