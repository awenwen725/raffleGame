package com.awenwen.domain.strategy.service.rule.impl;

import com.awenwen.domain.strategy.model.entity.RuleActionEntity;
import com.awenwen.domain.strategy.model.entity.RuleMatterEntity;
import com.awenwen.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import com.awenwen.domain.strategy.repository.IStrategyRepository;
import com.awenwen.domain.strategy.service.annotation.LogicStrategy;
import com.awenwen.domain.strategy.service.rule.ILogicFilter;
import com.awenwen.domain.strategy.service.rule.factory.DefaultLogicFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author awenwen
 * @description for some awards, they are available after several raffles.
 * this filter is applied at the middle in the raffle process
 * @create 2025/12/6 17:14
 */
@Slf4j
@Component
@LogicStrategy(logicMode = DefaultLogicFactory.LogicModel.RULE_LOCK)
public class RuleLockLogicFilter implements ILogicFilter<RuleActionEntity.RaffleCenterEntity> {

    @Resource
    private IStrategyRepository repository;

    // the times of raffles user has performed
    // right now is a scaler, query from database feature needed to be added
    private Long userRaffleCount = 0L;

    @Override
    public RuleActionEntity<RuleActionEntity.RaffleCenterEntity> filter(RuleMatterEntity ruleMatterEntity) {
        log.info("filter rule: perform times lock,  userId:{} strategyId:{} ruleModel:{}", ruleMatterEntity.getUserId(), ruleMatterEntity.getStrategyId(), ruleMatterEntity.getRuleModel());

        // query the raffle count needed in the system
        String ruleValue = repository.queryStrategyRuleValue(ruleMatterEntity.getStrategyId(), ruleMatterEntity.getAwardId(), ruleMatterEntity.getRuleModel());
        long raffleCount = Long.parseLong(ruleValue);

        // if user Raffle time is larger than defined, allow to pass
        if (userRaffleCount>= raffleCount) {
            return RuleActionEntity.<RuleActionEntity.RaffleCenterEntity>builder()
                    .code(RuleLogicCheckTypeVO.ALLOW.getCode())
                    .info(RuleLogicCheckTypeVO.ALLOW.getInfo())
                    .build();
        }

        // if user Raffle time is larger than defined, allow to pass
        return RuleActionEntity.<RuleActionEntity.RaffleCenterEntity>builder()
                .code(RuleLogicCheckTypeVO.TAKE_OVER.getCode())
                .info(RuleLogicCheckTypeVO.TAKE_OVER.getInfo())
                .build();
    }
}
