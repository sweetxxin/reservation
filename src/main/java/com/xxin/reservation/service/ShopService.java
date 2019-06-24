package com.xxin.reservation.service;

import com.xxin.reservation.entity.Hall;
import com.xxin.reservation.entity.Shop;
import com.xxin.reservation.entity.User;
import com.xxin.reservation.repository.ShopRepository;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author xxin
 * @Created
 * @Date 2019/6/13 15:25
 * @Description
 */
@Service
public class ShopService {
    @Autowired
    private ShopRepository shopRepository;

    public void saveShop(Shop shop){
        shopRepository.save(shop);
    }

    public Page<Shop> getShopList(String key, String val, int index, int each){
        if (val!=null&&!val.equals("")){
            Pageable pageable = PageRequest.of(index,each,new Sort(Sort.Direction.DESC, "create_time") );
            return shopRepository.getShopsLike(val,pageable);
        }
        Pageable pageable = PageRequest.of(index,each,new Sort(Sort.Direction.DESC, "createTime") );
        return shopRepository.findAll(pageable);
    }
    public Shop getShopById(String id){
        Optional<Shop> shopOpt = shopRepository.findById(id);
        if (shopOpt.isPresent()){
            return shopOpt.get();
        }
        return null;
    }
    public void deleteById(String id){
        shopRepository.deleteById(id);
    }

    public Shop getShopByUser(User user){
        return shopRepository.getShopByCharge(user);
    }
    public Page<Shop> getShopByHall( Hall hall,String val,Integer index, Integer each){
        if (val!=null&&!val.equals("")){
            Pageable pageable = PageRequest.of(index,each,new Sort(Sort.Direction.DESC, "create_time") );
            return shopRepository.getShopsOfHallAndLike(val,hall.getMainId() , pageable);
        }
        Pageable pageable = PageRequest.of(index,each,new Sort(Sort.Direction.DESC, "createTime") );
            return shopRepository.getShopByHall(hall, pageable);
    }
    public List<Shop> getShopByHall(Hall hall){
        return shopRepository.getShopByHall(hall);
    }
    public Shop getShopByName(String name){
        return shopRepository.getShopByName(name);
    }
}
