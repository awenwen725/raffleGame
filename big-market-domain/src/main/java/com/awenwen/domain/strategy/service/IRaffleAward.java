package com.awenwen.domain.strategy.service;

import com.awenwen.domain.strategy.model.entity.StrategyAwardEntity;

import java.util.List;

/**
 * @author awenwen
 * @description query award list in a strategy
 * @create 2025/12/13 18:31
 */
public interface IRaffleAward {


    /**
     * return Award list in a strategy
     * @param strategyId strategy ID
     * @return Award list
     */
    List<StrategyAwardEntity> queryRaffleStrategyAwardList(Long strategyId);
}
