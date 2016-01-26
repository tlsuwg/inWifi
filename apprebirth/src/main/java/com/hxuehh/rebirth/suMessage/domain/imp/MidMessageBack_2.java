package com.hxuehh.rebirth.suMessage.domain.imp;

import com.hxuehh.rebirth.suMessage.domain.MidMessage;
import com.hxuehh.rebirth.suMessage.domain.MidMessageCMDKeys;

public class MidMessageBack_2 extends MidMessage {
    public MidMessageBack_2(MidMessageOrder_2 requestID_From) {
        super(MidMessageCMDKeys.MidMessageCMD_Back);
        this.putKeyValue(MidMessage.Key_CMDkeyResquetID, requestID_From.getCMDkeyResquetID());
    }


  public  void setResquetID(MidMessageOrder_2 requestID_From){
        this.putKeyValue(MidMessage.Key_CMDkeyResquetID, requestID_From.getCMDkeyResquetID());
    }



    @Deprecated
    public MidMessageBack_2() {
        super();
    }

    public   String getCMDkeyResquetID(){
        Object oo=  getByKey(MidMessage.Key_CMDkeyResquetID);
        if(oo!=null)return (String)oo;
        return null;
    }


    public void setOK() {
        this.putKeyValue(MidMessage.Key_OK_Res, MidMessage.OK);
    }

    public void setUNOK() {
        this.putKeyValue(MidMessage.Key_OK_Res, MidMessage.UNOK);
    }


    public boolean isOK(){
       Object oo=getByKey(MidMessage.Key_OK_Res);
       if(oo==null)return false;
       if(!oo.equals(MidMessage.OK))return false;
       return true;
   }





}