package com.hxuehh.appCore.aidl;

import com.hxuehh.appCore.develop.Su;

import java.io.Serializable;
import java.util.LinkedList;


//主要是为了buye的较大数据传输时候使用的
//传输数组是个不稳定的方式

public class InstabilityByte extends LinkedList<byte[]> implements Serializable {
	private transient byte[] bs;
	public static final int every = 2<<8;
	private int lastSize;

	public InstabilityByte(byte[] bs) throws SuExceptionOrExProxy {
		super();
		this.bs = bs;
		if (bs != null&&bs.length>0) {
			split();
		}else{
            throw new SuExceptionOrExProxy("稳定器传输出现问题，当前为0");
        }
	}

	private void split() {
		// TODO Auto-generated method stub
		lastSize = bs.length % every;
		int times=(lastSize==0)?  (bs.length / every): (bs.length / every)+1;
		int time=0;
		for (;;) {
			byte[] thist = new byte[every];
			System.arraycopy(bs, time * every, thist, 0, every);
			this.add(thist);
			time++;
			if(time==times-1)break;
		}
		if (lastSize != 0) {
			byte[] thist = new byte[lastSize];
			System.arraycopy(bs, time * every, thist, 0, lastSize);
			this.add(thist);
		}
		Su.log("分拆" + this.size() + "小包");
	}

	public byte[] getBytes() throws SuExceptionOrExProxy {
		if (bs == null) {
			if(this.size()==0)throw new SuExceptionOrExProxy("稳定器传输出现问题，当前为0");
			Su.log("得到"+this.size()+"小包");
			int size = lastSize == 0 ? this.size() * every : ((this.size() - 1)
					* every + lastSize);
			bs = new byte[size];
			int allsize = 0;
			byte[] bso = null;			
			while (this.size()>=1&&(bso = this.remove(0)) != null) {
				System.arraycopy(bso, 0, bs, allsize,  bso.length);
				allsize = allsize + bso.length;
			}
			Su.log("得到"+bs.length);
		}
		return bs;
	}

}
