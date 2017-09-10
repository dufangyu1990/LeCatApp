package com.example.dufangyu.lecatapp.helper;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by dufangyu on 2016/9/5.
 */
public class GenericHelper {

    //根据我们提供的class，来获取这个类实现的那个泛型的类型
    public static <T>Class<T> getViewClass(Class<?> klass)
    {
        //getGenericSuperclass()获得带有泛型的父类
        Type type = klass.getGenericSuperclass();
        if(type==null || !(type instanceof ParameterizedType))
            return null;
        //ParameterizedType参数化类型，即泛型
        ParameterizedType parameterizedType = (ParameterizedType) type;
        //getActualTypeArguments获取参数化类型的数组，泛型可能有多个
        Type[] types = parameterizedType.getActualTypeArguments();
        if(types == null || types.length == 0) return null;
//        Log.d("dfy","返回"+(Class<T>) types[0]);
        return (Class<T>) types[0];
    }
}
