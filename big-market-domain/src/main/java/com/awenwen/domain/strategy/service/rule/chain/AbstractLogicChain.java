package com.awenwen.domain.strategy.service.rule.chain;

import lombok.extern.slf4j.Slf4j;

/**
 * @author awenwen
 * @description
 * @create 2025/12/6 22:50
 */
@Slf4j
public abstract class AbstractLogicChain implements ILogicChain{

    private ILogicChain next;

    @Override
    public ILogicChain next() {
        return next;
    }

    @Override
    public ILogicChain appendNext(ILogicChain next) {
        this.next = next;
        return next;
    }

    protected abstract String ruleModel();

}
