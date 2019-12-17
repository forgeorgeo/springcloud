package com.road.quzhibathhousemqtt.dao;

import com.road.quzhibathhousemqtt.constant.UsedConstant;
import com.road.quzhibathhousemqtt.util.AESUtil;

import com.road.quzhibathhousemqtt.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author wenqi
 * @title: DeviceDaoTest
 * @projectName quzhi-bathhouse-mqtt
 * @description:
 * @date 2019/3/8下午6:04
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class DeviceDaoTest {

    @Autowired
    private DeviceDao deviceDao;
    @Test
    public void updateDeviceFail() {

//        boolean validTime = DateUtil.comparisonTime(20190416101556L, UsedConstant.INTERVAL_TIME);
//        long addr = 107048156608L;
//        int count =deviceDao.updateDeviceStatus(addr,1,5,0);
//        System.out.println(count);


//        String list[] ={"19999101","19999102","19999103","19999104","19999105",
//                "19999106","19999107","19999108","19999109","19999110","19999111","19999112","19999113"
//        ,"19999114","19999115","19999116","19999117","19999118","19999119","19999120","19999121",
//                "19999122","19999123","19999124","19999125","19999126","19999127","19999128","19999129"
//        ,"19999130","19999131","19999132","19999133","19999134","19999135","19999136","19999137","19999138",
//                "19999139","19999140","19999141","19999142","19999143","19999144","19999145","19999146",
//                "19999147","19999148","19999149","19999150"};
//
//        for (int i=0;i<list.length;i++){
//
//            String bathPassword = AESUtil.getInstance().encode(list[i],
//                    UsedConstant.USE_CODE_KEY);
//
//            log.info(String.valueOf(list[i])+":"+bathPassword);
//
//        }

//        String bathPassword = AESUtil.getInstance().encode("19999101",
//                UsedConstant.USE_CODE_KEY);
//        log.info("加密密码"+bathPassword);
//        String de = AESUtil.getInstance().decode(bathPassword,UsedConstant.USE_CODE_KEY);
//        log.info("解密密码"+de);

        long time = 20190429103700L;
        Boolean result = DateUtil.comparisonTime(time,UsedConstant.INTERVAL_TIME);

        String tt= DateUtil.getFormatTimeTwelve();
        log.info("time:{},result:{},tt:{}",time,result,tt);
    }

    public static void main(String[] args) {
//        String key1 = "TheProductIsStupid";
//        String bathpasswd1 = "12345185";
//        String code1 = AESUtil.getInstance().encode(bathpasswd1, key1);
//        System.out.println("加密使用码="+code1);
        long time = 20190429103700L;
        Boolean result = DateUtil.comparisonTime(time,UsedConstant.INTERVAL_TIME);
        log.info("time:{},result:{}",time,result);
    }

}