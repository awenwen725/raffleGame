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
    private final DefaultChainFactory defaultChainFactory;

    public AbstractRaffleStrategy(IStrategyRepository repository, IStrategyDispatch strategyDispatch, DefaultChainFactory defaultChainFactory) {
        this.repository = repository;
        this.strategyDispatch = strategyDispatch;
        this.defaultChainFactory = defaultChainFactory;
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

        // 2. initial logic chain to filter before raffle
        ILogicChain logicChain = defaultChainFactory.openLogicChain(strategyId);

        // 3. perform logic chain, get award ID
        Integer awardId = logicChain.logic(userId, strategyId);

        // 4. query strategy applied in the middle or end of raffle process
        StrategyAwardRuleModelVO strategyAwardRuleModelVO = repository.queryStrategyAwardRuleModelVO(strategyId, awardId);

        // raffling
        RuleActionEntity<RuleActionEntity.RaffleCenterEntity> ruleActionCenterEntity = this.doCheckRaffleCenterLogic(
                RaffleFactorEntity
                        .builder()
                        .userId(userId)
                        .strategyId(strategyId)
                        .build(), strategyAwardRuleModelVO.raffleCenterRuleModelList());
        // apply centre rule
        if (RuleLogicCheckTypeVO.TAKE_OVER.getCode().equals(ruleActionCenterEntity.getCode())){
            log.info("centre raffle: rule_luck_award, get default award due to no stocks");
            return RaffleAwardEntity.builder()
                    .awardDesc("entre raffle: rule_luck_award, get default award due to no stocks")
                    .build();
        }

        // 6. default method
        return RaffleAwardEntity.builder()
                .awardId(awardId)
                .build();
    }

    protected abstract RuleActionEntity<RuleActionEntity.RaffleCenterEntity> doCheckRaffleCenterLogic(RaffleFactorEntity raffleFactorEntity, String... logics);
}
