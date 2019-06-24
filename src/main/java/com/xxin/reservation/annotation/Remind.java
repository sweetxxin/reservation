package com.xxin.reservation.annotation;

import java.lang.annotation.*;

/**
 * @author xxin
 * @Created
 * @Date 2019/6/22 14:53
 * @Description
 */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Remind {
}
