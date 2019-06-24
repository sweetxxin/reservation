package com.xxin.reservation.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * @author xxin
 * @Created
 * @Date 2019/6/21 13:52
 * @Description
 */
@Data
@Entity
@Table(name = "re_remind")
public class Remind {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid",strategy = "org.hibernate.id.UUIDGenerator" )
    @Column(name = "main_id")
    private String mainId;
    @ManyToOne
    @JoinColumn(referencedColumnName = "main_id",name = "order_id",nullable = true)
    private Order order;
    private String message;
    private String status;
    private Date createTime;
}
