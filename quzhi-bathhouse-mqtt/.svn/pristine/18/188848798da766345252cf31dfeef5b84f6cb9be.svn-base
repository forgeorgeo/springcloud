package com.road.quzhibathhousemqtt.bo;

import com.road.quzhibathhousemqtt.enums.CMDEnum;
import com.road.quzhibathhousemqtt.enums.WorkModeEnum;

import java.util.Date;

/**
 * @author wenqi
 * @Title: WorkDataBO
 * @ProjectName quzhi-bathhouse-mqtt
 * @Description:
 * @date 2019/2/20下午2:59
 */
public class WorkDataBO {


    /**
     * 工作状态
     */
    private WorkModeEnum workMode = WorkModeEnum.WORK_MODE_SEND;
    /**
     * 接收或发送时间
     */
    private Date workDate = new Date();
    /**
     * 当前解析结果
     */
    private CMDEnum analyRes = CMDEnum.CMD_FALSE;


    /**
     * 项目ID
     */
    private long projectId = 0;
    /**
     * 设备ID
     */
    private String deviceId = "";
    /**
     * 开始符
     */
    private int start = 0;
    /**
     * 控制域
     */
    private int ctrl = 80;
    /**
     * 地址域
     */
    private String addr = "";
    /**
     * 计数器
     */
    private long cmdId = 0;
    /**
     * 功能码
     */
    private int func = 0;
    /**
     * 版本
     */
    private int hold = 0;
    /**
     * 数控域
     */
    private String data = "";
    /**
     * 当前命令解析的密码
     */
    private String key = "";
    /**
     * 命令码
     */
    private String CMD = "";

    public WorkModeEnum getWorkModeEnum() {
        return workMode;
    }

    public void setWorkModeEnum(WorkModeEnum workMode) {
        this.workMode = workMode;
    }

    public Date getWorkDate() {
        return workDate;
    }

    public void setWorkDate(Date workDate) {
        this.workDate = workDate;
    }

    public CMDEnum getAnalyRes() {
        return analyRes;
    }

    public void setAnalyRes(CMDEnum analyRes) {
        this.analyRes = analyRes;
    }

    public long getProjectId() {
        return projectId;
    }

    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }
    public String getDeviceId(){
        return deviceId;
    }
    public void setDeviceId(String deviceId){
        this.deviceId = deviceId;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getCtrl() {
        return ctrl;
    }

    public void setCtrl(int ctrl) {
        this.ctrl = ctrl;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public long getCmdId() {
        return cmdId;
    }

    public void setCmdId(long cmdId) {
        this.cmdId = cmdId;
    }

    public int getFunc() {
        return func;
    }

    public void setFunc(int func) {
        this.func = func;
    }

    public int getHold() {
        return hold;
    }

    public void setHold(int hold) {
        this.hold = hold;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data.toUpperCase();
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


    /**
     * 当前解析结果
     * @return
     */
    public boolean result() {
        return (this.analyRes == CMDEnum.CMD_SUCCESS);
    }

    public String getCMD() {
        return CMD;
    }

    public void setCMD(String cMD) {
        CMD = cMD;
    }

    @Override
    public String toString() {
        return "WorkData [workMode=" + workMode + ", workDate=" + workDate + ", analyRes=" + analyRes + ", projectId="
                + projectId + ", deviceId=" + deviceId + ", start=" + start + ", ctrl=" + ctrl + ", addr=" + addr + ", cmdId="
                + cmdId + ", func=" + func + ", hold=" + hold + ", data=" + data + ", key=" + key + ", CMD=" + CMD
                + "]";
    }


}
