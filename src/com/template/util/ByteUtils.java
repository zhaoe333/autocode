/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */
package com.template.util;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Date;

/**
 *
 * 
 * @author <a href="http://mina.apache.org">Apache MINA Project</a>
 * @since MINA 2.0.0-M3
 */
public class ByteUtils {
	private static ByteBuffer buffer = ByteBuffer.allocate(8);

	public static String asHex(byte[] bytes) {
		return asHex(bytes, null);
	}

	/**
	 * 有一个不�?xff即返回false 否则返回true
	 * 
	 * @param bytes
	 * @return
	 */
	public static boolean checkFF(byte... bytes) {
		for (byte b : bytes) {
			if (b != (byte) 0xff) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 获取无符号int�?xff返回null
	 * 
	 * @param b
	 * @return
	 */
	public static Integer getInt(byte b) {
		if (checkFF(b)) {
			return null;
		}
		return 0xff & b;
	}

	public static String asHex(Byte[] bytes) {
		byte[] val = new byte[bytes.length];
		for (int i = 0; i < bytes.length; i++) {
			val[i] = bytes[i].byteValue();
		}
		return asHex(val, null);
	}

	public static String asHex(byte[] bytes, String separator) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			String code = Integer.toHexString(bytes[i] & 0xFF);
			if ((bytes[i] & 0xFF) < 16) {
				sb.append('0');
			}

			sb.append(code);

			if (separator != null && i < bytes.length - 1) {
				sb.append(separator);
			}
		}

		return sb.toString();
	}

	public static byte[] byte2BitArray(byte b) {
		byte[] array = new byte[8];
		for (int i = 0; i < 8; i++) {
			array[i] = (byte) (b & 1);
			b = (byte) (b >> 1);
		}
		return array;
	}

	public static byte BitArray2Byte(byte[] bits) {
		byte result = 0x00;
		for (int i = 0; i < bits.length; i++) {
			result += (byte) (bits[i] << i);
		}
		return result;
	}

	public static byte[] convertString2Bytes(String str) {
		byte[] result = new byte[str.length()];
		for (int i = 0; i < str.length(); i++) {
			result[i] = (byte) str.charAt(i);
		}
		return result;
	}

	public static String convertBytes2String(byte[] datas) {

		StringBuffer buffer = new StringBuffer("");
		for (int i = 0; i < datas.length; i++) {
			buffer.append((char) datas[i]);
		}
		return buffer.toString();
	}

	public static void print(byte[] datas) {
		String befConver = "";
		for (int i = 0; i < datas.length; i++) {
			befConver += datas[i] + " ";
			System.out.print("0x" + Integer.toHexString(new Byte(datas[i]).intValue()) + " ");
		}
		System.out.println("");
		System.out.println(befConver);
	}

	public static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			stringBuilder.append(" 0x");
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

