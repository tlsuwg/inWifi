package com.hxuehh.reuse_Process_Imp.analytics;

import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.StringUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Mark
 * Date: 11-10-26
 * Time: 上午10:39
 * To change this template use File | Settings | File Templates.
 */
public class Event {
    public String tag;
    public String time;
    public List<String> args = new ArrayList<String>();

    public Event(String tag, List<String> args) {
        this.tag = tag;
        this.args = args;
        this.time = new SimpleDateFormat("yyMMddHHmmss").format(new Date());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(tag);
        sb.append("|");
        sb.append(time);
        sb.append("|");
        sb.append(StringUtil.join(args, ","));
        return sb.toString();
    }
}
