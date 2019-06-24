package com.xxin.reservation.repository;

import com.xxin.reservation.entity.FormId;
import com.xxin.reservation.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author xxin
 * @Created
 * @Date 2019/6/21 14:03
 * @Description
 */
public interface FormIdRepository extends JpaRepository<FormId,String> {
    List<FormId> getFormIdByUser(User user);
}
