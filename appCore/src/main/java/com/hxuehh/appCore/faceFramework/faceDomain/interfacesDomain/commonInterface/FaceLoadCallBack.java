package com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.commonInterface;

import org.json.JSONObject;

/**
 * Created by suwg on 2014/5/20.
 */
public interface FaceLoadCallBack<Data,Response,Err> {//数据回调接口  但是这个太概括了


//    public static final int SESS=0x01;//

    public static final int FROM_HTTP=0x01;//来自网络
    public static final int FROM_HTTP_304=0x02;//来自网络转本地
    public static final int FROM_HTTP_ERR_DB_CACHE=0x03;//网络出错之后获取本地数据


    public static final int FROM_DB_CACHE=0x04;//直接读取本地
    public static final int FROM_FILE=0x05;

    public static final int FROM_CACHE=0x06;//来自内存cache  随着304业务开展这个内存池存在的意义很小了 可以做一级缓存   存在的必然性不是很存在了
    public static final int FROM_DOMAIN=0x07;//数据类似 内存

    public static final String fromNames[]= {"from_http","from_http_304","from_http_err_db_cache",
            "from_db_cache","from_file","from_cache","from_domain"};

//    public static String  getFrom(int k){
//        return fromNames[k-1];
//    };


    //    public static final int ERR=0x10;


    public static final int ERR_JSON=10;//json解析出错
    public static final int ERR_JSON_NULL =11;//json String 为null
    public static final int ERR_JSON_RES_NULL=12;//json 不是null  但是需要的字段为null  一般是服务器错误

    public static final int ERR_http_no_url=20;//http加载错误
    public static final int ERR_http_link=21;//http加载错误
    public static final int ERR_http_link_time_out=22;//http加载错误
    public static final int ERR_http_server=23;//http加载错误
    public static final int ERR_http_url_pram=24;//http加载错误
    public static final int ERR_http_UnknownHostException=25;//http加载错误


    public static final int ERR_no_net=40;//加载无网络

    public static final int ERR_clone=50;//json clone 出错
    public static final int ERR_Load_setting=51;// 没有设置回调判断是不是存在下一条的的方式

    public static final int ERR_key_back=60;// 客户主动调回
    public static final int ERR_loaded_all=61;//已经加载完成

    public static final int ERR_DB=70;// DB 出错




    boolean onCallBackErr(int key, Err params);  //是否解决了返回boolean
    boolean onCallBackData(int form, Response req, Data data, JSONObject params, long key);  //是否解决了返回boolean
}