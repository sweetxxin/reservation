package com.xxin.reservation.service;

import com.xxin.reservation.entity.Hall;
import com.xxin.reservation.entity.User;
import com.xxin.reservation.repository.HallRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author xxin
 * @Created
 * @Date 2019/6/14 22:01
 * @Description
 */
@Service
public class HallService {
    @Autowired
    private HallRepository hallRepository;
    public Hall getHallByName(String name){
        return hallRepository.getHallByName(name);
    }
    public Hall getHallByCharge(User user){
        return hallRepository.getHallByCharge(user);
    }

    public Page<Hall> getHallList(Integer index,Integer each,String val){
        if (val!=null&&!val.equals("")){
            Pageable pageable = PageRequest.of(index,each,new Sort(Sort.Direction.DESC, "create_time") );
            return hallRepository.getHallLike(val ,pageable);
        }
        Pageable pageable = PageRequest.of(index,each,new Sort(Sort.Direction.DESC, "createTime") );
        return hallRepository.findAll(pageable);
    }
    public List<Hall> getHallList(){
        return hallRepository.findAll();
    }
    public void deleteById(String id){
        hallRepository.deleteById(id);
    }

    public void save(Hall hall){
        hallRepository.save(hall);
    }
}
