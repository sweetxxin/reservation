package com.xxin.reservation.aop;

import com.xxin.reservation.entity.FormId;
import com.xxin.reservation.entity.Order;
import com.xxin.reservation.entity.Remind;
import com.xxin.reservation.entity.User;
import com.xxin.reservation.enums.UserType;
import com.xxin.reservation.service.WechatService;
import com.xxin.reservation.util.Message;
import com.xxin.reservation.util.RemindFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.Date;
import java.util.List;

/**
 * @author xxin
 * @Created
 * @Date 2019/6/22 14:51
 * @Description
 */
@Configuration
@Aspect
public class RemindAspect {
    @Autowired
    private WechatService wechatService;
    @Pointcut(value = "@annotation(com.xxin.reservation.annotation.Remind)")
    public void pointCut(){}
    @Around(value = "pointCut()")
    public Object remind(ProceedingJoinPoint joinPoint) throws Throwable {
       Message proceed = (Message) joinPoint.proceed();
       Order order = (Order) proceed.getData();
        System.out.println("aop拦截");
        User user = null;
        if (proceed.getMessage().contains("修改订单状态")){
            user = order.getCustomer();
        }else{
            user=order.getShop().getHall().getCharge();
        }
        List<FormId> formIds = wechatService.getFormIdByUser(user);
        if (formIds.size()>0){
            FormId formId = formIds.get(0);
            boolean res = false;
            if (user.getType()== UserType.CUSTOMER.getCode()){
                System.out.println("发送订单状态更改通知");
                 res = wechatService.sendRemind(RemindFactory.createChangeOrderRemind(order, user.getOpenId(),formId.getFormId()));
            }else{
                System.out.println("发送新建订单通知");
                 res = wechatService.sendRemind(RemindFactory.createNewOrderRemind(order, user.getOpenId(),formId.getFormId()));
            }
            wechatService.deleteFormId(formIds.get(0));
            if (res){
                Remind remind = wechatService.getRemindByOrder(order);
                if (remind==null){
                    remind = new Remind();
                    remind.setCreateTime(new Date());
                    remind.setStatus("未读");
                    remind.setOrder(order);
                    wechatService.saveRemind(remind);
                }
            }
        }else{
            System.out.println("formID不足或无效，无法发送通知");
        }
        return proceed;
    }
}
