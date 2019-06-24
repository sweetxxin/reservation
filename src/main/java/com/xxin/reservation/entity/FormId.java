package com.xxin.reservation.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * @author xxin
 * @Created
 * @Date 2019/6/21 14:00
 * @Description
 */
@Data
@Entity
@Table(name = "re_formId")
public class FormId {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid",strategy = "org.hibernate.id.UUIDGenerator" )
    @Column(name = "main_id")
    private String mainId;

    private String formId;
    @ManyToOne
    @JoinColumn(referencedColumnName = "main_id",name = "user_id")
    private User user;

    private Date createTime;
}

