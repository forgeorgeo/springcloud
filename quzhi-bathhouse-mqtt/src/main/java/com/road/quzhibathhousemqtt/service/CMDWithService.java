package com.road.quzhibathhousemqtt.service;

import com.road.quzhibathhousemqtt.bo.WorkDataBO;
import com.road.quzhibathhousemqtt.enums.FuncEnum;
import com.road.quzhibathhousemqtt.enums.WorkModeEnum;

/**
 * @author wenqi
 * @Title: CMDWithService
 * @ProjectName quzhi-bathhouse-mqtt
 * @Description: 命令处理服务
 * @date 2019/2/20下午3:51
 */
public interface CMDWithService {

    /**
     * 获取命令类型
     * @return
     */
    int getType();

    /**
     * 功能码是不是不可用
     * @param func
     * @return
     */
    boolean isEnc(int func);

    /**
     * 组装命令
     * @param key
     * @param addr
     * @param cmdId
     * @param func
     * @param hexData
     * @return
     */
    String CMDCom(String key, String addr, long cmdId, FuncEnum func, String hexData);

    /**
     * 组装设备命令
     * @param key
     * @param addr
     * @param cmdId
     * @param func
     * @param hexData
     * @return
     */
    String CMDComDevice(String key, String addr, int cmdId, FuncEnum func, String hexData);

    /**
     * 拆分命令
     * @param key
     * @param hexCmd
     * @return
     */
    WorkDataBO CMDSplit(String key, String hexCmd);

    /**
     * 根据类型拆分命令
     * @param key
     * @param wm
     * @param hexCmd
     * @return
     */
    WorkDataBO CMDSplitByMode(String key, WorkModeEnum wm, String hexCmd);

    /**
     * 命令des加密
     * @param key
     * @param hexCmd
     * @return
     */
    String CMDDec(String key, String hexCmd);


}
