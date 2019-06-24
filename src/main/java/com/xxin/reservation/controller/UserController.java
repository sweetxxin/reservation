package com.xxin.reservation.controller;

import com.xxin.reservation.entity.User;
import com.xxin.reservation.enums.UserType;
import com.xxin.reservation.service.UserService;
import com.xxin.reservation.util.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @author xxin
 * @Created
 * @Date 2019/6/13 17:04
 * @Description
 */
@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/index")
    public String index(){
        return "index";
    }
    @GetMapping("/reservation")
    public String reservation(){
        return "reservation";
    }
    @PostMapping("/login")
    @ResponseBody
    public Message login(String username, String password, HttpSession httpSession){
        User user = userService.login(username,password);
        Message message = new Message();
        message.setSuccess(false);
        System.out.println("登陆用户"+user);
        if (user!=null&&user.getPassword().equals(password)){
            httpSession.setAttribute("user",user);
            message.setSuccess(true);
            message.setMessage("登陆成功");
        }else{
            message.setMessage("登陆失败");
        }
        return message;
    }
    @GetMapping("/main")
    public String main(Model model){
        return "main";
    }

}
