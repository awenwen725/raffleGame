package com.awenwen.domain.strategy.service.rule.tree.impl;

import com.awenwen.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
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

    @Override
    public DefaultTreeFactory.TreeActionEntity logic(String userId, Long strategyId, Integer awardId) {
        return DefaultTreeFactory.TreeActionEntity.builder()
                .ruleLogicCheckType(RuleLogicCheckTypeVO.TAKE_OVER)
                .build();
    }
}
