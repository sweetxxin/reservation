package com.xxin.reservation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xxin.reservation.entity.Hall;
import com.xxin.reservation.entity.Shop;
import com.xxin.reservation.entity.User;
import com.xxin.reservation.enums.UserType;
import com.xxin.reservation.service.HallService;
import com.xxin.reservation.service.ShopService;
import com.xxin.reservation.service.UserService;
import com.xxin.reservation.util.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author xxin
 * @Created
 * @Date 2019/6/13 17:06
 * @Description
 */
@Controller
@RequestMapping("/shop")
public class ShopController {
    @Autowired
    private ShopService shopService;
    @Autowired
    private UserService userService;
    @Autowired
    private HallService hallService;

    @PostMapping("/save")
    @ResponseBody
    public Message save(String shop,HttpSession session){
        Message message = new Message();
        message.setMessage("信息不完整或者日期格式有误");
        ObjectMapper mapper = new ObjectMapper();
        Shop s = null;
        try {
            s = mapper.readValue(shop, Shop.class);
        } catch (IOException e) {
            message.setSuccess(false);
            System.out.println(e.getMessage());
            return message;
        }
        System.out.println(shop);
        User user = (User) session.getAttribute("user");
        Hall hall =null;
        if (user.getType()==UserType.HALL_ADMIN.getCode()){
           hall = hallService.getHallByCharge(user);
        }else if (s.getHall().getName()!=null&&!s.getHall().getName().equals("")){
             hall = hallService.getHallByName(s.getHall().getName());
        }
        if (hall!=null){
            User charge = s.getCharge();
            charge.setType(UserType.Shop_Admin.getCode());
            userService.saveUser(charge);
            s.setHall(hall);
            shopService.saveShop(s);
            System.out.println(s);
            message.setMessage("操作成功");
            message.setSuccess(true);
        }
        return message;
    }

    @PostMapping("/delete/{id}")
    @ResponseBody
    public Message delete(@PathVariable("id")String id){
        shopService.deleteById(id);
        Message message = new Message();
        message.setSuccess(true);
        return message;

    }

    @PostMapping("/list/{index}")
    @ResponseBody
    public Message list(@PathVariable("index")Integer index, Integer each, String key, String val, HttpSession session){
        Message message = new Message();
        message.setSuccess(true);
        if (session.getAttribute("user")!=null){
            User user = (User) session.getAttribute("user");
            if (user.getType()==UserType.HALL_ADMIN.getCode()){
                System.out.println("当前是营业厅管理员");
               Hall hall = hallService.getHallByCharge(user);
               message.setData(shopService.getShopByHall(hall,val, index, each));
            }else if (user.getType()==UserType.COMPANY_ADMIN.getCode()){
                System.out.println("当前是公司管理员");
                message.setData(shopService.getShopList(key,val ,index ,each ));
            }else{
                message.setSuccess(false);
            }
        }
        return message;
    }
}
