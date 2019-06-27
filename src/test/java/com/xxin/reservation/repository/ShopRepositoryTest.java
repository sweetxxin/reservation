package com.xxin.reservation.repository;

import com.xxin.reservation.entity.Hall;
import com.xxin.reservation.entity.Order;
import com.xxin.reservation.entity.Shop;
import com.xxin.reservation.entity.User;
import com.xxin.reservation.enums.UserType;
import com.xxin.reservation.service.ShopService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

/**
 * @author xxin
 * @Created
 * @Date 2019/6/13 16:42
 * @Description
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ShopRepositoryTest {
    @Autowired
    ShopRepository shopRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    private ShopService shopService;
    @Autowired
    HallRepository hallRepository;

    @Test
    public void addHall(){
        for (int i=6;i<10;i++) {
            Hall hall = new Hall();
            User charge = new User();
            charge.setUserName("天河营业厅管理"+i);
            charge.setType(UserType.HALL_ADMIN.getCode());
            hall.setCharge(charge);
            hall.setName("天河营业厅"+i);
            hall.setAddress("天河城地址"+i);
            hall.setCreateTime(new Date());
            hall.setPhone(1234456+i);
            userRepository.save(charge);
            hallRepository.save(hall);
        }
    }
    @Test public void addShop(){
        for (int i = 2; i <=5; i++) {
            User user = new User();
            user.setCreateTime(new Date());
            user.setMobile(124339222+i);
            user.setUserName("店长"+i);
            user.setType(UserType.Shop_Admin.getCode());
            userRepository.save(user);
            Hall hall = new Hall();
            hall.setMainId("dba16204-a226-45fa-b363-399cb5c6bde8");
            Shop shop = new Shop();
            shop.setCharge(user);
            shop.setHall(hall);
            shop.setCreateTime(new Date());
            shop.setAddress("黄埔大道中");
            shop.setName("商店"+i);
            shop.setPhone(8823321+i);
            shopRepository.save(shop);
        }
    }
    @Test
    public void addOrder(){
        Shop shop = new Shop();
        shop.setMainId("53f94086-8f0e-4d87-95de-79dbc67c84ff");
        for (int i = 15; i < 20; i++) {
            Order order = new Order();
            order.setStatus(3);
            order.setAssessment("好评");
            order.setOrderNo("OD20180600"+i);
            order.setAddress("大学城南");
            order.setCreateTime(new Date());
            order.setShop(shop);
            User user = new User();
            user.setType(UserType.CUSTOMER.getCode());
            user.setUserName("顾客"+i);
            user.setCreateTime(new Date());
            userRepository.save(user);
            order.setCustomer(user);
            orderRepository.save(order);
        }
    }

    @Test
    public void test(){
        for (int i=1;i<10;i++){
            Hall hall = new Hall();
            User charge = new User();
            charge.setUserName("营业厅管理"+i);
            hall.setCharge(charge);
            hall.setName("天河营业厅"+i);
            hall.setAddress("天河城");
            hall.setCreateTime(new Date());
            hall.setPhone(1234456+i);

            hallRepository.save(hall);

            User user = new User();
            user.setCreateTime(new Date());
            user.setMobile(124339222+i);
            user.setUserName("店长"+i);
            user.setType(UserType.Shop_Admin.getCode());
            userRepository.save(user);

            Shop shop = new Shop();
            shop.setCharge(user);
            shop.setHall(hall);
            shop.setCreateTime(new Date());
            shop.setAddress("黄埔大道中");
            shop.setName("商店"+i);
            shop.setPhone(8823321+i);
            shopRepository.save(shop);
        }

    }
    @Test
    public void like(){
        Pageable pageable = PageRequest.of(0, 3);
        System.out.println(shopRepository.getShopsLike("测试",pageable).getContent());
    }

}