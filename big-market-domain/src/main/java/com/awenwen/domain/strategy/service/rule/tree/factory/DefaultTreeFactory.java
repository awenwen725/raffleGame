package com.awenwen.domain.strategy.service.rule.tree.factory;

import com.awenwen.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import com.awenwen.domain.strategy.model.valobj.RuleTreeVO;
import com.awenwen.domain.strategy.service.rule.tree.ILogicTreeNode;
import com.awenwen.domain.strategy.service.rule.tree.factory.engine.IDecisionTreeEngine;
import com.awenwen.domain.strategy.service.rule.tree.factory.engine.impl.DecisionTreeEngine;
import lombok.*;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author awenwen
 * @description Logic Tree factory, managing Logical Tree perform
 * @create 2025/12/8 12:41
 */
@Service
public class DefaultTreeFactory {
    /** constructor injection*/
    private final Map<String, ILogicTreeNode> logicTreeNodeGroup;

    public DefaultTreeFactory() {
        this.logicTreeNodeGroup = new HashMap<>();
    }

    public IDecisionTreeEngine openLogicTree(RuleTreeVO ruleTreeVO) {
        return new DecisionTreeEngine(logicTreeNodeGroup, ruleTreeVO);
    }

    /**
     * value object used to judge next direction
     */
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TreeActionEntity {
        private RuleLogicCheckTypeVO ruleLogicCheckType;
        private StrategyAwardVO strategyAwardVO;
    }

    /**
     * Information used in the Logic Tree
     */
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class StrategyAwardVO {
        /** award ID */
        private Integer awardId;
        /** rule value */
        private String awardRuleValue;
    }
}
