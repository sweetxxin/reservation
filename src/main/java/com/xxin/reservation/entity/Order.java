package com.xxin.reservation.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * @author xxin
 * @Created
 * @Date 2019/6/13 15:03
 * @Description
 */
@Data
@Entity
@Table(name = "re_order")
public class Order {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid",strategy = "org.hibernate.id.UUIDGenerator" )
    @Column(name = "main_id")
    private String mainId;
    private String orderNo;
    @JsonFormat( pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat( pattern="yyyy-MM-dd HH:mm:ss")
    private Date reservationTime;
    @JsonFormat( pattern="yyyy-MM-dd HH:mm:ss")
    private Date finishTime;
    @ManyToOne
    @JoinColumn(referencedColumnName = "main_id",name = "customer_id")
    private User customer;

    @ManyToOne
    @JoinColumn(referencedColumnName = "main_id",name = "shop_id", nullable = false)
    private Shop shop;

    private int status;
    private String address;
    private String assessment;
}
