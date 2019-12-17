package com.road.quzhibathhousemqtt.controller;

import com.road.quzhibathhousemqtt.dto.*;
import com.road.quzhibathhousemqtt.service.ClockService;
import com.road.quzhibathhousemqtt.service.DeviceService;
import com.road.quzhibathhousemqtt.service.OrderService;
import com.road.quzhibathhousemqtt.util.HttpResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author wenqi
 * @Title: MQTTController
 * @ProjectName quzhi-bathhouse-mqtt
 * @Description:
 * @date 2019/2/21上午10:32
 */
@RestController
@RequestMapping("/MQTT")
@Slf4j
public class MQTTController {


    @Autowired
    private ClockService clockService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private OrderService orderService;

    /**
     * 查询时钟
     *
     * @param Key
     * @param Addr  设备地址
     * @param ProID 项目ID
     * @param cmdId 命令ID
     * @return
     */
    @RequestMapping(value = "/queryClock")
    public HttpResult queryClock(@RequestParam String Key,
                                 @RequestParam String Addr,
                                 @RequestParam String ProID,
                                 @RequestParam String cmdId) {
        log.info("[/MQTT/queryClock] 查询时钟 key: {} Addr: {} ProID: {} cmdId: {}", Key, Addr, ProID, cmdId);
        HttpResult result = clockService.queryClock(Key, Addr, ProID, Long.valueOf(cmdId));
        return result;
    }

    /**
     * 设置时钟
     *
     * @param Key
     * @param Addr
     * @param ProID
     * @param cmdId
     * @return
     */
    @RequestMapping(value = "/setClock")
    public HttpResult setClock(@RequestParam String Key, @RequestParam String Addr,
                               @RequestParam String ProID, @RequestParam String cmdId) {
        log.info("[/MQTT/setClock] 设置时钟 key: {} Addr: {} ProID: {} cmdId: {}", Key, Addr, ProID, cmdId);

        HttpResult result = clockService.setClock(Key, Addr, ProID, Long.valueOf(cmdId));

        return result;
    }

    /**
     * 设置密码
     *
     * @param Password
     * @param Addr
     * @param ProID
     * @param cmdId
     * @return
     */
    @RequestMapping(value = "/setPassword")
    public HttpResult setPassword(@RequestParam String Password, @RequestParam String Addr,
                                  @RequestParam String ProID, @RequestParam String cmdId) {
        log.info("[/MQTT/setPassword] 设置通讯密码 Password: {} Addr: {} ProID: {} cmdId: {}", Password, Addr, ProID, cmdId);

        HttpResult result = deviceService.setPassword(Password, Addr, ProID, cmdId);
        return result;
    }

    /**
     * 查询水表状态
     *
     * @param Key
     * @param Addr
     * @param WaterID
     * @param cmdId
     * @return
     */
    @RequestMapping(value = "/queryWaterStatus")
    public HttpResult queryWaterStatus(@RequestParam String Key, @RequestParam String Addr,
                                       @RequestParam String ProID, @RequestParam String WaterID,
                                       @RequestParam String cmdId) {
        log.info("[/MQTT/queryWaterStatus] 查询设备状态 Key: {} Addr: {} ProID: {} WaterID: {} cmdId: {}", Key, Addr, ProID, WaterID, cmdId);

        HttpResult result1 = deviceService.queryWaterStatus(Key, Addr, ProID, WaterID, cmdId);
        return result1;
    }


    /**
     * 下发水表rate  费率
     *
     * @param key
     * @param addr
     * @param projectId
     * @param usrMode
     * @param rate
     * @param unit
     * @param timeOut
     * @param maxConsume
     * @param deviceType
     * @param rateVersion
     * @param cmdId
     * @return
     */
    @RequestMapping("/sendWaterRate")
    public HttpResult sendWaterRate(@RequestParam String key, @RequestParam String addr,
                                    @RequestParam String projectId, @RequestParam String usrMode,
                                    @RequestParam String rate, @RequestParam String unit,
                                    @RequestParam String timeOut, @RequestParam String maxConsume,
                                    @RequestParam String deviceType, @RequestParam String rateVersion,
                                    @RequestParam String cmdId) {
        WaterRateDTO dto = new WaterRateDTO(key, addr, projectId, usrMode, rate, unit,
                timeOut, maxConsume, deviceType, rateVersion, cmdId);
        log.info("[/MQTT/sendWaterRate] 下发水表费率 " + dto.toString());

        return deviceService.sendWaterRate(dto);
    }

    /**
     * 下发洗衣吹风等设备 费率
     *
     * @param key
     * @param addr
     * @param projectId
     * @param rate1
     * @param time1
     * @param rate2
     * @param time2
     * @param rate3
     * @param time3
     * @param rate4
     * @param time4
     * @param deviceType
     * @param rateVersion
     * @param cmdId
     * @return
     */
    @RequestMapping("/sendRateWashAndBlower")
    public HttpResult sendRateWashAndBlower(@RequestParam String key, @RequestParam String addr,
                                            @RequestParam String projectId, @RequestParam String rate1,
                                            @RequestParam String time1, @RequestParam String rate2,
                                            @RequestParam String time2, @RequestParam String rate3,
                                            @RequestParam String time3, @RequestParam String rate4,
                                            @RequestParam String time4, @RequestParam String deviceType,
                                            @RequestParam String rateVersion, @RequestParam String cmdId) {
        WashBlowerRateDTO rateDTO = new WashBlowerRateDTO(key, addr, projectId, rate1, time1, rate2, time2, rate3,
                time3, rate4, time4, deviceType, rateVersion, cmdId);
        return deviceService.sendRateWashAndBlower(rateDTO);
    }


