package com.awenwen.domain.strategy.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author awenwen
 * @description
 * @create 2025/11/16 15:34
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StrategyConditionEntity {
    /** 用户ID */
    private String userId;
    /** 策略ID */
    private Integer strategyId;
}
