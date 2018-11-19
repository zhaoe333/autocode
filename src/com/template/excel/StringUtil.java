package com.template.excel;

import java.io.UnsupportedEncodingException;

public class StringUtil {
    /**
     * 判断一个字符是Ascill字符还是其它字符（如汉，日，韩文字符）
     *
     * @param c 需要判断的字符
     * @return 返回true,Ascill字符
     */
    public static boolean isLetter(char c) {
        int k = 0x80;
        return c / k == 0 ? true : false;
    }

    /**
     * 得到一个字符串的长度,显示的长度,一个汉字或日韩文长度为2,英文字符长度为1
     *
     * @param s 需要得到长度的字符串
     * @return i得到的字符串长度
     */
    public static int length(String s) {
        if (s == null) {
            return 0;
        }
        char[] c = s.toCharArray();
        int len = 0;
        for (int i = 0; i < c.length; i++) {
            len++;
            if (!isLetter(c[i])) {
                len++;
            }
        }
        return len;
    }

    /**
     * 截取一段字符的长度,不区分中英文,如果数字不正好，则少取一个字符位
     *
     *
     * @param  origin 原始字符串
     * @param len 截取长度(一个汉字长度按2算的)
     * @param c 后缀
     * @return 返回的字符串
     */
    public static String substring(String origin, int len, String c) {
        if (origin == null || origin.equals("") || len < 1) {
            return "";
        }
        if (length(origin) <= len) {
            return origin;
        }
        byte[] strByte = new byte[len];
        if (len > length(origin)) {
            return origin + c;
        }
        try {
            System.arraycopy(origin.getBytes("GBK"), 0, strByte, 0, len);
            int count = 0;
            for (int i = 0; i < len; i++) {
                int value = (int) strByte[i];
                if (value < 0) {
                    count++;
                }
            }
            if (count % 2 != 0) {
                len = (len == 1) ? ++len : --len;
            }
            return new String(strByte, 0, len, "GBK") + c;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * 判断字符串是否存在
     * @param str
     * @return
     */
    public static boolean isExist(String str) {
        if (str != null && str.length() > 0) { 
            return true;
        } 
        return false;
    }
    
    public static boolean isNumber(String str) {
        //验证数字：^[0-9]*$
        return str.matches("^\\d+(\\,\\d+)*$");
    }
    public static boolean isInterger(String str){
    	return str.matches( "^-?\\d+$");
    }
    //验证是否为普通字符串
    public static boolean isNornalStr(String str){
    	return str.matches("^[a-zA-Z0-9_\\-—.%]*$");
    }
    public static boolean isNornalURL(String str){
    	return str.matches("^[a-zA-Z0-9?=./_-]*$");
    }
    public static boolean isUnicode(String str){
    	return str.matches("^[a-zA-Z0-9%]*$");
    }
    // 转码iso-8859-1
    public static String ecodeStr(String str) {
        try {
            str = new String(str.getBytes("UTF-8"), "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }
    
    // 转码utf-8
    public static String decodeStr(String str) {
        try {
            str = new String(str.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }
    
    public static String joinStr(String[] array,String joinChar){
    	if(array==null)
    	return "";
    	if(joinChar==null)joinChar=",";
    	String str="";
    	for(int i=0;i<array.length;i++){
    		str=str+array[i];
    		if(i<array.length-1)str=str+joinChar;
    	}
    	return str;
    }
    
    public static String toUpperCaseFirstOne(String param){
    	if(Character.isUpperCase(param.charAt(0))){
    		return param;
    	}else{
    		return (new StringBuilder()).append(Character.toUpperCase(param.charAt(0))).append(param.substring(1)).toString();
    	}
    }
    
    public static String toHump(String param,String key){
    	StringBuffer buffer = new StringBuffer();
    	String[] strs = param.split(key);
    	buffer.append(strs[0]);
    	for(int i=1;i<strs.length;i++){
    		buffer.append(toUpperCaseFirstOne(strs[i]));
    	}
    	return buffer.toString();
    }
}
