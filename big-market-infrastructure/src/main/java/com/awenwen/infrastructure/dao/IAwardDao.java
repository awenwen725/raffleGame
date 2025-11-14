package com.awenwen.infrastructure.dao;

import com.awenwen.infrastructure.dao.po.Award;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author awenwen
 * @description award item description
 * @create 2025/11/13 22:26
 */
@Mapper
public interface IAwardDao {
    List<Award> queryAwardList();
}
