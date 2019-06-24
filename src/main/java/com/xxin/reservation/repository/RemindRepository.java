package com.xxin.reservation.repository;

import com.xxin.reservation.entity.Order;
import com.xxin.reservation.entity.Remind;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author xxin
 * @Created
 * @Date 2019/6/22 14:44
 * @Description
 */
public interface RemindRepository extends JpaRepository<Remind,String> {
    Remind getRemindByOrder(Order order);
    @Query(value = "select * from re_remind where order_id in (select main_id from re_order where customer_id=?1)",nativeQuery = true)
    Page<Remind> getRemindByCustomerId(String id, Pageable pageable);

    @Query(value = "select * from re_remind where order_id in(select main_id from re_order where shop_id in(select main_id from re_shop where hall_id=?1))",nativeQuery = true)
    Page<Remind> getRemindByHallId(String id, Pageable pageable);
    @Query(value = "select count(*) from re_remind where status=?1 and order_id in (select main_id from re_order where customer_id=?2)",nativeQuery = true)
    Integer countStatusRemind(String status,String id);

}
