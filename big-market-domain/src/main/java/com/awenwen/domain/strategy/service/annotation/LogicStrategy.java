package com.awenwen.domain.strategy.service.annotation;

import com.awenwen.domain.strategy.service.rule.filter.factory.DefaultLogicFactory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author awenwen
 * @description self-define enum
 * @create 2025/11/18 19:41
 *
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface LogicStrategy {
    DefaultLogicFactory.LogicModel logicMode();
}
