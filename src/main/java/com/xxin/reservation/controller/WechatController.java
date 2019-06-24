package com.xxin.reservation.controller;

import com.xxin.reservation.annotation.Remind;
import com.xxin.reservation.entity.*;
import com.xxin.reservation.enums.OrderStatus;
import com.xxin.reservation.enums.UserType;
import com.xxin.reservation.service.*;
import com.xxin.reservation.util.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


/**
 * @author xxin
 * @Created
 * @Date 2019/6/17 16:00
 * @Description
 */
@RestController
@RequestMapping("/wx")
public class WechatController {
@Autowired
private WechatService wechatService;
@Autowired
private UserService userService;
@Autowired
private OrderService orderService;
@Autowired
private ShopService shopService;
@Autowired
private HallService hallService;

    @RequestMapping(value = "/auth/{code}")
    public Message auth(@PathVariable("code") String code) throws IOException {
        Message message = new Message();
        WeChat weChat = wechatService.getAuth(code);
        User user = userService.getUserByOpenId(weChat.getOpenid());
        if (user==null){//第一次打开小程序
            message.setSuccess(false);
            user = new User();
            user.setOpenId(weChat.getOpenid());
            user.setType(-1);
            message.setSuccess(true);
            message.setMessage("第一次打开小程序，去登陆");
        }else{
            message.setMessage("微信授权成功，获取openId");
            message.setSuccess(true);
        }
        message.setData(user);
        return message;
    }

    @RequestMapping(value = "/login/{openId}")
    public Message login(@PathVariable("openId") String openId,String val,String password,String formId){
        Message message = new Message();
        message.setSuccess(false);
        System.out.println(password+":"+val+":"+formId+":"+openId);
        if (openId==null){
            message.setMessage("登陆出错");
            return message;
        }

        User user = userService.getUserByUserName(val);
        User u = userService.getUserByOpenId(openId);
        if (user==null){//不存在用户名
            if (u!=null){//但是openID已经被绑定
                message.setSuccess(false);
                message.setMessage("该小程序已经绑定了用户:"+u.getUserName());
                System.out.println("该小程序已经绑定了用户2:"+u.getUserName());
                return message;
            }//新建用户
            user = new User();
            user.setType(UserType.CUSTOMER.getCode());
            user.setCreateTime(new Date());
            user.setUserName(val);
            user.setPassword(password);
            user.setOpenId(openId);
            user.setMobile(Integer.valueOf(password));
            userService.saveUser(user);
            message.setData(user);
            message.setMessage("用户不存在,新建顾客");
            message.setSuccess(true);
        }else{//存在该用户名，
            if (password.equals(user.getMobile()+"")||user.getPassword().equals(password)){//用户名密码都对
                if (u==null&&(user.getOpenId()==null||user.getOpenId().equals(""))){//openID还没有绑定
                    user.setOpenId(openId);
                    userService.saveUser(user);
                    message.setMessage("登陆成功");
                    message.setData(user);
                    message.setSuccess(true);
                }else {
                    message.setSuccess(false);
                    message.setMessage("用户名已经被绑定:"+user.getUserName());
                    System.out.println("该小程序已经绑定了用户1:"+user.getUserName());
                    return message;
                }
            }else{
                message.setMessage("密码错误");
            }
        }
        if (formId!=null&&!formId.equals("")){
            wechatService.collectFormId(formId,user);
        }
        return message;
    }

    @RequestMapping(value = "/info/{openId}")
    public Message info(@PathVariable("openId") String openId){
        Message message = new Message();
        message.setMessage("获取用户信息");
        HashMap map = new HashMap();
        User user = userService.getUserByOpenId(openId);
        map.put("user",user);
        int create=0,process=0,finish=0,fail=0;
        if (user.getType()== UserType.CUSTOMER.getCode()){
          fail =  orderService.countOrderByUser(user, OrderStatus.FAIL.getCode());
          create = orderService.countOrderByUser(user, OrderStatus.CREATE.getCode());
          process = orderService.countOrderByUser(user, OrderStatus.DEALING.getCode());
          finish = orderService.countOrderByUser(user,OrderStatus.SUCCESS.getCode());
        }else if (user.getType()==UserType.Shop_Admin.getCode()){
            Shop shop = shopService.getShopByUser(user);
            fail = orderService.countOrderByShop(shop,OrderStatus.FAIL.getCode());
            create = orderService.countOrderByShop(shop, OrderStatus.CREATE.getCode());
            process = orderService.countOrderByShop(shop,OrderStatus.DEALING.getCode());
            finish = orderService.countOrderByShop(shop,OrderStatus.SUCCESS.getCode());
        }else if (user.getType()==UserType.HALL_ADMIN.getCode()){
            Hall hall = hallService.getHallByCharge(user);
            fail = orderService.countOrderByHallInStatus(hall,OrderStatus.FAIL.getCode());
            create = orderService.countOrderByHallInStatus(hall,OrderStatus.CREATE.getCode());
            process = orderService.countOrderByHallInStatus(hall,OrderStatus.DEALING.getCode());
            finish = orderService.countOrderByHallInStatus(hall,OrderStatus.SUCCESS.getCode());
        }else{
            fail = orderService.countOrderInStatus(OrderStatus.FAIL.getCode());
            process = orderService.countOrderInStatus(OrderStatus.DEALING.getCode());
            finish = orderService.countOrderInStatus(OrderStatus.SUCCESS.getCode());
        }
        map.put("orderAmount",process+finish+fail+create);
        map.put("orderProcess", process);
        map.put("orderFinish", finish);
        map.put("orderFail", fail);
        map.put("orderCreate", create);
        message.setData(map);
        return message;
    }

