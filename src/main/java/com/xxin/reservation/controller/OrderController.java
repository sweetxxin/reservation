package com.xxin.reservation.controller;

import com.xxin.reservation.entity.Hall;
import com.xxin.reservation.entity.Shop;
import com.xxin.reservation.entity.User;
import com.xxin.reservation.enums.UserType;
import com.xxin.reservation.service.HallService;
import com.xxin.reservation.service.OrderService;
import com.xxin.reservation.service.ShopService;
import com.xxin.reservation.util.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author xxin
 * @Created
 * @Date 2019/6/13 17:05
 * @Description
 */
@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private HallService hallService;
    @PostMapping("/list/{index}")
    @ResponseBody
    public Message list(@PathVariable("index")Integer index, Integer each, String col, String val, HttpSession session){
        Message message = new Message();
        message.setSuccess(true);
        if (session.getAttribute("user")!=null){
            User user = (User) session.getAttribute("user");
            if (user.getType()== UserType.HALL_ADMIN.getCode()){
                Hall hall = hallService.getHallByCharge(user);
                List<Shop> shops = shopService.getShopByHall(hall);
                message.setData(orderService.getOrdersByShopIn(shops,hall.getMainId(),val,index ,each));
                System.out.println("当前用户是营业厅管理员");
            }else if (user.getType()== UserType.Shop_Admin.getCode()){
                Shop shop = shopService.getShopByUser(user);
                message.setData(orderService.getOrdersByShop(shop.getMainId(),index ,val,each ));
                System.out.println("当前用户是商店管理员");
            }else if (user.getType()== UserType.COMPANY_ADMIN.getCode()){
                message.setData(orderService.getOrdersList(col,val ,index ,each ));
                System.out.println("当前用户是公司管理员");
            }else {
                message.setSuccess(false);
            }
        }
        return message;
    }

}
