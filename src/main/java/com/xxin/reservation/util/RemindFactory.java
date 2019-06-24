package com.xxin.reservation.util;

import com.xxin.reservation.entity.Order;
import com.xxin.reservation.enums.OrderStatus;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashMap;

/**
 * @author xxin
 * @Created
 * @Date 2019/6/22 1:08
 * @Description
 */
public class RemindFactory {
    private static final String[] orderStatus = {"刚创建","处理中","已完成","失败"};
    public static HashMap<String,Object> createNewOrderRemind(Order order,String openId,String formId){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        HashMap<String,HashMap> hashMap = new HashMap<>();
        HashMap<String,Object> keyword1 = new HashMap<>();
        keyword1.put("value", order.getOrderNo());
        HashMap<String,Object> keyword2 = new HashMap<>();
        keyword2.put("value", order.getCustomer().getMobile()+"");
        HashMap<String,Object> keyword3 = new HashMap<>();
        keyword3.put("value", sdf.format( order.getReservationTime()));
        HashMap<String,Object> keyword4 = new HashMap<>();
        keyword4.put("value",order.getCustomer().getUserName());
        HashMap<String,Object> keyword5 = new HashMap<>();
        keyword5.put("value", order.getShop().getName());
        HashMap<String,Object> keyword6 = new HashMap<>();
        keyword6.put("value", order.getAddress());
        hashMap.put("keyword1", keyword1);
        hashMap.put("keyword2", keyword2);
        hashMap.put("keyword3", keyword3);
        hashMap.put("keyword4", keyword4);
        hashMap.put("keyword5", keyword5);
        hashMap.put("keyword6", keyword6);

        HashMap<String,Object> param = initParam(openId,formId);
        param.put("template_id", "MUxqNfdnJHWN4P6gy-KoWYLr77k_3ka3gTxPS9H-ZZ0");
        param.put("data",hashMap);
        return param;
    }
    public static HashMap<String,Object> createChangeOrderRemind(Order order,String openId,String formId){
        HashMap<String,HashMap> hashMap = new HashMap<>();
        HashMap<String,Object> keyword1 = new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        keyword1.put("value",orderStatus[order.getStatus()-1]);
        HashMap<String,Object> keyword2 = new HashMap<>();
        keyword2.put("value", order.getOrderNo());
        HashMap<String,Object> keyword3 = new HashMap<>();
        keyword3.put("value", sdf.format(order.getReservationTime()));
        HashMap<String,Object> keyword4 = new HashMap<>();
        keyword4.put("value",order.getAddress());
        HashMap<String,Object> keyword5 = new HashMap<>();
        keyword5.put("value", order.getShop().getName());
        hashMap.put("keyword1", keyword1);
        hashMap.put("keyword2", keyword2);
        hashMap.put("keyword3", keyword3);
        hashMap.put("keyword4", keyword4);
        hashMap.put("keyword5", keyword5);

        HashMap<String,Object> param = initParam(openId,formId);
        param.put("template_id", "clR1H8AfFR7tr39BOsEz7oNMJejlEpGav0tojHMXo3s");
        param.put("data",hashMap);
        return param;
    }

    private static HashMap<String,Object> initParam(String openId,String formId){
        HashMap<String,Object>param = new HashMap<>();
        param.put("touser", openId);
        param.put("page","/pages/remind/remind");
        param.put("form_id", formId);
        return param;
    }
}
