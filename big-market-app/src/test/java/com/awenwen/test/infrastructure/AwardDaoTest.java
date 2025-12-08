package com.awenwen.test.infrastructure;

import com.awenwen.infrastructure.persistent.dao.IAwardDao;
import com.awenwen.infrastructure.persistent.dao.IStrategyAwardDao;
import com.awenwen.infrastructure.persistent.dao.IStrategyDao;
import com.awenwen.infrastructure.persistent.dao.IStrategyRuleDao;
import com.awenwen.infrastructure.persistent.po.Award;
import com.awenwen.infrastructure.persistent.po.Strategy;
import com.awenwen.infrastructure.persistent.po.StrategyAward;
import com.awenwen.infrastructure.persistent.po.StrategyRule;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author awenwen
 * @description test for award persistency object query test
 * @create 2025/11/14 15:47
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class AwardDaoTest {
    @Resource
    private IAwardDao awardDao;

    @Resource
    private IStrategyRuleDao strategyRuleDao;

    @Resource
    private IStrategyDao strategyDao;

    @Resource
    private IStrategyAwardDao  strategyAwardDao;

    @Test
    public void testQueryStrategyRuleList() {
        List<Award> awards = awardDao.queryAwardList();
        log.info("awards: {}", awards);

        List<StrategyRule> strategyRules = strategyRuleDao.queryStrategyRuleList();
        log.info("strategyRules: {}", strategyRules);

        List<Strategy> strategies = strategyDao.queryStrategyList();
        log.info("strategies: {}", strategies);

        List<StrategyAward> strategyAwards = strategyAwardDao.queryStrategyAwardList();
        log.info("strategyAwards: {}", strategyAwards);
    }
}
