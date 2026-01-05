package com.awenwen.domain.strategy.service;

import com.awenwen.domain.strategy.model.valobj.StrategyAwardStockKeyVO;

/**
 * @author awenwen
 * @description interface of consumer which updates database in stock substraction
 * @create 2025/12/13 14:51
 */
public interface IRaffleStock {
    /**
     * acquire data from stock substraction message queue
     * @return information object used in subtract stock
     * @throws InterruptedException the case when queue is null
     */
    StrategyAwardStockKeyVO takeQueueValue() throws InterruptedException;

    /**
     * update stock subtraction information
     * @param strategyId strategy ID
     * @param awardId award ID
     */
    void updateStrategyAwardStock(Long strategyId, Integer awardId);
}
