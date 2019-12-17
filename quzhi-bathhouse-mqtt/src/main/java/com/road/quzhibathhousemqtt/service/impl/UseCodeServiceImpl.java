package com.road.quzhibathhousemqtt.service.impl;

import com.road.quzhibathhousemqtt.bo.UseCodeBO;
import com.road.quzhibathhousemqtt.bo.WorkDataBO;
import com.road.quzhibathhousemqtt.constant.UsedConstant;
import com.road.quzhibathhousemqtt.dao.AccountDao;
import com.road.quzhibathhousemqtt.dao.BathroomCollectDevDao;
import com.road.quzhibathhousemqtt.dao.DeviceDao;
import com.road.quzhibathhousemqtt.entity.Account;
import com.road.quzhibathhousemqtt.entity.BathroomCollectDev;
import com.road.quzhibathhousemqtt.entity.DeviceInfo;
import com.road.quzhibathhousemqtt.entity.ProjectInfo;
import com.road.quzhibathhousemqtt.enums.CMDEnum;
import com.road.quzhibathhousemqtt.enums.UseModeEnum;
import com.road.quzhibathhousemqtt.enums.WorkModeEnum;
import com.road.quzhibathhousemqtt.service.*;
import com.road.quzhibathhousemqtt.util.AESUtil;
import com.road.quzhibathhousemqtt.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wenqi
 * @title: UseCodeServiceImpl
 * @projectName quzhi-bathhouse-mqtt
 * @description:
 * @date 2019/4/11上午10:33
 */
@Service
@Slf4j
public class UseCodeServiceImpl implements UseCodeService {

    @Autowired
    private CMDWithService cmdWithService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private ComReponseService reponseService;

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private BathroomCollectDevDao collectDevDao;

    @Autowired
    private BathBookingService bookingService;

    @Autowired
    private DeviceDao deviceDao;

