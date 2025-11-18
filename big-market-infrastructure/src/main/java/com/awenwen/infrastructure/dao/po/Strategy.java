package com.awenwen.infrastructure.dao.po;

import lombok.Data;

import java.util.Date;
/**
 * @author awenwen
 * @description Award gotten Strategy
 * @create 2025/11/13 22:26
 */
@Data
public class Strategy {
    /**
     * 自增ID
     */
    private Long id;
    /**
     * 抽奖策略ID
     */
    private Long strategyId;
    /**
     * 抽奖策略描述
     */
    private String strategyDesc;

    /** 规则模型，rule配置的模型同步到此表，便于使用*/
    private String ruleModels;

    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
}
