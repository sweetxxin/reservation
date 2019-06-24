package com.xxin.reservation.enums;

/**
 * @author xxin
 * @Created
 * @Date 2019/6/13 15:18
 * @Description
 */
public enum UserType {
    CUSTOMER(1,"顾客"),
    Shop_Admin(2,"商店管理员"),
    HALL_ADMIN(3,"营业厅管理员"),
    COMPANY_ADMIN(4,"公司管理员");
    private int code;
    private String message;
    UserType(int code,String message){
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
