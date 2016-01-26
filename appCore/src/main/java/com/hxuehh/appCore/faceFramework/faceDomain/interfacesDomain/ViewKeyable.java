package com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain;

/**
 * Created by suwg on 2014/5/20.
 */
public interface ViewKeyable {


//    protected int viewKey;  //为什么加载的对象都叫View呢  其实任何业务 任何对象都是面向到视图层的 ，这个也就是对象的唯一属性
//                                面向URl等cache池的封装 就是一个可以
//                              当然这个也是容器类的实现  （view or activity 传递给对象）
//
//  protected int viewKey;
//
//    @Override
//    public int getViewKey() {
//        return viewKey;
//    }
//
//    @Override
//    public void setViewKey(int key) {
//        viewKey = key;
//    }

    public int getViewKey() ;
    public void setViewKey(int key) ;
}
