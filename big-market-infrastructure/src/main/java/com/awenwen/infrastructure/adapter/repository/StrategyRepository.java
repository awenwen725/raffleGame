package com.awenwen.infrastructure.adapter.repository;

import com.awenwen.domain.strategy.model.entity.StrategyAwardEntity;
import com.awenwen.domain.strategy.repository.IStrategyRepository;
import com.awenwen.infrastructure.dao.IStrategyAwardDao;
import com.awenwen.infrastructure.dao.IStrategyDao;
import com.awenwen.infrastructure.dao.po.StrategyAward;
import com.awenwen.infrastructure.redis.IRedisService;
import com.awenwen.types.common.Constants;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author awenwen
 * @description strategy repository implements
 * @create 2025/11/15 23:08
 */
@Repository
public class StrategyRepository implements IStrategyRepository {
    @Resource
    private IStrategyAwardDao strategyAwardDao;

    @Resource
    private IRedisService redisService;

    @Override
    public List<StrategyAwardEntity> queryStrategyAwardList(Long strategyId) {
        // 1. read from cache first
        String cacheKey = Constants.RedisKey.STRATEGY_AWARD_LIST_KEY + strategyId;
        List<StrategyAwardEntity> strategyAwardEntities = redisService.getValue(cacheKey);
        if (null != strategyAwardEntities && !strategyAwardEntities.isEmpty()) {
            return strategyAwardEntities;
        }

        // 2. cache missed, read from sql
        List<StrategyAward> strategyAwards = strategyAwardDao.queryStrategyAwardListByStrategyId(strategyId);
        strategyAwardEntities = new ArrayList<>(strategyAwards.size());

        for (StrategyAward strategyAward : strategyAwards) {
            StrategyAwardEntity strategyAwardEntity = StrategyAwardEntity.builder()
                    .strategyId(strategyAward.getStrategyId())
                    .awardId(strategyAward.getAwardId())
                    .awardTitle(strategyAward.getAwardTitle())
                    .awardSubtitle(strategyAward.getAwardSubtitle())
                    .awardCount(strategyAward.getAwardCount())
                    .awardCountSurplus(strategyAward.getAwardCountSurplus())
                    .awardRate(strategyAward.getAwardRate())
                    .sort(strategyAward.getSort())
                    .ruleModels(strategyAward.getRuleModels())
                    .build();
            strategyAwardEntities.add(strategyAwardEntity);
        }
        // 3. add to redis
        redisService.setValue(cacheKey, strategyAwardEntities);

        return strategyAwardEntities;
    }

    /**
     *
     * @param key given key
     * @param rateRange the range of random numbers
     * @param strategyAwardSearchRateTable the map which matches awards with random number
     * @param <K> key type
     * @param <V> value type
     * @description
     *           the lottery strategy is to generate a map that matches awards with random number.
     *           in this method, the map will be generated, and stored into Redis.
     *
     *           RMap returned from Redisson will be a new empty local map when there is no data in redis,
     *           after putting key-value, the value will be lazily putted into redis
     */
    @Override
    public <K, V> void storeStrategyAwardSearchRateTable(Long key, Integer rateRange, Map<K, V> strategyAwardSearchRateTable) {
        // 1. store the range of random number
        redisService.setValue(Constants.RedisKey.STRATEGY_RATE_RANGE_KEY + key, rateRange);

        // 2. store the map
        Map<K, V> cacheRateMap = redisService.getMap(Constants.RedisKey.STRATEGY_RATE_TABLE_KEY + key);
        cacheRateMap.putAll(strategyAwardSearchRateTable);
    }

    @Override
    public int getRateRange(Long strategyId) {
        return redisService.getValue(Constants.RedisKey.STRATEGY_RATE_RANGE_KEY + strategyId);
    }

    @Override
    public Integer getStrategyAwardAssemble(Long strategyId, int rateKey) {
        return redisService.getFromMap(Constants.RedisKey.STRATEGY_RATE_TABLE_KEY + strategyId, rateKey);
    }
}
