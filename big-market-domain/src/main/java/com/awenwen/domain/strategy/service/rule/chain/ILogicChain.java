package com.awenwen.domain.strategy.service.rule.chain;

import com.awenwen.domain.strategy.service.rule.chain.factory.DefaultChainFactory;

/**
 * @author awenwen
 * @description interface to perform responsibility chain
 * @create 2025/12/6 22:50
 */
public interface ILogicChain extends ILogicChainArmory{
    /**
     * 责任链接口
     *
     * @param userId    user ID
     * @param strategyId strategy ID
     * @return award ID
     */
    DefaultChainFactory.StrategyAwardVO logic(String userId, Long strategyId);
}
