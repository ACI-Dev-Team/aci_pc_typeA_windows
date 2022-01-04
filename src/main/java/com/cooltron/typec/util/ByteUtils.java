package com.cooltron.typec.util;


public class ByteUtils {

	static final char[] digits = {
			'0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', 'a', 'b',
			'c', 'd', 'e', 'f', 'g', 'h',
			'i', 'j', 'k', 'l', 'm', 'n',
			'o', 'p', 'q', 'r', 's', 't',
			'u', 'v', 'w', 'x', 'y', 'z'
	};

	public static String toHexStrWithSpace(byte[] bytes) {
		if (null == bytes || bytes.length == 0)
			return "";
		StringBuffer sb = new StringBuffer();
		for (byte b : bytes) {
			sb.append(toHexStr(b)).append(" ");
		}
		return sb.toString();
	}

	public static String toHexStr(byte b) {
		return toUnsignedString0(b, 4, 2);
	}

	private static String toUnsignedString0(int val, int shift, int len) {
		char[] buf = new char[len];
		for (int i = 0; i < buf.length; ++i) {
			buf[i] = '0';
		}
		formatUnsignedInt(val, shift, buf, 0, len);
		return new String(buf).toUpperCase();
	}

	private static int formatUnsignedInt(int val, int shift, char[] buf, int offset, int len) {
		int charPos = len;
		int radix = 1 << shift;
		int mask = radix - 1;
		do {
			buf[offset + --charPos] = digits[val & mask];
			val >>>= shift;
		} while (val != 0 && charPos > 0);

		return charPos;
	}

	public static byte checksum(byte[] msg, int checkLen) {
		if(msg==null || msg.length==0 || checkLen==0){
			return 0;
		}
		if(msg.length<checkLen){
			checkLen = msg.length;
		}
		long result = 0;
		for(int i=0;i<checkLen;i++){
			result += msg[i];
		}
		return (byte) (result & 0xff);
	}

}
