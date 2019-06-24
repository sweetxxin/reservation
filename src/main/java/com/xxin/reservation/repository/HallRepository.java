package com.xxin.reservation.repository;

import com.xxin.reservation.entity.Hall;
import com.xxin.reservation.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author xxin
 * @Created
 * @Date 2019/6/14 21:01
 * @Description
 */
public interface HallRepository extends JpaRepository<Hall,String> {
    Hall getHallByName(String name);
    Hall getHallByCharge(User charge);
    @Query(value = "select * from re_hall where address like %:val% or name like %:val%",nativeQuery = true)
    Page<Hall> getHallLike(@Param("val") String val, Pageable pageable);
    @Query(value = "select count(*) from re_hall",nativeQuery = true)
    Integer countHall();


}
