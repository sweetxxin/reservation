package com.xxin.reservation.repository;

import com.xxin.reservation.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author xxin
 * @Created
 * @Date 2019/6/13 15:22
 * @Description
 */
public interface UserRepository extends JpaRepository<User,String> {
    User getUserByUserName(String name);
    User getUserByOpenId(String id);
}
