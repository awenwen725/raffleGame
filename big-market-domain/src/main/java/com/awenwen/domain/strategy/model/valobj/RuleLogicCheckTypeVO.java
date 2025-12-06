package com.awenwen.domain.strategy.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author awenwen
 * @description value obj describing filter setting result
 * @create 2025/11/18 19:22
 */

@Getter
@AllArgsConstructor
public enum RuleLogicCheckTypeVO {
    ALLOW("0000", "passed；执行后续的流程，不受规则引擎影响"),
    TAKE_OVER("0001","take over；后续的流程，受规则引擎执行结果影响"),
    ;

    private final String code;
    private final String info;
}
