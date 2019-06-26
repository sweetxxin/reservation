package com.xxin.reservation.service;

import com.xxin.reservation.entity.Hall;
import com.xxin.reservation.entity.Order;
import com.xxin.reservation.entity.Shop;
import com.xxin.reservation.entity.User;
import com.xxin.reservation.enums.OrderStatus;
import com.xxin.reservation.repository.OrderRepository;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author xxin
 * @Created
 * @Date 2019/6/13 15:24
 * @Description
 */
@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ShopService shopService;

    public String getNextIncreaseKey(){
        return orderRepository.getNextIncreaseKey();
    }
    public void saveOrder(Order order){
        orderRepository.save(order);
    }

    public Page<Order> getOrdersList(String key, String val, int index, int each){
        if (val!=null&&!val.equals("")){
            Pageable pageable = PageRequest.of(index,each,new Sort(Sort.Direction.DESC, "create_time") );
            return orderRepository.getOrdersLike(val,pageable);
        }
        Pageable pageable = PageRequest.of(index,each,new Sort(Sort.Direction.DESC, "createTime"));
        return orderRepository.findAll(pageable);
    }


    public Order getOrdersById(String mainId){
        Optional<Order> orderOpt = orderRepository.findById(mainId);
        if (orderOpt.isPresent()){
            return orderOpt.get();
        }
        return null;
    }

    public Page<Order> getOrdersByShop(String shopId,Integer index,String val,Integer each){
        Pageable pageable = PageRequest.of(index,each,new Sort(Sort.Direction.DESC, "createTime"));
        Shop shop= new Shop();
        shop.setMainId(shopId);
        if (val!=null&&!val.equals("")){
            if (parseKeyword(val)==-1){
                pageable = PageRequest.of(index,each,new Sort(Sort.Direction.DESC, "create_time"));
                return orderRepository.getOrdersOfShopLike(val, shopId, pageable);
            }else{
                return orderRepository.getOrdersByShopEqualsAndStatusEquals(shop, parseKeyword(val),pageable );
            }
        }
        return orderRepository.getOrdersByShop(shop,pageable);
    }
    public Page<Order> getOrdersByShopIn(List<Shop> shops,String hallId,String val,Integer index,Integer each){
        Pageable pageable = PageRequest.of(index,each,new Sort(Sort.Direction.DESC, "createTime") );
        if (val!=null&&!val.equals("")){
            if (parseKeyword(val)==-1){
                Pageable pageable2 = PageRequest.of(index,each,new Sort(Sort.Direction.DESC, "create_time") );
                return orderRepository.getOrdersInShopLike(val,hallId ,pageable2);
            }else{
                return orderRepository.getOrdersByShopInAndStatusEquals(shops, parseKeyword(val), pageable);
            }
        }
        return orderRepository.getOrdersByShopIn(shops,pageable);
    }
    public Page<Order> getOrdersByHallId(String hallId, int index, int each){
        Pageable pageable = PageRequest.of(index,each,new Sort(Sort.Direction.DESC, "create_time"));
        return orderRepository.getOrdersByHallId(hallId, pageable);
    }
    public Page<Order> getOrderByCustomer(User customer,int index,int each,String val){
        List<String>list = new ArrayList<>();
        list.add("createTime");
        list.add("status");
        Pageable pageable = PageRequest.of(index,each,new Sort(Sort.Direction.DESC, list) );
        if (val!=null&&!val.equals("")){
            if (parseKeyword(val)==-1){
                return orderRepository.getOrdersByCustomerLike(customer.getMainId(),val ,pageable );
            }else {
                return orderRepository.getOrdersByCustomerEqualsAndStatusEquals(customer, parseKeyword(val), pageable);
            }
        }
        return orderRepository.getOrdersByCustomer(customer,pageable);
    }

    public Integer countOrderByUser(User user,Integer status){
        return orderRepository.countOrderByCustomerAndStatusEquals(user, status);
    }
    public Integer countOrderByShop(Shop shop,Integer status){
        return orderRepository.countOrderByShopAndStatusEquals(shop,status );
    }
    public Integer countOrderInStatus(Integer status){
        return orderRepository.countOrderInStatus(status);
    }
    public Integer countOrderByHallInStatus(Hall hall,Integer status){
        List<Shop>shops = shopService.getShopByHall(hall);
        return orderRepository.countOrderByShopInAndStatusEquals(shops,status );

    }
    private Integer parseKeyword(String val){
        Integer res = -1;
        if (val==null||val.equals(""))return res;
        if (val.contains("完成")||val.contains("成功")){
            res = OrderStatus.SUCCESS.getCode();
            System.out.println("模糊查询成功");
        }else if (val.contains("未")){
            res= OrderStatus.CREATE.getCode();
            System.out.println("模糊查询未处理");
        }else if (val.contains("中")||val.contains("处理中")){
           res = OrderStatus.DEALING.getCode();
            System.out.println("模糊处理中");
        }
        return res;
    }
}
