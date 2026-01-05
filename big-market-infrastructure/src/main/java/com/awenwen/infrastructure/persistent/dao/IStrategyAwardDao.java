package com.awenwen.infrastructure.persistent.dao;

/**
 * @author awenwen
 * @description Strategy Award Dao
 * @create 2025/11/13 22:25
 */

import com.awenwen.infrastructure.persistent.po.StrategyAward;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IStrategyAwardDao{
    List<StrategyAward> queryStrategyAwardList();

    List<StrategyAward> queryStrategyAwardListByStrategyId(Long strategyId);

    String queryStrategyAwardRuleModels(StrategyAward strategyAward);

    void updateStrategyAwardStock(StrategyAward strategyAward);
}
