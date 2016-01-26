package com.hxuehh.reuse_Process_Imp.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by kait on 10/16/13.
 */

//这个废弃了 主要的原因是必须和当前的ac绑定，需要class实例的时候也未必存在  suwg
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface InvokeLocal {
    String method();
}
