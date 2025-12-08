package com.awenwen.domain.strategy.service.rule.tree.factory.engine.impl;

import com.awenwen.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import com.awenwen.domain.strategy.model.valobj.RuleTreeNodeLineVO;
import com.awenwen.domain.strategy.model.valobj.RuleTreeNodeVO;
import com.awenwen.domain.strategy.model.valobj.RuleTreeVO;
import com.awenwen.domain.strategy.service.rule.tree.ILogicTreeNode;
import com.awenwen.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import com.awenwen.domain.strategy.service.rule.tree.factory.engine.IDecisionTreeEngine;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 * @author awenwen
 * @description implementation of Logic Tree Engine
 * @create 2025/12/8 12:44
 */
@Slf4j
public class DecisionTreeEngine implements IDecisionTreeEngine {

    /** Constructor Injection*/
    private final Map<String, ILogicTreeNode> logicTreeNodeGroup;

    // Tree object
    private final RuleTreeVO ruleTreeVO;

    public DecisionTreeEngine(Map<String, ILogicTreeNode> logicTreeNodeGroup, RuleTreeVO ruleTreeVO) {
        this.logicTreeNodeGroup = logicTreeNodeGroup;
        this.ruleTreeVO = ruleTreeVO;
    }

    @Override
    public DefaultTreeFactory.StrategyAwardData process(String userId, Long strategyId, Integer awardId) {
        DefaultTreeFactory.StrategyAwardData strategyAwardData = null;

        // acquire Tree node and
        String nextNode = ruleTreeVO.getTreeRootRuleNode();
        Map<String, RuleTreeNodeVO> treeNodeMap = ruleTreeVO.getTreeNodeMap();

        // perform Logic Tree
        RuleTreeNodeVO ruleTreeNode = treeNodeMap.get(nextNode);
        while (null != nextNode) {
            // acquire Decision Node
            ILogicTreeNode logicTreeNode = logicTreeNodeGroup.get(ruleTreeNode.getRuleKey());

            //
            DefaultTreeFactory.TreeActionEntity logicEntity = logicTreeNode.logic(userId, strategyId, awardId);
            RuleLogicCheckTypeVO ruleLogicCheckTypeVO = logicEntity.getRuleLogicCheckType();
            strategyAwardData = logicEntity.getStrategyAwardData();
            log.info("Logic Tree Engine,【{}】treeId:{} node:{} code:{}", ruleTreeVO.getTreeName(), ruleTreeVO.getTreeId(), nextNode, ruleLogicCheckTypeVO.getCode());

            // next node
            nextNode = nextNode(ruleLogicCheckTypeVO.getCode(), ruleTreeNode.getTreeNodeLineVOList());
            ruleTreeNode = treeNodeMap.get(nextNode);
        }


        return strategyAwardData;
    }

    private String nextNode(String matterValue, List<RuleTreeNodeLineVO> treeNodeLineVOList) {
        if (null == treeNodeLineVOList || treeNodeLineVOList.isEmpty()) return null;
        for (RuleTreeNodeLineVO nodeLine : treeNodeLineVOList) {
            if (decisionLogic(matterValue, nodeLine)) {
                return nodeLine.getRuleNodeTo();
            }
        }
        throw new RuntimeException("Logic Tree Engine，nextNode error，next executable node not found！");
    }

    public boolean decisionLogic(String matterValue, RuleTreeNodeLineVO nodeLine) {
        switch (nodeLine.getRuleLimitType()) {
            case EQUAL:
                return matterValue.equals(nodeLine.getRuleLimitValue().getCode());
            // current stage it is no need to implement
            case GT:
            case LT:
            case GE:
            case LE:
            default:
                return false;
        }
    }
}
