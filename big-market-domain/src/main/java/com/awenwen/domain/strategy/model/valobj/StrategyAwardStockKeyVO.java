package com.awenwen.domain.strategy.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author awenwen
 * @description information object used when subtracting stock
 * @create 2025/12/12 15:51
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StrategyAwardStockKeyVO {

    // Strategy ID
    private Long strategyId;
    // Award ID
    private Integer awardId;
}
