package com.qizhou.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.lang.model.type.TypeMirror;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)

/**
 * 自定义缓存类型的接口
 */
public @interface AutoApiWithCache {

    /**
     * 缓存提供器对应的key
     * @return
     */
    String providerKey();

    NetworkFetchStrategy fetchStrategy() default NetworkFetchStrategy.OnlyRemote;

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
