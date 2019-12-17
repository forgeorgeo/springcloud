package com.road.quzhibathhousemqtt.entity;

import lombok.Data;

/**
 * @author wenqi
 * @Title: BathCmd
 * @ProjectName quzhi-bathhouse-mqtt
 * @Description:
 * @date 2019/2/25下午3:05
 */

@Data
public class BathCmd {

    /**
     * 记录ID
     */
    private long recid;

    /**
     * 项目编号
     */
    private long prjid;

    /**
     * 澡堂集中器ID
     */
    private long devid;

    /**
     * sn码
     */
    private String sncode;

    /**
     * 设备类型
     */
    private int devtype;

    /**
     * 计数器
     */
    private long cmdcount;

    /**
     * 目标地址
     */
    private long dealaddr;

    /**
     * 命令类型
     */
    private int cmdtype;

    /**
     * 命令类容
     */
    private String cmddata;

    /**
     * 下发命令(组合数据)
     */
    private String cmdrlt;

    /**
     * 处理标记 1 生成失败 2  已发送 5 操作完成 8 超时
     */
    private int dealtype;

    /**
     * 处理结果
     */
    private String dealcmdrlt;

    /**
     * 错误代码
     */
    private String errcode;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private String createtime;

    /**
     * 更新时间
     */
    private String uptime;

    public BathCmd() {

    }






}
