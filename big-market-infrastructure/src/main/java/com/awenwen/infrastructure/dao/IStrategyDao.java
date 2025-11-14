package com.awenwen.infrastructure.dao;

import com.awenwen.infrastructure.dao.po.Strategy;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author awenwen
 * @description Award gotten Strategy
 * @create 2025/11/13 22:25
 */
@Mapper
public interface IStrategyDao {
    List<Strategy> queryStrategyList();
}
