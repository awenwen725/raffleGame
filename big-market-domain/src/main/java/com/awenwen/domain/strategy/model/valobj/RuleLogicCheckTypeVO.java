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
    ALLOW("0000", "passed；perform remain process, do not affected by strategy"),
    TAKE_OVER("0001","take over；affected by strategy"),
    ;

    private final String code;
    private final String info;
}
