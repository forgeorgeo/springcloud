package com.road.quzhibathhousemqtt.service.impl;

import com.road.quzhibathhousemqtt.bo.CMDBaseBO;
import com.road.quzhibathhousemqtt.bo.WorkDataBO;
import com.road.quzhibathhousemqtt.enums.CMDEnum;
import com.road.quzhibathhousemqtt.enums.FuncEnum;
import com.road.quzhibathhousemqtt.enums.WorkModeEnum;
import com.road.quzhibathhousemqtt.service.CMDWithService;
import com.road.quzhibathhousemqtt.util.ServerUtil;
import org.springframework.stereotype.Service;

/**
 * @author wenqi
 * @Title: CMDWithServiceImpl
 * @ProjectName quzhi-bathhouse-mqtt
 * @Description:
 * @date 2019/2/20下午8:08
 */
@Service
public class CMDWithServiceImpl extends CMDBaseBO implements CMDWithService {

    public static final short HEADER = 0x68;

    @Override
    public int getType() {
        return HEADER;
    }

    @Override
    public boolean isEnc(int code) {
        FuncEnum func =  FuncEnum.getEnum(code);
        switch (func){
            case FUNC_GET_COMM_PASSWORD:
            case FUNC_SET_COMM_PASSWORD:
            case FUNC_SET_DEVICE_TIME:
            case FUNC_DEVICE_LOGIN:
            case FUNC_DEVICE_HEART_BEAT:
                return false;
        }
        return true;
    }

    @Override
    public String CMDCom(String key, String addr, long cmdId, FuncEnum func, String hexData) {
        if (func == FuncEnum.FUNC_UNKNOWN) {
            return "";
        }
        if ((hexData != null) && (!hexData.isEmpty()) && (!ServerUtil.isHex(hexData))) {
            return "";
        }

        String hexCmd = com(key, 113, addr, 128, cmdId, func.getCode(), hexData);

        return minaCMD(hexCmd, true);
    }

    @Override
    public String CMDComDevice(String key, String addr, int cmdId, FuncEnum func, String hexData) {
        if (func == FuncEnum.FUNC_UNKNOWN){
            return "";
        }

        if (!ServerUtil.isHex(hexData)){
            return "";
        }


        String hexCmd = com(key, 104, addr, 129, cmdId, func.getCode(), hexData);

        return minaCMD(hexCmd, true);
    }

    @Override
    public WorkDataBO CMDSplit(String key, String hexCmd) {
        hexCmd = minaCMD(hexCmd, false);

        WorkDataBO wd = split(key, hexCmd);
        if (wd.getAnalyRes() == CMDEnum.CMD_SUCCESS) {
            int cmdheader = ServerUtil.hexToInt(hexCmd.substring(0, 2));
            if (cmdheader != 104) {
                wd.setAnalyRes(CMDEnum.CMD_ERROR_START);
            }
            if (FuncEnum.getEnum(wd.getFunc()) == FuncEnum.FUNC_UNKNOWN){
                wd.setAnalyRes(CMDEnum.CMD_ERROR_FUNC);
            }
        }
        return wd;
    }

    @Override
    public WorkDataBO CMDSplitByMode(String key, WorkModeEnum wm, String hexCmd) {
        hexCmd = minaCMD(hexCmd, false);

        WorkDataBO wd = split(key, hexCmd);
        if (wd.getAnalyRes() == CMDEnum.CMD_SUCCESS) {
            int cmdheader = ServerUtil.hexToInt(hexCmd.substring(0, 2));
            // 校验是不是以十六进制71(十进制113)开头
            if (cmdheader != 113) {
                wd.setAnalyRes(CMDEnum.CMD_ERROR_START);
            }
            if (FuncEnum.getEnum(wd.getFunc()) == FuncEnum.FUNC_UNKNOWN) {
                wd.setAnalyRes(CMDEnum.CMD_ERROR_FUNC);
            }

            if (!wd.getWorkModeEnum().equals(wm)){
                wd.setAnalyRes(CMDEnum.CMD_ERROR_CTRL);
            }

        }
        return wd;
    }

    @Override
    public String CMDDec(String key, String hexCmd) {
        hexCmd = minaCMD(hexCmd, false);

        if ((!ServerUtil.isEmpty(key)) && (isEnc(getFunc(hexCmd)))) {
            return Dec(key, hexCmd);
        }
        return hexCmd;
    }
}
