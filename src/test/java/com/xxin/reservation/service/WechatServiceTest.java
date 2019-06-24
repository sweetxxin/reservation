package com.xxin.reservation.service;

import com.xxin.reservation.entity.Order;
import com.xxin.reservation.entity.Shop;
import com.xxin.reservation.entity.User;
import com.xxin.reservation.enums.OrderStatus;
import org.aspectj.weaver.ast.Or;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

import static org.junit.Assert.*;



@RunWith(SpringRunner.class)
@SpringBootTest
public class WechatServiceTest {
    @Autowired
    private WechatService wechatService;
    @Autowired
    private OrderService orderService;
    @Test
    public void getAccessToken() throws IOException {
        System.out.println(wechatService.getAccessToken());
    }
    @Test
    public void sendTemplateInfo() throws IOException {

    }
    @Test
    public void collectFormId(){
        User user = new User();
        user.setMainId("9aa53189-4ac4-478c-a2f9-1f3b971bdf23");
        wechatService.collectFormId("123456", user);
    }
    @Test
    public void order(){
        Order order= new Order();
        User user = new User();
        user.setMainId("28b7d1e2-627f-4368-8f78-5c2e17f10540");
        Shop shop = new Shop();
        shop.setMainId("640e718a-83a2-48c9-873a-c41e7e2fd40e");
        order.setCustomer(user);
        order.setAddress("test");
        order.setAssessment("test评价");
        order.setReservationTime(new Date());
        order.setShop(shop);
        order.setStatus(OrderStatus.CREATE.getCode());
        orderService.saveOrder(order);
        System.out.println(order);
        System.out.println(orderService.getOrdersById(order.getMainId()));
    }
    @Test
    public void nextKey(){
        System.out.println(orderService.getNextIncreaseKey());
    }
}