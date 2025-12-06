package com.awenwen.infrastructure.dao;

/**
 * @author awenwen
 * @description 策略奖品的明细配置 概率 规则
 * @create 2025/11/13 22:25
 */

import com.awenwen.infrastructure.dao.po.StrategyAward;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IStrategyAwardDao{
    List<StrategyAward> queryStrategyAwardList();

    List<StrategyAward> queryStrategyAwardListByStrategyId(Long strategyId);

    String queryStrategyAwardRuleModels(StrategyAward strategyAward);
}
