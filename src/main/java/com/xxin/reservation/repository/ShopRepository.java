package com.xxin.reservation.repository;

import com.xxin.reservation.entity.Hall;
import com.xxin.reservation.entity.Shop;
import com.xxin.reservation.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author xxin
 * @Created
 * @Date 2019/6/13 15:20
 * @Description
 */
public interface ShopRepository extends JpaRepository<Shop,String> {
    @Query(value = "select * from re_shop where `name` like %:val% or address like %:val% or description like %:val%",nativeQuery = true)
    Page<Shop> getShopsLike(@Param("val")String val ,Pageable pageable);

    @Query(value = "select * from re_shop where `name` like %:val% or address like %:val% or description like %:val% and hall_id=:hallId",nativeQuery = true)
    Page<Shop> getShopsOfHallAndLike(@Param("val")String val ,@Param("hallId")String hallId,Pageable pageable);

    Shop getShopByCharge(User charge);

    Page<Shop> getShopByHall(Hall hall,Pageable pageable);
    List<Shop> getShopByHall(Hall hall);

    Integer countShopByHall(Hall hall);
    @Query(value = "SELECT COUNT(*) FROM re_shop",nativeQuery = true)
    Integer countShop();

    Shop getShopByName(String name);

}
