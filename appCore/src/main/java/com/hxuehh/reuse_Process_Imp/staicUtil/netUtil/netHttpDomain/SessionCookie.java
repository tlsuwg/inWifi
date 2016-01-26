package com.hxuehh.reuse_Process_Imp.staicUtil.netUtil.netHttpDomain;

/**
 * Created with IntelliJ IDEA.
 * User: tianyanlei
 * Date: 13-3-29
 * Time: 下午4:47
 * To change this template use File | Settings | File Templates.
 */
public class SessionCookie extends AbstractCookie {
    private String mCookieName;
    private String mCookieValue;
    private String mDomain;
    private String mPath;

    @Override
    protected String getCookieName() {
        return mCookieName;
    }

    @Override
    protected String getCookieValue() {
        return mCookieValue;
    }

    @Override
    protected String getCookieDomain() {
        return mDomain;
    }

    @Override
    public String getPath() {
        return mPath;
    }

    public void setCookieName(String mCookieName) {
        this.mCookieName = mCookieName;
    }

    public void setCookieValue(String mCookieValue) {
        this.mCookieValue = mCookieValue;
    }

    public void setDomain(String mDomain) {
        this.mDomain = mDomain;
    }

    public void setPath(String mPath) {
        this.mPath = mPath;
    }
}