    /**
     * 设置水表机号
     *
     * @param Key
     * @param Addr
     * @param ProID
     * @param SetMode
     * @param cmdId
     * @return
     */
    @RequestMapping(value = "/setWaterID")
    public HttpResult setWaterID(@RequestParam String Key, @RequestParam String Addr,
                                 @RequestParam String ProID, @RequestParam String SetMode,
                                 @RequestParam String cmdId) {
        log.info("[/MQTT/setWaterID] 设置水表机号 Key: {} Addr: {} ProID: {} SetMode: {} cmdId: {}", Key, Addr, ProID, SetMode, cmdId);

        HttpResult result = deviceService.setWaterID(Key, Addr, ProID, SetMode, cmdId);
        return result;
    }

    /**
     * 下发/删除 设备列表
     *
     * @param Key
     * @param Addr
     * @param ProID
     * @param TotalDevCount
     * @param SetMode
     * @param DevAddrList
     * @param cmdId
     * @return
     */
    @RequestMapping(value = "/setWaterList")
    public HttpResult setWaterList(@RequestParam String Key, @RequestParam String Addr,
                                   @RequestParam String ProID, @RequestParam String TotalDevCount,
                                   @RequestParam String SetMode, @RequestParam String DevAddrList,
                                   @RequestParam String cmdId) {
        log.info("[/MQTT/setWaterList] 下发删除设备列表 Key: {} Addr: {} ProID: {} TotalDevCount: {}  SetMode: {}   DevAddrList: {} cmdId: {}", Key, Addr, ProID, TotalDevCount, SetMode, DevAddrList, cmdId);


        SetWaterListDTO waterListDTO = new SetWaterListDTO(Key, Addr, ProID, TotalDevCount,
                SetMode, DevAddrList, cmdId);
        HttpResult result = deviceService.setWaterList(waterListDTO);
        return result;
    }

    /**
     * 删除设备
     *
     * @param Key
     * @param Addr
     * @param ProID
     * @param waterId
     * @param deviceId
     * @param cmdId
     * @return
     */
    @RequestMapping("/deleteDevice")
    public HttpResult deleteDevice(@RequestParam String Key, @RequestParam String Addr,
                                   @RequestParam String ProID, @RequestParam String waterId,
                                   @RequestParam String deviceId, @RequestParam String cmdId) {
        log.info("[/MQTT/deleteDevice] 下发删除设备 Key: {} Addr: {} ProID: {} waterId: {}  deviceId: {}  cmdId: {}", Key, Addr, ProID, waterId, deviceId, cmdId);
        DeleteDeviceDTO deviceDTO = new DeleteDeviceDTO(Key, Addr, ProID, waterId, deviceId, cmdId);
        return deviceService.deleteDevice(deviceDTO);
    }

    /**
     * app远程关闭订单
     *
     * @param Key
     * @param Addr
     * @param ProID
     * @param timeIds
     * @param accountId
     * @param accountType
     * @param cmdId
     * @return
     */
    @RequestMapping(value = "/remoteCloseOrder")
    public HttpResult remoteCloseOrder(@RequestParam String Key, @RequestParam String Addr,
                                       @RequestParam String ProID, @RequestParam String timeIds,
                                       @RequestParam String accountId, @RequestParam String accountType,
                                       @RequestParam String cmdId) {
        log.info("[/MQTT/remoteCloseOrder] 远程关闭订单 Key: {} Addr: {} ProID: {} timeIds: {}  accountId: {}   accountType: {}  cmdId: {}",
                Key, Addr, ProID, timeIds, accountId, accountType, cmdId);

        RemoteCloseOrderDTO closeOrderDTO = new RemoteCloseOrderDTO(Key, Addr, ProID, timeIds, accountId,
                accountType, cmdId);
        HttpResult result = orderService.remoteCloseOrder(closeOrderDTO);
        return result;
    }


    /**
     * 初始化集中器
     *
     * @param Key
     * @param Addr
     * @param ProID
     * @param cmdId
     * @return
     */
    @RequestMapping(value = "/initDevice")
    public HttpResult initDevice(@RequestParam String Key, @RequestParam String Addr,
                                 @RequestParam String ProID, @RequestParam String cmdId) {
        log.info("[/MQTT/initDevice] 初始化集中器 Key: {} Addr: {} ProID: {}  cmdId: {}", Key, Addr, ProID, cmdId);
        HttpResult result = deviceService.initDevice(Key, Addr, ProID, cmdId);
        return result;
    }

    /**
     * 更新采集器命令
     *
     * @param key
     * @param addr
     * @param ip
     * @param port
     * @param username
     * @param password
     * @param fileName
     * @param cmdId
     * @return
     */
    @RequestMapping(value = "/updateCollector")
    public HttpResult updateCollector(@RequestParam String key, @RequestParam String projectId,
                                      @RequestParam String addr, @RequestParam String ip,
                                      @RequestParam int port, @RequestParam String username,
                                      @RequestParam String password, @RequestParam int fileName,
                                      @RequestParam String cmdId) {
        log.info("[/MQTT/updateCollector] 更新采集器 key:{},addr:{},ip:{},port:{},fileName:{},cmdId:{}", key, addr, ip, port, fileName, cmdId);
        return deviceService.updateCollector(key, projectId, addr, ip, port, username,
                password, fileName, cmdId);
    }

}
