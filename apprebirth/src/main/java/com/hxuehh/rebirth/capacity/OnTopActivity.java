package com.hxuehh.rebirth.capacity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.net.LocalServerSocket;
import android.net.LocalSocket;
import android.net.LocalSocketAddress;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.hxuehh.appCore.aidl.BytesClassAidl;
import com.hxuehh.appCore.app.SuApplication;
import com.hxuehh.appCore.develop.Su;
import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.commonInterface.FaceCommCallBack;
import com.hxuehh.appCore.faceFramework.faceEcxeption.FaceException;
import com.hxuehh.appCore.faceFramework.faceUI.androidWrap.FaceBaseActivity_1;
import com.hxuehh.rebirth.R;
import com.hxuehh.rebirth.capacity.video.send.BytesManger;
import com.hxuehh.rebirth.capacity.video.SuVideoSense;
import com.hxuehh.rebirth.device.domain.DeviceCapacityBase;
import com.hxuehh.reuse_Process_Imp.app.threadManager.ThreadManager;
import com.hxuehh.reuse_Process_Imp.appSetting.AppStaticSetting;
import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.DateUtil;
import com.hxuehh.reuse_Process_Imp.staicUtil.store.file.FileUtil;
import com.hxuehh.reuse_Process_Imp.staticKey.AidlCacheKeys;
import com.hxuehh.reuse_Process_Imp.staticKey.ViewKeys;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by suwg on 2015/9/14.
 */
public class OnTopActivity extends FaceBaseActivity_1 {

    public static final String ActionOK = "com.su.OnTopActivity.USBLinkAction.ok";
    public static final String ActionErr = "com.su.OnTopActivity.USBLinkAction.err";

    public static final int BaseFileTime=1000;
    public static int oneBytes = 1024;
    public static boolean isAddHead=false;//那么就需要另外一端 播放纠错

    Integer type = DeviceCapacityBase.Type_Video_Surveillance_Sense;
    boolean isLightOpen;//灯

    @Override
    public int getViewKey() {
        return ViewKeys.OnTopActivity;
    }

    FaceCommCallBack faceCommCallBack = new FaceCommCallBack() {
        @Override
        public boolean callBack(Object[] t) {
            finish();
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.on_top_lin);
        setActionReceivers(new String[]{SuVideoSense.End}, new FaceCommCallBack[]{faceCommCallBack});

        try {
            BytesClassAidl al = SuApplication.getInstance().getReMoveAidlValue(AidlCacheKeys.openSingletonAcForDeviceCap, BytesClassAidl.To_Me);
            type = (Integer) al.getTrue();
            al = SuApplication.getInstance().getReMoveAidlValue(AidlCacheKeys.LighterOpen, BytesClassAidl.To_Me);
            if (al != null)
                isLightOpen = (boolean) al.getTrue();

        } catch (RemoteException e) {
            e.printStackTrace();
            onErr(e.getMessage());
            finish();
            return;
        }
        if(isAddHead)
        getHeadByte();
        initMainView();
    }


    byte[] byteHead;
    int headSize = 28;
    File fileForBase;
    //    boolean isOnlySound=true;
    boolean isOnlySound;

