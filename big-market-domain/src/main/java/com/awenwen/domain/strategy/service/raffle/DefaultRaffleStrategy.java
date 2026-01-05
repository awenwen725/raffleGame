package com.awenwen.domain.strategy.service.raffle;

import com.awenwen.domain.strategy.model.entity.StrategyAwardEntity;
import com.awenwen.domain.strategy.model.valobj.RuleTreeVO;
import com.awenwen.domain.strategy.model.valobj.StrategyAwardRuleModelVO;
import com.awenwen.domain.strategy.model.valobj.StrategyAwardStockKeyVO;
import com.awenwen.domain.strategy.repository.IStrategyRepository;
import com.awenwen.domain.strategy.service.IRaffleAward;
import com.awenwen.domain.strategy.service.IRaffleStock;
import com.awenwen.domain.strategy.service.IRaffleStrategy;
import com.awenwen.domain.strategy.service.armory.IStrategyDispatch;
import com.awenwen.domain.strategy.service.AbstractRaffleStrategy;
import com.awenwen.domain.strategy.service.rule.chain.ILogicChain;
import com.awenwen.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import com.awenwen.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import com.awenwen.domain.strategy.service.rule.tree.factory.engine.IDecisionTreeEngine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;


/**
 * @author awenwen
 * @description
 * @create 2025/11/18 15:38
 */
@Slf4j
@Service
public class DefaultRaffleStrategy extends AbstractRaffleStrategy implements IRaffleStock, IRaffleAward {

    public DefaultRaffleStrategy(IStrategyRepository repository, IStrategyDispatch strategyDispatch, DefaultChainFactory defaultChainFactory, DefaultTreeFactory defaultTreeFactory) {
        super(repository, strategyDispatch, defaultChainFactory, defaultTreeFactory);
    }

    @Override
    public DefaultChainFactory.StrategyAwardVO raffleLogicChain(String userId, Long strategyId) {
        ILogicChain logicChain = defaultChainFactory.openLogicChain(strategyId);
        return logicChain.logic(userId, strategyId);
    }

    @Override
    public DefaultTreeFactory.StrategyAwardVO raffleLogicTree(String userId, Long strategyId, Integer awardId) {
        StrategyAwardRuleModelVO strategyAwardRuleModelVO = repository.queryStrategyAwardRuleModelVO(strategyId, awardId);
        if (null == strategyAwardRuleModelVO) {
            return DefaultTreeFactory.StrategyAwardVO.builder().awardId(awardId).build();
        }
        RuleTreeVO ruleTreeVO = repository.queryRuleTreeVOByTreeId(strategyAwardRuleModelVO.getRuleModels());
        if (null == ruleTreeVO) {
            throw new RuntimeException("Raffle Strategy exists，NOT FOUND configurations of rule_tree、rule_tree_node、rule_tree_line in Database" + strategyAwardRuleModelVO.getRuleModels());
        }
        IDecisionTreeEngine treeEngine = defaultTreeFactory.openLogicTree(ruleTreeVO);
        return treeEngine.process(userId, strategyId, awardId);
    }

    @Override
    public StrategyAwardStockKeyVO takeQueueValue() throws InterruptedException {
        return repository.takeQueueValue();
    }

    @Override
    public void updateStrategyAwardStock(Long strategyId, Integer awardId) {
        repository.updateStrategyAwardStock(strategyId, awardId);
    }

    @Override
    public List<StrategyAwardEntity> queryRaffleStrategyAwardList(Long strategyId) {
        return repository.queryStrategyAwardList(strategyId);
    }
}
