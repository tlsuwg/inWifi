package com.hxuehh.appCore.aidl;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

//对象传递
public class BytesClassAidl extends BytesClass implements Parcelable, Serializable {
	
	private static final long serialVersionUID = 1024L;
	
	public static final int To_Me = 0;
	public static final int To_File = 1;
	public static final int To_File_Me = 2;
	public static final int To_Thread_Wait =3 ;

	private int toWhich;
	private int key;

	
	@Deprecated
	private Serializable tag;//预留标志区间  不建议使用 只建议使用bs 全部承载
	@Deprecated
	public Serializable getTag() {
		return tag;
	}
	@Deprecated
	public void setTag(Serializable tag) {
		this.tag = tag;
	}

	public BytesClassAidl() {
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public int getToWhich() {
		return toWhich;
	}

	public void setToWhich(int toWhich) {
		this.toWhich = toWhich;
	}

	public BytesClassAidl(int key, int meorFile, int size, String className,
                          byte[] bs) throws RuntimeException {
		super(size,className,bs);
		this.key = key;
		this.toWhich = meorFile;

	}

	public BytesClassAidl(int key, int meorFile, String className, byte[] bs)
			throws RuntimeException {
        super(className,bs);
		this.key = key;
		this.toWhich = meorFile;
	}

	public BytesClassAidl(int key, int meorFile, Serializable i)
			throws RuntimeException {// 没有加密
	super(i);
		this.key = key;
		this.toWhich = meorFile;
	}

	public BytesClassAidl(Parcel pl) {
        super(pl);
		toWhich = pl.readInt();
		key = pl.readInt();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest,flags);
		dest.writeInt(toWhich);
		dest.writeInt(key);
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	public static final Creator<BytesClassAidl> CREATOR = new Creator<BytesClassAidl>() {
		@Override
		public BytesClassAidl[] newArray(int size) {
			return new BytesClassAidl[size];
		}

		@Override
		public BytesClassAidl createFromParcel(Parcel in) {
			return new BytesClassAidl(in);
		}
	};



}
