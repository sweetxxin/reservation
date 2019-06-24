package com.xxin.reservation.controller;

import com.xxin.reservation.service.StatisticService;
import com.xxin.reservation.util.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xxin
 * @Created
 * @Date 2019/6/16 0:21
 * @Description
 */
@Controller
@RequestMapping("/statistic")
public class StatisticController {
    @Autowired
    private StatisticService statisticService;
    @PostMapping("/hall/{index}")
    @ResponseBody
    public Message getStatisticOfHall(@PathVariable("index")String index){
        Message message = new Message();
        message.setSuccess(true);
        message.setData(statisticService.getStatisticOfHall(index));
        return message;
    }

    @PostMapping("/shop/{index}")
    @ResponseBody
    public Message getStatisticOfShop(@PathVariable("index")String index){
        Message message = new Message();
        message.setSuccess(true);
        message.setData(statisticService.getStatisticOfShop(index));
        return message;
    }
    @PostMapping("/all")
    @ResponseBody
    public Message getAllStatistic(Model model){
        HashMap hashMap = statisticService.getTop5Shop();
        hashMap.putAll(statisticService.countOrderEachYear());
        hashMap.put("shopAmount",statisticService.countShop());
        hashMap.put("hallAmount",statisticService.countHall());
        hashMap.put("totalOrder",statisticService.countAllOrder());
        hashMap.putAll(statisticService.countRankHall());

        model.addAttribute("statistic",hashMap);
        Message message = new Message();
        message.setSuccess(true);
        message.setData(hashMap);
        System.out.println(hashMap);
        return message;
    }
}
