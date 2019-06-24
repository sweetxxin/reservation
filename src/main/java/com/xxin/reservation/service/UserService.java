package com.xxin.reservation.service;

import com.xxin.reservation.entity.User;
import com.xxin.reservation.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author xxin
 * @Created
 * @Date 2019/6/13 15:24
 * @Description
 */
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User login(String name,String password){
       return userRepository.getUserByUserName(name);
    }
    public void saveUser(User user){
        userRepository.save(user);
    }
    public User getUserByUserName(String name){
        return userRepository.getUserByUserName(name);
    }
    public User getUserById(String id){
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()){
            return userOpt.get();
        }
        return null;
    }
    public User getUserByOpenId(String id){
        return userRepository.getUserByOpenId(id);
    }

}
