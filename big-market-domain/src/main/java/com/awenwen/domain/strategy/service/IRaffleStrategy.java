package com.awenwen.domain.strategy.service;

import com.awenwen.domain.strategy.model.entity.RaffleAwardEntity;
import com.awenwen.domain.strategy.model.entity.RaffleFactorEntity;

/**
 * @author awenwen
 * @description the interface of raffle perform
 * @create 2025/11/18 15:34
 */
public interface IRaffleStrategy {
    /**
     * perform raffle
     *
     * @param raffleFactorEntity parameter for raffle
     * @return award entity
     */
    RaffleAwardEntity performRaffle(RaffleFactorEntity raffleFactorEntity);
}
