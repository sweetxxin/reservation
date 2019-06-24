package com.xxin.reservation.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * @author xxin
 * @Created
 * @Date 2019/6/13 14:58
 * @Description
 */
@Data
@Entity
@Table(name = "re_user")
public class User {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid",strategy = "org.hibernate.id.UUIDGenerator" )
    @Column(name = "main_id")
    private String mainId;
    private String userName;
    private String password;
    private String openId;
    private int type;
    @Column(length = 12)
    private long mobile;
    private String formId;
    private String nickName;
    @JsonFormat( pattern="yyyy-MM-dd HH:mm")
    private Date createTime;
}
