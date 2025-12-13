package com.awenwen.domain.strategy.service.armory;

/**
 * @author awenwen
 * @description Interface of performing raffle
 * @create 2025/11/16 22:12
 */
public interface IStrategyDispatch {

    /**
     * a test method for default rule
     * acquire award according to strategy
     * @param strategyId strategy ID
     * @return award ID
     */
    Integer getRandomAwardId(Long strategyId);

    /**
     * a test method for a given rule weight
     * acquire award according to strategy with a given rule weight
     * @param strategyId strategy ID
     * @param ruleWeightValue ruleWeightValue
     * @return award ID
     */
    Integer getRandomAwardId(Long strategyId, String ruleWeightValue);


    /**
     * a test method for a given rule weight under distribute circumstance
     * acquire award according to strategy with a given rule weight
     *
     * @param key = strategyId + _ + ruleWeightValue；
     * @return 抽奖结果
     */
    Integer getRandomAwardId(String key);

    /**
     * subtract stock by award ID and strategy ID
     * @param strategyId strategy ID
     * @param awardId   Award ID
     * @return substraction result
     */
    Boolean subtractionAwardStock(Long strategyId, Integer awardId);
}
