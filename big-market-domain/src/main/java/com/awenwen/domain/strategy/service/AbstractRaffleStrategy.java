package com.awenwen.domain.strategy.service;

import com.awenwen.domain.strategy.model.entity.RaffleAwardEntity;
import com.awenwen.domain.strategy.model.entity.RaffleFactorEntity;
import com.awenwen.domain.strategy.model.entity.RuleActionEntity;
import com.awenwen.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import com.awenwen.domain.strategy.model.valobj.StrategyAwardRuleModelVO;
import com.awenwen.domain.strategy.repository.IStrategyRepository;
import com.awenwen.domain.strategy.service.armory.IStrategyDispatch;
import com.awenwen.domain.strategy.service.rule.chain.ILogicChain;
import com.awenwen.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import com.awenwen.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import com.awenwen.types.enums.ResponseCode;
import com.awenwen.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @author awenwen
 * @description define the standard raffle process
 * @create 2025/11/18 15:37
 */
@Slf4j
public abstract class AbstractRaffleStrategy implements IRaffleStrategy {
    /** constructor injection */
    // operation with database
    protected IStrategyRepository repository;
    // perform raffle
    protected IStrategyDispatch strategyDispatch;
    // perform logic chain
    protected final DefaultChainFactory defaultChainFactory;
    // perform logic tree
    protected final DefaultTreeFactory defaultTreeFactory;

    public AbstractRaffleStrategy(IStrategyRepository repository, IStrategyDispatch strategyDispatch, DefaultChainFactory defaultChainFactory,  DefaultTreeFactory defaultTreeFactory) {
        this.repository = repository;
        this.strategyDispatch = strategyDispatch;
        this.defaultChainFactory = defaultChainFactory;
        this.defaultTreeFactory = defaultTreeFactory;
    }


    @Override
    public RaffleAwardEntity performRaffle(RaffleFactorEntity raffleFactorEntity) {
        // 1. parameter check
        String userId = raffleFactorEntity.getUserId();
        Long strategyId = raffleFactorEntity.getStrategyId();
        if (null == strategyId || StringUtils.isBlank(userId)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(),
                    ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }

        // 2. perform logic chain
        DefaultChainFactory.StrategyAwardVO chainStrategyAwardVO = raffleLogicChain(userId, strategyId);
        log.info("Before Raffle, Logic Chain {} {} {} {}", userId, strategyId, chainStrategyAwardVO.getAwardId(), chainStrategyAwardVO.getLogicModel());
        // return when perform default rule
        if (!DefaultChainFactory.LogicModel.RULE_DEFAULT.getCode().equals(chainStrategyAwardVO.getLogicModel())) {
            return RaffleAwardEntity.builder()
                    .awardId(chainStrategyAwardVO.getAwardId())
                    .build();
        }

        // 3. perform Logic Tree
        DefaultTreeFactory.StrategyAwardVO treeStrategyAwardVO = raffleLogicTree(userId, strategyId, chainStrategyAwardVO.getAwardId());
        log.info("Centre raffle, Logic Tree, {} {} {} {}", userId, strategyId, treeStrategyAwardVO.getAwardId(), treeStrategyAwardVO.getAwardRuleValue());

        // 4. return award
        return RaffleAwardEntity.builder()
                .awardId(treeStrategyAwardVO.getAwardId())
                .awardConfig(treeStrategyAwardVO.getAwardRuleValue())
                .build();
    }

    /**
     * abstract method, perform logic chain
     *
     * @param userId user ID
     * @param strategyId strategy ID
     * @return DefaultChainFactory.StrategyAwardVO: information of award after logic chain
     */
    public abstract DefaultChainFactory.StrategyAwardVO raffleLogicChain(String userId, Long strategyId) ;

    /**
     * abstract method, perform logic tree
     *
     * @param userId user ID
     * @param strategyId strategy ID
     * @param awardId award ID
     * @return DefaultTreeFactory.StrategyAwardVO: information of final acquired award
     */
    public abstract DefaultTreeFactory.StrategyAwardVO raffleLogicTree(String userId, Long strategyId, Integer awardId);
}
