package com.hxuehh.rebirth.suMessage.domain;


import com.google.gson.Gson;
import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.commonInterface.FaceCommCallBack;
import com.hxuehh.appCore.faceFramework.faceEcxeption.FaceException;
import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.MathUtil;
import com.hxuehh.reuse_Process_Imp.staicUtil.store.file.FileUtil;

import java.io.IOException;
import java.util.HashMap;


/**
 * Created by suwg on 2015/8/15.
 */


public abstract class MidMessage {


    public static final String Key_FileName = "V$#%3_80_fna";
    public static final String Key_ErrInfo = "V$#%3_81_err";//错误  优先查看

    public static final String Key_OK_Res = "V$#%3_85_isok";
    public static String OK = "1";
    public static String UNOK = "0";

    public static final String Key_Res = "V$#%3_85_res";//主要结果
    public static final String Key_BackWarning = "V$#%3_83_bk_w";//一般性添加 只是解释 不必当真 可作为副标题

    public static final String Key_CMDkeyResquetID = "V$#%3_82_res_id";//请求回执 固定使用

    public static final String Key_DeviceID = "V$#%3_84_dID";//自己设备的ID
    public static final String Key_Device_info = "V$#%3_86_dif";//设备基础信息

    public static final String Key_To_Which_DeviceID = "V$#%3_87_dID";
    public static final String Key_All_Device_info = "V$#%3_88_allD";//查询设备
    public static final String Key_To_ClientID = "V$#%3_89_tcid";//这个就是广播了
    public static final String Key_DeviceCapaci_ID = "V$#%3_90_caid";//能力
    public static final String Key_Service_typeID = "V$#%3_91_seid";//服务类型ID
    public static final String Key_Device_status = "V$#%3_92_status";//灯光,屏幕 等状态  1，0标示 查询状态


    public static final String Key_Mi_Reg_ID = "V$#%3_93_r_Reg_regid";//mi注册的ID
    public static final String Key_CMD = "V$#%3_94_cmd";
    public static final String Key_NoHand_Back = "V$#%3_95_no_handle";//没有处理立即返回
    public static final String Key_Imei = "V$#%3_96_imei";


    public static final String Key_DeviceToClientNotification_level = "V$#%3_97_dtoc_mt_l";
    public static final int Key_DeviceToClientNotification_level_1 = 1;
    public static final int Key_DeviceToClientNotification_level_2 = 2;


    //    public static final String Key_DeviceID="V$#%3_80";
    //    public static final String Key_DeviceID="V$#%3_80";


    private HashMap<String, Object> outMap;


    byte[] bytes;
    int headKey;
    int cmd;
    int endKey;
    int jsonLength;


    public int getCmd() {
        return cmd;
    }

    transient String fileName;

    public MidMessage(int cmd) {
        this.cmd = cmd;
    }

    public MidMessage() {

    }