    private void getHeadByte() {
        fileForBase = FileUtil.getFileExist(SuApplication.getInstance().getCacheDir().getPath() + "/video", "rebirth_base.3gp");
        if (fileForBase.exists()) {
            try {
                byteHead = FileUtil.getBytesFront(fileForBase.getAbsolutePath(), headSize);
                if (byteHead != null && byteHead.length == headSize) {
//                    for (int i = 1; i <= 4; i++) {
//                        byteHead[32 - i] = (byte) 0xff;
//                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void onErr(String message) {
        Su.log("onErr:" + message);
        try {
            SuApplication.getInstance().putAidlValue(new BytesClassAidl(AidlCacheKeys.OnTopActivityRes, BytesClassAidl.To_Me, message));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        this.sendBroadcast(new Intent(ActionErr + type));
        finish();
    }

    private void onOk(String absoluteFile) {
        try {
            SuApplication.getInstance().putAidlValue(new BytesClassAidl(AidlCacheKeys.OnTopActivityRes, BytesClassAidl.To_Me, absoluteFile));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        this.sendBroadcast(new Intent(ActionOK + type));

        if (type == DeviceCapacityBase.Type_Video_Surveillance_Sense) {

        } else {
            finish();
        }

    }


    public void onClickGetPhone(View view) {
        mCamera.takePicture(null, null, jpegCallback);
    }

    SurfaceView mSurfaceView;
    SurfaceHolder mSurfaceHolder;
    ImageView mSurfaceView_On_top_image_view;
    Camera mCamera;
    DisplayMetrics dm;



    private void initMainView() {
        dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        mSurfaceView = (SurfaceView) findViewById(R.id.mSurfaceView_On_top);
        mSurfaceView_On_top_image_view = (ImageView) findViewById(R.id.mSurfaceView_On_top_image_view);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.setKeepScreenOn(true);

        mSurfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                mSurfaceHolder = holder;
                if (type == DeviceCapacityBase.Type_Photograph) {
                    showCamera();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                mSurfaceHolder = holder;
                Su.log("surfaceChanged");
                if (type == DeviceCapacityBase.Type_Video_Surveillance_Sense) {
                    if (isAddHead&&byteHead == null) {
                        try {
                            initializeVideo(true);
                        } catch (Exception e) {
                            e.printStackTrace();
                            onErr(e.getMessage() + "");
                        }
                    } else {
                        videoGetSend();
                    }
                }
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                Su.log("surfaceDestroyed");
            }
        });


//        mSurfaceHolder.setFixedSize(dm.widthPixels, dm.heightPixels);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        if (AppStaticSetting.isTestPhotoGraph) {
            findViewById(R.id.onClickGetPhone).setVisibility(View.VISIBLE);
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        trueCloseGet();
        closeLight();
        closeMediaRecorder();
        releaseCamera();
    }


    private void closeMediaRecorder() {
        try {
            if (mMediaRecorder != null) {
                mMediaRecorder.stop();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (mMediaRecorder != null) {
                mMediaRecorder.release();
                mMediaRecorder = null;
                isVideoGetting = false;
            }
        }
    }


    private void showCamera() {

        mCamera = Camera.open();
        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setPictureFormat(PixelFormat.JPEG);
        parameters.setJpegQuality(100);  // 设置照片的质量
//        parameters.setPreviewFrameRate(5);// 预览帧率

        if (isLightOpen) {
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        }

//        mCamera.setDisplayOrientation(90);

//        List<Camera.Size> sizes = parameters.getSupportedPreviewSizes();
//        Camera.Size optimalSize = getOptimalPreviewSize(sizes, dm.widthPixels, dm.heightPixels);
//        if(optimalSize!=null) {
//            parameters.setPreviewSize(optimalSize.width, optimalSize.height);
//            parameters.setPictureSize(optimalSize.width, optimalSize.height);
//            mSurfaceHolder.setFixedSize(optimalSize.width, optimalSize.height);
//        } else {
//            parameters.setPreviewSize(320, 240);
//            parameters.setPictureSize(320, 240);
//            mSurfaceHolder.setFixedSize(320, 240);
//        }
        mCamera.setParameters(parameters);
        try {
            mCamera.setPreviewDisplay(mSurfaceHolder);
        } catch (IOException e) {
            e.printStackTrace();
            onErr(e.getMessage());
            return;
        }
        mCamera.startPreview();
        if (!AppStaticSetting.isTestPhotoGraph)
            getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mCamera.takePicture(null, null, jpegCallback);
                }
            }, 300);
    }


//    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizeList, int PreviewWidth, int PreviewHeight) {
//        Camera.Size last = null;
//        if (sizeList.size() > 1) {
//            Iterator<Camera.Size> itor = sizeList.iterator();
//            while (itor.hasNext()) {
//                Camera.Size cur = itor.next();
//                if (cur.width <= PreviewHeight
//                        && cur.height <= PreviewWidth) {
//                    return cur;
//                }
//                last = cur;
//            }
//        }
//        return null;
//    }



   void  closeLight(){
       if (isLightOpen&&mCamera != null) {
           try {
               mCamera.lock();
               Camera.Parameters parameters = mCamera.getParameters();
               parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
               mCamera.setParameters(parameters);
               mCamera.unlock();
           }catch (Exception e){
               e.printStackTrace();
           }
       }
    }
    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }


