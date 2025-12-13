package com.awenwen.trigger.job;

import com.awenwen.domain.strategy.model.valobj.StrategyAwardStockKeyVO;
import com.awenwen.domain.strategy.service.IRaffleStock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author awenwen
 * @description
 * @create 2025/12/13 15:09
 */
@Slf4j
@Component
public class UpdateAwardStockJob {
    /** constructor injection */
    private IRaffleStock raffleStock;

    public UpdateAwardStockJob(IRaffleStock raffleStock) {
        this.raffleStock = raffleStock;
    }

    @Scheduled(cron = "0/5 * * * * ?")
    public void exec() {
        try {
            log.info("timer task, update raffle stock subtraction");
            StrategyAwardStockKeyVO strategyAwardStockKeyVO = raffleStock.takeQueueValue();
            if (null == strategyAwardStockKeyVO) return;
            log.info("timer task, update raffle stock subtraction, strategyId:{} awardId:{}", strategyAwardStockKeyVO.getStrategyId(), strategyAwardStockKeyVO.getAwardId());
            raffleStock.updateStrategyAwardStock(strategyAwardStockKeyVO.getStrategyId(), strategyAwardStockKeyVO.getAwardId());
        } catch (Exception e) {
            log.error("timer task, update raffle stock subtraction error!", e);
        }
    }
}
