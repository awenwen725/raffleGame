package com.awenwen.domain.strategy.service;

import com.awenwen.domain.strategy.model.entity.RaffleAwardEntity;
import com.awenwen.domain.strategy.model.entity.RaffleFactorEntity;

/**
 * @author awenwen
 * @description
 * @create 2025/11/18 15:34
 */
public interface IRaffleStrategy {
    /**
     * perform raffle；用抽奖因子入参，执行抽奖计算，返回奖品信息
     *
     * @param raffleFactorEntity 抽奖因子实体对象，根据入参信息计算抽奖结果
     * @return award entity
     */
    RaffleAwardEntity performRaffle(RaffleFactorEntity raffleFactorEntity);
}
