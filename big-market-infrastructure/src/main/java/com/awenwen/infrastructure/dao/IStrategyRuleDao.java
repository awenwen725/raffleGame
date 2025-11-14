package com.awenwen.infrastructure.dao;

import com.awenwen.infrastructure.dao.po.StrategyRule;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author awenwen
 * @description 抽奖策略规则
 * @create 2025/11/13 22:26
 */
@Mapper
public interface IStrategyRuleDao {
    List<StrategyRule> queryStrategyRuleList();
}
