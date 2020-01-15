package com.hapi.datasource

import com.alibaba.fastjson.JSON
import java.lang.reflect.Type

object JsonUtil {

     fun <T>fromJson(str:String, clazz: Type):T{
         return JSON.parseObject<T>(str, clazz);
     }
    fun toJson(any: Any):String{
        return JSON.toJSONString(any);
    }
}