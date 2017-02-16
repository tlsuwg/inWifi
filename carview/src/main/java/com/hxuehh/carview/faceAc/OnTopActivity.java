package com.hxuehh.carview.faceAc;

import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.net.LocalServerSocket;
import android.net.LocalSocket;
import android.net.LocalSocketAddress;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.hxuehh.appCore.app.SuApplication;
import com.hxuehh.appCore.develop.LogUtil;
import com.hxuehh.appCore.develop.Su;
import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.commonInterface.FaceCommCallBack;
import com.hxuehh.appCore.faceFramework.faceUI.androidWrap.FaceBaseActivity_1;
import com.hxuehh.carview.R;
import com.hxuehh.carview.domain.FileCacher;
import com.hxuehh.carview.domain.FileStorageMode;
import com.hxuehh.carview.domain.SuFile;
import com.hxuehh.carview.domain.VideoXHItem;
import com.hxuehh.carview.faceAc.view.ShowVideoItemSpinnerAdapter;
import com.hxuehh.reuse_Process_Imp.FaceUIImp.viewsImp.ProView;
import com.hxuehh.reuse_Process_Imp.app.threadManager.ThreadManager;
import com.hxuehh.reuse_Process_Imp.appSetting.AppStaticSetting;
import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.DateUtil;
import com.hxuehh.reuse_Process_Imp.staicUtil.store.SharedPreferencesUtils;
import com.hxuehh.reuse_Process_Imp.staicUtil.store.file.FileUtil;
import com.hxuehh.reuse_Process_Imp.staticKey.SharedPreferencesKeys;
import com.hxuehh.reuse_Process_Imp.staticKey.ViewKeys;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


/**
 * Created by suwg on 2015/9/14.
 */
public class OnTopActivity extends FaceBaseActivity_1 {

    public static final String ActionOK = "com.su.OnTopActivity.USBLinkAction.ok";
    public static final String ActionErr = "com.su.OnTopActivity.USBLinkAction.err";

    public static final int BaseFileTime = 1000;

    boolean isLightOpen;//灯

    public static final int Type_Video_Surveillance_Sense = 11;//录像 ，本地移动侦测
    public static final int Type_Photograph = 6;//拍照


    Integer type = Type_Video_Surveillance_Sense;
    FileCacher mFileCacher;
    RelativeLayout seekbar_lin;
    LinearLayout top_video_lin, spinner_lin;
    TextView video_view_size, video_view_rate;
    int toutcIDs[] = new int[]{R.id.left_lin, R.id.right_lin, R.id.bottom_lin};
    int buttonIDS[] = new int[]{R.id.showSetting, R.id.showDirFile, R.id.dis, R.id.showDirFileInApp};
    int wid = 176, hie = 144;

    ProView mProView;
    boolean ischeckAllOK;
    Spinner mSpinner;

    Button select_button_for_video;

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
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getWindow().setFormat(PixelFormat.TRANSLUCENT);

        super.onCreate(savedInstanceState);
        tofile = getIntent().getBooleanExtra("is_test", false);

        if (tofile) {
            mFileCacher = new FileCacher(getApplication());
            boolean is = mFileCacher.checkDir();
            if (!is) {
                openSetting();
                finish();
                return;
            }

            int w = SharedPreferencesUtils.getInteger(SharedPreferencesKeys.width);
            int h = SharedPreferencesUtils.getInteger(SharedPreferencesKeys.heigth);
            if (w > 0 && h > 0) {
                wid = w;
                hie = h;
            }
        }

