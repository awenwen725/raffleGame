package com.awenwen.domain.strategy.service.rule.chain.impl;

import com.awenwen.domain.strategy.repository.IStrategyRepository;
import com.awenwen.domain.strategy.service.rule.chain.AbstractLogicChain;
import com.awenwen.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author awenwen
 * @description Black List chain
 * @create 2025/12/6 23:00
 */
@Slf4j
@Component("rule_blacklist")
public class BlackListLogicChain extends AbstractLogicChain {

    @Resource
    private IStrategyRepository repository;

    @Override
    protected String ruleModel() {
        return "rule_blacklist";
    }

    @Override
    public Integer logic(String userId, Long strategyId) {
        log.info("filter Black List userId:{} strategyId:{} ruleModel:{}",userId, strategyId, ruleModel());

        // query black List
        String ruleValue = repository.queryStrategyRuleValue(strategyId, ruleModel());
        String[] splitRuleValue = ruleValue.split(Constants.COLON);
        Integer awardId = Integer.parseInt(splitRuleValue[0]);


        // check whether user is in the black List
        String[] userBlackIds = splitRuleValue[1].split(Constants.SPLIT);
        for (String userBlackId : userBlackIds) {
            if (userId.equals(userBlackId)) {
                log.info("Black List Chain take over userId: {} strategyId: {} ruleModel: {} awardId: {}", userId, strategyId, ruleModel(), awardId);
                return awardId;
            }
        }
        // pass to another chain
        log.info("Black List Chain passed userId: {} strategyId: {} ruleModel: {}", userId, strategyId, ruleModel());
        return next().logic(userId, strategyId);
    }
}
