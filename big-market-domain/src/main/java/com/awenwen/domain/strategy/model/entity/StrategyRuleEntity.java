package com.awenwen.domain.strategy.model.entity;

import com.awenwen.types.common.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author awenwen
 * @description the rule configured in certain strategy, used when strategy is loading
 * @create 2025/11/17 21:25
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StrategyRuleEntity {
    /** strategy ID */
    private Long strategyId;
    /** award ID, if it is strategy rule, it will be NULL*/
    private Integer awardId;
    /**  deprecated */
    private Integer ruleType;
    /** rule name [rule_blacklist, rule_weight, rule_default] */
    private String ruleModel;
    /** value of rule name
     * rule_blacklist - blocked user ID list
     * rule_weight - award ID list according to user's points
     * rule_default - default award ID list
     **/
    private String ruleValue;
    /** rule description */
    private String ruleDesc;

    /**
     * ONLY AVAILABLE while ruleModel is rule_weight
     * acquire award list according to different user's point
     * ruleValue form of rule_weight eg；4000:102,103,104,105 5000:102,103,104,105,106,107 6000:102,103,104,105,106,107,108,109
     */
    public Map<String, List<Integer>> getRuleWeightValues() {
        if (!"rule_weight".equals(ruleModel)) {
            return null;
        }
        // divide into different value groups
        String[] ruleGroups = ruleValue.split(Constants.SPACE);
        Map<String, List<Integer>> resultMap = new HashMap<>();
        for (String ruleGroup : ruleGroups) {
            if (null == ruleGroup || ruleGroup.isEmpty()) {
                return resultMap;
            }
            // separate needed points with weight values by colon
            String[] parts = ruleGroup.split(Constants.COLON);
            if (parts.length != 2) {
                throw new IllegalArgumentException("rule_weight rule_rule invalid input format" + ruleGroup);
            }
            // separate values
            String[] wightLists = parts[1].split(Constants.SPLIT);
            List<Integer> wightList = new ArrayList<>();
            for (String wight : wightLists) {
                wightList.add(Integer.parseInt(wight));
            }
            
            resultMap.put(ruleGroup, wightList);
        }
        return resultMap;
    }
}
