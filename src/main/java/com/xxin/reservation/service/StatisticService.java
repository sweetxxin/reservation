package com.xxin.reservation.service;

import com.xxin.reservation.entity.Hall;
import com.xxin.reservation.entity.Shop;
import com.xxin.reservation.enums.OrderStatus;
import com.xxin.reservation.repository.HallRepository;
import com.xxin.reservation.repository.OrderRepository;
import com.xxin.reservation.repository.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author xxin
 * @Created
 * @Date 2019/6/16 0:24
 * @Description
 */
@Service
public class StatisticService {
    @Autowired
    private ShopRepository shopRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private HallRepository hallRepository;
    public HashMap<String,Object>getStatisticOfHall(String id){
        HashMap hashMap = new HashMap();
        Hall hall = new Hall();
        hall.setMainId(id);
        List<Shop> shopList = shopRepository.getShopByHall(hall);
        hashMap.put("shopAmount",shopRepository.countShopByHall(hall));
        hashMap.put("orderAmount", orderRepository.countOrderByShopInAndStatusEquals(shopList, OrderStatus.SUCCESS.getCode()));
        Integer top = 0;
        int index=0;
        for (int i=0;i<shopList.size();i++){
            Integer tmp = orderRepository.countOrderByShopAndStatusEquals(shopList.get(i),OrderStatus.SUCCESS.getCode());
            if (top<tmp){
                top = tmp;
                index=i;
            }
        }
        if (shopList.size()>0){
            hashMap.put("topShop", shopList.get(index));
            hashMap.put("topAmount",top );
        }
        return hashMap;
    }
    public HashMap<String,Object> getStatisticOfShop(String id){
        HashMap hashMap = new HashMap();
        Shop shop = new Shop();
        shop.setMainId(id);
        hashMap.put("orderAmount", orderRepository.countOrderByShopAndStatusEquals(shop,OrderStatus.SUCCESS.getCode()));
        hashMap.put("customerAmount", orderRepository.countCustomerByShop(shop.getMainId()));
        return hashMap;

    }

    public HashMap<String,Object> getTop5Shop(){
        Object[][] objects = orderRepository.countTop5Shop(OrderStatus.SUCCESS.getCode());
        HashMap<String,Object> hashMap = new HashMap<>();
        HashMap<String,Object> resMap = new HashMap<>();
        for (Object[] obj:objects){
                hashMap.put(shopRepository.findById(obj[1].toString()).get().getName(), obj[0]);
        }
        resMap.put("topShop", hashMap);
        return resMap;
    }
    public Integer countAllOrder(){
        return orderRepository.countOrderInStatus(OrderStatus.SUCCESS.getCode());
    }

    public Integer countShop(){
        return shopRepository.countShop();
    }
    public Integer countHall(){
        return hallRepository.countHall();
    }
    public HashMap<String, Object> countRankHall(){
        List<Hall> halls = hallRepository.findAll();
        HashMap<String,Object> treeMap = new HashMap<>();
        HashMap<String,Object> resMap = new HashMap<>();
        for (Hall hall:halls){
            List<Shop> shops = shopRepository.getShopByHall(hall);
            Integer tmp = orderRepository.countOrderByShopInAndStatusEquals(shops,OrderStatus.SUCCESS.getCode());
            treeMap.put(hall.getName(),tmp);
        }
        resMap.put("rankHall",treeMap);
        return resMap;
//        List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(treeMap.entrySet());
//        Collections.sort(list,new Comparator<Map.Entry<String,Integer>>() {
//            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
//                return o2.getValue().compareTo(o1.getValue());
//            }
//        });
//        return list;
    }

    public HashMap<String,Object> countOrderEachYear(){
        Object[][] objects = orderRepository.countOrderEachYear(OrderStatus.SUCCESS.getCode());
        HashMap<String,Object> hashMap = new HashMap<>();
        HashMap<String,Object> resMap = new HashMap<>();
        for (Object[] obj:objects){
            hashMap.put(obj[0].toString(),obj[1]);
        }
        resMap.put("yearOrder",hashMap);
        return resMap;
    }
}
