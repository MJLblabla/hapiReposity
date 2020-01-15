package com.qizhou.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 自动生成　reposity　的方法名
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface AutoApi {

    /**
     * 默认值对应的字段
     * @return
     */
    String[] keys() default {};

    /**
     * 默认值　　ex :    @AutoApi(keys = ["begin", "sort", "limit"], defaultValues = ["0", "\"GS\"", "20"])
     * @return
     */
    String[] defaultValues() default {};


}
