package com.awenwen.domain.strategy.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author awenwen
 * @description award Entity
 * @create 2025/11/16 15:33
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AwardEntity {
    /** user ID */
    private String userId;
    /** award ID */
    private Integer awardId;
}
