/**
 * 
 */
package com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil;

import java.math.BigInteger;

/**
 * @author suwg
 * @data 2014-4-9
 */
public class MathUtil {
	
	
	public static final String H16="0123456789ABCDEF";
	
	private static byte charToByte(char c) {
		return (byte) H16.indexOf(c);
	}

	// ��ff ת����byte
	public static byte[] hexStringToBytes(String hexString) throws Exception {
		if (hexString == null || hexString.equals("")) {
			throw new Exception("����������null");
		}
		
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return d;
	}

	
	
//	// ��ָ��byte���� ת���� 16����ff
	public static String bytesToHexString(byte[] src,int length) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}
	
	
	public static Object byteToHexString(byte b) {
		StringBuilder stringBuilder = new StringBuilder("");
			int v = b & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		
		return stringBuilder.toString();
	}
	
	
	// ��2����ת����byte
		public static byte[] bigIntStringToBytes(String bigInt) throws Exception {
			if (bigInt == null || bigInt.equals("")) {
				throw new Exception("����������null");
			}
			
			int length = bigInt.length() / 8;
			
			byte[] d = new byte[length];
			for (int i = 0; i < length; i++) {
				int pos = i * 8;
				String kk=bigInt.substring(pos, pos+8);
				BigInteger srcout = new BigInteger(kk, 2);// ת��ΪBigInteger����
				 d[i] = (byte)srcout.intValue();
			}
			return d;
		}

		/**
		 * @param b
		 * @return
		 */
	
		
	

	// 2����ת���� int
	// BigInteger srcout = new BigInteger(outstr, 2);// ת��ΪBigInteger����
	// int out = srcout.intValue();



    /**
     * String类型转int类型
     *
     * @param number
     * @return
     */
    public static int stringToInt(String number) {
        int num = 0;
        if (number != null && !"".equals(number)) {
            try {
                num = Integer.parseInt(number);
            } catch (NumberFormatException e) {
                num = 0;
            }
        }
        return num;
    }


    public static int bytes4_2int(byte[] b) {//4个
        int mask = 0xff;
        int temp = 0;
        int res = 0;
        for (int i = 0; i < 4; i++) {
            res <<= 8;
            temp = b[i] & mask;
            res |= temp;
        }
        return res;
    }


    public static byte[] int2_4bytes(int num) {
        byte[] b = new byte[4];
        for (int i = 0; i < 4; i++) {
            b[i] = (byte) (num >>> (24 - i * 8));
        }
        return b;
    }


	public static byte[] removeFrist(byte[] bytes2) {
		if(bytes2==null||bytes2.length<1)return null;
		int leng=bytes2.length-1;
		byte bs[]=new byte[bytes2.length-1];
		System.arraycopy(bytes2,1,bs,0,leng);
		return bs;
	}



	public static byte[] bytesTobitTrue(byte[] bs) {//化成位字节  111;  这个是0在前
		if(bs==null)return null;
		byte[] bb=new byte[bs.length*8];
		int i=0;
		for(byte b:bs){
			for(int k=7;k>=0;k--){
			bb[i*8+7-k]= (byte) ((b>>k)&0x01);
			}
			i++;
		}
		return bb;
	}

	public static byte[] bytesTobit(byte[] bs) {//化成位字节 0000  这个其实是不对的，因为1在最后一位才是1，在最前一位是
		if(bs==null)return null;
		byte[] bb=new byte[bs.length*8];
		int i=0;
		for(byte b:bs){
			for(int k=0;k<=7;k++){
				bb[i*8+k]= (byte) ((b>>k)&0x01);
			}
			i++;
		}
		return bb;
	}



	public static byte[] bitToBytes(byte[] bs) throws Exception {//这个是不对的，按照从前到后的方式算出值   1000000就是1 ；翻转了
		if(bs==null)return null;
		if(bs.length%8!=0)throw new Exception("不合适的2进制长度");
		int size=bs.length/8;
		byte[] bbytes=new byte[size];

		for(int k=0;k<size;k++) {
			byte b = (byte) 0;
			for (int i = 0; i <=7; i++) {
				byte index=bs[i+k*8];
				if(index!=0&index!=1)throw new Exception("2进制只有1,0");
//				System.out.println(i+" "+index+"  "+(index << (7-i))+" ");
				b = (byte) (b | (index << (i)));
			}
			bbytes[k]=b;
		}
		return bbytes;
	}

	public static byte[] bitToBytesTrue(byte[] bs) throws Exception {//0101转换成byte
		if(bs==null)return null;
		if(bs.length%8!=0)throw new Exception("不合适的2进制长度");
		int size=bs.length/8;
		byte[] bbytes=new byte[size];

		for(int k=0;k<size;k++) {
			byte b = 0;
			for (int i =0; i <=  7; i++) {
				byte index=bs[k*8+i];
				if(index!=0&&index!=1)throw new Exception("2进制只有1,0");
				int kkkk=(index << (7-i));
				b = (byte) (b | kkkk);
			}
			bbytes[k]=b;
		}
		return bbytes;
	}

}