        setContentView(R.layout.video_lin);
//        setActionReceivers(new String[]{SuVideoSense.End}, new FaceCommCallBack[]{faceCommCallBack});
        initview();
        getAllSize();
        initSurFace();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        closeAllRecoder();
    }


    private void openSetting() {
        startActivity(new Intent(getFaceContext(), StorageInitAc.class));
    }

    public void showFile(String param, boolean isApp) {

        if (!isApp) {
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(android.content.Intent.ACTION_VIEW);
            Uri uri = Uri.fromFile(new File(param));
            intent.setDataAndType(uri, "*/*");
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, FileDirActivity.class);
            intent.putExtra("file_dir", param);
            startActivity(intent);
        }

    }

    SeekBar mSeekBar;
    Map mapAllSize = new TreeMap();
    Map mapSizeCanDo = new TreeMap();
    Map mapSizeCanDoSize = new TreeMap();
    //    boolean isSeekChange;


    private void initview() {
        mProView = new ProView(getFaceContext());
        mProView.addIntoView(getFaceContext(), R.id.pro_lin);
        spinner_lin = (LinearLayout) findViewById(R.id.spinner_lin);
        mSpinner = (Spinner) findViewById(R.id.spinner_for_select);
        select_button_for_video = (Button) findViewById(R.id.select_button_for_video);

        if (tofile)
            for (int i : toutcIDs) {
                findViewById(i).setOnTouchListener(mOnTouchListener);
            }

        if (tofile)
            for (int i : buttonIDS) {
                findViewById(i).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.showDirFile: {
                                showFile(mSufile.getDirPath(), false);
                                top_video_lin.setVisibility(View.GONE);
                            }
                            break;
                            case R.id.showDirFileInApp: {
                                showFile(mSufile.getDirPath(), true);
                                top_video_lin.setVisibility(View.GONE);
                            }
                            break;
                            case R.id.showSetting: {
                                openSetting();
                                top_video_lin.setVisibility(View.GONE);
                            }
                            break;
                            case R.id.dis: {
                                top_video_lin.setVisibility(View.GONE);
                            }
                            break;
//                        case R.id.showSetting:{
//
//                        }break;

                        }

                    }
                });
            }


        seekbar_lin = (RelativeLayout) findViewById(R.id.seekbar_lin);
        seekbar_lin.setVisibility(View.GONE);

        top_video_lin = (LinearLayout) findViewById(R.id.top_video_lin);
        top_video_lin.setVisibility(View.GONE);
        video_view_size = (TextView) findViewById(R.id.video_view_size);
        video_view_rate = (TextView) findViewById(R.id.video_view_rate);

        if (!tofile) {
            mSeekBar = (SeekBar) findViewById(R.id.seekbar);
            mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    Map current = null;
                    if (!ischeckAllOK) {
                        current = mapAllSize;
                    } else {
                        current = mapSizeCanDo;
                    }
                    if (current.size() > progress) {
                        int wi = (int) current.keySet().toArray()[progress];
                        int hi = (int) current.get(wi);
                        wid = wi;
                        hie = hi;
                        Su.log("onProgressChanged:" + wi + " " + hi + " allsize: " + current.size() + "; index:" + progress);
//                      isSeekChange = true;
                        video_view_size.setText(current.size() + ":" + wi + "x" + hi);
                        video_view_rate.setText("");
                        LinearLayout.LayoutParams cameraFL = new LinearLayout.LayoutParams(wi, hi); // set size
                        mSurfaceView.setLayoutParams(cameraFL);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
        }

        select_button_for_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferencesUtils.putInteger(SharedPreferencesKeys.width, wid);
                SharedPreferencesUtils.putInteger(SharedPreferencesKeys.heigth, hie);
                Toast.makeText(getFaceContext(), "设置" + wid + "x" + hie, Toast.LENGTH_SHORT).show();
            }
        });

        dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        mSurfaceView = (SurfaceView) findViewById(R.id.mSurfaceView_On_top);
        mSurfaceView_On_top_image_view = (ImageView) findViewById(R.id.mSurfaceView_On_top_image_view);
        if (AppStaticSetting.isTestPhotoGraph) {
            findViewById(R.id.onClickGetPhone).setVisibility(View.VISIBLE);
        }
    }


    private void onErr(String message) {
        Su.log("onErr:" + message);
//        try {
//            SuApplication.getInstance().putAidlValue(new BytesClassAidl(AidlCacheKeys.OnTopActivityRes, BytesClassAidl.To_Me, message));
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
//        this.sendBroadcast(new Intent(ActionErr + type));
//        finish();


    }

    private void onOk(String absoluteFile) {
//        try {
//            SuApplication.getInstance().putAidlValue(new BytesClassAidl(AidlCacheKeys.OnTopActivityRes, BytesClassAidl.To_Me, absoluteFile));
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
//        this.sendBroadcast(new Intent(ActionOK + type));
//
//        if (type == Type_Video_Surveillance_Sense) {
//
//        } else {
//            finish();
//        }
    }


    public void onClickGetPhone(View view) {
        mCamera.takePicture(null, null, jpegCallback);
    }

    public void stopReCode(View view) {
        closeAllRecoder();
    }


    public void checkfile(View view) {
        if (mSufile != null) {
            long size = mSufile.getSize();
            String ss = FileUtil.convertFileSize(size);
            Toast.makeText(this, "dangqian" + ss, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
        }
    }


    SurfaceView mSurfaceView;
    SurfaceHolder mSurfaceHolder;
    ImageView mSurfaceView_On_top_image_view;
    Camera mCamera;
    DisplayMetrics dm;


    View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int action = event.getAction();
            if (action == MotionEvent.ACTION_MOVE) {
                top_video_lin.setVisibility(View.VISIBLE);
            } else if (action == MotionEvent.ACTION_DOWN) {
            } else if (action == MotionEvent.ACTION_UP) {
                top_video_lin.setVisibility(View.VISIBLE);
            } else if (action == MotionEvent.ACTION_CANCEL) {
            }
            return true;
        }
    };

    public static int oneBytes = 1024;
    LocalSocket receiver, sender;
    LocalServerSocket lss;
    private boolean tofile;
    long showRate;


    private void initLocalSocket() throws IOException {
        if (lss != null) return;
        Su.log("initLocalSocket");
        showRate=0;
        String name = "videobytes";
        lss = new LocalServerSocket(name);
        receiver = new LocalSocket();
        receiver.connect(new LocalSocketAddress(name));
        int size = oneBytes*64;
        receiver.setReceiveBufferSize(size);
        receiver.setSendBufferSize(size);
        sender = lss.accept();
        sender.setReceiveBufferSize(size);
        sender.setSendBufferSize(size);
    }

    private void trueCloseGet() {
        Su.log("trueCloseGet");
        try {
            if (lss != null)
                lss.close();
            if (receiver != null)
                receiver.close();
            if (sender != null)
                sender.close();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            lss = null;
            receiver = null;
            sender = null;
        }
    }


    private void initSurFace() {

        mSurfaceView.setVisibility(View.VISIBLE);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.setKeepScreenOn(true);
        mSurfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
//                mSurfaceHolder = holder;
                if (type == Type_Photograph) {
                    showCamera();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//                mSurfaceHolder = holder;
                Su.log("surfaceChanged  " + width + " " + height);
                if (type == Type_Video_Surveillance_Sense) {
                    if (width == 1) {
                        mProView.setLoadingName("开始巡检设备支持的尺寸，请稍等...");
                        mSeekBar.setMax(mapAllSize.size());
                        seekbar_lin.setVisibility(View.VISIBLE);
                        return;
                    }

                    getHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            video_view_rate.setText("流速：0KB/s");
                        }
                    });

                    try {
                        if (!tofile) {
                            trueCloseGet();
                            initLocalSocket();
                        }
//                        releaseCamera();
                        closeAllRecoder();
                        initializeVideo(true);
                        Su.log("change OK");
                        if (!ischeckAllOK) {
                            mapSizeCanDo.put(wid, hie);
                            deleteWH(true);
                        }
                    } catch (Exception e) {
                        Su.log("change 出错");
//                        e.printStackTrace();
                        onErr(e.getMessage() + "");
                        closeAllRecoder();
                        getHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                video_view_size.setText((ischeckAllOK ? "" : mapAllSize.size() + "") + ":" + wid + "x" + hie + "出错");
                                video_view_rate.setText(ischeckAllOK ? "可支持，但出错，\n" +
                                        "分辨率间切换导致" : "分辨率不支持");
                            }
                        });
                        if (!ischeckAllOK) {
                            deleteWH(false);
                        }
                    }
                }
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                Su.log("surfaceDestroyed");
                releaseCamera();
            }
        });

        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
