package com.awenwen.domain.strategy.repository;

import com.awenwen.domain.strategy.model.entity.StrategyAwardEntity;

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

    <K, V> void storeStrategyAwardSearchRateTable(Long key, Integer rateRange, Map<K, V> strategyAwardSearchRateTable);

    int getRateRange(Long strategyId);

    Integer getStrategyAwardAssemble(Long strategyId, int rateKey);
}
