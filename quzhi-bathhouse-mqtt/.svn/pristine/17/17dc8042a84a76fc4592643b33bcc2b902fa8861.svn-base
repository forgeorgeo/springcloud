package com.road.quzhibathhousemqtt.service.impl;

import com.road.quzhibathhousemqtt.bo.WorkDataBO;
import com.road.quzhibathhousemqtt.constant.UsedConstant;
import com.road.quzhibathhousemqtt.dao.BathCmdDao;
import com.road.quzhibathhousemqtt.dao.DeviceDao;
import com.road.quzhibathhousemqtt.entity.BathCmd;
import com.road.quzhibathhousemqtt.enums.BathCmdStatusEnum;
import com.road.quzhibathhousemqtt.service.CMDService;
import com.road.quzhibathhousemqtt.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wenqi
 * @Title: CMDServiceImpl
 * @ProjectName quzhi-bathhouse-mqtt
 * @Description:
 * @date 2019/2/25下午1:41
 */
@Slf4j
@Service
public class CMDServiceImpl implements CMDService {

    @Autowired
    private DeviceDao deviceDao;

    @Autowired
    private BathCmdDao cmdDao;

    @Autowired
    private RedisService redisService;

    @Override
    public boolean updateDeviceStatus(WorkDataBO workData) {

        int count = deviceDao.updateDeviceStatus(0, workData.getAddr());

        return count == 1 ? true : false;

    }


    @Override
    public void updateCmdResult(WorkDataBO workData, String terminal, String value) {
        BathCmd bathCmd = new BathCmd();
        bathCmd.setRecid(workData.getCmdId());

        // 操作完成
        bathCmd.setDealtype(BathCmdStatusEnum.OPERATION_COMPLETED.getCode());

        if (UsedConstant.OPERATION_SUCCESS_MARK.equals(workData.getData().substring(0, 2))) {
            bathCmd.setErrcode("0");
            bathCmd.setDealcmdrlt("操作完成");

        } else {
            bathCmd.setErrcode("-1");
            bathCmd.setDealcmdrlt("硬件操作失败");
        }
        // 操作完成
        bathCmd.setDealtype(BathCmdStatusEnum.OPERATION_COMPLETED.getCode());

        cmdDao.updateCmdResult(bathCmd);
        redisService.remove(UsedConstant.CMD_REDIS_MARK + workData.getCmdId());
    }

    @Override
    public String getCommPassword(int projectId, String addr, int deviceTypeId) {

        String password = deviceDao.getCommPassword(projectId);
        return password;
    }

    @Override
    public void updateCmdResult(int projectId, long cmdId, int func, int type, String addr, String terminal, String value) {
        BathCmd bathCmd = new BathCmd();
        bathCmd.setRecid(cmdId);


        if (UsedConstant.OPERATION_SUCCESS_MARK.equals(value.substring(0, 2))) {
            bathCmd.setDealcmdrlt("操作完成");
            bathCmd.setErrcode("0");
        } else {
            bathCmd.setErrcode("-1");
            bathCmd.setDealcmdrlt("硬件操作失败");
        }
        // 操作完成
        bathCmd.setDealtype(BathCmdStatusEnum.OPERATION_COMPLETED.getCode());

        cmdDao.updateCmdResult(bathCmd);
        redisService.remove(UsedConstant.CMD_REDIS_MARK + cmdId);
    }
}
