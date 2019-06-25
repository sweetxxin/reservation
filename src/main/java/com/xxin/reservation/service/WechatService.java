package com.xxin.reservation.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xxin.reservation.entity.*;
import com.xxin.reservation.repository.FormIdRepository;
import com.xxin.reservation.repository.RemindRepository;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

/**
 * @author xxin
 * @Created
 * @Date 2019/6/17 16:07
 * @Description
 */
@Service
public class WechatService {
    @Value("${wx.appId}")
    private String appId;
    @Value("${wx.appSecret}")
    private String appSecret;

    public static final MediaType JSON=MediaType.parse("application/json; charset=utf-8");

    private String accessToken;
    @Autowired
    private FormIdRepository formIdRepository;
    @Autowired
    private RemindRepository remindRepository;
    public WeChat getAuth(String code) throws IOException {
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + appId + "&secret=" + appSecret + "&js_code=" + code + "&grant_type=authorization_code";
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        String result = response.body().string();
        ObjectMapper mapper = new ObjectMapper();
        WeChat weChat =  mapper.readValue(result, WeChat.class);
        System.out.println(weChat);
        return weChat;
    }
    public String getAccessToken() throws IOException {
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+appId+"&secret="+appSecret;
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        String result = response.body().string();
        ObjectMapper mapper = new ObjectMapper();
        HashMap hashMap = mapper.readValue(result, HashMap.class);
        System.out.println(hashMap);
        return hashMap.get("access_token").toString();

    }
    public boolean sendRemind(HashMap param) throws IOException {
        this.accessToken = getAccessToken();
        String url = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token="+this.accessToken;
        OkHttpClient client = new OkHttpClient();
        ObjectMapper mapper = new ObjectMapper();
        RequestBody requestBody = RequestBody.create(JSON, mapper.writeValueAsString(param));
        Request request = new Request.Builder().post(requestBody)
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        String result = response.body().string();
        HashMap hashMap = mapper.readValue(result, HashMap.class);
        if (hashMap.get("errmsg").equals("ok")){
            return true;
        }
        System.out.println(hashMap);
        return false;
    }


    public void saveRemind(Remind remind){
        remindRepository.save(remind);
    }
    public Remind getRemindByOrder(Order order){
        return remindRepository.getRemindByOrder(order);
    }
    public Page<Remind> getRemindByCustomerId(String id, int index, int each){
        List<String>list = new ArrayList<>();
        list.add("create_time");
        list.add("status");
        Pageable pageable = PageRequest.of(index,each,new Sort(Sort.Direction.ASC, list));
        return remindRepository.getRemindByCustomerId(id,pageable);
    }
    public Page<Remind> getRemindByHallId(String id, int index, int each){
        List<String>list = new ArrayList<>();
        list.add("create_time");
        list.add("status");
        Pageable pageable = PageRequest.of(index,each,new Sort(Sort.Direction.ASC, list));
        return remindRepository.getRemindByHallId(id,pageable);
    }
    public Remind getRemindById(String id){
        return remindRepository.findById(id).get();
    }
    public Integer countUnreadRemind(String id){
        return remindRepository.countStatusRemind("已读",id );
    }


    public List<FormId> getFormIdByUser(User user){
        return formIdRepository.getFormIdByUser(user);
    }
    public void deleteFormId(FormId formId){
        formIdRepository.delete(formId);
    }
    public void collectFormId(String id, User user){
        FormId formId = new FormId();
        formId.setFormId(id);
        formId.setUser(user);
        formId.setCreateTime(new Date());
        formIdRepository.save(formId);
    }

}