	public static byte[] hexStringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) ("0123456789ABCDEF".indexOf(hexChars[pos]) << 4
					| "0123456789ABCDEF".indexOf(hexChars[pos + 1]));
		}
		return d;
	}

	public static byte[] intToByteArray(final int integer, int length) {
		byte[] byteArray = new byte[length];
		for (int i = 0; i < length; i++){
			byteArray[i]=(byte)(integer >>> ((length-1-i)*8));
		}
		return byteArray;
	}

	public static byte[] intToByteArray(final int integer) {
		int byteNum = (40 - Integer.numberOfLeadingZeros(integer < 0 ? ~integer : integer)) / 8;
		byte[] byteArray = new byte[4];

		for (int n = 0; n < byteNum; n++)
			byteArray[3 - n] = (byte) (integer >>> (n * 8));

		return (byteArray);
	}

	public static byte[] intToByteArray2(final int integer) {
		int byteNum = (40 - Integer.numberOfLeadingZeros(integer < 0 ? ~integer : integer)) / 8;
		byte[] byteArray = new byte[4];

		for (int n = 0; n < byteNum; n++)
			byteArray[3 - n] = (byte) (integer >>> (n * 8));
		return new byte[] { byteArray[2], byteArray[3] };
	}

	public static int byteArrayToInt(byte[] b, int offset) {
		byte[] arr = new byte[4];
		for (int i = 0; i < offset; i++) {
			arr[i] = 0x00;
		}
		System.arraycopy(b, 0, arr, offset, b.length);
		offset = 0;
		int value = 0;
		for (int i = 0; i < 4; i++) {
			int shift = (4 - 1 - i) * 8;
			value += (arr[i + offset] & 0x000000FF) << shift;
		}
		return value;
	}
	/***
	 * 转换日期字符串，为byte数组
	 * 
	 * @param datetime
	 *            格式 yyyy-MM-dd HH:mm:ss
	 * @return 例如�?015-11-22 13:55:46 会返�?{0x0f,0x0b,0x16,0x0d,0x37,0x2e}
	 */
	public static byte[] convertDatetimeStr2ByteArr(String datetime) {
		byte[] data = new byte[6];
		String[] strArr = datetime.trim().split(" ");
		int i = 0;
		for (String s : strArr[0].split("-")) {
			data[i++] = (byte) (Integer.parseInt(s) % 100);
		}
		for (String s : strArr[1].split(":")) {
			data[i++] = (byte) Integer.parseInt(s);
		}
		return data;
	}

	public static byte[] convertDatetime2ByteArr(Date date) {
		String datetime = DateTimeUtils.formatDate(date);
		byte[] data = new byte[6];
		String[] strArr = datetime.trim().split(" ");
		int i = 0;
		for (String s : strArr[0].split("-")) {
			data[i++] = (byte) (Integer.parseInt(s) % 100);
		}
		for (String s : strArr[1].split(":")) {
			data[i++] = (byte) Integer.parseInt(s);
		}
		return data;
	}

	/***
	 * 转化二进制数组为时间字符�?	 * 
	 * @param data
	 * @return 例如，{0x0f,0x0b,0x16,0x0d,0x37,0x2e} 返回 2015-11-22 13:55:46
	 */
	public static String convertByteArrtDatetimeStr(byte[] data) {
		return "20" + data[0] + "-" + data[1] + "-" + data[2] + " " + data[3] + ":" + data[4] + ":" + data[5];
	}

	public static Date convertByteArrtDatetime(byte[] data) throws Exception {
		String dateStr = "20" + data[0] + "-" + data[1] + "-" + data[2] + " " + (data[3] == 0x00 ? "00" : data[3]) + ":"
				+ (data[4] == 0x00 ? "00" : data[4]) + ":" + (data[5] == 0x00 ? "00" : data[5]);
		return DateTimeUtils.parseDate(dateStr, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 将byte转换为一个长度为8的byte数组，数组每个�?代表bit
	 */
	public static byte[] getBitArray(byte b) {
		byte[] array = new byte[8];
		for (int i = 7; i >= 0; i--) {
			array[i] = (byte) (b & 1);
			b = (byte) (b >> 1);
		}
		return array;
	}

	/**
	 * 把byte转为字符串的bit
	 */
	public static String byteToBit(byte b) {
		return "" + (byte) ((b >> 7) & 0x1) + (byte) ((b >> 6) & 0x1) + (byte) ((b >> 5) & 0x1)
				+ (byte) ((b >> 4) & 0x1) + (byte) ((b >> 3) & 0x1) + (byte) ((b >> 2) & 0x1) + (byte) ((b >> 1) & 0x1)
				+ (byte) ((b >> 0) & 0x1);
	}

	public static byte[] getBytes(char[] chars) {
		byte[] bytes = new byte[chars.length];
		for (int i = 0; i < chars.length; i++) {
			char c = chars[i];
			bytes[i] = (byte) c;
		}
		return bytes;
	}

//	public static byte[] addAll(byte[]... bytess) {
//		int length = 0;
//		for (byte[] bytes : bytess) {
//			length += bytes.length;
//		}
//		byte[] result = new byte[length];
//		int pos = 0;
//		for (byte[] bytes : bytess) {
//			System.arraycopy(bytes, 0, result, pos, bytes.length);
//			pos += bytes.length;
//		}
//		return result;
//	}
	public static byte[] addAll(Object... objs) {
		int length = 0;
		for (Object obj : objs) {
			if (obj.getClass().isArray()) {
				byte[] objArr = (byte[]) obj;
				length += objArr.length;
			} else {
				length += 1;
			}
		}
		byte[] result = new byte[length];
		int pos = 0;
		for (Object obj : objs) {
			if (obj.getClass().isArray()) {
				byte[] objArr = (byte[]) obj;
				System.arraycopy(objArr, 0, result, pos, objArr.length);
				pos += objArr.length;
			} else {
				result[pos] = (byte)obj;
				pos += 1;
			}
		}
		return result;
	}
	/**
	 * 参数只能�?byte或�?byte数组 不可以用Byte对象或�?Byte对象数组
	 * 
	 * @param <T>
	 * @param objs
	 * @return
	 */
	public static <T> T[] addAll(Class<T> clazz, Object... objs) {
		int length = 0;
		for (Object obj : objs) {
			if (obj.getClass().isArray()) {
				T[] objArr = (T[]) obj;
				length += objArr.length;
			} else {
				length += 1;
			}
		}
		T[] result = (T[]) Array.newInstance(clazz, length);
		int pos = 0;
		for (Object obj : objs) {
			if (obj.getClass().isArray()) {
				T[] objArr = (T[]) obj;
				System.arraycopy(objArr, 0, result, pos, objArr.length);
				pos += objArr.length;
			} else {
				result[length] = (T) obj;
				pos += 1;
			}
		}
		return result;
	}

	/**
	 * 
	 * @param a
	 *            �?��转化的byte
	 * @param der
	 *            除数
	 * @param diff
	 *            差�?
	 * @return
	 */
	public static Double toDouble(byte a, double der, double diff) {
		int temp = 0xff & a;
		// double result=(double)temp/der-diff;
		return BigDecimal.valueOf(temp).divide(BigDecimal.valueOf(der)).subtract(BigDecimal.valueOf(diff))
				.doubleValue();
	}

	/**
	 * 
	 * @param a�?��转化的byte
	 * @param b�?��转化的byte
	 * @param der
	 *            除数
	 * @param diff差�?
	 * @return
	 */
	public static Double toDouble(byte a, byte b, double der, int diff) {
		int temp = ByteUtils.byteArrayToInt(new byte[] { 0x00, 0x00, a, b }, 0);
		// return ((double)temp)/der-diff;
		return BigDecimal.valueOf(temp).divide(BigDecimal.valueOf(der)).subtract(BigDecimal.valueOf(diff))
				.doubleValue();
	}

	public static Double toDouble(byte a, byte b, byte c, byte d, double der, int diff) {
		int temp = ByteUtils.byteArrayToInt(new byte[] { a, b, c, d }, 0);
		// return ((double)temp)/der-diff;
		return BigDecimal.valueOf(temp).divide(BigDecimal.valueOf(der)).subtract(BigDecimal.valueOf(diff))
				.doubleValue();
	}

	public static Double intToDouble(int temp, double der, int diff) {
		return BigDecimal.valueOf(temp).divide(BigDecimal.valueOf(der)).subtract(BigDecimal.valueOf(diff))
				.doubleValue();
	}

	public static byte[] cleanEnd(byte[] bytes, byte end) {
		int i = bytes.length - 1;
		for (; i > 0; i--) {
			if (bytes[i] != end) {
				break;
			}
		}
		return Arrays.copyOfRange(bytes, 0, i + 1);
	}

	public static byte[] cleanEnd(byte[] bytes, byte endByte, int start, int end) {
		for (; end > start - 1; end--) {
			if (bytes[end] != endByte) {
				break;
			}
		}
		return Arrays.copyOfRange(bytes, start, end + 1);
	}

	public static byte[] fillEnd(byte[] bytes, int length, byte end) {
		byte[] result = new byte[length];
		for (int i = 0; i < length; i++) {
			if (i < bytes.length) {
				result[i] = bytes[i];
			} else {
				result[i] = end;
			}
		}
		return result;
	}

	public static String binaryString2hexString(String bString) {
		if (bString == null || bString.equals(""))
			return null;
		StringBuffer tmp = new StringBuffer();
		tmp.append("0x");
		int iTmp = 0;
		for (int i = 0; i < bString.length(); i += 4) {
			iTmp = 0;
			for (int j = 0; j < 4; j++) {
				iTmp += Integer.parseInt(bString.substring(i + j, i + j + 1)) << (4 - j - 1);
			}
			tmp.append(Integer.toHexString(iTmp));
		}
		return tmp.toString();
	}

	/**
	 * 
	 * @param d
	 *            �?��转化的double
	 * @param multiply
	 *            乘数
	 * @param add
	 *            加�?
	 * @return
	 */
	public static int DoubleToInt(double d, double multiply, double add) {
		int result = BigDecimal.valueOf(d).add(BigDecimal.valueOf(add)).multiply(BigDecimal.valueOf(multiply))
				.intValue();
		return result;
	}
}