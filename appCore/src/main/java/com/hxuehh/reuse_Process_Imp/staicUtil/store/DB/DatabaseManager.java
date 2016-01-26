package com.hxuehh.reuse_Process_Imp.staicUtil.store.DB;

import android.content.Context;

import com.hxuehh.appCore.app.SuApplication;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Mark
 * Date: 11-5-23
 * Time: 下午3:32
 * To change this template use File | Settings | File Templates.
 */
public class DatabaseManager {
    private Map<String, Database> dbCache;

    private Context context;

    public DatabaseManager() {
        this(SuApplication.getInstance());
    }

    public DatabaseManager(Context context) {
        this.context = context;
        dbCache = new HashMap<String, Database>();
    }


    private static DatabaseManager inst ;
    public static DatabaseManager getInstance() {
        if(inst==null)inst = new DatabaseManager();
        return inst;
    }


    public Database openDatabase(String name) {
        if (dbCache.containsKey(name)) {
            return dbCache.get(name);
        } else {
            Database db = new Database(name);
            dbCache.put(name, db);
            return db;
        }
    }
}
