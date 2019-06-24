package com.xxin.reservation.util;

import lombok.Data;

/**
 * @author xxin
 * @Created
 * @Date 2019/6/13 15:23
 * @Description
 */
@Data
public class Message {
    private boolean success;
    private Object data;
    private String message;
}
