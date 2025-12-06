package com.awenwen.domain.strategy.repository;

import com.awenwen.domain.strategy.model.entity.StrategyAwardEntity;
import com.awenwen.domain.strategy.model.entity.StrategyEntity;
import com.awenwen.domain.strategy.model.entity.StrategyRuleEntity;
import com.awenwen.domain.strategy.model.valobj.StrategyAwardRuleModelVO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author awenwen
 * @description strategy repository
 * @create 2025/11/15 23:06
 */
public interface IStrategyRepository {

    List<StrategyAwardEntity> queryStrategyAwardList(Long strategyId);

    <K, V> void storeStrategyAwardSearchRateTable(String key, Integer rateRange, Map<K, V> strategyAwardSearchRateTable);

    int getRateRange(Long strategyId);

    int getRateRange(String key);

    Integer getStrategyAwardAssemble(String key, int rateKey);

    StrategyEntity queryStrategyEntityByStrategyId(Long strategyId);

    StrategyRuleEntity queryStrategyRule(Long strategyId, String ruleWeight);

    String queryStrategyRuleValue(Long strategyId, Integer awardId, String ruleModel);

    StrategyAwardRuleModelVO queryStrategyAwardRuleModelVO(Long strategyId, Integer awardId);
}
