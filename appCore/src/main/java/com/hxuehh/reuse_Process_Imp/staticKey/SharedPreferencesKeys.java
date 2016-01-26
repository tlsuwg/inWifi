package com.hxuehh.reuse_Process_Imp.staticKey;

/**
 * Created by suwg on 2014/8/18.
 */
public class SharedPreferencesKeys {

    public static String device_used_type="device_used_type";//身份
    public static final int device_used_type_noSet=-1;//还没有设置
    public static final int device_used_type_only_server =1;//只是服务器
    public static final int device_used_type_server_Device=4;//标准服务器+设备
    public static final int device_used_type_device=3;//不是服务器 但是需要一直和谁连接
    public static final int device_used_type_client=2;//控制器



    public static String device_UUID="device_UUID";
    public static String main_Server_ip="main_Server_IP";//身份
    public static String DeviceService_TCPLongLink_Satus="DeviceService_TCPLongLink_Satus";//只是代表服务是不是被启动或者被关闭了



    public static String deviceWifiName ="device_Wifi_Name";
    public static String mainServerWifiName ="main_server_Wifi_Name";
    public static String client_WifiName ="client_Wifi_Name";
    public static String LightSensor_MIN="Light_Sensor_MIN";
    public static String LightSensor_MAN="Light_Sensor_MAN";
    public static String MIregID="mi_reg_id";
    public static String Scene_DeviceCapacity_ID="Scene_DeviceCapacity_ID_";
    public static String Scene_Keep_sensor_Time="Scene_Keep_sensor_Time";
    public static String Scene_Keep_screen_Time="Scene_Keep_screen_Time";

    public static String Mi_Imei_Phone_loc="Mi_Imei_Phone_loc";
    public static String AccelerationSettingOpen="AccelerationSettingOpen";
    public static String VoiceSynthesisHistory="VoiceSynthesisHistory";
    public static String VoiceSynthesisVoicerName="VoiceSynthesisVoicerName";
    public static String Type_WCH_Rotate_SettingTime="Type_WCH_Rotate_SettingTime";
    public static String WCH_Light_Setting_R="WCH_Light_Setting_R";
    public static String WCH_Light_Setting_G="WCH_Light_Setting_G";
    public static String WCH_Light_Setting_B="WCH_Light_Setting_B";
    public static String describeContents="describeContents";
    public static String AccelerationSensorAmplification="AccelerationSensorAmplification";
    public static String Device_AccelerationSensor_X="Device_AccelerationSensor_X";
    public static String Device_AccelerationSensor_Y="Device_AccelerationSensor_Y";
    public static String Device_AccelerationSensor_Z="Device_AccelerationSensor_Z";
    public static String DeviceCapacityBaseUserSettingEnable ="DeviceCapacityBaseUserSettingEnable";
    public static String DeviceCapacityBaseUserSettingSenseEnable ="DeviceCapacityBaseUserSettingSenseEnable";


    public static String SunShaoQingphoneNumberForServer ="SunShaoQingphoneNumberForServer";
    public static String SunShaoQingregisterNoSendServerStatus ="SunShaoQingregisterNoSendServerStatus";
    public static String SunShaoQingRegisterSucceed="SunShaoQingRegisterSucceed";
    public static String SunShaoQingWifiPassWord="SunShaoQingWifiPassWord";
}
