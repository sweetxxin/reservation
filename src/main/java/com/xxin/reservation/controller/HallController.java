package com.xxin.reservation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xxin.reservation.entity.Hall;
import com.xxin.reservation.entity.Shop;
import com.xxin.reservation.entity.User;
import com.xxin.reservation.enums.UserType;
import com.xxin.reservation.service.HallService;
import com.xxin.reservation.service.UserService;
import com.xxin.reservation.util.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

/**
 * @author xxin
 * @Created
 * @Date 2019/6/15 16:54
 * @Description
 */
@Controller
@RequestMapping("/hall")
public class HallController {
    @Autowired
    private HallService hallService;
    @Autowired
    private UserService userService;

    @PostMapping("/list/{index}")
    @ResponseBody
    public Message list(@PathVariable("index")Integer index, Integer each, String key, String val, HttpSession session){
        Message message = new Message();
        message.setSuccess(true);
        message.setData(hallService.getHallList(index, each,val ));
        return message;
    }
    @PostMapping("/delete/{id}")
    @ResponseBody
    public Message delete(@PathVariable("id")String id){
        hallService.deleteById(id);
        Message message = new Message();
        message.setSuccess(true);
        return message;

    }

    @PostMapping("/save")
    @ResponseBody
    public Message save(String hall,HttpSession session){
        Message message = new Message();
        message.setMessage("信息不完整或者日期格式有误");
        ObjectMapper mapper = new ObjectMapper();
        message.setSuccess(false);
        Hall s = null;
        try {
            s = mapper.readValue(hall, Hall.class);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return message;
        }
        User user = userService.getUserByUserName(s.getCharge().getUserName());
        if (user==null){
            user = new User();
            user.setMobile(s.getCharge().getMobile());
            user.setUserName(s.getCharge().getUserName());
            user.setType(UserType.HALL_ADMIN.getCode());
            user.setPassword("123456");
            user.setCreateTime(new Date());
            userService.saveUser(user);
        }
        message.setSuccess(true);
        s.setCharge(user);
        hallService.save(s);
        System.out.println(s);
        return message;
    }
}
