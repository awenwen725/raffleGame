package com.awenwen.domain.strategy.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author awenwen
 * @description parameters for raffle
 * @create 2025/11/18 15:37
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RaffleFactorEntity {
    /** user ID */
    private String userId;
    /** strategy ID */
    private Long strategyId;
    /** award ID*/
    public Integer awardId;
}
