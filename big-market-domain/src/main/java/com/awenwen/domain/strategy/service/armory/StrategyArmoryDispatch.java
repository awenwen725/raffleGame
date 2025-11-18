package com.awenwen.domain.strategy.service.armory;

import com.awenwen.domain.strategy.model.entity.StrategyAwardEntity;
import com.awenwen.domain.strategy.model.entity.StrategyEntity;
import com.awenwen.domain.strategy.model.entity.StrategyRuleEntity;
import com.awenwen.domain.strategy.repository.IStrategyRepository;
import com.awenwen.types.common.Constants;
import com.awenwen.types.enums.ResponseCode;
import com.awenwen.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.*;

/**
 * @author awenwen
 * @description
 * @create 2025/11/15 23:04
 */
@Slf4j
@Service
public class StrategyArmoryDispatch implements IStrategyArmory, IStrategyDispatch {
    @Resource
    private IStrategyRepository repository;

    private final SecureRandom random = new SecureRandom();

    @Override
    public boolean assembleLotteryStrategy(Long strategyId) {
        // 1. query the strategy configuration
        List<StrategyAwardEntity> strategyAwardEntities = repository.queryStrategyAwardList(strategyId);
        assembleLotteryStrategy(String.valueOf(strategyId), strategyAwardEntities);
        // 2. lottery weight configuration
        StrategyEntity strategyEntity = repository.queryStrategyEntityByStrategyId(strategyId);
        String ruleWeight = strategyEntity.getRuleWeight();
        if (null == ruleWeight) {
            return true;
        }

        // 3. parse the strategy rule
        StrategyRuleEntity strategyRuleEntity = repository.queryStrategyRule(strategyId, ruleWeight);
        if (null == strategyRuleEntity) {
            throw new AppException(ResponseCode.STRATEGY_RULE_WEIGHT_IS_NULL.getCode(),
                    ResponseCode.STRATEGY_RULE_WEIGHT_IS_NULL.getInfo());
        }

        // assemble for each weight strategy
        Map<String, List<Integer>> ruleWeightValueMap = strategyRuleEntity.getRuleWeightValues();
        for (String WeightRule : ruleWeightValueMap.keySet()) {
            List<Integer> ruleWeightValue = ruleWeightValueMap.get(WeightRule);
            ArrayList<StrategyAwardEntity> strategyAwardListEntitiesClone = new ArrayList<>(strategyAwardEntities);
            strategyAwardListEntitiesClone.removeIf(entity -> !ruleWeightValue.contains(entity.getAwardId()));
            assembleLotteryStrategy(String.valueOf(strategyId).concat(Constants.UNDERLINE).concat(WeightRule), strategyAwardListEntitiesClone);
        }

        return true;
    }

    private void assembleLotteryStrategy(String key, List<StrategyAwardEntity> strategyAwardEntities) {
        // 2.acquire minimum probability
        BigDecimal minAwardRate = strategyAwardEntities.stream()
                .map(StrategyAwardEntity::getAwardRate)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);

        // 3. acquire total probability
        BigDecimal totalRate = strategyAwardEntities.stream()
                .map(StrategyAwardEntity::getAwardRate)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 4. generate random number map according to probabilities
        BigDecimal rateRange = totalRate.divide(minAwardRate, 0, RoundingMode.CEILING);

        ArrayList<Integer> strategyAwardSearchRateTable = new ArrayList<>(rateRange.intValue());
        for (StrategyAwardEntity strategyAwardEntity : strategyAwardEntities) {
            Integer awardId = strategyAwardEntity.getAwardId();
            BigDecimal awardRate = strategyAwardEntity.getAwardRate();

            // calculate the number bits takes for each award
            for (int i = 0;
                 i < rateRange.multiply(awardRate)
                         .setScale(0, RoundingMode.CEILING).intValue();
                 i++
            ) {
                strategyAwardSearchRateTable.add(awardId);
            }
        }

        // 5. shuffle map
        Collections.shuffle(strategyAwardSearchRateTable, random);

        // 6. trans to map
        HashMap<Integer, Integer> shuffledStrategyAwardSearchRateTable = new HashMap<>();
        for (int i = 0; i < strategyAwardSearchRateTable.size(); i++) {
            shuffledStrategyAwardSearchRateTable.put(i, strategyAwardSearchRateTable.get(i));
        }

        // 7. add to redis
        repository.storeStrategyAwardSearchRateTable(key, shuffledStrategyAwardSearchRateTable.size(), shuffledStrategyAwardSearchRateTable);

    }

    @Override
    public Integer getRandomAwardId(Long strategyId) {
        int rateRange = repository.getRateRange(strategyId);

        return repository.getStrategyAwardAssemble(String.valueOf(strategyId), new SecureRandom().nextInt(rateRange));
    }

    @Override
    public Integer getRandomAwardId(Long strategyId, String ruleWeightValue) {
        String key = String.valueOf(strategyId).concat(Constants.UNDERLINE).concat(ruleWeightValue);
        int rateRange = repository.getRateRange(key);

        return repository.getStrategyAwardAssemble(key, new SecureRandom().nextInt(rateRange));
    }




}
