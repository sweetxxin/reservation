package com.xxin.reservation.repository;

import com.xxin.reservation.entity.Order;
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
 * @Date 2019/6/13 15:21
 * @Description
 */
public interface OrderRepository extends JpaRepository<Order,String> {
    /*
     * @Description 查询指定商店的订单
     * @Param: shop
     * @Param: pageable
     * @Return: org.springframework.data.domain.Page<com.xxin.reservation.entity.Order>
     * @Author: xxin
     * @Date: 2019-06-15 0:45
     */
    Page<Order> getOrdersByShop(Shop shop, Pageable pageable);
    /*
     * @Description 查询指定营业厅的订单
     * @Param: hallId
     * @Param: pageable
     * @Return: org.springframework.data.domain.Page<com.xxin.reservation.entity.Order>
     * @Author: xxin
     * @Date: 2019-06-15 0:45
     */
    @Query(value = "select * from re_order where shop_id in (select shop_id from re_shop where hall_id = ?1)",nativeQuery = true)
    Page<Order> getOrdersByHallId(String hallId,Pageable pageable);

    /*
     * @Description 查询指定客户的订单
     * @Return: java.util.List<com.xxin.reservation.entity.Order>
     * @Author: xxin
     * @Date: 2019-06-15 0:46
     */
    Page<Order> getOrdersByCustomer(User customer,Pageable pageable);

    /*
     * @Description 模糊查询订单
     * @Param: val
     * @Param: pageable
     * @Return: org.springframework.data.domain.Page<com.xxin.reservation.entity.Order>
     * @Author: xxin
     * @Date: 2019-06-15 0:46
     */
    @Query(value = "select * from re_order where status like %:val% or address like %:val% or assessment like %:val% or order_no like %:val%",nativeQuery = true)
    Page<Order> getOrdersLike(@Param("val")String val , Pageable pageable);

    @Query(value = "select * from re_order where status like %:val% or address like %:val% or assessment like %:val% or order_no like %:val% and shop_id=:shopId",nativeQuery = true)
    Page<Order> getOrdersOfShopLike(@Param("val")String val , @Param("shopId")String shopId, Pageable pageable);

    @Query(value = "select * from re_order where status like %:val% or address like %:val% or assessment like %:val% or order_no like %:val% and shop_id in (select main_id from re_shop where hall_id=:hallId)",nativeQuery = true)
    Page<Order> getOrdersInShopLike(@Param("val")String val , @Param("hallId")String hallId, Pageable pageable);
    Page<Order> getOrdersByShopIn(List<Shop> shops,Pageable pageable);

    @Query(value = "select * from re_order where customer_id=:id and status like %:val% or address like %:val% or assessment like %:val% or order_no like %:val%",nativeQuery = true)
    Page<Order> getOrdersByCustomerLike(String id,String val,Pageable pageable);

    Page<Order>getOrdersByCustomerEqualsAndStatusEquals(User user,Integer status,Pageable pageable);
    Page<Order>getOrdersByShopEqualsAndStatusEquals(Shop shop,Integer status,Pageable pageable);
    Page<Order>getOrdersByShopInAndStatusEquals(List<Shop> shops,Integer status,Pageable pageable);

    /*
     * @Description 统计
     * @Param: shops
     * @Param: status
     * @Return: java.lang.Integer
     * @Author: xxin
     * @Date: 2019-06-17 18:16
     */
    Integer countOrderByShopInAndStatusEquals(List<Shop>shops,Integer status);
    Integer countOrderByShopAndStatusEquals(Shop shop,Integer status);
    Integer countOrderByCustomerAndStatusEquals(User user,Integer status);
    @Query(value = "select count(*) from re_order where status=?1",nativeQuery = true)
    Integer countOrderInStatus(Integer status);

    @Query(value = "select count( DISTINCT customer_id) from re_order WHERE shop_id =?1",nativeQuery = true)
    Integer countCustomerByShop(String id);
    @Query(value = "select count(main_id) as top,shop_id  from re_order where status=?1 GROUP BY shop_id ORDER by top desc limit 0,5",nativeQuery = true)
    Object[][] countTop5Shop(Integer status);

    @Query(value = "SELECT DATE_FORMAT(create_time,'%Y') as yearNum,COUNT(*) as amount FROM re_order  where status=?1 GROUP BY DATE_FORMAT( create_time, '%Y' )",nativeQuery = true)
    Object[][] countOrderEachYear(Integer status);

    @Query(value = "select concat(DATE_FORMAT(CURDATE(),'%Y%m%d'),right(10001+IFNULL(max(right(order_no,4)),0),4)) from re_order where mid(order_no,1,8)=DATE_FORMAT(CURDATE(),'%Y%m%d')",nativeQuery = true)
    public String getNextIncreaseKey();



}
