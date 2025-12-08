package com.awenwen.domain.strategy.service.rule.tree.factory.engine;

import com.awenwen.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;

/**
 * @author awenwen
 * @description core process of Logic Tree
 * @create 2025/12/8 12:42
 */
public interface IDecisionTreeEngine {
    DefaultTreeFactory.StrategyAwardVO process(String userId, Long strategyId, Integer awardId);

}
