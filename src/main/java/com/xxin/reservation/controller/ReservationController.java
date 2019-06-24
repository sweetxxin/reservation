package com.xxin.reservation.controller;

import com.xxin.reservation.entity.*;
import com.xxin.reservation.enums.OrderStatus;
import com.xxin.reservation.enums.UserType;
import com.xxin.reservation.service.OrderService;
import com.xxin.reservation.service.ShopService;
import com.xxin.reservation.service.UserService;
import com.xxin.reservation.service.WechatService;
import com.xxin.reservation.util.Message;
import com.xxin.reservation.util.RemindFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author xxin
 * @Created
 * @Date 2019/6/16 13:00
 * @Description
 */
@Controller
@RequestMapping("/reservation")
public class ReservationController {
    @Autowired
    private ShopService shopService;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private WechatService wechatService;
    @RequestMapping("/index/{id}")
    public String index(@PathVariable("id") String id, Model model){
        Shop shop = shopService.getShopById(id);
        System.out.println(shop);
        model.addAttribute("shop",shop);
        return "reservation";
    }

    @PostMapping("/new/{id}")
    @ResponseBody
    @com.xxin.reservation.annotation.Remind
    public Message newReservation(@PathVariable("id")String id,String name,String address,long mobile,String time) throws ParseException, IOException {
        User user = userService.getUserByUserName(name);
        if (user==null){
            user = new User();
            user.setUserName(name);
            user.setType(UserType.CUSTOMER.getCode());
            user.setMobile(mobile);
            user.setPassword("123456");
            user.setCreateTime(new Date());
            userService.saveUser(user);
        }
        Order order = new Order();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        order.setCreateTime(new Date());
        order.setShop(shopService.getShopById(id));
        order.setCustomer(user);
        order.setOrderNo(orderService.getNextIncreaseKey());
        order.setAddress(address);
        order.setAssessment("未评价");
        order.setReservationTime(sdf.parse(time ));
        order.setStatus(OrderStatus.CREATE.getCode());
        orderService.saveOrder(order);
        Message message = new Message();
        message.setSuccess(true);
        message.setData(order);
        message.setMessage("预约成功");
        return message;
    }

}
