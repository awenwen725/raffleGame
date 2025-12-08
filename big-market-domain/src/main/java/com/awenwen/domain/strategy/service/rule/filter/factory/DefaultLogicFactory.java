package com.awenwen.domain.strategy.service.rule.filter.factory;

import com.awenwen.domain.strategy.model.entity.RuleActionEntity;
import com.awenwen.domain.strategy.service.annotation.LogicStrategy;
import com.awenwen.domain.strategy.service.rule.filter.ILogicFilter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author awenwen
 * @description generate a map that contains all Filters
 * use Annotation and Spring DI to search all filters automatically
 * the check code [LogicModel] is defined inner factory
 * @create 2025/11/18 15:39
 */

@Service
public class DefaultLogicFactory {
    public Map<String, ILogicFilter<?>> logicFilterMap = new ConcurrentHashMap<>();

    public DefaultLogicFactory(List<ILogicFilter<?>> logicFilters) {
        logicFilters.forEach(logic -> {
            LogicStrategy strategy = AnnotationUtils.findAnnotation(logic.getClass(), LogicStrategy.class);
            if (null != strategy) {
                logicFilterMap.put(strategy.logicMode().getCode(), logic);
            }
        });
    }

    public <T extends RuleActionEntity.RaffleEntity> Map<String, ILogicFilter<T>> openLogicFilter() {
        return (Map<String, ILogicFilter<T>>) (Map<?, ?>) logicFilterMap;
    }
    @Getter
    @AllArgsConstructor
    public enum LogicModel {

        RULE_WIGHT("rule_weight","[before raffle] 根据抽奖权重返回可抽奖范围KEY", "before"),
        RULE_BLACKLIST("rule_blacklist","[before raffle] when user is in black list, return default award for black list", "before"),
        RULE_LOCK("rule_lock", "[centre raffle]the given award will be available after performing raffle n times", "center"),
        RULE_LUCK_AWARD("rule_luck_award", "after raffle】the given award will be available after performing raffle n times", "after")
        ;

        private final String code;
        private final String info;
        private final String type;

        public static boolean isCenter(String code) {
            return "center".equals(LogicModel.valueOf(code.toUpperCase()).type);
        }

        public static boolean isAfter(String code) {
            return "after".equals(LogicModel.valueOf(code.toUpperCase()).type);
        }
    }
}
