package com.hxuehh.appCore.aidl;


import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import com.hxuehh.appCore.aidl.cacheImp.FileCache;
import com.hxuehh.appCore.aidl.cacheImp.MemoryCache;
import com.hxuehh.appCore.app.AidlstartCheckAppService;
import com.hxuehh.appCore.app.SuApplication;
import com.hxuehh.appCore.develop.Su;
import com.hxuehh.appCore.develop.DevRunningTime;

public class AidlServiceSetGetClass_CheckMain extends Service {

    public static final String MyServiceSetGetClassStartOK="su.AidlServiceSetGetClass_CheckMain.AidlServiceSetGetClass_CheckMain.StartOK";

//	主要还是设置一个内存区域；进行读写

	private MemoryCache mMemoryCache;
	private FileCache mFileCache;
	boolean isTestOn;
	public static final boolean isAwakeMainPro=true;



	public class MyServiceImpl extends IMyServiceSetGetClass.Stub {
		public MyServiceImpl() {
			Su.log("new" + this.hashCode());
		}

		@Override
		public BytesClassAidl getValue(int key) {
			BytesClassAidl oo = mMemoryCache.getValueByKey(key);
			if (oo == null) {
				oo = mFileCache.getValueByKey(key);
				if (oo != null && oo.getToWhich() == BytesClassAidl.To_File_Me) {
					mMemoryCache.inToCache(key, oo);
				}
			}
			return oo;
		}

		@Override
		public BytesClassAidl putValue(BytesClassAidl mClassKeyByte)
				throws RemoteException {
			
			switch (mClassKeyByte.getToWhich()) {
			case BytesClassAidl.To_File:
				return mFileCache.inToCache(mClassKeyByte.getKey(),
						mClassKeyByte);
			case BytesClassAidl.To_File_Me:
				BytesClassAidl mm = mMemoryCache.inToCache(
						mClassKeyByte.getKey(), mClassKeyByte);
				mFileCache.inToCache(mClassKeyByte.getKey(), mClassKeyByte);
				return mm;

			default:
				DevRunningTime.isHasCache();
				return mMemoryCache.inToCache(mClassKeyByte.getKey(),
						mClassKeyByte);
			}

		}

		@Override
		public BytesClassAidl getReMoveValue(int key, int meOrFile)
				throws RemoteException {
			
			switch (meOrFile) {
			case BytesClassAidl.To_File:
				return mFileCache.removeFromCacheByKey(key);
			case BytesClassAidl.To_File_Me: {
				BytesClassAidl mm = mMemoryCache.removeFromCacheByKey(key);
				if (mm != null) {
					mFileCache.removeFromCacheByKey(key);
				} else {
					return mFileCache.removeFromCacheByKey(key);
				}
				return mm;
			}
				case BytesClassAidl.To_Thread_Wait: {
//					try {
//						synchronized (this) {
//							this.wait();
//						}
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}

					while (isTestOn){
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}

					Su.log(this+"  end");

					BytesClassAidl mm = mMemoryCache.removeFromCacheByKey(key);
					if (mm != null) {
						mFileCache.removeFromCacheByKey(key);
					} else {
						return mFileCache.removeFromCacheByKey(key);
					}
					return mm;
				}
			default:
				BytesClassAidl mms = mMemoryCache.removeFromCacheByKey(key);
				DevRunningTime.isHasCache();
				return mms;
			}
		}

		@Override
		public int getMemoryCacheSize() {
			return mMemoryCache.getSize();
		}

	}

	@Override
	public IBinder onBind(Intent intent) {
		return new MyServiceImpl();
	}

	@Override
	public void onCreate() {

		super.onCreate();
		isTestOn =true;
		mMemoryCache = new MemoryCache();
		mFileCache = new FileCache(AidlServiceSetGetClass_CheckMain.this);

		if(!SuApplication.getInstance().isMainAppPro()) {//主进程就不需要了，直接使用Aidl方式进行检测（启动）
			new Thread(new Runnable() {
				@Override
				public void run() {
					startAidlCheckAppService();
				}
			}).start();
		}

	}


//	主要是用这个链接 检测app进程
	ServiceConnection mServiceConnectionForCheckApp = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder bb) {
			Su.log("AidlstartCheckAppService  onServiceConnected ");
//			((SuSuperService.MyBinder) bb).getService();
		}
		@Override
		public void onServiceDisconnected(ComponentName name) {
			Su.log("AidlstartCheckAppService  onServiceDisconnected   "+((isAwakeMainPro==true)?"需要唤醒APP":""));
			if(isAwakeMainPro)
			sendBroadcast(new Intent("com.hxuehh.rebirth.SuReLife"));
		}
	};



	private void startAidlCheckAppService() {
		Su.log("启动  AidlstartCheckAppService  main链接");
		Intent in=new Intent(this, AidlstartCheckAppService.class);
		SuApplication.getInstance().bindService(in, mServiceConnectionForCheckApp, Context.BIND_AUTO_CREATE);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return START_STICKY;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		Su.log("onStart" + intent);
	}

	@Override
	public void onDestroy() {
		Su.log(this+" onDestroy");
		isTestOn =false;
		super.onDestroy();
	}

	@Override
	public boolean onUnbind(Intent intent) {
		return super.onUnbind(intent);
	}

	@Override
	public void onRebind(Intent intent) {
		
		super.onRebind(intent);
		Su.log("onRebind"+intent);
	}

}
