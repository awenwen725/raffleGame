package com.awenwen.domain.strategy.service.rule.chain.factory;

import com.awenwen.domain.strategy.model.entity.StrategyEntity;
import com.awenwen.domain.strategy.repository.IStrategyRepository;
import com.awenwen.domain.strategy.service.rule.chain.ILogicChain;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author awenwen
 * @description factory class to manage responsibility chain
 * @create 2025/12/7 22:15
 */
@Service
public class DefaultChainFactory {
    private final Map<String, ILogicChain> logicChainGroup;
    protected IStrategyRepository repository;
    // directly injection by constructor
    public DefaultChainFactory(Map<String, ILogicChain> logicChainGroup, IStrategyRepository repository) {
        this.logicChainGroup = logicChainGroup;
        this.repository = repository;
    }


    /**
     * initialize the responsibility chain
     * @param strategyId strategy ID
     * @return dispatched chain
     */
    public ILogicChain openLogicChain(Long strategyId) {
        StrategyEntity strategy = repository.queryStrategyEntityByStrategyId(strategyId);
        String[] ruleModels = strategy.ruleModels();
        // dispatch the default chain when no result from database
        if (null == ruleModels || 0 == ruleModels.length) return logicChainGroup.get("default");

        // dispatch chain
        ILogicChain logicChain = logicChainGroup.get(ruleModels[0]);
        ILogicChain current = logicChain;
        for (int i = 1; i < ruleModels.length; i++) {
            ILogicChain nextChain = logicChainGroup.get(ruleModels[i]);
            current = current.appendNext(nextChain);
        }

        // add default chain in the last
        current.appendNext(logicChainGroup.get("default"));
        return logicChain;
    }

}