    @RequestMapping(value = "/order/{openId}")
    public Message order(@PathVariable("openId")String openId, Integer index,Integer each,String val){
        Message message = new Message();
        message.setMessage("获取订单信息");
        message.setSuccess(true);
        User user = userService.getUserByOpenId(openId);
        if (user.getType()==UserType.CUSTOMER.getCode()){
            message.setData(orderService.getOrderByCustomer(user,index,each,val));
        }else if (user.getType()==UserType.Shop_Admin.getCode()){
            Shop shop = shopService.getShopByUser(user);
            message.setData(orderService.getOrdersByShop(shop.getMainId(), index,val ,each ));
        }else if (user.getType()==UserType.HALL_ADMIN.getCode()){
            Hall hall = hallService.getHallByCharge(user);
            List<Shop>shops = shopService.getShopByHall(hall);
            message.setData(orderService.getOrdersByShopIn(shops,hall.getMainId() , val, index, each));
        }else{
            message.setData(orderService.getOrdersList(null,val ,index ,each ));
        }
        return message;
    }

    @RequestMapping(value = "/order/new/{openId}")
    @com.xxin.reservation.annotation.Remind
    public Message newOrder(@PathVariable("openId")String openId,String name,long mobile,String address, String reservationTime,  String shopName, String hallName) throws ParseException, IOException {
        Message message = new Message();
        message.setMessage("微信小程序新建预约");
        message.setSuccess(true);
//        User user = userService.getUserByOpenId(openId);
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
        order.setAddress(address);
        order.setCustomer(user);
        order.setOrderNo(orderService.getNextIncreaseKey());
        order.setAssessment("未评价");
        order.setShop(shopService.getShopByName(shopName));
        order.setStatus(OrderStatus.CREATE.getCode());
        order.setCreateTime(new Date());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        System.out.println("新预约:"+name+" "+address+" "+reservationTime+" "+shopName+" "+mobile);
        order.setReservationTime(sdf.parse(reservationTime));
        orderService.saveOrder(order);
        message.setData(order);
        return message;
    }
    @RequestMapping(value = "/order/change/{mainId}")
    @Remind
    public Message change(@PathVariable("mainId")String mainId,Integer status) throws IOException {
        Order order = orderService.getOrdersById(mainId);
        System.out.println(status);
        order.setStatus(status);
        if (status==OrderStatus.SUCCESS.getCode()){
            order.setFinishTime(new Date());
        }
        orderService.saveOrder(order);
        Message message = new Message();
        message.setSuccess(true);
        message.setData(order);
        message.setMessage("修改订单状态");
        return message;
    }

    @RequestMapping(value = "/order/assessment/{mainId}")
    public Message assessment(@PathVariable("mainId")String mainId,String assessment) {
        Order order = orderService.getOrdersById(mainId);
        order.setAssessment(assessment);
        orderService.saveOrder(order);
        Message message = new Message();
        message.setSuccess(true);
        message.setMessage("订单评价");
        return message;
    }
    @RequestMapping("/collect/{openId}")
    public Message collect(@PathVariable("openId")String openId,String formId){
        Message message = new Message();
        message.setMessage("收集formId");
        if (openId==null||openId.equals("")){
            return message;
        }
        if (formId.contains("the formId is a mock one")){
            return message;
        }
        User user = userService.getUserByOpenId(openId);
        wechatService.collectFormId(formId,user );
        message.setSuccess(true);
        return message;
    }

    @RequestMapping("/remind/{openId}")
    public Message remind(@PathVariable("openId")String openId,Integer each,Integer index){
        User user = userService.getUserByOpenId(openId);
        Message message = new Message();
        message.setMessage("获取通知");
        message.setSuccess(true);
        if (user.getType()==UserType.CUSTOMER.getCode()){
            message.setData(wechatService.getRemindByCustomerId(user.getMainId(),index ,each ));
        }else if (user.getType()==UserType.HALL_ADMIN.getCode()){
            Hall hall = hallService.getHallByCharge(user);
            message.setData(wechatService.getRemindByHallId(hall.getMainId(),index ,each ));
        }
        return message;
    }
    @RequestMapping("/remind/read/{id}")
    public Message read(@PathVariable("id")String id){
        com.xxin.reservation.entity.Remind remind = wechatService.getRemindById(id);
        remind.setStatus("已读");
        wechatService.saveRemind(remind);
        Message message = new Message();
        message.setMessage("已读");
        message.setSuccess(true);
        return message;
    }
    @RequestMapping("/remind/unread/{openId}")
    public Message unRead(@PathVariable("openId")String openId){
        User user = userService.getUserByOpenId(openId);
        Message message = new Message();
        message.setSuccess(true);
        message.setMessage("计算未读通知");
        if (user.getType()==UserType.CUSTOMER.getCode()){
            message.setData(wechatService.countUnreadRemind(user.getMainId()));
        }
        return message;
    }

    @RequestMapping("/hallList")
    public Message hallList(){
        Message message = new Message();
        List<String>list = new ArrayList<>();
        for (Hall hall:hallService.getHallList()){
            list.add(hall.getName());
        }
        message.setData(list);
        message.setSuccess(true);
        message.setMessage("营业厅列表");
        return message;
    }
    @RequestMapping("/shopList")
    public Message shopList(String hallName){
        Message message= new Message();
        message.setSuccess(true);
        List<String> list = new ArrayList<>();
        for (Shop shop:shopService.getShopByHall( hallService.getHallByName(hallName))){
            list.add(shop.getName());
        }
        message.setData(list);
        message.setMessage("获取商店列表");
        return message;
    }
}
