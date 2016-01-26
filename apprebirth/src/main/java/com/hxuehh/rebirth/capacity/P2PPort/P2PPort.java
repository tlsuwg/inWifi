package com.hxuehh.rebirth.capacity.P2PPort;

import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.view.View;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.hxuehh.appCore.app.SuApplication;
import com.hxuehh.appCore.develop.Face_UnsolvedForDlp;
import com.hxuehh.appCore.develop.Su;
import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.commonInterface.FaceCommCallBack;
import com.hxuehh.appCore.faceFramework.faceDomain.utilDomain.LoadCursorSetting;
import com.hxuehh.appCore.faceFramework.faceEcxeption.FaceException;
import com.hxuehh.appCore.faceFramework.faceUI.androidWrap.FaceBaseActivity_1;
import com.hxuehh.rebirth.all.domain.RunningStatus;
import com.hxuehh.rebirth.app.IntentChangeManger;
import com.hxuehh.rebirth.device.domain.DeviceCapacityBase;
import com.hxuehh.rebirth.device.domain.DeviceCapacityInParameter;
import com.hxuehh.rebirth.device.domain.DeviceCapacityOutResult_3;
import com.hxuehh.rebirth.device.domain.imp.CommonDeviceCapacityOutResult;
import com.hxuehh.rebirth.suMessage.domain.MidMessage;
import com.hxuehh.reuse_Process_Imp.appSetting.AppStaticSetting;
import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.StringUtil;
import com.hxuehh.reuse_Process_Imp.staticKey.DeviceCapacitySetting;

import java.io.Serializable;

/**
 * Created by suwg on 2015/8/16.
 */
public class P2PPort extends DeviceCapacityBase {

    public transient LocationClient mLocationClient;
    public transient MyLocationListener mMyLocationListener;

    transient String info;

    transient FaceCommCallBack faceCommCallBack = new FaceCommCallBack() {
        @Override
        public boolean callBack(Object[] t) {
            if(t==null||t.length==0)return false;
            info = (String) t[0];
            addDeviceStatus(new RunningStatus(RunningStatus.Running, info));
            return false;
        }
    };

    @Override
    public int getType() {
        return DeviceCapacityBase.Type_P2PForPort;
    }

    @Override
    public boolean testHardware_SDK() {
        return true;
    }


    @Override
    public boolean isDevEnable() {
        return false;
    }

    @Override
    public String getDevUnEnableInfo() {
        return "服务器扛不住这么多人了，暂停使用";
    }

    @Face_UnsolvedForDlp
    @Override
    public DeviceCapacityOutResult_3 doChangeStatus(DeviceCapacityInParameter t) throws FaceException {
        DeviceCapacityOutResult_3 mDeviceCapacityOutResult_3=   super.doChangeStatus(t); if(mDeviceCapacityOutResult_3!=null)return mDeviceCapacityOutResult_3;
        if (t == null || !(t instanceof Loc_GeographicParameter)) throw new FaceException("没有设置参数");
        final Loc_GeographicParameter tt = (Loc_GeographicParameter) t;
        CommonDeviceCapacityOutResult commonDeviceCapacityOutResult = new CommonDeviceCapacityOutResult(true);
        if (tt.cmdType == 0) {//启动 m没有什么作用了  因为早就启动了
            if (!isInit) {
                onCreat();
            }
            if (mLocationClient != null)
                mLocationClient.requestLocation();
        } else if (tt.cmdType == -1) {//关闭
            onDestry();
        } else if (tt.cmdType == 2) {//最后一次 定位
            if (!isInit) {
                onCreat();
            }
            if (mLocationClient != null)
                mLocationClient.requestLocation();
            if (StringUtil.isEmpty(info)) {
                throw new FaceException("暂时没有定位到，请稍后查询");
            } else {
                commonDeviceCapacityOutResult.putKeyValue(MidMessage.Key_Res, info);
            }
        } else if (tt.cmdType == 3) {//开启定位汇报
            if (tt.time > 1000) {
                initLocation(tt.time);
            } else {
                throw new FaceException("定位间隔时间需要设置大于1秒一次");
            }
        }
        return commonDeviceCapacityOutResult;
    }


    @Override
    public int getMAXHistorySize() {
        return DeviceCapacitySetting.Loc_Geographic_his_max;
    }

    @Deprecated
    @Override
    public void activeReportOfEvent(Object[] f) {
        if (!isInit) {
            onCreat();
        }
    }

    boolean isInit;

