package com.awenwen.domain.strategy.service.rule.tree.impl;

import com.awenwen.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import com.awenwen.domain.strategy.model.valobj.StrategyAwardStockKeyVO;
import com.awenwen.domain.strategy.repository.IStrategyRepository;
import com.awenwen.domain.strategy.service.armory.IStrategyDispatch;
import com.awenwen.domain.strategy.service.rule.tree.ILogicTreeNode;
import com.awenwen.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author awenwen
 * @description check stock remain
 * not yet fully accomplish, currently allow all nodes
 * @create 2025/12/8 16:34
 */
@Slf4j
@Component("rule_stock")
public class RuleStockLogicTreeNode implements ILogicTreeNode {

    private IStrategyRepository strategyRepository;

    private IStrategyDispatch strategyDispatch;

    /** Constructor Injection*/
    RuleStockLogicTreeNode(IStrategyRepository strategyRepository, IStrategyDispatch strategyDispatch) {
        this.strategyRepository = strategyRepository;
        this.strategyDispatch = strategyDispatch;
    }

    @Override
    public DefaultTreeFactory.TreeActionEntity logic(String userId, Long strategyId, Integer awardId, String ruleValue) {
        log.info("Decision Tree, stock check, userId:{} strategyId:{} awardId:{}", userId, strategyId, awardId);
        Boolean subtractStatus = strategyDispatch.subtractionAwardStock(strategyId, awardId);
        // cache substraction operation success
        if (subtractStatus) {
            log.info("Decision Tree, stock check success! userId:{} strategyId:{} awardId:{}", userId, strategyId, awardId);

            // add to message queue
            strategyRepository.awardStockConsumeSendQueue(StrategyAwardStockKeyVO.builder()
                    .strategyId(strategyId)
                    .awardId(awardId)
                    .build());

            return DefaultTreeFactory.TreeActionEntity.builder()
                    .ruleLogicCheckType(RuleLogicCheckTypeVO.TAKE_OVER)
                    .strategyAwardVO(DefaultTreeFactory.StrategyAwardVO.builder()
                            .awardId(awardId)
                            .awardRuleValue(ruleValue)
                            .build())
                    .build();
        }
        // substraction failure
        log.warn("Decision Tree, stock not enough waring! userId:{} strategyId:{} awardId:{}", userId, strategyId, awardId);
        return DefaultTreeFactory.TreeActionEntity.builder()
                .ruleLogicCheckType(RuleLogicCheckTypeVO.ALLOW)
                .build();
    }
}
