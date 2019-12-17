package com.road.quzhibathhousemqtt.service.impl;

import com.alibaba.fastjson.JSON;
import com.road.quzhibathhousemqtt.bo.*;
import com.road.quzhibathhousemqtt.constant.UsedConstant;
import com.road.quzhibathhousemqtt.dao.*;
import com.road.quzhibathhousemqtt.dto.*;
import com.road.quzhibathhousemqtt.entity.Account;
import com.road.quzhibathhousemqtt.entity.RateType;
import com.road.quzhibathhousemqtt.entity.WithholdingMoney;
import com.road.quzhibathhousemqtt.enums.*;
import com.road.quzhibathhousemqtt.mqtt.MQTTPushClient;
import com.road.quzhibathhousemqtt.service.CMDWithService;
import com.road.quzhibathhousemqtt.service.ComReponseService;
import com.road.quzhibathhousemqtt.service.OrderService;
import com.road.quzhibathhousemqtt.service.RedisService;
import com.road.quzhibathhousemqtt.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author wenqi
 * @Title: OrderServiceImpl
 * @ProjectName quzhi-bathhouse-mqtt
 * @Description:
 * @date 2019/2/22下午4:57
 */
@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private CMDWithService cmdWithService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private ComReponseService reponseService;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private RateDao rateDao;

    @Autowired
    private WithholdingMoneyDao withholdingMoneyDao;

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private BathBookingDao bathBookingDao;

    @Autowired
    private DeviceDao deviceDao;


    @Override
    public HttpResult remoteCloseOrder(RemoteCloseOrderDTO closeOrderDTO) {


        try {
            String addr = String.format("%12s", closeOrderDTO.getAddr()).replace(' ', '0');
            StringBuilder downData = new StringBuilder();
            downData.append(closeOrderDTO.getTimeIds());
            downData.append(ServerUtil.intToHex(Integer.valueOf(closeOrderDTO.getProID()), 8));
            downData.append(ServerUtil.intToHex(Integer.valueOf(closeOrderDTO.getAccountId()), 8));
            String downCmd = cmdWithService.CMDCom(closeOrderDTO.getKey(), closeOrderDTO.getAddr(),
                    Long.valueOf(closeOrderDTO.getCmdId()), FuncEnum.FUNC_REMOTE_CLOSE_ORDER,
                    downData.toString());
            log.info("远程关闭订单项目" + closeOrderDTO.getProID() + "命令:" + downCmd);
            MQTTCMDStatus cmdStatus = new MQTTCMDStatus();
            cmdStatus.setPrjID(closeOrderDTO.getProID());
            cmdStatus.setCmdTypeId(FuncEnum.FUNC_REMOTE_CLOSE_ORDER.getCode().toString());
            cmdStatus.setCmdId(closeOrderDTO.getCmdId());
            cmdStatus.setCreateTime(DateUtil.getFormatTime());
            cmdStatus.setSncode(addr);

            redisService.hmSet(UsedConstant.CMD_REDIS_MARK + cmdStatus.getCmdId(),
                    MapConvertBaseUtil.object2Map(cmdStatus));


            MQTTPushClient client = MQTTPushClient.getInstance();

            client.publish(closeOrderDTO.getAddr() + UsedConstant.DEVICE_TOPIC_MARK, downCmd);
            return ResultUtil.success();

        } catch (Exception e) {
            e.printStackTrace();
            log.error("远程关闭订单异常", e);

        }

        return ResultUtil.error(ResultEnum.CMD_SEND_ERROR);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void orderStart(WorkDataBO workData, String message, UseCodeBO useCodeBO, Account account) {
        log.info(useCodeBO.toString());
        RateType rateType = rateDao.getRateInfo(useCodeBO.getProjectId(), useCodeBO.getDeviceType());
        if (rateType == null) {
            log.error("使用码设备类型错误,projectId:{} bathroomId:{} deviceType:{}", useCodeBO.getProjectId(), useCodeBO.getBathroomId(),
                    useCodeBO.getDeviceType());
            workData.setAnalyRes(CMDEnum.CMD_ERROR_DEVICE_TYPE);
            log.error("使用码设备类型错误：" + reponseService.getCmdString(workData));
            reponseService.useResponseError(workData, useCodeBO.getDeviceId(), useCodeBO.getWaterId(), workData.getAnalyRes());
            return;
        }

        WithholdingMoney withholdingMoney = withholdingMoneyDao.getWithholdingMoney(useCodeBO.getProjectId(),
                rateType.getDeviceBigType());
        if (withholdingMoney == null) {
            log.error("使用码设备大类获取预扣金额错误,projectId:{} bathroomId:{} deviceBigType:{}", useCodeBO.getProjectId(), useCodeBO.getBathroomId(),
                    rateType.getDeviceBigType()
            );
            workData.setAnalyRes(CMDEnum.CMD_ERROR_DEVICE_TYPE);
            log.error("使用码设备大类获取预扣金额错误：" + reponseService.getCmdString(workData));
            reponseService.useResponseError(workData, useCodeBO.getDeviceId(), useCodeBO.getWaterId(), workData.getAnalyRes());
            return;
        }

        long money = account.getRealMoney() + account.getGivenMoney();
        if (money <= 0) {
            log.error("使用码预扣金额不足,projectId:{} accountId:{} money:{} withholdingMoney:{}", useCodeBO.getProjectId(), account.getAccountId(),
                    money, withholdingMoney.getMoney()
            );
            workData.setAnalyRes(CMDEnum.CMD_ERROR_MONEY_NOT_ENOUGH);
            log.error("使用码预扣金额不足：" + reponseService.getCmdString(workData));
            reponseService.useResponseError(workData, useCodeBO.getDeviceId(), useCodeBO.getWaterId(), workData.getAnalyRes());
            return;
        }
        long perMoney = 0;
        if (money < withholdingMoney.getMoney()) {
             perMoney = money;
        }else {
             perMoney = withholdingMoney.getMoney();
        }

        long realMoney = account.getRealMoney();
        long givenMoney = account.getGivenMoney();
        // 扣除现金  和赠送金额
        long withholdingReal = 0;
        long withholdingGiven = 0;
        if (realMoney >= perMoney) {
            realMoney = realMoney - perMoney;
            withholdingReal = perMoney;
        } else {
            realMoney = realMoney - perMoney;
            givenMoney = givenMoney + realMoney;
            // 所扣除现金和赠送金额
            withholdingGiven = Math.abs(realMoney);
            withholdingReal  = perMoney + realMoney;
            realMoney = 0;
        }

        int count = accountDao.updateAccountMoney(useCodeBO.getProjectId(),
                account.getAccountId(), realMoney, givenMoney);

        if (count <= 0) {
            log.error("使用码修改账户金额失败,projectId:{} accountId:{} realMoney:{} givenMoney:{}", useCodeBO.getProjectId(), account.getAccountId(),
                    realMoney, givenMoney
            );
            workData.setAnalyRes(CMDEnum.CMD_FALSE);
            log.error("使用码修改账户金额失败：" + reponseService.getCmdString(workData));
            reponseService.useResponseError(workData, useCodeBO.getDeviceId(), useCodeBO.getWaterId(), workData.getAnalyRes());
            return;
        }

        String ConsumeDT = DateUtil.getFormatTime();
        String orderId = "10" + ConsumeDT + DateUtil.getRandom4();
        CreateOrderBO orderBO = new CreateOrderBO();
        orderBO.setOrderId(orderId);
        orderBO.setConsumeDT(ConsumeDT);
        orderBO.setProjectId(useCodeBO.getProjectId());
        orderBO.setAccountId(account.getAccountId());
        orderBO.setDeviceId(useCodeBO.getDeviceId());
        orderBO.setBefMoneyReal(account.getRealMoney());
        orderBO.setBefMoneyGiven(account.getGivenMoney());
        orderBO.setPerMoneyReal(withholdingReal);
        orderBO.setPerMoneyGiven(withholdingGiven);
        orderBO.setPerMoney(perMoney);
        orderBO.setIsbath(1);
        orderBO.setBathroomId(useCodeBO.getBathroomId());
        orderBO.setSnCode(workData.getAddr());
        orderBO.setControllerId(useCodeBO.getControllerId());
        count = orderDao.createOrder(orderBO);
        if (count <= 0) {
            log.error("使用码创建订单失败" + orderBO.toString()
            );
            workData.setAnalyRes(CMDEnum.CMD_FALSE);
            log.error("使用码创建订单失败：" + reponseService.getCmdString(workData));
            reponseService.useResponseError(workData, useCodeBO.getDeviceId(), useCodeBO.getWaterId(), workData.getAnalyRes());

            throw new RuntimeException("订单创建数据入库异常");
        }
        count = bathBookingDao.updateOrderStart(useCodeBO.getProjectId(), useCodeBO.getBathroomId(),
                account.getAccountId(), ConsumeDT, orderId);
        if (count <= 0) {
            log.error("使用码，修改预约状态失败 projectId:{}, bathroomId:{} accountId:{} consumDt:{} orderId:{}",
                    useCodeBO.getProjectId(), useCodeBO.getBathroomId(),
                    account.getAccountId(), ConsumeDT, orderId
            );
            workData.setAnalyRes(CMDEnum.CMD_FALSE);
            log.error("使用码，修改预约状态失败：" + reponseService.getCmdString(workData));
            reponseService.useResponseError(workData, useCodeBO.getDeviceId(), useCodeBO.getWaterId(), workData.getAnalyRes());

            throw new RuntimeException("使用码，修改预约状态失败");
        }
        deviceDao.updateDeviceStatu(useCodeBO.getProjectId(), useCodeBO.getDeviceId(), 3);
        //回复
        String downData = ServerUtil.intToHex(CMDEnum.CMD_SUCCESS.getCode(), 2);
        downData += ServerUtil.intToHex(useCodeBO.getDeviceId(), 8);
        downData += orderId;
        downData += ServerUtil.intToHex(useCodeBO.getProjectId(), 8);
        downData += ServerUtil.intToHex(account.getAccountId(), 8);
        //只有普通用户才会预约
        downData += ServerUtil.intToHex(2, 2);
        downData += ServerUtil.intToHex(orderBO.getPerMoney(), 8);
        downData += ConsumeDT;
        String downCmd = cmdWithService.CMDCom(workData.getKey(), workData.getAddr(), workData.getCmdId(), FuncEnum.getEnum(workData.getFunc()), downData);

        MQTTPushClient client = MQTTPushClient.getInstance();

        client.publish(workData.getAddr() + UsedConstant.DEVICE_TOPIC_MARK, downCmd);
        log.info(reponseService.getCmdString(workData));

    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void uploadOrderData(WorkDataBO workData, String message) {

        if (workData.getWorkModeEnum().equals(WorkModeEnum.WORK_MODE_SEND)) {
            log.info(reponseService.getCmdString(workData));
            return;
        }
        String data = workData.getData();
        // 判断数据长度

        if (data.length() != UsedConstant.UPLOAD_ORDER_DATA_LEN) {
            workData.setAnalyRes(CMDEnum.CMD_ERROR_DATA);
            log.info(reponseService.getCmdString(workData));
            reponseService.responseError(workData, null, workData.getAnalyRes());
            return;
        }

        String timeIds = workData.getData().substring(12, 32);
        try {
            UploadOrderBO orderBO = updateOrderSplit(workData);

            CreateOrderBO createOrderBO = orderDao.getOrderInfo(orderBO.getProjectId(), orderBO.getAccountId(), orderBO.getOrderId());
            if (createOrderBO == null) {
                workData.setAnalyRes(CMDEnum.CMD_ERROR_NOT_ORDER);

                log.error("订单入库未找到订单  " + reponseService.getCmdString(workData));

                reponseService.orderResponseError(workData, timeIds, workData.getAnalyRes());
                return;
            }
            if (createOrderBO.getUpStatusId() == 1) {
                workData.setAnalyRes(CMDEnum.CMD_ERROR_ORDER_UPLOADED);

                log.error("订单入库订单已支付  " + reponseService.getCmdString(workData));

                reponseService.orderResponseError(workData, timeIds, workData.getAnalyRes());
                return;
            }


            long upMoney = orderBO.getAmount();
            long upLeadMoney = createOrderBO.getPerMoney() - upMoney;

            long upLeadMoneyReal = 0;
            long UpLeadMoneyGiven = 0;
            if (upLeadMoney >= 0) {
                if (createOrderBO.getPerMoneyReal() >= upMoney) {
                    upLeadMoneyReal = createOrderBO.getPerMoneyReal() - upMoney;
                    UpLeadMoneyGiven = createOrderBO.getPerMoneyGiven();

                } else {
                    upLeadMoneyReal = upMoney - createOrderBO.getPerMoneyReal();
                    UpLeadMoneyGiven = createOrderBO.getPerMoneyGiven() - upLeadMoneyReal;
                    upLeadMoneyReal = 0;
                }
            } else {
                workData.setAnalyRes(CMDEnum.CMD_ERROR_YK_LT_AMOUNT);
                log.error("订单入库金额大于预扣金额  " + reponseService.getCmdString(workData));
                reponseService.orderResponseError(workData, timeIds, workData.getAnalyRes());
                return;
            }
            orderBO.setUpMoney(orderBO.getAmount());
            orderBO.setUpLeadMoney(upLeadMoney);
            orderBO.setUpLeadMoneyReal(upLeadMoneyReal);
            orderBO.setUpLeadMoneyGiven(UpLeadMoneyGiven);
            int count = orderDao.uploadOrder(orderBO);
            if (count <= 0) {
                workData.setAnalyRes(CMDEnum.CMD_FALSE);

                log.error("订单入库插入失败 " + reponseService.getCmdString(workData));

                reponseService.orderResponseError(workData, timeIds, workData.getAnalyRes());
                return;
            }

            if (upLeadMoneyReal > 0 || UpLeadMoneyGiven > 0) {
                Account account = accountDao.getAccountById(orderBO.getProjectId(), orderBO.getAccountId());
                long realMoney = account.getRealMoney() + upLeadMoneyReal;
                long givenMoney = account.getGivenMoney() + UpLeadMoneyGiven;
                count = accountDao.updateAccountMoney(orderBO.getProjectId(), orderBO.getAccountId(), realMoney, givenMoney);
                if (count <= 0) {
                    workData.setAnalyRes(CMDEnum.CMD_FALSE);

                    log.error("修改账号金额失败 " + reponseService.getCmdString(workData));

                    reponseService.orderResponseError(workData, timeIds, workData.getAnalyRes());
                    throw new RuntimeException("订单入库 修改账号金额失败");
                }
            }

            count = bathBookingDao.updateEndStatus(orderBO.getProjectId(), orderBO.getOrderId(), orderBO.getAccountId());

            if (count <= 0) {
                workData.setAnalyRes(CMDEnum.CMD_FALSE);

                log.error("修改预约码状态失败 " + reponseService.getCmdString(workData));

                reponseService.orderResponseError(workData, timeIds, workData.getAnalyRes());
                throw new RuntimeException("订单入库 修改预约状态失败");
            }

            deviceDao.updateDeviceStatu(orderBO.getProjectId(), orderBO.getDeviceId(), 0);


            reponseService.orderResponseOK(workData, timeIds);

        } catch (Exception e) {
            workData.setAnalyRes(CMDEnum.CMD_ERROR_DATA);

            log.error("入库失败  " + reponseService.getCmdString(workData));

            reponseService.orderResponseError(workData, timeIds, workData.getAnalyRes());

            log.error("上传消费数据异常", e);
            throw new RuntimeException("订单入库失败");
        }


    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void uploadUnusualOrderData(WorkDataBO workData, String message) {
        log.info("异常订单数据:"+reponseService.getCmdString(workData));
        if (workData.getWorkModeEnum().equals(WorkModeEnum.WORK_MODE_SEND)) {
            log.info(reponseService.getCmdString(workData));
            return;
        }
        String data = workData.getData();
        // 判断数据长度

        if (data.length() != UsedConstant.UNUSUAL_UPLOAD_ORDER_DATA_LEN) {
            workData.setAnalyRes(CMDEnum.CMD_ERROR_DATA);
            log.info(reponseService.getCmdString(workData));
            reponseService.responseError(workData, null, workData.getAnalyRes());
            return;
        }
        String responseMessage = workData.getData().substring(0, 8);
        responseMessage += workData.getData().substring(8, 12);
        responseMessage += workData.getData().substring(12, 20);
        try {
            UploadUnusualOrderBO orderBO = unusualOrderSplit(workData);

            CreateOrderBO createOrderBO = orderDao.getOrderInfoByDeviceId(orderBO.getProjectId(), orderBO.getDeviceId());


            if (createOrderBO == null) {
                workData.setAnalyRes(CMDEnum.CMD_ERROR_NOT_ORDER);
                log.error("异常订单入库未找到订单  " + reponseService.getCmdString(workData));
                reponseService.orderResponseError(workData, responseMessage, workData.getAnalyRes());
                return;
            }
            if (createOrderBO.getUpStatusId() == 1) {
                workData.setAnalyRes(CMDEnum.CMD_ERROR_ORDER_UPLOADED);
                log.error("异常订单入库订单已支付  " + reponseService.getCmdString(workData));
                reponseService.orderResponseError(workData, responseMessage, workData.getAnalyRes());
                return;
            }


            long upMoney = orderBO.getAmount();
            long upLeadMoney = createOrderBO.getPerMoney() - upMoney;

            long upLeadMoneyReal = 0;
            long UpLeadMoneyGiven = 0;
            if (upLeadMoney >= 0) {
                if (createOrderBO.getPerMoneyReal() >= upMoney) {
                    upLeadMoneyReal = createOrderBO.getPerMoneyReal() - upMoney;
                    UpLeadMoneyGiven = createOrderBO.getPerMoneyGiven();

                } else {
                    upLeadMoneyReal = upMoney - createOrderBO.getPerMoneyReal();
                    UpLeadMoneyGiven = createOrderBO.getPerMoneyGiven() - upLeadMoneyReal;
                    upLeadMoneyReal = 0;
                }
            } else {
                workData.setAnalyRes(CMDEnum.CMD_ERROR_YK_LT_AMOUNT);
                log.error("异常订单入库金额大于预扣金额  " + reponseService.getCmdString(workData));
                reponseService.orderResponseError(workData, responseMessage, workData.getAnalyRes());
                return;
            }
            orderBO.setUpMoney(orderBO.getAmount());
            orderBO.setUpLeadMoney(upLeadMoney);
            orderBO.setUpLeadMoneyReal(upLeadMoneyReal);
            orderBO.setUpLeadMoneyGiven(UpLeadMoneyGiven);
            orderBO.setAccountId(createOrderBO.getAccountId());
            orderBO.setOrderId(createOrderBO.getOrderId());
            int count = orderDao.uploadUnusualOrder(orderBO);
            if (count <= 0) {
                workData.setAnalyRes(CMDEnum.CMD_FALSE);

                log.error("异常订单入库插入失败 " + reponseService.getCmdString(workData));

                reponseService.orderResponseError(workData, responseMessage, workData.getAnalyRes());
                return;
            }

            if (upLeadMoneyReal > 0 || UpLeadMoneyGiven > 0) {
                Account account = accountDao.getAccountById(orderBO.getProjectId(), orderBO.getAccountId());
                long realMoney = account.getRealMoney() + upLeadMoneyReal;
                long givenMoney = account.getGivenMoney() + UpLeadMoneyGiven;
                count = accountDao.updateAccountMoney(orderBO.getProjectId(), orderBO.getAccountId(), realMoney, givenMoney);
                if (count <= 0) {
                    workData.setAnalyRes(CMDEnum.CMD_FALSE);

                    log.error("异常订单修改账号金额失败 " + reponseService.getCmdString(workData));

                    reponseService.orderResponseError(workData, responseMessage, workData.getAnalyRes());
                    throw new RuntimeException("异常订单入库 修改账号金额失败");
                }
            }

            count = bathBookingDao.updateEndStatus(orderBO.getProjectId(), orderBO.getOrderId(), orderBO.getAccountId());

            if (count <= 0) {
                workData.setAnalyRes(CMDEnum.CMD_FALSE);

                log.error("修改预约码状态失败 " + reponseService.getCmdString(workData));

                reponseService.orderResponseError(workData, responseMessage, workData.getAnalyRes());
                throw new RuntimeException("异常订单入库 修改预约状态失败");
            }

            deviceDao.updateDeviceStatu(orderBO.getProjectId(), orderBO.getDeviceId(), 0);

            reponseService.orderResponseOK(workData, responseMessage);
        } catch (Exception e) {
            workData.setAnalyRes(CMDEnum.CMD_ERROR_DATA);
            log.error("异常入库失败  " + reponseService.getCmdString(workData));
            reponseService.orderResponseError(workData, responseMessage, workData.getAnalyRes());
            log.error("异常上传消费数据异常", e);
            throw new RuntimeException("异常订单入库失败");
        }




    }


    @Override
    public void cancelReponse(WorkDataBO workData, String message) {
        if (workData.getWorkModeEnum().equals(WorkModeEnum.WORK_MODE_SEND)) {
            log.info(reponseService.getCmdString(workData));
            return;
        }
        String data = workData.getData();
        // 判断数据长度

        if (data.length() != UsedConstant.CANCEL_CODE_DATA_LEN) {
            workData.setAnalyRes(CMDEnum.CMD_ERROR_DATA);
            log.info(reponseService.getCmdString(workData));
            reponseService.responseError(workData, null, workData.getAnalyRes());
            return;
        }

        String resultCode = data.substring(0, 2);
        if (resultCode.equals(UsedConstant.OPERATION_SUCCESS_MARK)) {
            String itemId = data.substring(2, 22);
            String date = data.substring(4, 18);
            Date order_time = DateUtil.fomatDate2(date);


            OrderStartDTO orderStartDTO = new OrderStartDTO();
            String time = DateUtil.getFormatTime(order_time);
            orderStartDTO.setItemId(itemId);
            orderStartDTO.setOrderId(time);
            orderStartDTO.setProjectId(ServerUtil.hexToInt(data.substring(22, 30)));
            // 已取消状态
            orderStartDTO.setBookStatus(BookingStatusEnum.CANCELLED_STATUS.getCode());
            orderStartDTO.setAccountId(ServerUtil.hexToInt(data.substring(30, 38)));
            long phone = ServerUtil.hexToLong(data.substring(38, 50));
            int count = orderDao.cancelCode(orderStartDTO);
            CodeDeviceResultDto resultDto = new CodeDeviceResultDto();
            resultDto.setAccountId(orderStartDTO.getAccountId());
            resultDto.setProjectId(orderStartDTO.getProjectId());
            MQTTPushClient client = MQTTPushClient.getInstance();
            resultDto.setCreateTime(System.currentTimeMillis());
            if (count < 1) {
                workData.setAnalyRes(CMDEnum.CMD_ERROR_DATA);
                log.error("取消预约码修改状态失败  " + reponseService.getCmdString(workData));
//                reponseService.responseError(workData, message, workData.getAnalyRes());
                resultDto.setType(2);
                resultDto.setResult(false);

                client.publish(UsedConstant.PHONE_CLIENT_MQTT_MARK + phone, JSON.toJSONString(resultDto));

            } else {
                resultDto.setType(2);
                resultDto.setResult(true);

                client.publish(UsedConstant.PHONE_CLIENT_MQTT_MARK + phone, JSON.toJSONString(resultDto));
            }

        }
        reponseService.processSetCommon(workData);

    }


    UploadOrderBO updateOrderSplit(WorkDataBO workData) {


        UploadOrderBO orderBO = new UploadOrderBO();
        orderBO.setProjectId(Integer.parseInt(workData.getData().substring(0, 8), 16));
        orderBO.setWaterId(Integer.parseInt(workData.getData().substring(8, 12), 16));
        String orderId = workData.getData().substring(12, 32);
        orderBO.setOrderId(orderId);
        orderBO.setDeviceId(Integer.parseInt(workData.getData().substring(32, 40), 16));
        orderBO.setAccountId(Integer.parseInt(workData.getData().substring(40, 48), 16));
        orderBO.setAccountType(Integer.parseInt(workData.getData().substring(48, 50), 16));
        orderBO.setAmount(Integer.parseInt(workData.getData().substring(50, 58), 16));
        orderBO.setStartTime(workData.getData().substring(58, 72));
        orderBO.setEndTime(workData.getData().substring(72, 86));
        orderBO.setPulseNum(Long.parseLong(workData.getData().substring(86, 94), 16));
        orderBO.setFlowRate(Integer.parseInt(workData.getData().substring(94, 98), 16));
        orderBO.setUseTime(Integer.parseInt(workData.getData().substring(98, 102), 16));
        orderBO.setPauseCnt(Integer.parseInt(workData.getData().substring(102, 104), 16));
        orderBO.setErrState(Integer.parseInt(workData.getData().substring(104, 106), 16));


        return orderBO;
    }

    UploadUnusualOrderBO unusualOrderSplit(WorkDataBO workData){
        UploadUnusualOrderBO orderBO = new UploadUnusualOrderBO();
        orderBO.setProjectId(Integer.parseInt(workData.getData().substring(0, 8), 16));
        orderBO.setWaterId(Integer.parseInt(workData.getData().substring(8, 12), 16));
        orderBO.setDeviceId(Integer.parseInt(workData.getData().substring(12, 20), 16));
        orderBO.setAmount(Integer.parseInt(workData.getData().substring(20, 28), 16));
        orderBO.setEndTime(workData.getData().substring(28, 42));
        orderBO.setPulseNum(Long.parseLong(workData.getData().substring(42, 50), 16));
        orderBO.setFlowRate(Integer.parseInt(workData.getData().substring(50, 54), 16));
        orderBO.setUseTime(Integer.parseInt(workData.getData().substring(54, 58), 16));
        orderBO.setPauseCnt(Integer.parseInt(workData.getData().substring(58, 60), 16));
        orderBO.setErrState(Integer.parseInt(workData.getData().substring(60, 62), 16));

        return orderBO;
    }

}
