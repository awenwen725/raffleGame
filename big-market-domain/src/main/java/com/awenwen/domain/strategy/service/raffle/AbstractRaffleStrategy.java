package com.awenwen.domain.strategy.service.raffle;

import com.awenwen.domain.strategy.model.entity.RaffleAwardEntity;
import com.awenwen.domain.strategy.model.entity.RaffleFactorEntity;
import com.awenwen.domain.strategy.model.entity.RuleActionEntity;
import com.awenwen.domain.strategy.model.entity.StrategyEntity;
import com.awenwen.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import com.awenwen.domain.strategy.model.valobj.StrategyAwardRuleModelVO;
import com.awenwen.domain.strategy.repository.IStrategyRepository;
import com.awenwen.domain.strategy.service.IRaffleStrategy;
import com.awenwen.domain.strategy.service.armory.IStrategyDispatch;
import com.awenwen.domain.strategy.service.rule.factory.DefaultLogicFactory;
import com.awenwen.types.enums.ResponseCode;
import com.awenwen.types.exception.AppException;
import com.sun.corba.se.spi.servicecontext.UnknownServiceContext;
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
    protected IStrategyRepository repository;

    protected IStrategyDispatch strategyDispatch;

    public AbstractRaffleStrategy(IStrategyRepository repository, IStrategyDispatch strategyDispatch) {
        this.repository = repository;
        this.strategyDispatch = strategyDispatch;
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

        // 2. query strategy
        StrategyEntity strategy = repository.queryStrategyEntityByStrategyId(strategyId);

        // 3. set before raffle filter
        RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> raffleBeforeEntityRuleActionEntity = this.doCheckRaffleBeforeLogic(
                RaffleFactorEntity
                .builder()
                .userId(userId)
                .strategyId(strategyId)
                .build(), strategy.ruleModels());
        // handle two separate control flow: black list, rule weight
        if (RuleLogicCheckTypeVO.TAKE_OVER.getCode().equals(raffleBeforeEntityRuleActionEntity.getCode())) {
            if (DefaultLogicFactory.LogicModel.RULE_BLACKLIST.getCode().equals(raffleBeforeEntityRuleActionEntity.getRuleModel())) {
                // return fixed award
                return RaffleAwardEntity.builder()
                        .awardId(raffleBeforeEntityRuleActionEntity.getData().getAwardId())
                        .build();
            } else if (DefaultLogicFactory.LogicModel.RULE_WIGHT.getCode().equals(raffleBeforeEntityRuleActionEntity.getRuleModel())) {
                // set different
                RuleActionEntity.RaffleBeforeEntity raffleBeforeEntity = raffleBeforeEntityRuleActionEntity.getData();
                String ruleWeightValueKey = raffleBeforeEntity.getRuleWeightValueKey();
                Integer awardId = strategyDispatch.getRandomAwardId(strategyId, ruleWeightValueKey);
                return RaffleAwardEntity.builder()
                        .awardId(awardId)
                        .build();
            }
        }

        // 4. if not in two strategy, perform default raffle process
        Integer awardId = strategyDispatch.getRandomAwardId(strategyId);

        // 5. query strategy applied in the middle or end of raffle process
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

    protected abstract RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> doCheckRaffleBeforeLogic(RaffleFactorEntity raffleFactorEntity, String... logics);

    protected abstract RuleActionEntity<RuleActionEntity.RaffleCenterEntity> doCheckRaffleCenterLogic(RaffleFactorEntity raffleFactorEntity, String... logics);
}
