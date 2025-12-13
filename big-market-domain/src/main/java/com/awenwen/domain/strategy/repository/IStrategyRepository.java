package com.awenwen.domain.strategy.repository;

import com.awenwen.domain.strategy.model.entity.StrategyAwardEntity;
import com.awenwen.domain.strategy.model.entity.StrategyEntity;
import com.awenwen.domain.strategy.model.entity.StrategyRuleEntity;
import com.awenwen.domain.strategy.model.valobj.RuleTreeVO;
import com.awenwen.domain.strategy.model.valobj.StrategyAwardRuleModelVO;
import com.awenwen.domain.strategy.model.valobj.StrategyAwardStockKeyVO;

import java.util.List;
import java.util.Map;

/**
 * @author awenwen
 * @description strategy repository
 * @create 2025/11/15 23:06
 */
public interface IStrategyRepository {

    List<StrategyAwardEntity> queryStrategyAwardList(Long strategyId);

    void storeStrategyAwardSearchRateTable(String key, Integer rateRange, Map<Integer, Integer> strategyAwardSearchRateTable);

    int getRateRange(Long strategyId);

    int getRateRange(String key);

    Integer getStrategyAwardAssemble(String key, int rateKey);

    StrategyEntity queryStrategyEntityByStrategyId(Long strategyId);

    StrategyRuleEntity queryStrategyRule(Long strategyId, String ruleWeight);

    String queryStrategyRuleValue(Long strategyId, Integer awardId, String ruleModel);

    String queryStrategyRuleValue(Long strategyId, String ruleModel);

    StrategyAwardRuleModelVO queryStrategyAwardRuleModelVO(Long strategyId, Integer awardId);

    /**
     * query Logic Tree according to tree ID
     * @param treeId logic tree ID
     * @return the root node of Logic Tree
     */
    RuleTreeVO queryRuleTreeVOByTreeId(String treeId);

    /**
     * subtract stock in cache
     * @param cacheKey cache key
     * @return  subtraction result
     */
    Boolean subtractionAwardStock(String cacheKey);


    /**
     * add order information into message queue
     * @param strategyAwardStockKeyVO user information object
     */
    void awardStockConsumeSendQueue(StrategyAwardStockKeyVO strategyAwardStockKeyVO);

    void updateStrategyAwardStock(Long strategyId, Integer awardId);

    StrategyAwardStockKeyVO takeQueueValue() throws InterruptedException;
}
