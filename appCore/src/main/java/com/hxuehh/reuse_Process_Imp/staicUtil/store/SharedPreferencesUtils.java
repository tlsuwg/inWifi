package com.hxuehh.reuse_Process_Imp.staicUtil.store;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import com.hxuehh.appCore.app.SuApplication;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;


@SuppressLint("NewApi")
public class SharedPreferencesUtils {
	private static SharedPreferences mPreferences;

	static {
        mPreferences = SuApplication.getInstance().getSharedPreferences("_core_aidl",
                Context.MODE_PRIVATE);
	}

	public static void putInteger(String name, int value) {
		mPreferences.edit().putInt(name, value).commit();
	}

	public static int getInteger(String name) {
		return mPreferences.getInt(name, -1);
	}

	public static void putString(String name, String value) {
		mPreferences.edit().putString(name, value).commit();
	}

	public static String getString(String name) {
		return mPreferences.getString(name, "");
	}

	public static String getStringDefaultNull(String name) {
		return mPreferences.getString(name, null);
	}

	public static void putBoolean(String name, boolean flag) {
		mPreferences.edit().putBoolean(name, flag).commit();
	}

	public static boolean getBoolean(String name) {
		return mPreferences.getBoolean(name, false);
	}

	public static long getLong(String name) {
		return mPreferences.getLong(name, 0l);
	}

	public static void putLong(String name, long value) {
		mPreferences.edit().putLong(name, value).commit();
	}

	public static void clear() {
		mPreferences.edit().clear().commit();
	}

	public static void remove(String name) {
		mPreferences.edit().remove(name).commit();

	}

	public static void putObject(Serializable oo, String Key) {
		String stream = "";
		try {
			ByteArrayOutputStream bao = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bao);
			oos.writeObject(oo);
			oos.flush();
			oos.close();
			bao.close();
			stream = Base64.encodeToString(bao.toByteArray(), Base64.DEFAULT);
			putString(Key, stream);
		} catch (Exception e) {
			// LogUtil.w(e);
		}
	}


	public static Object getObject(String Key) {
		ByteArrayInputStream bis = null;
		ObjectInputStream ois = null;
		try {
			String data = getString(Key);
			if (data == null)
				return null;
			bis = new ByteArrayInputStream(Base64.decode(data, Base64.DEFAULT));
			ois = new ObjectInputStream(bis);
			Object object = ois.readObject();
			return object;
		} catch (Exception e) {
			// LogUtil.w(e);
		} finally {
			try {
				if (bis != null) {
					bis.close();
				}
				if (ois != null) {
					ois.close();
				}
			} catch (IOException e) {
				// LogUtil.w(e);
			}
		}
		return null;
	}
}
