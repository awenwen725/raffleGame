package com.awenwen.domain.strategy.service.rule.chain.impl;

import com.awenwen.domain.strategy.repository.IStrategyRepository;
import com.awenwen.domain.strategy.service.armory.IStrategyDispatch;
import com.awenwen.domain.strategy.service.rule.chain.AbstractLogicChain;
import com.awenwen.domain.strategy.service.rule.chain.ILogicChain;
import com.awenwen.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import com.awenwen.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author awenwen
 * @description
 * @create 2025/12/6 23:32
 */
@Slf4j
@Component("rule_weight")
public class RuleWeightLogicChain extends AbstractLogicChain {
    @Resource
    private IStrategyRepository repository;

    @Resource
    protected IStrategyDispatch strategyDispatch;

    // need to add operation after user domain finished
    public Long userScore = 0L;

    /**
     * Rule Weight Chain
     * 1. rule_weight raw data format；4000:102,103,104,105 5000:102,103,104,105,106,107 6000:102,103,104,105,106,107,108,109
     * 2. parse format, find the strategy feats current user
     */
    @Override
    public DefaultChainFactory.StrategyAwardVO logic(String userId, Long strategyId) {
        log.info("Logic Chain, Rule Weight, userId: {} strategyId: {} ruleModel: {}", userId, strategyId, ruleModel());

        String ruleValue = repository.queryStrategyRuleValue(strategyId, ruleModel());

        // 1. parse raw data, like: 4000:102,103,104,105
        // into key-value form: 4000 -> 4000:102,103,104,105
        Map<Long, String> analyticalValueGroup = getAnalyticalValue(ruleValue);
        if (null == analyticalValueGroup || analyticalValueGroup.isEmpty()) {
            log.warn("rule Weight: warning no configuration of ruleValue userId: {} strategyId: {} ruleModel: {}", userId, strategyId, ruleModel());
            return next().logic(userId, strategyId);
        }

        // 2. transfer into List and sort
        List<Long> analyticalSortedKeys = new ArrayList<>(analyticalValueGroup.keySet());
        Collections.sort(analyticalSortedKeys);

        // find the minimum feasible strategy
        Long nextValue = analyticalSortedKeys.stream()
                .sorted(Comparator.reverseOrder())
                .filter(analyticalSortedKeyValue -> userScore >= analyticalSortedKeyValue)
                .findFirst()
                .orElse(null);

        // 3. perform raffle
        if (null != nextValue) {
            Integer awardId = strategyDispatch.getRandomAwardId(strategyId, analyticalValueGroup.get(nextValue));
            log.info("Logic Chain, Rule Weight take over, userId: {} strategyId: {} ruleModel: {} awardId: {}", userId, strategyId, ruleModel(), awardId);
            return DefaultChainFactory.StrategyAwardVO.builder()
                    .awardId(awardId)
                    .logicModel(ruleModel())
                    .build();
        }

        // 4. passing to other chain
        log.info("Logic Chain, Rule Weight allows pass, userId: {} strategyId: {} ruleModel: {}", userId, strategyId, ruleModel());
        return next().logic(userId, strategyId);
    }

    @Override
    protected String ruleModel() {
        return DefaultChainFactory.LogicModel.RULE_WEIGHT.getCode();
    }

    /**
     *  split several weight rules
     * @param ruleValue raw data from database
     * @return weight rule map
     * key: boundary score
     * value: available award ID List
     */

    private Map<Long, String> getAnalyticalValue(String ruleValue) {
        String[] ruleValueGroups = ruleValue.split(Constants.SPACE);
        Map<Long, String> ruleValueMap = new HashMap<>();

        for (String ruleValueGroup : ruleValueGroups) {
            if (ruleValueGroup == null || ruleValueGroup.isEmpty()) {
                return ruleValueMap;
            }
            String[] ruleValues = ruleValueGroup.split(Constants.COLON);
            if (ruleValues.length != 2) {
                throw new IllegalArgumentException("rule_weight rule_rule invalid input format" + ruleValue);
            }
            ruleValueMap.put(Long.parseLong(ruleValues[0]), ruleValueGroup);
        }
        return ruleValueMap;
    }
}
