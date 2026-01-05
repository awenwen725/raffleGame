package com.awenwen.domain.strategy.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author awenwen
 * @description detail information of award configured in certain strategy
 * @create 2025/11/16 15:34
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StrategyAwardEntity {
    /** strategy ID */
    private Long strategyId;
    /** award ID */
    private Integer awardId;
    /** award title */
    private String awardTitle;
    /** award subtitle */
    private String awardSubtitle;
    /** award total stock */
    private Integer awardCount;
    /** award remain stock */
    private Integer awardCountSurplus;
    /** rate list */
    private BigDecimal awardRate;
    /** sort */
    private Integer sort;
    /** rule model */
    private String ruleModels;
}
