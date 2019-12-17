package com.road.quzhibathhousemqtt.util;

import com.road.quzhibathhousemqtt.enums.ResultEnum;

/**
 * @author wenqi
 * @Title: ResultUtil
 * @ProjectName quzhi-bathhouse-mqtt
 * @Description:
 * @date 2019/2/21上午11:25
 */
public class ResultUtil {

    public static HttpResult success(Object object){
        HttpResult result = new HttpResult();
        result.setError_code(0);
        result.setMassage("成功");
        result.setData(object);
        return result;
    }

    public static HttpResult success(){
        return success(null);
    }
    public static HttpResult error(Integer code,String msg){
        HttpResult result = new HttpResult();
        result.setError_code(code);
        result.setMassage(msg);
        return result;
    }

    public static HttpResult error(ResultEnum resultEnum){
        HttpResult result = new HttpResult();
        result.setError_code(resultEnum.getCode());
        result.setMassage(resultEnum.getMsg());
        return result;
    }
}
