package com.road.quzhibathhousemqtt.bo;

import com.road.quzhibathhousemqtt.enums.CMDEnum;
import com.road.quzhibathhousemqtt.enums.WorkModeEnum;
import com.road.quzhibathhousemqtt.util.ServerUtil;

import java.io.ByteArrayOutputStream;

/**
 * @author wenqi
 * @Title: CMDBaseBO
 * @ProjectName quzhi-bathhouse-mqtt
 * @Description: 命令协议
 * @date 2019/2/20下午2:02
 */
public class CMDBaseBO {

    // 协议相关长度
    /**
     * 命令包(除去数据域)的固定长度19
     */
    public final static int CMD_LEN_WITH_OUT_DATA = 0x13;

    /**
     * 命令包(控制域1,地址域6,计数器4,功能码1,保留1)的固定长度13
     */
    public final static int CMD_DATA_LEN_WITH_OUT_DATA = 0x0D;
    /**
     * 命令包开始加密的位置
     */
    public final static int CMD_ENC_START_POSTION = 0x0E;

    /**
     * 命令包开始校验的位置
     */
    public final static int CMD_CHECK_START_POSTION = 0x04;
    /**
     * 通讯密码长度
     */
    public final static int CMD_PASSWORD_LEN = 0x06;


    // 协议常用值
    /**
     * 协议开始符(#:0x23)
     */
    public final static String CMD_STAET_CHAR = "#";

    /**
     * 协议终止符
     */
    public final static short CMD_END_CHAR = 0x16;

    /**
     * 协议完成符(\n)
     */
    public final static short CMD_FINISH_CHAR = 0x0A;

    /**
     * 控制域发送
     */
    public final static short CMD_CTRL_SEVER = 0x80;

    /**
     * 控制域接收
     */
    public final static short CMD_CTRL_CLIENT = 0x81;


