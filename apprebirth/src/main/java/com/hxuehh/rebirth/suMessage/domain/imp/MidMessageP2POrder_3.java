package com.hxuehh.rebirth.suMessage.domain.imp;

import com.hxuehh.rebirth.suMessage.domain.MidMessage;

import java.util.HashMap;

/**
 * Created by suwg on 2015/9/28.
 */
public class MidMessageP2POrder_3 extends MidMessageOrder_2 {
    String from;
    public MidMessageP2POrder_3(int cmd,String from) {
        super(cmd);
        this.from=from;
        this.putKeyValue(MidMessage.Key_DeviceID,from);
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    @Override
    public void setOutMap(HashMap<String, Object> outMap) {
        super.setOutMap(outMap);
        if(outMap.containsKey(MidMessage.Key_CMDkeyResquetID)){
            setmResquetID((String) outMap.get(MidMessage.Key_CMDkeyResquetID));
        }
    }
}
