package com.awenwen.domain.strategy.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author awenwen
 * @description awardEntity
 * @create 2025/11/16 15:33
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AwardEntity {
    /** 用户ID */
    private String userId;
    /** 奖品ID */
    private Integer awardId;
}
