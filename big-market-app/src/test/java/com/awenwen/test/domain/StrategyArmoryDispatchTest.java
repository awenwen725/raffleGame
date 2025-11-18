package com.awenwen.test.domain;

import com.awenwen.domain.strategy.service.armory.StrategyArmoryDispatch;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
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
public class StrategyArmoryDispatchTest {
    @Resource
    private StrategyArmoryDispatch strategyArmoryDispatch;


    @Before
    public void testStrategyArmory() {
        boolean success = strategyArmoryDispatch.assembleLotteryStrategy(100001L);
        log.info("result = {}", success);
    }

    @Test
    public void testGetRandomAwardId() {
        log.info("getRandomAwardId: {}", strategyArmoryDispatch.getRandomAwardId(100001L));
    }

    @Test
    public void testGetRandomAwardId_ruleWeight() {
        log.info("getRandomAwardId 60: {}", strategyArmoryDispatch.getRandomAwardId(100001L, "60:102,103,104,105"));
        log.info("getRandomAwardId 200: {}", strategyArmoryDispatch.getRandomAwardId(100001L, "200:106,107"));
        log.info("getRandomAwardId 1000: {}", strategyArmoryDispatch.getRandomAwardId(100001L, "1000:105"));
    }


}