    @Override
    public void useCode(WorkDataBO workData, String message) {


        if (workData.getWorkModeEnum().equals(WorkModeEnum.WORK_MODE_SEND)) {
            log.info(reponseService.getCmdString(workData));
            return;
        }
        String data = workData.getData();
        // 判断数据长度

        if (data.length() != UsedConstant.USE_CODE_DATA_LEN) {
            workData.setAnalyRes(CMDEnum.CMD_ERROR_DATA);
            log.error("使用码数据长度错误：" + reponseService.getCmdString(workData));
            reponseService.responseError(workData, null, workData.getAnalyRes());
            return;
        }
        // 设备时间戳 长度14
        long timestamp = Long.parseLong(workData.getData().substring(28, 42));
        int deviceId = Integer.parseInt(workData.getData().substring(8, 16), 16);
        int waterId =Integer.parseInt(workData.getData().substring(24, 28), 16);
        boolean validTime = DateUtil.comparisonTime(timestamp, UsedConstant.INTERVAL_TIME);
        if (!validTime) {
            log.error("使用码超时,timestamp:{} workData:{}", timestamp, reponseService.getCmdString(workData));
            workData.setAnalyRes(CMDEnum.CMD_ERROR_TIME_OUT);
            log.error("使用码数据长度错误：" + reponseService.getCmdString(workData));
            reponseService.useResponseError(workData, deviceId,waterId, workData.getAnalyRes());
            return;
        }
        UseCodeBO useCodeBO = new UseCodeBO();
        useCodeBO.setUseCode(workData.getData().substring(0, 8));
        useCodeBO.setDeviceId(deviceId);
        useCodeBO.setProjectId(Integer.parseInt(workData.getData().substring(16, 24), 16));
        useCodeBO.setWaterId(waterId);

        useCodeBO.setDeviceType(Integer.parseInt(workData.getData().substring(42, 46), 16));
        useCodeBO.setRateVersion(String.valueOf(Integer.parseInt(workData.getData().substring(46, 54), 16)));
        useCodeBO.setUseMode(Integer.parseInt(workData.getData().substring(54, 56)));
        /**
         * 判断是否有改
         */
        ProjectInfo projectInfo = accountDao.getProjectInfo(useCodeBO.getProjectId());
        if (projectInfo == null) {
            log.error("使用码项目ID错误,projectId:{}", useCodeBO.getProjectId());
            workData.setAnalyRes(CMDEnum.CMD_ERROR_NOT_PROJECT);
            log.error("使用码项目ID错误：" + reponseService.getCmdString(workData));
            reponseService.useResponseError(workData, useCodeBO.getDeviceId(),useCodeBO.getWaterId(), workData.getAnalyRes());
            return;
        }

        DeviceInfo deviceInfo = deviceDao.getDeviceInfoById(useCodeBO.getProjectId(),useCodeBO.getDeviceId());
        if (deviceInfo == null ) {
            log.error("使用码设备ID错误 projectId:{},deviceId:{}", useCodeBO.getProjectId(),useCodeBO.getDeviceId());
            workData.setAnalyRes(CMDEnum.CMD_ERROR_NOT_DEVICE);
            log.error("使用码设备ID错误：" + reponseService.getCmdString(workData));
            reponseService.useResponseError(workData, useCodeBO.getDeviceId(),useCodeBO.getWaterId(), workData.getAnalyRes());
            return;
        }
        BathroomCollectDev  collectDev = collectDevDao.getBathroomCollect(useCodeBO.getProjectId(),workData.getAddr());
        if (collectDev ==null || collectDev.getIsSoftDelete() ==1){
            log.error("使用码未找到澡堂信息,projectId:{} sn:{} isSoftDelete:{}", useCodeBO.getProjectId(), workData.getAddr(),
                    collectDev.getIsSoftDelete());
            workData.setAnalyRes(CMDEnum.CMD_ERROR_PROJECT_SN);
            log.error("使用码未找到澡堂信息：" + reponseService.getCmdString(workData));
            reponseService.useResponseError(workData, useCodeBO.getDeviceId(),useCodeBO.getWaterId(), workData.getAnalyRes());
            return;
        }
        useCodeBO.setControllerId(collectDev.getId());
        useCodeBO.setBathroomId(collectDev.getBathroomId());
        if (deviceInfo.getBathroomId()==0|| deviceInfo.getBathroomId()!=useCodeBO.getBathroomId()){
            log.error("使用码设备澡堂信息错误 projectId:{},deviceId:{},bathroomId:{},useBathroomId:{}", useCodeBO.getProjectId(),useCodeBO.getDeviceId(),deviceInfo.getBathroomId(),useCodeBO.getBathroomId());
            workData.setAnalyRes(CMDEnum.CMD_ERROR_NOT_DEVICE);
            log.error("使用码设备澡堂信息错误：" + reponseService.getCmdString(workData));
            reponseService.useResponseError(workData, useCodeBO.getDeviceId(),useCodeBO.getWaterId(), workData.getAnalyRes());
            return;
        }


        String bathPassword = AESUtil.getInstance().encode(useCodeBO.getUseCode(),
                UsedConstant.USE_CODE_KEY);
        Account account = accountDao.getAccountByBathCode(useCodeBO.getProjectId(), bathPassword);
        if (account == null) {
            log.error("使用码未找到用户 projectId:{},bathPassword:{} useCode:{}",useCodeBO.getProjectId(), bathPassword, useCodeBO.getUseCode());
            workData.setAnalyRes(CMDEnum.CMD_ERROR_USE_CODE);
            log.error("使用码未找到用户：" + reponseService.getCmdString(workData));
            reponseService.useResponseError(workData, useCodeBO.getDeviceId(),useCodeBO.getWaterId(), workData.getAnalyRes());
            return;
        }
        if (account.getIsBathPassword() == 0) {
            log.error("使用码禁用 projectId:{},bathPassword:{} accountId:{}",useCodeBO.getProjectId(), bathPassword, account.getAccountId());
            workData.setAnalyRes(CMDEnum.CMD_ERROR_USE_CODE);
            log.error("使用码禁用：" + reponseService.getCmdString(workData));
            reponseService.useResponseError(workData, useCodeBO.getDeviceId(),useCodeBO.getWaterId(), workData.getAnalyRes());
            return;
        }

        UseModeEnum useModeEnum = UseModeEnum.getEnum(useCodeBO.getUseMode());

        switch (useModeEnum) {
            case WATER_MODE:

                // 不需要预约码
                if (projectInfo.getIsBathBooking()==0){

                }else {

                    bookingService.startUse(workData,message,useCodeBO,account);

                }
                break;
            case NOE_MODE:
            case TWO_MODE:
            case THREE_MODE:
            case FOUR_MODE:
                break;
            default:
                log.error("使用模式错误 useMode:{}" + useModeEnum.getCode());
                workData.setAnalyRes(CMDEnum.CMD_ERROR_USE_MODE);
                log.error("使用码模式错误：" + reponseService.getCmdString(workData));
                reponseService.useResponseError(workData, useCodeBO.getDeviceId(),useCodeBO.getWaterId(), workData.getAnalyRes());
                return;
        }


    }
}
