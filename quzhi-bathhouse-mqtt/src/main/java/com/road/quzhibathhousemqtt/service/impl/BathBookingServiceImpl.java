package com.road.quzhibathhousemqtt.service.impl;

import com.road.quzhibathhousemqtt.bo.UseCodeBO;
import com.road.quzhibathhousemqtt.bo.WorkDataBO;
import com.road.quzhibathhousemqtt.dao.BathBookingDao;
import com.road.quzhibathhousemqtt.dao.RateDao;
import com.road.quzhibathhousemqtt.entity.Account;
import com.road.quzhibathhousemqtt.entity.BathBooking;
import com.road.quzhibathhousemqtt.entity.RateType;
import com.road.quzhibathhousemqtt.enums.CMDEnum;
import com.road.quzhibathhousemqtt.service.BathBookingService;
import com.road.quzhibathhousemqtt.service.ComReponseService;
import com.road.quzhibathhousemqtt.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wenqi
 * @title: BathBookingServiceImpl
 * @projectName quzhi-bathhouse-mqtt
 * @description:
 * @date 2019/4/11下午4:33
 */
@Service
@Slf4j
public class BathBookingServiceImpl implements BathBookingService {

    @Autowired
    private ComReponseService reponseService;

    @Autowired
    private BathBookingDao bathBookingDao;

    @Autowired
    private OrderService orderService;



    @Override
    public void startUse(WorkDataBO workData, String message, UseCodeBO useCodeBO, Account account) {

        /**
         * 判断是否有预约
         */
        BathBooking bathBooking = bathBookingDao.getBookingByStop(useCodeBO.getProjectId(),
                useCodeBO.getBathroomId(),account.getAccountId());

        if (bathBooking ==null ){
            log.error("使用码没有预约信息,projectId:{} bathroomId:{} accountId:{}", useCodeBO.getProjectId(), useCodeBO.getBathroomId(),
                    account.getAccountId());
            workData.setAnalyRes(CMDEnum.CMD_ERROR_NOT_BOOKING);
            log.error("使用码未找到澡堂信息：" + reponseService.getCmdString(workData));
            reponseService.useResponseError(workData, useCodeBO.getDeviceId(),useCodeBO.getWaterId(), workData.getAnalyRes());
            return;
        }

        orderService.orderStart(workData, message, useCodeBO, account);

    }
}
