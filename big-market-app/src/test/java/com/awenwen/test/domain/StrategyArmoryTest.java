package com.awenwen.test.domain;

import com.awenwen.domain.strategy.service.armory.StrategyArmory;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author awenwen
 * @description
 * @create 2025/11/16 21:06
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class StrategyArmoryTest {
    @Resource
    private StrategyArmory strategyArmory;

    @Test
    public void testStrategyArmory() {
        boolean success = strategyArmory.assembleLotteryStrategy(100002L);
        log.info("result = {}", success);


    }

    @Test
    public void testStrategyArmoryGetValue() {
        Integer award1 = strategyArmory.getRandomAwardId(100002L);
        Integer award2 = strategyArmory.getRandomAwardId(100002L);
        Integer award3 = strategyArmory.getRandomAwardId(100002L);
        Integer award4 = strategyArmory.getRandomAwardId(100002L);

        log.info("award1 = {}", award1);
        log.info("award2 = {}", award2);
        log.info("award3 = {}", award3);
        log.info("award4 = {}", award4);
    }
}
