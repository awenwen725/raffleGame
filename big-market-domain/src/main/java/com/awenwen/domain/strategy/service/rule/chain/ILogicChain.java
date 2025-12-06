package com.awenwen.domain.strategy.service.rule.chain;

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
    Integer logic(String userId, Long strategyId);
}