//        mSurfaceHolder.setFixedSize(dm.widthPixels, dm.heightPixels);
    }

    private void deleteWH(final boolean isOK) {
        getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Su.log(" remove set max =================================");
                if (isOK) {
                    mapSizeCanDoSize.put(wid, showRate);
                }
                mapAllSize.remove(wid);
                mSeekBar.setMax(mapAllSize.size());
//                            Toast.makeText(getFaceContext(),"不支持的分辨率",Toast.LENGTH_SHORT).showRate();
                if (mapAllSize.size() == 0) {
                    closeAllRecoder();
                    if (mapSizeCanDo.size() == 0) {
                        mProView.setErrorInfo("巡检完成,没有可以使用的尺寸,请重启手机重试");
                        return;
                    }
                    mProView.setOk("巡检完成,可以选择查看", false);
                    Object[] wids = mapSizeCanDo.keySet().toArray();
                    StringBuffer sb = new StringBuffer();
                    List<VideoXHItem> list = new ArrayList();
                    for (Object w : wids) {
                        sb.append(w).append("x").append(mapSizeCanDo.get(w)).append("，视频流速：").append(mapSizeCanDoSize.get(w) + "Kb/s\n");
                        VideoXHItem mVideoXHItem = new VideoXHItem((Integer) w, (Integer) (mapSizeCanDo.get(w)), (Long) (mapSizeCanDoSize.get(w)));
                        list.add(mVideoXHItem);
                    }

                    Su.log(sb.toString());
                    ischeckAllOK = true;

                    spinner_lin.setVisibility(View.VISIBLE);
                    mSeekBar.setVisibility(View.GONE);
//                    mSurfaceView.setVisibility(View.GONE);

                    select_button_for_video.setVisibility(View.VISIBLE);

                    mSpinner.setAdapter(new ShowVideoItemSpinnerAdapter(list));
                    mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            VideoXHItem mVideoXHItem = (VideoXHItem) parent.getItemAtPosition(position);
                            if (mVideoXHItem != null) {
                                wid = mVideoXHItem.w;
                                hie = mVideoXHItem.h;

                                video_view_size.setText(mapSizeCanDo.size() + ":" + wid + "x" + hie);
                                video_view_rate.setText("");
                                LinearLayout.LayoutParams cameraFL = new LinearLayout.LayoutParams(wid, hie); // set size
                                mSurfaceView.setLayoutParams(cameraFL);
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
//                    seekbar_lin.setVisibility(View.GONE);
//                    mSurfaceView.setVisibility(View.GONE);
//                    mSeekBar.setMax(mapSizeCanDo.size());
                }
            }
        }, isOK ? 2500 : 900);
    }

    private void getAllSize() {


        mCamera = Camera.open();
//        mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
        mCamera.lock();
        Camera.Parameters parameters = mCamera.getParameters();
        if (!tofile && mapAllSize.size() == 0) {
            List<Camera.Size> tempList = parameters.getSupportedPreviewSizes();
            StringBuffer sb = new StringBuffer();
            if (tempList != null && tempList.size() > 0) {
                for (int i = 0; i < tempList.size(); i++) {
                    int wi = tempList.get(i).width;
                    int hi = tempList.get(i).height;
                    sb.append("[").append(wi).append(",").append(hi).append("],");
                    mapAllSize.put(wi, hi);
                }
                Su.log(sb.toString());
            }
        }
        mCamera.unlock();
        releaseCamera();
    }


    private void closeMediaRecorder() {
        try {
            if (mMediaRecorder != null) {
                mMediaRecorder.stop();
                mMediaRecorder.reset();
            }
        } catch (Exception e) {
//            e.printStackTrace();
        } finally {
            if (mMediaRecorder != null) {
                mMediaRecorder.release();
                mMediaRecorder = null;
            }
        }
    }


    private void showCamera() {

        mCamera = Camera.open();
//        mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
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


    void closeLight() {
        if (isLightOpen && mCamera != null) {
            try {
                mCamera.lock();
                Camera.Parameters parameters = mCamera.getParameters();
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                mCamera.setParameters(parameters);
                mCamera.unlock();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void releaseCamera() {
        if (mCamera != null) {
//            mCamera.setPreviewCallback(null);
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


    MediaRecorder mMediaRecorder;
    SuFile mSufile;

    private void initializeVideo(boolean isRe) throws Exception {
        Su.log("initializeVideo");

        if(mMediaRecorder==null) {
            mMediaRecorder = new MediaRecorder();
        }else{
            mMediaRecorder.reset();
        }
        // 与屏幕方向相反即为 BACK_FACING_CAMERA
        // 与屏幕方向一致即为 FRONT_FACING_CAMERA

        if(mCamera==null){
            Su.log("新建mCamera");
            mCamera = Camera.open();
//            mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
            mCamera.lock();
            Camera.Parameters parameters = mCamera.getParameters();

            if (Integer.parseInt(Build.VERSION.SDK) >= 8) {
                try {
                    Method downPolymorphic = mCamera.getClass().getMethod("setDisplayOrientation", new Class[]{int.class});
                    if (downPolymorphic != null) {
                        downPolymorphic.invoke(mCamera, new Object[]{90});
                    }
                }catch (Exception e){ }
            } else {
                parameters.setRotation(90);
            }
//            parameters.setPictureFormat(PixelFormat.JPEG);
//            parameters.setJpegQuality(100);  // 设置照片的质量
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);//1连续对焦
//            parameters.setPreviewFrameRate(5);
            if (isLightOpen) {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            }
            mCamera.setParameters(parameters);
            mCamera.cancelAutoFocus();//只有加上了这一句，才会自动对焦。
//			mCamera.stopPreview();
//			mCamera.startPreview();
            mCamera.unlock();
//			mCamera.reconnect();
//            mMediaRecorder.setCamera(mCamera);
        }else{
            Su.log("old  mCamera");
        }

        mMediaRecorder.setCamera(mCamera);
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);
        mMediaRecorder.setVideoSize(wid, hie);
        Su.log("initializeVideo" + wid + "X" + hie);
        mMediaRecorder.setVideoFrameRate(14);
        mMediaRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());

        if (tofile) {
            mSufile = mFileCacher.getNewFile();
            mMediaRecorder.setOutputFile(mSufile.getPath());    //called after set**Source before prepare
            FileStorageMode mFileStorageMode = mSufile.getmFileStorageMode();
            switch (mFileStorageMode.mFileStorageMode) {
                case FileStorageMode.FileStorageMode_Size: {
                    mMediaRecorder.setMaxFileSize(mFileStorageMode.oneFileStorageModeSizeOneFileSize);
                }
                break;
                case FileStorageMode.FileStorageMode_Time: {
                    mMediaRecorder.setMaxDuration(mFileStorageMode.mFileStorageMode_OneTime);
                }
                break;
            }


            mMediaRecorder.setOnInfoListener(new MediaRecorder.OnInfoListener() {
                                                 @Override
                                                 public void onInfo(MediaRecorder mr, int what, int extra) {
                                                     Su.log(" mMediaRecorder setOnInfoListener " + what + " " + extra + " " + this);
//                                                 switch (what) {
//                                                     case MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED:
//                                                     case MediaRecorder.MEDIA_RECORDER_INFO_MAX_FILESIZE_REACHED: {
//                                                         reReCode();
//                                                     }
//                                                     break;
//                                                 }
                                                 }
                                             }

            );
        } else {
//            sender.getOutputStream().write(1);
            mMediaRecorder.setOutputFile(sender.getFileDescriptor()); //设置以流方式输出
//            mMediaRecorder.setOutputFile(FileDescriptor.err);
        }

        mMediaRecorder.setOnErrorListener(new MediaRecorder.OnErrorListener() {
                                              @Override
                                              public void onError(MediaRecorder mr, int what, int extra) {
                                                  Su.log(" mMediaRecorder setOnErrorListener " + what + " " + extra + " " + this);

                                                  closeAllRecoder();
                                                  getHandler().post(new Runnable() {
                                                      @Override
                                                      public void run() {
                                                          video_view_size.setText((ischeckAllOK ? "" : mapAllSize.size() + "") + ":" + wid + "x" + hie + "出错");
                                                          video_view_rate.setText(ischeckAllOK ? "可支持，但出错，\n" +
                                                                  "分辨率间切换导致" : "分辨率不支持");
                                                      }
                                                  });


                                                  if (!ischeckAllOK) {
                                                      mapSizeCanDo.remove(wid);
                                                      deleteWH(false);
                                                  }

                                              }
                                          }
        );



        mMediaRecorder.prepare();
        mMediaRecorder.start();

        if (!tofile) {
            ThreadManager.getInstance().getNewThread("video get bytes", new Runnable() {
                long startTime;
                LocalSocket receiverForThread = receiver;
                boolean isVideoGettingForThread = true;
                long allGetBytesSize;
                @Override
                public void run() {
                    Su.log("bytes geting " + isVideoGettingForThread);
                    byte[] bs = new byte[oneBytes * 32];
                    try {
                        if (receiverForThread == null) return;
                        InputStream input = receiverForThread.getInputStream();
                        while (isVideoGettingForThread) {
//                        Su.log("geting .");
                            int readed = input.read(bs);
//                            Su.log("read" + readed);
                            if (allGetBytesSize == 0) {
                                startTime = new Date().getTime();
                            }
                            allGetBytesSize += readed;
                            long timeUse = (new Date().getTime() - startTime);
                            if (timeUse > 0) {
                                showRate = (long) (allGetBytesSize / (timeUse / 1000d) / 1000d);
                                getHandler().post(new Runnable() {
                                    @Override
                                    public void run() {
                                        video_view_rate.setText("流速：" + showRate + "KB/s");
                                    }
                                });
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        isVideoGettingForThread = false;
                    }
                    Su.log("read end");
                }
            }).start();
        }


    }

    private void reReCode() {
        closeAllRecoder();
        mSurfaceView.setVisibility(View.GONE);
    }

    private void showErr() {

    }


    private void closeAllRecoder() {
        Su.log("closeAllRecoder");
        closeLight();
        closeMediaRecorder();
        releaseCamera();
    }

}