    private Camera.PictureCallback jpegCallback = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] _data, Camera _camera) {
            Bitmap bm = BitmapFactory.decodeByteArray(_data, 0, _data.length);
            {//旋转
                Matrix m = new Matrix();
                m.setRotate(90, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
                bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), m, true);
            }
            File myCaptureFile = new File(SuApplication.getInstance().getCacheDir(), DateUtil.getCurrentTime() + ".jpg");
            try {
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
                bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                bos.flush();
                bos.close();
                if (!AppStaticSetting.isTestPhotoGraph) {
                    onOk(myCaptureFile.getAbsolutePath());
                } else {
                    mSurfaceView_On_top_image_view.setImageBitmap(bm);
                }
            } catch (Exception e) {
                e.printStackTrace();
                onErr(e.getMessage());
            } finally {
                if (AppStaticSetting.isTestPhotoGraph) {
                    mCamera.startPreview();//继续
                }
            }
        }
    };


    public void setBrightness(final int brightness) {
        getHandler().post(new Runnable() {
            @Override
            public void run() {
                Window window = getWindow();
                WindowManager.LayoutParams lp = window.getAttributes();
                lp.screenBrightness = Float.valueOf(brightness) * (1f / 255f);
                window.setAttributes(lp);
            }
        });
    }
