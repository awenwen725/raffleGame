package com.awenwen.domain.strategy.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author awenwen
 * @description define enumeration
 * @create 2025-12-8 12:27
 */
@Getter
@AllArgsConstructor
public enum RuleLimitTypeVO {

    EQUAL(1, "equal"),
    GT(2, "greater"),
    LT(3, "less"),
    GE(4, "greater&equal"),
    LE(5, "less&equal"),
    ENUM(6, "enum"),
    ;

    private final Integer code;
    private final String info;

}
