package com.awenwen.domain.strategy.model.entity;

import com.awenwen.types.common.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * @author awenwen
 * @description strategy information
 * @create 2025/11/16 10:59
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StrategyEntity {
    /** strategy ID */
    private Long strategyId;
    /** strategy Description */
    private String strategyDesc;
    /** rule model in strategy */
    private String ruleModels;

    /**
     * apply to rule_models, split into several rule models
     * */
    public String[] ruleModels() {
        if (StringUtils.isBlank(ruleModels)) return null;
        return ruleModels.split(Constants.SPLIT);
    }

    /**
     * acquire rule weight list
     * format of rule_weight
     * 【4000:102,103,104,105
     * 5000:102,103,104,105,106,107
     * 6000:102,103,104,105,106,107,108,109】
     * @return ruleWeightList
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