    /**
     * ascii码转字符串
     *
     * @param bytes
     * @return
     */
    protected static String asciiDecode(String bytes) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(bytes.length() / 2);
        // 将每2位16进制整数组装成一个字节
        String hexString = "0123456789ABCDEF";
        for (int i = 0; i < bytes.length(); i += 2) {
            baos.write((hexString.indexOf(bytes.charAt(i)) << 4 | hexString.indexOf(bytes.charAt(i + 1))));
        }
        return new String(baos.toByteArray());
    }

    /**
     * 获取数据包开始符
     */
    public static int getHeader(String hexCmd) {
        if (hexCmd.length() >= 2) {
            return ServerUtil.hexToInt(hexCmd.substring(0, 2));
        }
        return 0;
    }


    /**
     * 取数据包数据长度
     * @param hexCmd
     * @return
     */
    protected static int getDataLen(String hexCmd) {
        if (hexCmd.length() >= 6){
            return ServerUtil.hexToInt(hexCmd.substring(2, 6)) - CMD_DATA_LEN_WITH_OUT_DATA ;
        }
        return 0;
    }


    /**
     * 取数据包完整长度
     * @param hexCmd
     * @return
     */
    protected static int getCMDLen(String hexCmd) {
        if (hexCmd.length() >= 6){
            return ServerUtil.hexToInt(hexCmd.substring(2, 6)) - CMD_DATA_LEN_WITH_OUT_DATA + CMD_LEN_WITH_OUT_DATA;
        }
        return 0;
    }

    /**
     *  获取数据包功能码
     * @param hexCmd
     * @return
     */
    protected static int getFunc(String hexCmd) {
        if (hexCmd.length() >= 28){
            return ServerUtil.hexToInt(hexCmd.substring(26, 28));
        }
        return 0;
    }

    /**
     * 常规验证
     * @param hexCmd
     * @return
     */
    protected static CMDEnum verifyCommon(String hexCmd) {
        // 判断是否为空
        if (hexCmd.isEmpty()){
            return CMDEnum.CMD_ERROR_LEN;
        }
        // 判断长度
        int cmdLen = getCMDLen(hexCmd);
        if (hexCmd.length() < cmdLen * 2){
            return CMDEnum.CMD_ERROR_LEN;
        }

        // 判断包头
        int start = ServerUtil.hexToInt(hexCmd.substring(0, 2));
        int start2 = ServerUtil.hexToInt(hexCmd.substring(6, 8));
        if (start != start2){
            return CMDEnum.CMD_ERROR_START;
        }

        // 判断控制域
        int ctrl = ServerUtil.hexToInt(hexCmd.substring(8, 10));
        if (!((ctrl == CMD_CTRL_SEVER ) || (ctrl == CMD_CTRL_CLIENT))){
            return CMDEnum.CMD_ERROR_CTRL;
        }

        // 判断结尾
        int endChar = ServerUtil.hexToInt(hexCmd.substring(cmdLen * 2 - 2, cmdLen * 2));
        if (endChar !=CMD_END_CHAR ){
            return CMDEnum.CMD_ERROR_END;
        }


        return CMDEnum.CMD_SUCCESS;
    }

    /**
     * 校验和验证
     * @param hexCmd
     * @return
     */
    protected static CMDEnum verifyCheck(String hexCmd) {
        // 判断是否为空
        if (hexCmd.isEmpty()){
            return CMDEnum.CMD_ERROR_LEN;
        }
        // 判断长度
        int cmdLen = getCMDLen(hexCmd);
        if (hexCmd.length() < cmdLen * 2){
            return CMDEnum.CMD_ERROR_LEN;
        }

        // 报文格式校验
        int ck = 0;
        for (int i = CMD_CHECK_START_POSTION ; i < cmdLen - 2; i++) {
            ck = (ck + ServerUtil.hexToInt(hexCmd.substring(i * 2, i * 2 + 2))) % 0x100;
        }
        //报文里
        int cmdCk = ServerUtil.hexToInt(hexCmd.substring(cmdLen * 2 - 4, cmdLen * 2 - 2));
        if (cmdCk != ck){
            return CMDEnum.CMD_ERROR_CHECK;
        }


        return CMDEnum.CMD_SUCCESS;
    }

    /**
     * 组合命令
     * @param start 开始符
     * @param addr 地址域
     * @param ctrl 控制域
     * @param cmdId 计数器
     * @param func 功能码
     * @param hold 版本
     * @param hexData 数据域
     * @return
     */
    public static String Com(int start, String addr, int ctrl, long cmdId, int func, int hold, String hexData) {
        String cmd = "";
        // 组合数据 -开始符
        cmd += ServerUtil.intToHex(start, 2);
        // 长度cmdLen是控制域、地址域、计数器、功能码、保留、数据域（应用层）的字节总数
        int cmdLen = hexData.length() / 2 + CMD_DATA_LEN_WITH_OUT_DATA;
        cmd += ServerUtil.intToHex(cmdLen, 4);
        // 组合数据 -开始符
        cmd += ServerUtil.intToHex(start, 2);
        // 组合数据 -控制域
        cmd += ServerUtil.intToHex(ctrl, 2);
        // 组合数据 -地址域BCD
        String bcdAddr = addr;
        bcdAddr = bcdAddr.substring(bcdAddr.length() - 12, bcdAddr.length());
        cmd += bcdAddr;
        // 组合数据 -计数器
        cmd += ServerUtil.intToHex(cmdId, 8);
        // 组合数据 -功能码
        cmd += ServerUtil.intToHex(func, 2);
        // 组合数据 -保留
        cmd += ServerUtil.intToHex(hold, 2);
        // 组合数据 -数据域 14 是数据域前面的长度
        cmd += hexData;
        // 组合数据 -效检CK 控制域、地址域、数据域的字节,累加和
        int ck = 0, cmdlen = cmd.length() / 2;
        for (int i = CMD_CHECK_START_POSTION ; i < cmdlen; i++){
            ck = (ck + ServerUtil.hexToInt(cmd.substring(i * 2, i * 2 + 2))) % 0x100;
        }
        cmd += ServerUtil.intToHex(ck, 2);
        // 组合数据 -终止符
        cmd += ServerUtil.intToHex(CMD_END_CHAR, 2);

        return cmd;
    }

    // 拆解命令
    public static WorkDataBO Split(String hexCmd) {
        WorkDataBO wd = new WorkDataBO();
        // 组合解析-开始符
        wd.setStart(ServerUtil.hexToInt(hexCmd.substring(0, 2)));
        // 长度
        int dataLen = ServerUtil.hexToInt(hexCmd.substring(2, 6));
        dataLen = (dataLen - CMD_DATA_LEN_WITH_OUT_DATA) * 2;
        // 组合解析-控制域
        wd.setCtrl(ServerUtil.hexToInt(hexCmd.substring(8, 10)));
        wd.setWorkModeEnum(WorkModeEnum.getEnum(wd.getCtrl()));
        // 组合解析-地址域 BCD
        wd.setAddr(hexCmd.substring(10, 22));
        // 组合解析-计数器
        wd.setCmdId(ServerUtil.hexToLong(hexCmd.substring(22, 30)));
        // 组合解析-功能码
        wd.setFunc(ServerUtil.hexToInt(hexCmd.substring(30, 32)));
        // 组合解析-保留
        wd.setHold(ServerUtil.hexToInt(hexCmd.substring(32, 34)));
        // 组合解析-数据域
        wd.setData(hexCmd.substring(34, 34 + dataLen));
        // 解析成功
        wd.setAnalyRes(CMDEnum.CMD_SUCCESS);
        // 判断控制符是否是正确的
        if (wd.getWorkDate().equals(WorkModeEnum.WORK_MODE_NONE)){
            wd.setAnalyRes(CMDEnum.CMD_ERROR_CTRL);
        }

        return wd;
    }

    /**
     * 数据加密
     * @param key
     * @param hexCmd
     * @return
     */
    protected static String Enc(String key, String hexCmd) {
        if (ServerUtil.isEmpty(hexCmd)){
            return "";
        }

        if (ServerUtil.isEmpty(key)){
            return "";
        }
        //ascii码转字符串
        if (key.length() == 12){
            key = asciiDecode(key);
        }
        int cmdLen = getCMDLen(hexCmd);

        if (hexCmd.length() < cmdLen * 2){
            return "";
        }

        int[] intCmd = new int[cmdLen];
        for (int i = 0; i < cmdLen; i++){
            intCmd[i] = ServerUtil.hexToInt(hexCmd.substring(i * 2, i * 2 + 2)) % 0x100;
        }

        String encKey = String.format("%" + CMD_PASSWORD_LEN + "s", key);
        encKey = encKey.replace(' ', '0');
        int encKeyLen = encKey.length();
        encKey = encKey.substring(encKeyLen - CMD_PASSWORD_LEN, encKeyLen);

        int[] intKey = new int[CMD_PASSWORD_LEN];
        for (int i = 0; i < CMD_PASSWORD_LEN; i++){
            intKey[i] = encKey.charAt(i);
        }

        for (int i = CMD_ENC_START_POSTION ; i < (cmdLen - 1); i++){
            intCmd[i] = (intCmd[i] + intKey[(i - CMD_ENC_START_POSTION) % CMD_ENC_START_POSTION]) % 0x100;
        }

        String encHexCmd = "";
        for (int i = 0; i < cmdLen; i++) {
            encHexCmd += ServerUtil.intToHex(intCmd[i], 2);
        }
        return encHexCmd;
    }


    /**
     * 数据解密
     * @param key
     * @param hexCmd
     * @return
     */
    protected static String Dec(String key, String hexCmd) {
        if (ServerUtil.isEmpty(hexCmd)){
            return "";
        }

        if (ServerUtil.isEmpty(key)){
            return "";
        }

        int cmdLen = getCMDLen(hexCmd);
        if (hexCmd.length() < cmdLen * 2){
            return "";
        }

        int[] intCmd = new int[cmdLen];
        for (int i = 0; i < cmdLen; i++){
            intCmd[i] = ServerUtil.hexToInt(hexCmd.substring(i * 2, i * 2 + 2)) % 0x100;
        }

        String encKey = String.format("%" + CMD_PASSWORD_LEN + "s", key);
        encKey = encKey.replace(' ', '0');
        int encKeyLen = encKey.length();
        encKey = encKey.substring(encKeyLen - CMD_PASSWORD_LEN, encKeyLen);

        int[] intKey = new int[CMD_PASSWORD_LEN];
        for (int i = 0; i < CMD_PASSWORD_LEN; i++){
            intKey[i] = encKey.charAt(i);
        }

        for (int i = CMD_ENC_START_POSTION; i < (cmdLen - 1); i++){
            intCmd[i] = (intCmd[i] - intKey[(i - CMD_ENC_START_POSTION) % CMD_PASSWORD_LEN] + 0x100) % 0x100;
        }

        String decHexCmd = "";
        for (int i = 0; i < cmdLen; i++) {
            decHexCmd += ServerUtil.intToHex(intCmd[i], 2);
        }
        return decHexCmd;
    }

    /**
     * 判断该功能的命令是否应该加密
     * @param func
     * @return
     */
    public boolean isEnc(int func) {
        return false;
    }

    /**
     * 加密组合命令
     * @param key 加密
     * @param start 开始字符
     * @param addr 地址域
     * @param ctrl 控制域
     * @param cmdId 计数器
     * @param func  功能码
     * @param hexData 数据域
     * @return
     */
    public String com(String key, int start, String addr, int ctrl, long cmdId, int func, String hexData) {
        String hexCmd = Com(start, addr, ctrl, cmdId, func, (short) 0, hexData);
        //20190119去掉加密
		/*if (!IsEmpty(key) && isEnc(func)) {
			if (IsNum(key))
				hexCmd = Enc(key, hexCmd);
			else
				hexCmd = "";
		}*/
        return hexCmd.toUpperCase();
    }


    /**
     * 命令解密析解
     * @param key
     * @param hexCmd
     * @return
     */
    public WorkDataBO split(String key, String hexCmd) {
        WorkDataBO wd = new WorkDataBO();
        wd.setKey(key);

        if (ServerUtil.isEmpty(hexCmd)) {
            wd.setAnalyRes(CMDEnum.CMD_ERROR_LEN);
            return wd;
        }

        if (!ServerUtil.isHex(hexCmd)) {
            wd.setAnalyRes(CMDEnum.CMD_ERROR_CHAR);
            return wd;
        }

        if ((!ServerUtil.isEmpty(key)) && (!ServerUtil.isNum(key))) {
            wd.setAnalyRes(CMDEnum.CMD_ERROR_KEY);
            return wd;
        }
        //取长度  控制域、地址域、计数器、功能码、保留+数据域
        int cmdLen = getCMDLen(hexCmd);
        if (hexCmd.length() < cmdLen * 2) {
            wd.setAnalyRes(CMDEnum.CMD_ERROR_LEN);
            return wd;
        }

        String cmd = hexCmd.substring(0, cmdLen * 2);

        CMDEnum ce = verifyCommon(cmd);
        if (ce != CMDEnum.CMD_SUCCESS) {
            wd.setAnalyRes(ce);
            return wd;
        }

        //20190119去掉解密
		/*if (!ServerUtil.isEmpty(key) && (isEnc(GetFunc(cmd))))
			cmd = Dec(key, cmd);*/

        ce = verifyCheck(cmd);
        if (ce != CMDEnum.CMD_SUCCESS) {
            wd.setAnalyRes(ce);
            return wd;
        }

        WorkDataBO splitWD = Split(cmd);

        splitWD.setKey(key);
        return splitWD;
    }

    /**
     * 加头尾组合成最终的命令或者去头尾的可解析的命令
     * @param cmd
     * @param isCom 是否解析
     * @return
     */
    protected String minaCMD(String cmd, boolean isCom) {
        if (ServerUtil.isEmpty(cmd)){
            return "";
        }

        String start = CMD_STAET_CHAR;
        String end = ServerUtil.intToHex(CMD_FINISH_CHAR, 2).toUpperCase();
        if (isCom){
            return (start + cmd + end).toUpperCase();
        }

        int len = cmd.length();
        String cmdStart = cmd.substring(0, 1).toUpperCase();
        String cmdEnd = cmd.substring(len - 2, len).toUpperCase();
        if (cmdEnd.equals(end)){
            cmd = cmd.substring(0, len - 2);
        }
        len = cmd.length();
        if (cmdStart.equals(start)){
            cmd = cmd.substring(1, len);
        }
        return cmd;
    }


}
