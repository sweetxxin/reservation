package com.xxin.reservation.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Value("${intercept.url}")
    private List<String> urls;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        System.out.println(uri+"********************");
        System.out.println(urls);
        for (String url:urls){
            if (uri.startsWith(url)){
                if (request.getSession().getAttribute("user")==null){
                    response.sendRedirect("/index");
                    return false;
                }
            }
        }
        return true;
    }
}
