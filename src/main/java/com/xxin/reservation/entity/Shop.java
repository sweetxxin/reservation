package com.xxin.reservation.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * @author xxin
 * @Created
 * @Date 2019/6/13 15:10
 * @Description
 */
@Data
@Entity
@Table(name = "re_shop")
public class Shop {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid",strategy = "org.hibernate.id.UUIDGenerator" )
    @Column(name = "main_id")
    private String mainId;
    private String name;
    private String address;
    private int phone;
    private String description;
    @JsonFormat( pattern="yyyy-MM-dd HH:mm")
    private Date createTime;
    @ManyToOne
    @JoinColumn(referencedColumnName = "main_id",name = "charge_id",nullable = true)
    private User charge;

    @ManyToOne
    @JoinColumn(referencedColumnName = "main_id",name = "hall_id",nullable = true)
    private Hall hall;
}
