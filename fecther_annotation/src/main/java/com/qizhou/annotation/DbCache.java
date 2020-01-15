package com.qizhou.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public  @interface  DbCache {
    String KeyField() default  "";
    Class chacheProvideClass() ;
    int fetchStrategy() default 0;

    String[] keys() default {};
    String[] defaultValues() default {};
}
