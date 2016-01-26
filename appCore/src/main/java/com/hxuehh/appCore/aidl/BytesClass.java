package com.hxuehh.appCore.aidl;

import android.os.Parcel;
import android.os.Parcelable;

import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.SerializeUtil;

import java.io.Serializable;

//这个是一般的可以传输的
public class BytesClass implements Parcelable, Serializable {

	private static final long serialVersionUID = 1024L;

	private int size;
	private String className;
	private byte[] bs;//核心区域，这个可以是java基础对象，也可以是javaobject对象，当然可以做加密处理，

	public BytesClass() {
	}


	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public byte[] getBs() {
		return bs;
	}

	public void setBs(byte[] bs) {
		this.bs = bs;
	}



	public BytesClass( int size, String className,
                      byte[] bs) throws RuntimeException {
		super();
		this.size = size;
		this.className = className;
		if (bs == null)
			throw new RuntimeException("没有对象就不要传递了，至少包装一下");
		this.bs = bs;
	}

	public BytesClass( String className, byte[] bs)
			throws RuntimeException {
		this.className = className;
		if (bs == null)
			throw new RuntimeException("没有对象就不要传递了，至少包装一下");
		this.bs = bs;
		this.size = bs.length;
	}

	public BytesClass(Serializable i)
			throws RuntimeException {// 没有加密
		if (i == null)
			throw new RuntimeException("没有对象就不要传递了，至少包装一下");
		this.className = i.getClass().getCanonicalName();
		bs = SerializeUtil.serialize(i);
		if (bs == null)
			throw new RuntimeException("对象转byte[]出错");
		this.size = bs.length;
	}

	public BytesClass(Parcel pl){
		size = pl.readInt();
		className = pl.readString();
		bs = new byte[size];
		pl.readByteArray(bs);
	}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(size);
		dest.writeString(className);
		dest.writeByteArray(bs);
	}



	public static final Creator<BytesClass> CREATOR = new Creator<BytesClass>() {
		@Override
		public BytesClass[] newArray(int size) {
			return new BytesClass[size];
		}

		@Override
		public BytesClass createFromParcel(Parcel in) {
			return new BytesClass(in);
		}
	};


    public Object getTrue() {
        return SerializeUtil.unserialize(bs);
    }
}
