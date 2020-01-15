package com.qizhou.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * ｓｐ缓存
 * 以接口参数中的基本类型和string　加上方法名字做为sp中的key
 * 如果是参数是对象提交　则以hashcode加上方法名字做为sp中的key
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface SpCache {
    int cacheTime() default 3 * 1000 *60 *60;
    NetworkFetchStrategy fetchStrategy() default NetworkFetchStrategy.CacheFirst;

    String[] keys() default {};
    String[] defaultValues() default {};
}
