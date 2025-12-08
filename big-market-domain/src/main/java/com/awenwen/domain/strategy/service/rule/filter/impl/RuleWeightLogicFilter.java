package com.awenwen.domain.strategy.service.rule.filter.impl;

import com.awenwen.domain.strategy.model.entity.RuleActionEntity;
import com.awenwen.domain.strategy.model.entity.RuleMatterEntity;
import com.awenwen.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import com.awenwen.domain.strategy.repository.IStrategyRepository;
import com.awenwen.domain.strategy.service.annotation.LogicStrategy;
import com.awenwen.domain.strategy.service.rule.filter.ILogicFilter;
import com.awenwen.domain.strategy.service.rule.filter.factory.DefaultLogicFactory;
import com.awenwen.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author awenwen
 * @description
 * @create 2025/11/18 15:39
 */
@Slf4j
@Component
@LogicStrategy(logicMode = DefaultLogicFactory.LogicModel.RULE_WIGHT)
public class RuleWeightLogicFilter implements ILogicFilter {
    @Resource
    private IStrategyRepository repository;

    public Long userScore = 10L;

    @Override
    public RuleActionEntity filter(RuleMatterEntity ruleMatterEntity) {
        log.info("规则过滤-权重范围 userId:{} strategyId:{} ruleModel:{}", ruleMatterEntity.getUserId(), ruleMatterEntity.getStrategyId(), ruleMatterEntity.getRuleModel());

        String userId = ruleMatterEntity.getUserId();
        Long strategyId = ruleMatterEntity.getStrategyId();
        String ruleValue = repository.queryStrategyRuleValue(ruleMatterEntity.getStrategyId(), ruleMatterEntity.getAwardId(), ruleMatterEntity.getRuleModel());

        //1. query User score (not finished, ), and create ruleValueMap for different weight rule
        Map<Long, String> analyticalValues = getAnalyticalValue(ruleValue);
        if (null == analyticalValues || analyticalValues.isEmpty()) {
            return RuleActionEntity.<RuleActionEntity.RaffleBeforeEntity>builder()
                    .code(RuleLogicCheckTypeVO.ALLOW.getCode())
                    .info(RuleLogicCheckTypeVO.ALLOW.getInfo())
                    .build();
        }

        // 2. sort the keys
        List<Long> analyticalSortedKeys = new ArrayList<>(analyticalValues.keySet());
        Collections.sort(analyticalSortedKeys);

        // 3. find the rule less than user score
        Long nextValue = analyticalSortedKeys.stream()
                .filter(key -> userScore >= key)
                .findFirst()
                .orElse(null);

        if (null != nextValue) {
            return RuleActionEntity.<RuleActionEntity.RaffleBeforeEntity>builder()
                    .data(RuleActionEntity.RaffleBeforeEntity.builder()
                            .strategyId(strategyId)
                            .ruleWeightValueKey(analyticalValues.get(nextValue))
                            .build())
                    .ruleModel(DefaultLogicFactory.LogicModel.RULE_WIGHT.getCode())
                    .code(RuleLogicCheckTypeVO.TAKE_OVER.getCode())
                    .info(RuleLogicCheckTypeVO.TAKE_OVER.getInfo())
                    .build();
        }

        return RuleActionEntity.<RuleActionEntity.RaffleBeforeEntity>builder()
                .code(RuleLogicCheckTypeVO.ALLOW.getCode())
                .info(RuleLogicCheckTypeVO.ALLOW.getInfo())
                .build();
    }

    /**
     *  split several weight rules
     * @param ruleValue
     * @return
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

