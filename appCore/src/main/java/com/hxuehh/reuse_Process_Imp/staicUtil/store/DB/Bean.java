package com.hxuehh.reuse_Process_Imp.staicUtil.store.DB;

import com.hxuehh.reuse_Process_Imp.appSetting.AppStaticSetting;

/**
 * Created by IntelliJ IDEA.
 * User: Mark
 * Date: 11-6-15
 * Time: 下午4:40
 * To change this template use File | Settings | File Templates.
 */
public abstract class Bean {
    protected Database db;

    public Bean() {
        db = DatabaseManager.getInstance().openDatabase(getDatabaseName());
        createTable();
    }

    public String getDatabaseName() {
        return AppStaticSetting.DEFAULT_DATABASE;
    }

    public Database getDatabase() {
        return db;
    }

    /**
     * Abstract create table method
     */
    public abstract void createTable();


}
