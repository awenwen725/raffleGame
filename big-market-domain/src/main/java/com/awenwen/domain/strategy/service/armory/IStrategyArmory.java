package com.awenwen.domain.strategy.service.armory;

/**
 * @author awenwen
 * @description strategy deployment
 * @create 2025/11/15 23:01
 */
public interface IStrategyArmory {

    /**
     * 装配抽奖策略配置「触发的时机可以为活动审核通过后进行调用」
     *
     * @param strategyId 策略ID
     * @return 装配结果
     */
    boolean assembleLotteryStrategy(Long strategyId);



    Integer getRandomAwardId(Long strategyId);

    /**
     * 装配抽奖策略配置「触发的时机可以为活动审核通过后进行调用」
     *
     * @param activityId 活动ID
     * @return 装配结果
     */
    // boolean assembleLotteryStrategyByActivityId(Long activityId);
}