//    =======================================

    LocalSocket receiver, sender;
    MediaRecorder mMediaRecorder;
    LocalServerSocket lss;
    boolean isVideoGetting;
    String ip;
    int port;

    private void videoGetSend() {
        try {
            getIPTosend();
            initLocalSocket();
            initializeVideo(false);
        } catch (Exception e) {
            e.printStackTrace();
            trueCloseGet();
            onErr(e.getMessage() + "");
        }
    }

    private void getIPTosend() throws FaceException {
        try {
            BytesClassAidl ips = SuApplication.getInstance().getAidlValue(AidlCacheKeys.openSingletonAcForDeviceCap_VideoIP);
            BytesClassAidl ports = SuApplication.getInstance().getAidlValue(AidlCacheKeys.openSingletonAcForDeviceCap_VideoPort);
            if (ips != null && ports != null) {
                ip = (String) ips.getTrue();
                port = (int) ports.getTrue();
            } else {
                throw new FaceException("没有设置目标端口参数");
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            throw new FaceException("获取目标端口参数出错");
        }
    }




    private void initLocalSocket() throws IOException {
        if (lss != null) return;

        String name = "videobytes";
        lss = new LocalServerSocket(name);
        receiver = new LocalSocket();
        receiver.connect(new LocalSocketAddress(name));
        int size = oneBytes * 10;
        receiver.setReceiveBufferSize(size);
        receiver.setSendBufferSize(size);
        sender = lss.accept();
        sender.setReceiveBufferSize(size);
        sender.setSendBufferSize(size);
        isVideoGetting = true;

        final BytesManger manger = new BytesManger(ip, port);

        ThreadManager.getInstance().getNewThread("video get bytes", new Runnable() {
            @Override
            public void run() {
                byte[] bs = new byte[oneBytes];
                byte[] headbs = new byte[oneBytes + headSize];
                int headreaad = 0;
                try {
                    InputStream input = receiver.getInputStream();
                    boolean isFirst = true;

                    if(!isAddHead)isFirst=false;

//                    manger.add(byteHead);
//                    manger.add(byteHead);
//                    manger.add(byteHead);

                    while (isVideoGetting) {
                        int readed = input.read(bs);
                        Su.log("read" + readed);
                        if (readed > 0) {
                            byte bscopy[] = null;
                            if (isFirst&&isAddHead) {
                                if (readed + headreaad < headSize) {
                                    System.arraycopy(bs, 0, headbs, headreaad, readed);
                                    headreaad += readed;
                                    Su.log("不够28继续");
                                    continue;
                                } else {
                                    if (headreaad != 0) {
                                        System.arraycopy(bs, 0, headbs, headreaad, readed);//放到头里面
                                        headreaad += readed;
                                        bscopy = new byte[headreaad];
                                        System.arraycopy(headbs, 0, bscopy, 0, headreaad);//放到copy
                                        Su.log("2次" + bscopy.length);
                                    } else {
                                        bscopy = new byte[readed];
                                        System.arraycopy(bs, 0, bscopy, 0, readed);
                                    }
                                }
                                System.arraycopy(byteHead, 0, bscopy, 0, headSize);
                                Su.log("置换 isFirst");
                                headreaad = 0;
                                headbs = null;
                                isFirst = false;
                            } else {
                                bscopy = new byte[readed];
                                System.arraycopy(bs, 0, bscopy, 0, readed);
                            }
                            manger.add(bscopy);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();


    }

//    byte[] aa = { 0x01, 0x42, (byte) 0x80, 0x0A, (byte) 0xFF,
//            (byte) 0xE1, 0x00, 0x12, 0x67, 0x42, (byte) 0x80, 0x0A,
//            (byte) 0xE9, 0x02, (byte) 0xC1, 0x29, 0x08, 0x00, 0x00,
//            0x1F, 0x40, 0x00, 0x04, (byte) 0xE2, 0x00, 0x20, 0x01,
//            0x00, 0x04, 0x68, (byte) 0xCE, 0x3C, (byte) 0x80 };

    private void trueCloseGet() {
        try {
            if (lss != null)
                lss.close();
            lss = null;
            if (receiver != null)
                receiver.close();
            receiver = null;
            if (sender != null)
                sender.close();
            sender = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    private void initializeVideo(boolean Tofile) throws IOException {

        if (mMediaRecorder == null)
            mMediaRecorder = new MediaRecorder();
        else {
            mMediaRecorder.reset();
        }

        // 与屏幕方向相反即为 BACK_FACING_CAMERA
        // 与屏幕方向一致即为 FRONT_FACING_CAMERA
        {
            mCamera = Camera.open();
//            mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
            mCamera.lock();
            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setRotation(90);
            parameters.setPictureFormat(PixelFormat.JPEG);
            parameters.setJpegQuality(100);  // 设置照片的质量
//            parameters.setPreviewFrameRate(5);
            if (isLightOpen) {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            }
            mCamera.setParameters(parameters);
//			mCamera.stopPreview();
//			mCamera.startPreview();
            mCamera.unlock();
//			mCamera.reconnect();
            mMediaRecorder.setCamera(mCamera);
        }


        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        if (!isOnlySound)
            mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);


        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        //mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);

//            mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);

        // audio: AMR-NB
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        if (!isOnlySound)
            mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H263);
//        mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);



        // 设置视频录制的分辨率。必须放在设置编码和格式的后面，否则报错
//        mMediaRecorder.setVideoSize(176, 144);
        if (!isOnlySound)
            mMediaRecorder.setVideoSize(320, 240);
        //mMediaRecorder.setVideoSize(720, 480);

        // 设置录制的视频帧率。必须放在设置编码和格式的后面，否则报错
//        mMediaRecorder.setVideoFrameRate(14);


//        recorder.setPreviewDisplay(videopreview.getHolder().getSurface());

        // 设置输出文件方式： 直接本地存储   or LocalSocket远程输出
        if (Tofile)    //Native
        {
            mMediaRecorder.setOutputFile(fileForBase.getPath());    //called after set**Source before prepare
            mMediaRecorder.setMaxDuration(BaseFileTime);
//            mMediaRecorder.setMaxFileSize(1024);

            mMediaRecorder.setOnErrorListener(new MediaRecorder.OnErrorListener() {
                @Override
                public void onError(MediaRecorder mr, int what, int extra) {
                    Su.log(" mMediaRecorder setOnErrorListener " + what + " " + extra);

                }
            });

            mMediaRecorder.setOnInfoListener(new MediaRecorder.OnInfoListener() {
                @Override
                public void onInfo(MediaRecorder mr, int what, int extra) {
                    Su.log(" mMediaRecorder setOnInfoListener " + what + " " + extra);
                    switch (what) {
                        case MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED: {
                            closeLight();
                            closeMediaRecorder();
                            releaseCamera();
                            getHeadByte();

                            videoGetSend();
                        }
                        break;
                    }
                }
            });

        } else    //Remote
        {
            mMediaRecorder.setOutputFile(sender.getFileDescriptor()); //设置以流方式输出
            //        mMediaRecorder.setMaxDuration(0);
//        mMediaRecorder.setMaxFileSize(0);
        }


//            mMediaRecorder.setOnInfoListener(this);
//            mMediaRecorder.setOnErrorListener(this);

        // 预览
        if (!isOnlySound)
            mMediaRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());

        mMediaRecorder.prepare();
        // 〇state: prepared => recording
        mMediaRecorder.start();
        onOk("开始录制");
    }
}
