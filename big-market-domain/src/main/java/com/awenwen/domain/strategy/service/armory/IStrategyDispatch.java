package com.awenwen.domain.strategy.service.armory;

/**
 * @author awenwen
 * @description
 * @create 2025/11/16 22:12
 */
public interface IStrategyDispatch {
    Integer getRandomAwardId(Long strategyId);

    Integer getRandomAwardId(Long strategyId, String ruleWeightValue);

}
