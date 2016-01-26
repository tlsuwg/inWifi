package com.hxuehh.reuse_Process_Imp.staicUtil.store.DB.beans;

import android.database.Cursor;

import com.hxuehh.reuse_Process_Imp.staicUtil.netUtil.netHttpDomain.SessionCookie;
import com.hxuehh.reuse_Process_Imp.staicUtil.store.DB.Bean;

/**
 * Created with IntelliJ IDEA.
 * User: tianyanlei
 * Date: 13-3-21
 * Time: 下午6:04
 * To change this template use File | Settings | File Templates.
 */
public class CookieTable extends Bean {
    private static final String TB_NAME = "cookies";
    private static final String KEY = "key";
    private static final String VALUE = "value";
    private static final String DOMAIN = "domain";
    private static final String PATH = "path";

    private static CookieTable mInstance;

    public static CookieTable getInstance(){
        if (mInstance == null){
            mInstance = new CookieTable();
        }
        return mInstance;
    }
    @Override
    public void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS " + TB_NAME +
                " (" +
                    KEY + " TEXT," +
                    VALUE + " TEXT," +
                    DOMAIN + " TEXT," +
                    PATH + " TEXT"+
                ");";
        db.execSql(sql);
    }

    public void saveCookie(SessionCookie cookie){
        if (getCookieByDomain(cookie.getDomain()) != null){
            removeCookieByDomain(cookie.getDomain());
        }
        String sql = "insert into " + TB_NAME +
                " values('"
                + cookie.getName()   + "','"
                + cookie.getValue()  + "','"
                + cookie.getDomain() + "','"
                + cookie.getPath()
                + "');";
        try {
            db.execSql(sql);
        }catch (Exception e){

        }
    }

    public SessionCookie getCookieByDomain(String domain){
        String sql = "SELECT * FROM " + TB_NAME + " where " + DOMAIN + "='" + domain + "';";
        Cursor cursor = null;
        SessionCookie cookie = null;
        try {
            cursor = db.getDb().rawQuery(sql,new String[]{});
            if (cursor != null && cursor.getCount() > 0){
                cursor.moveToFirst();
                cookie = new SessionCookie();
                cookie.setCookieName(cursor.getString(cursor.getColumnIndex(KEY)));
                cookie.setCookieValue(cursor.getString(cursor.getColumnIndex(VALUE)));
                cookie.setDomain(cursor.getString(cursor.getColumnIndex(DOMAIN)));
                cookie.setPath(cursor.getString(cursor.getColumnIndex(PATH)));
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (cursor != null){
                cursor.close();
            }
        }
        return cookie;
    }

    public void removeCookieByDomain(String domain){
        String sql = "DELETE FROM " + TB_NAME + " where " + DOMAIN + "='" + domain + "';";
        try {
            db.execSql(sql);
        }catch (Exception e){

        }
    }

    public void updateCookieByDomain(String domain,SessionCookie cookie){
        String sql = "UPDATE " + TB_NAME + " SET "
                + KEY + " = '" + cookie.getName() + "',"
                + VALUE + " = '" + cookie.getValue() + "',"
                + DOMAIN + " = '" + cookie.getDomain() + "',"
                + PATH + " = '" + cookie.getPath() +"'"
                + " WHERE " + DOMAIN + " = '" + cookie.getDomain() + "';";
        try {
            db.execSql(sql);
        }catch (Exception e){

        }
    }

}
