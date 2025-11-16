package com.awenwen.domain.strategy.model.entity;

import com.awenwen.types.common.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * @author awenwen
 * @description
 * @create 2025/11/16 10:59
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StrategyEntity {
    /** 抽奖策略ID */
    private Long strategyId;
    /** 抽奖策略描述 */
    private String strategyDesc;
    /** 规则模型，rule配置的模型同步到此表，便于使用*/
    private String rule_models;

    public String[] ruleModels() {
        if (StringUtils.isEmpty(rule_models)) {
            return null;
        }
        return rule_models.split(Constants.SPLIT);
    }

    public String getRuleWeight() {
        String[] ruleModels = this.ruleModels();
        if (StringUtils.isEmpty(rule_models)) {
            return null;
        }
        for (String ruleModel : ruleModels) {
            if("rule_weight".equals(ruleModel)) {
                return ruleModel;
            }
        }
        return null;
    }
}