    @Override
    public void onCreat() {

        if (!isInit)
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    mLocationClient = new LocationClient(SuApplication.getInstance());
                    mMyLocationListener = new MyLocationListener(faceCommCallBack);
                    mLocationClient.registerLocationListener(mMyLocationListener);
                    initLocation(AppStaticSetting.Loc_Geographic_Time);
                    mLocationClient.start();//定位SDK start之后会默认发起一次定位请求，开发者无须判断isstart并主动调用request
                    mLocationClient.requestLocation();
                    isInit = true;
                }
            });
    }

    @Override
    public void stop() {
        if(mLocationClient!=null)
        mLocationClient.stop();
    }

    @Override
    public void onDestry() {
        stop();
    }

    @Override
    protected boolean iSActiveReportOfEvent() {

        return false;
    }
    @Override
    public boolean isShowStatus() {
        return false;
    }


    @Override
    public void OnItemClickListener(FaceBaseActivity_1 mContext, View view, int position, Object allData, LoadCursorSetting mLoadCursorSetting, Object[] t) throws RemoteException, FaceException {
        super.OnItemClickListener(mContext, view, position, allData, mLoadCursorSetting, t);
        IntentChangeManger.jumpTo(mContext, IntentChangeManger.flag_loc_geographic_ac, false);
    }


    private void initLocation(int span) {
        LocationClientOption option = new LocationClientOption();
        LocationClientOption.LocationMode tempMode = LocationClientOption.LocationMode.Hight_Accuracy;
//        LocationClientOption.LocationMode tempMode = LocationClientOption.LocationMode.Battery_Saving;
//        LocationClientOption.LocationMode tempMode =  LocationClientOption.LocationMode.Device_Sensors;

        option.setLocationMode(tempMode);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备

        String tempcoor = "gcj02";//国家测绘局标准
//        String tempcoor = "bd09ll";//百度经纬度标准
//        String     tempcoor="bd09";//百度墨卡托标准
        option.setCoorType(tempcoor);//可选，默认gcj02，设置返回的定位结果坐标系，

        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(false);//可选，默认false,设置是否使用gps
        option.setLocationNotify(false);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIgnoreKillProcess(true);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        mLocationClient.setLocOption(option);
    }

    public static class MyLocationListener implements BDLocationListener {
        FaceCommCallBack faceCommCallBack;

//        61 ： GPS定位结果，GPS定位成功。
//                62 ： 无法获取有效定位依据，定位失败，请检查运营商网络或者wifi网络是否正常开启，尝试重新请求定位。
//                63 ： 网络异常，没有成功向服务器发起请求，请确认当前测试手机网络是否通畅，尝试重新请求定位。
//                65 ： 定位缓存的结果。
//                66 ： 离线定位结果。通过requestOfflineLocaiton调用时对应的返回结果。
//                67 ： 离线定位失败。通过requestOfflineLocaiton调用时对应的返回结果。
//                68 ： 网络连接失败时，查找本地离线定位时对应的返回结果。
//                161： 网络定位结果，网络定位定位成功。
//                162： 请求串密文解析失败。
//                167： 服务端定位失败，请您检查是否禁用获取位置信息权限，尝试重新请求定位。
//                502： key参数错误，请按照说明文档重新申请KEY。
//                505： key不存在或者非法，请按照说明文档重新申请KEY。
//                601： key服务被开发者自己禁用，请按照说明文档重新申请KEY。
//                602： key mcode不匹配，您的ak配置过程中安全码设置有问题，请确保：sha1正确，“;”分号是英文状态；且包名是您当前运行应用的包名，请按照说明文档重新申请KEY。
//                501～700：key验证失败，请按照说明文档重新申请KEY。

        public MyLocationListener(FaceCommCallBack faceCommCallBack) {
            this.faceCommCallBack = faceCommCallBack;
        }




        @Override
        public void onReceiveLocation(BDLocation location) {
            //Receive Location
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            sb.append("\nradius : ");
            sb.append(location.getRadius());
            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());// 单位：公里每小时
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
                sb.append("\nheight : ");
                sb.append(location.getAltitude());// 单位：米
                sb.append("\ndirection : ");
                sb.append(location.getDirection());
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append("\ndescribe : ");
                sb.append("gps定位成功");

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                sb.append("\n省：");//获取省份信息
                sb.append(location.getProvince());
                sb.append("市：");
                sb.append(location.getCity());//获取城市信息
                sb.append("区/县：");
                sb.append(location.getDistrict());//获取区县信息
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());//获取反地理编码
                //运营商信息
                sb.append("\noperationers : ");
                sb.append(location.getOperators());
                sb.append("\ndescribe : ");
                sb.append("网络定位成功");
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }

            String indo = sb.toString();
            if (faceCommCallBack != null)
                faceCommCallBack.callBack(indo);
            Su.log(indo);
//                logMsg(sb.toString());
//                Log.i("BaiduLocationApiDem", sb.toString());
            // mLocationClient.setEnableGpsRealTimeTransfer(true);
        }
    }

    @Face_UnsolvedForDlp
    public static class Loc_GeographicParameter extends DeviceCapacityInParameter implements Serializable {
        int cmdType;
        int time;
//        cmdType==0 启动  -1关闭  2查询当前  3一直开启并报告 4是不是可以记录一下呢

        public Loc_GeographicParameter(int cmdType, int time) {
            this.cmdType = cmdType;
            this.time = time;
        }
    }
}
