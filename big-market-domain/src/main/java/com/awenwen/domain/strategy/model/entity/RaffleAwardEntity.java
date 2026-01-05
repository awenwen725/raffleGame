package com.awenwen.domain.strategy.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author awenwen
 * @description information of won award
 * @create 2025/11/18 15:35
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RaffleAwardEntity {
    /** award ID */
    private Integer awardId;
    /** award description */
    private String awardConfig;
    /** number */
    private Integer sort;
}
