package com.xxin.reservation.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StatisticServiceTest {
    @Autowired
    private StatisticService statisticService;

    @Test
    public void getStatisticOfHall() {
//        System.out.println(statisticService.getStatisticOfHall("b2d6a6a6-927d-4cef-af53-ecbfe47303a2"));
        System.out.println(statisticService.getStatisticOfShop("ddc8ff70-6d22-4f34-b4d1-ca3afa21633a"));
    }
    @Test
    public void getTopThreeShop(){
        statisticService.getTop5Shop();
    }
    @Test
    public void countAllOrder(){
        System.out.println(statisticService.countAllOrder());
    }
    @Test
    public void countRankHall(){
        System.out.println(statisticService.countRankHall());
    }
}