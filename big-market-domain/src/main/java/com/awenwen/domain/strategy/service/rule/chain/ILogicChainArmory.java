package com.awenwen.domain.strategy.service.rule.chain;

/**
 * @author awenwen
 * @description dispatch responsibility chain
 * @create 2025/12/6 22:50
 */
public interface ILogicChainArmory {
    ILogicChain next();

    ILogicChain appendNext(ILogicChain next);
}