    public void setCmd(int cmd) {
        this.cmd = cmd;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public void putKeyValue(String key, Object value) {
        if (outMap == null) {
            outMap = new HashMap<String, Object>();
        }
        outMap.put(key, value);
    }

    public HashMap<String, Object> getOutMap() {
        return outMap;
    }

    public void setOutMap(HashMap<String, Object> outMap) {
        this.outMap = outMap;
    }

    public Object getByKey(String key) {
        if (outMap == null) {
            return null;
        }
        return outMap.get(key);
    }

    public void appendByte(byte[] bs) {
        if (bytes == null) {
            bytes = bs;
        } else {
            if (bs == null) return;
            byte[] bytes2 = new byte[bytes.length + bs.length];
            System.arraycopy(bytes, 0, bytes2, 0, bytes.length);
            System.arraycopy(bs, 0, bytes2, bytes.length, bs.length);
        }
    }


    public void setFileBytes(String filePath) throws IOException, FaceException {
        if (bytes == null) throw new FaceException("byte已经存在");
        byte[] filebytes = FileUtil.getBytes(filePath);
        bytes = filebytes;
        putKeyValue(Key_FileName, filePath);
    }


    public byte[] getAllBytes() {

        byte[] headByte = MathUtil.int2_4bytes(headKey);
        byte[] endByte = MathUtil.int2_4bytes(endKey);
        byte[] cmdLengthByte = MathUtil.int2_4bytes(cmd);

        byte[] mStringBytes = new byte[]{};
        if (outMap != null) {
            String ste = new Gson().toJson(outMap);
            mStringBytes = ste.getBytes();
            this.jsonLength = mStringBytes.length;
        }
        byte[] jsonLengthByte = MathUtil.int2_4bytes(jsonLength);//长度

        int kk = bytes == null ? 0 : bytes.length;
        byte[] AllBytes = new byte[3 * 4 + mStringBytes.length + kk + 4];
        System.arraycopy(headByte, 0, AllBytes, 0, 4);
        System.arraycopy(cmdLengthByte, 0, AllBytes, 4, 4);
        System.arraycopy(jsonLengthByte, 0, AllBytes, 8, 4);

        int index = 12;
        if (mStringBytes.length > 0) {
            System.arraycopy(mStringBytes, 0, AllBytes, index, mStringBytes.length);
            index += mStringBytes.length;
        }

        if (kk > 0) {
            System.arraycopy(bytes, 0, AllBytes, index, bytes.length);
            index += bytes.length;
        }
        System.arraycopy(endByte, 0, AllBytes, index, 4);
        return AllBytes;
    }


    public String getStringPartCMD() {
        if (outMap != null) {
            outMap.put(Key_CMD, cmd + "");
            return new Gson().toJson(outMap);
        }
        return null;
    }


    public MidMessage getTrue(byte[] bs) throws FaceException {
        int index = 0;

        int uesd = 4;
        this.headKey = getInt(index, uesd, bs);
        index += uesd;

        uesd = 4;
        this.cmd = getInt(index, uesd, bs);
        index += uesd;

        uesd = 4;
        this.jsonLength = getInt(index, uesd, bs);
        index += uesd;

        if (jsonLength > 0) {
            uesd = jsonLength;
            String info = getString(index, uesd, bs);
            index += uesd;
            outMap = new Gson().fromJson(info, HashMap.class);
            if (outMap.containsKey(Key_FileName)) {
                fileName = (String) outMap.get(Key_FileName);
            }
        }

        uesd = bs.length - 3 * 4 - jsonLength - 4;
        bytes = getBytesOut(index, uesd, bs);
        index += uesd;


        uesd = 4;
        this.endKey = getInt(index, uesd, bs);
        index += uesd;

        return this;

    }


    private static byte[] getBytesOut(int start, int used, byte[] bs) throws FaceException {
        if (bs == null) throw new FaceException("get true bytes is null");
        byte[] bs2 = new byte[used];
        System.arraycopy(bs, start, bs2, 0, used);
        return bs2;

    }

    private static String getString(int start, int used, byte[] bs) throws FaceException {
        if (bs == null) throw new FaceException("get true bytes is null");
        byte[] bs2 = new byte[used];
        System.arraycopy(bs, start, bs2, 0, used);
        return new String(bs2);

    }

    private static int getInt(int start, int used, byte[] bs) throws FaceException {
        if (bs == null) throw new FaceException("get true bytes is null");
        byte[] bs2 = new byte[used];
        System.arraycopy(bs, start, bs2, 0, used);
        int kk = MathUtil.bytes4_2int(bs2);
        return kk;
    }


    transient FaceCommCallBack mFaceCommCallBack;

    public FaceCommCallBack getmFaceCommCallBack() {
        return mFaceCommCallBack;
    }

    public void setmFaceCommCallBack(FaceCommCallBack mFaceCommCallBack) {
        this.mFaceCommCallBack = mFaceCommCallBack;
    }


    transient Object tag;//这个tag不会传递出去的

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }


    transient boolean isSendToNet;

    public boolean isSendToNet() {
        return isSendToNet;
    }

    public void setIsSendToNet(boolean isSendToNet) {
        this.isSendToNet = isSendToNet;
    }


    boolean isDiscard;//是不是丢弃

    public boolean isDiscard() {
        return isDiscard;
    }

    public void setIsDiscard(boolean isDiscard) {
        this.isDiscard = isDiscard;
    }
}
