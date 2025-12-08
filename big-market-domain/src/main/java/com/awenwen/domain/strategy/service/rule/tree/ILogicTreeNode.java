package com.awenwen.domain.strategy.service.rule.tree;

import com.awenwen.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;

/**
 * @author awenwen
 * @description core action of Logic Tree Decision Node, decide Logic Tree's direction
 * @create 2025/12/8 12:41
 */
public interface ILogicTreeNode {
    DefaultTreeFactory.TreeActionEntity logic(String userId, Long strategyId, Integer awardId);
}
