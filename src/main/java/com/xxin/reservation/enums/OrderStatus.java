package com.xxin.reservation.enums;

import lombok.Data;

/**
 * @author xxin
 * @Created
 * @Date 2019/6/13 15:15
 * @Description
 */
public enum OrderStatus {
    CREATE(1,"未处理"),
    DEALING(2,"进行中"),
    SUCCESS(3,"已完成"),
    FAIL(4,"失败");

    private int code;
    private String message;

    OrderStatus(int code,String message){
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

}
