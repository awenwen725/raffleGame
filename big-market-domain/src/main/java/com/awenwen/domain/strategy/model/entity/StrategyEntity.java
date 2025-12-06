package com.awenwen.domain.strategy.model.entity;

import com.awenwen.types.common.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * @author awenwen
 * @description raffle or award rules used in strategy
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
    private String ruleModels;

    /** 适用于 rule_weight 权重规则配置
     * 【4000:102,103,104,105
     * 5000:102,103,104,105,106,107
     * 6000:102,103,104,105,106,107,108,109】
     * */
    public String[] ruleModels() {
        if (StringUtils.isBlank(ruleModels)) return null;
        return ruleModels.split(Constants.SPLIT);
    }

    /**
     * separate all
     * @return return ruleWeightList
     */
    public String getRuleWeight() {
        String[] ruleModels = this.ruleModels();
        if (null == ruleModels) return null;
        for (String ruleModel : ruleModels) {
            if ("rule_weight".equals(ruleModel)) return ruleModel;
        }
        return null;
    }
}
