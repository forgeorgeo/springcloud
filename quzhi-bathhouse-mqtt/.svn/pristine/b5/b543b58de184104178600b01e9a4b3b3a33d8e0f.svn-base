package com.road.quzhibathhousemqtt.task;

import com.road.quzhibathhousemqtt.bo.MQTTCMDStatus;
import com.road.quzhibathhousemqtt.bo.MQTTDeviceStatus;
import com.road.quzhibathhousemqtt.constant.UsedConstant;
import com.road.quzhibathhousemqtt.dao.BathCmdDao;
import com.road.quzhibathhousemqtt.dao.DeviceDao;
import com.road.quzhibathhousemqtt.entity.BathCmd;
import com.road.quzhibathhousemqtt.enums.BathCmdStatusEnum;
import com.road.quzhibathhousemqtt.enums.CMDEnum;
import com.road.quzhibathhousemqtt.enums.WaterStatusEnum;
import com.road.quzhibathhousemqtt.service.RedisService;
import com.road.quzhibathhousemqtt.util.DateUtil;
import com.road.quzhibathhousemqtt.util.MapConvertBaseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author wenqi
 * @Title: MQTTTimingTask
 * @ProjectName quzhi-bathhouse-mqtt
 * @Description:
 * @date 2019/2/28上午9:39
 */
@Slf4j
@Component
public class MQTTTimingTask {

    @Autowired
    private DeviceDao deviceDao;

    @Autowired
    private BathCmdDao cmdDao;


    @Autowired
    private RedisService redisService;


    /**
     * 项目启动5秒后执行命令超时检查任务
     */
    public final static int CMD_TIMEOUT_TASK_FIXED_DELAY = 5000;

    /**
     * 每隔180秒执行一下命令超时检查任务
     */
    public final static int CMD_TIMEOUT_TASK_FIXED_RATE = 180000;

    /**
     * 项目启动8秒后执行 设备离线任务
     */
    public final static int DEVICE_STATUS_TASK_FIXED_DELAY = 8000;

    /**
     * 检测设备是否已在线
     */
    public final static int DEVICE_STATUS_ONLINE_TASK_FIXED_RATE = 60000;

    /**
     * 每隔180秒执行一下设备离线任务
     */
    public final static int DEVICE_STATUS_TASK_FIXED_RATE = 180000;

    @Scheduled(fixedRate = CMD_TIMEOUT_TASK_FIXED_RATE)
    public void cmdTimeOutCheckingTask() {
        log.info("执行cmd超时检查任务");
        // 获取当前时间3分钟之前时间 格式20190109160059
        String sTime = DateUtil.getAfterMinute("-3");
        // 从redis取发送参数并发送
        Set<String> redisKeys = redisService.getKeys(UsedConstant.CMD_REDIS_MARK +"*");
        log.info("redis:"+redisKeys.size());
        for (String oneKey : redisKeys) {
            log.info("oneKey->" + oneKey);
            Map map =redisService.hmGet(oneKey);
            MQTTCMDStatus mqttCmdStatus = (MQTTCMDStatus)  MapConvertBaseUtil.map2Object(map,MQTTCMDStatus.class);

            String upTime = mqttCmdStatus.getCreateTime();
            // 比较最后更新时间与当前时间是否小于3分钟
            if (upTime != null && Long.valueOf(upTime) < Long.valueOf(sTime)) {
                try {
                    // 查询数据库是否有该集中控制器，没有的话清除redis,如果状态已经为离线则不执行更新

                    BathCmd cmd = new BathCmd();
                    cmd.setDealtype(BathCmdStatusEnum.TIME_OUT.getCode());
                    cmd.setDealcmdrlt("超时");
                    cmd.setRecid(Long.parseLong(mqttCmdStatus.getCmdId()));

                    int ret = cmdDao.updateCmdTimeOutStatus(cmd);


                    log.info("定时执行更新下发命令状态为超时" + (ret == 1 ? "成功" : "失败"));

                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("定时执行更新下发命令状态为超时异常", e);
                }
                // 更新数据库状态为离线时清除redis
                redisService.remove(oneKey);
            }

        }
        log.info(redisKeys.toString()+" 未超时");
    }

    @Scheduled(fixedRate = DEVICE_STATUS_TASK_FIXED_RATE)
    public void deviceStatusCheckingTask() {
        log.info("执行设备状态检查");
        // 获取当前时间3分钟之前时间 格式20190109160059
        String sTime = DateUtil.getAfterMinute("-3");
        Set<String> redisKeys = redisService.getKeys(UsedConstant.DEVICE_REDIS_MARK +"*");
        log.info("redis:"+redisKeys.size());
        for (String oneKey : redisKeys) {
            log.info("oneKey->" + oneKey);
            Map map =redisService.hmGet(oneKey);

            MQTTDeviceStatus mqttDevStatus = (MQTTDeviceStatus) MapConvertBaseUtil.map2Object(map,MQTTDeviceStatus.class);

            String upTime = mqttDevStatus.getUpTime();
            // 比较最后更新时间与当前时间是否小于3分钟
            if (upTime != null && Long.valueOf(upTime) < Long.valueOf(sTime)) {
                // 更新数据库设置状态为离线
                try {
                    // 查询数据库是否有该集中控制器，没有的话清除redis,如果状态已经为离线则不执行更新
                    String addr = mqttDevStatus.getAddr();
                    @SuppressWarnings("unchecked")
                    HashMap<String, Object> retMap = (HashMap<String, Object>) deviceDao.queryDevStatus(addr);

                    //数据库有该集中控制器
                    if (retMap != null) {

                        int ret = deviceDao.updateDeviceStatus(WaterStatusEnum.OFFLINE_STATUS.getCode(), addr);

                        int ret1 = 0;
                        //项目ID不为0，防止未设置过的集中控制器
                        if (!"0".equals(mqttDevStatus.getPrjID())) {
                            ret1 = deviceDao.updateConcentratorWaterStatus(mqttDevStatus.getPrjID(), WaterStatusEnum.OFFLINE_STATUS.getCode(), addr);
                        }

                        log.info("定时执行更新集中控制器状态为离线" + (ret == 1 ? "成功" : "失败"));
                        log.info("定时执行更新集中控制器下水表状态为离线" + (ret1 == 1 ? "成功" : "失败"));
                    }
                    redisService.remove(oneKey);
                    log.info("该集中控制器已经删除，清除redis");

                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("定时执行更新设备状态为离线异常", e);
                }
            }
        }
        log.info(redisKeys.toString()+" 未超时");
    }





}
