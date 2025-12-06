package com.awenwen.domain.strategy.service.rule;

import com.awenwen.domain.strategy.model.entity.RuleActionEntity;
import com.awenwen.domain.strategy.model.entity.RuleMatterEntity;

/**
 * @author awenwen
 * @description Filter used for implementing different strategy at different time in raffle process
 * Template parameter is class that differs filter with applying raffle stage: Before, Centre, After
 * @create 2025/11/18 15:35
 */
public interface ILogicFilter <T extends RuleActionEntity.RaffleEntity> {
    RuleActionEntity<T> filter(RuleMatterEntity ruleMatterEntity);

}
