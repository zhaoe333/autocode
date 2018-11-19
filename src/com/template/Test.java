package com.template;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.apache.commons.io.IOUtils;

import com.template.excel.ExcelTool;
import com.template.util.ByteUtils;
import com.template.util.DateTimeUtils;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

public class Test {
	public static void main(String[] args) throws Exception {
//		// 创建SSLContext对象，并使用我们指定的信任管理器初始化
//        TrustManager[] tm = { new MyX509TrustManager() };
//        SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
//        sslContext.init(null, tm, new java.security.SecureRandom());
//        // 从上述SSLContext对象中得到SSLSocketFactory对象
//        SSLSocketFactory ssf = sslContext.getSocketFactory();
////        HttpsURLConnection.setDefaultSSLSocketFactory(ssf);
//        // 创建URL对象
//        URL myURL = new URL("https://ebanks.gdb.com.cn/sperbank/perbankLogin.jsp");
//        // 创建HttpsURLConnection对象，并设置其SSLSocketFactory对象
//        HttpsURLConnection httpsConn = (HttpsURLConnection) myURL.openConnection();
//        httpsConn.setSSLSocketFactory(ssf);
//        httpsConn.setHostnameVerifier(new HostnameVerifier() {
//			@Override
//			public boolean verify(String hostname, SSLSession session) {
//				return true;
//			}
//		});
//        httpsConn.addRequestProperty("Content-Type","application/x-www-form-urlencoded");
//        httpsConn.connect();
//        System.out.println(IOUtils.toString(httpsConn.getInputStream()));
		
	}

}

class MyThread extends Thread {
	private int order;

	public MyThread(int order) {
		super();
		this.order = order;
	}

	@Override
	public void run() {
		System.out.println(Thread.currentThread().getName() + "---" + order + "正在执行。。。");
		try {
			Thread.sleep(1000l);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(Thread.currentThread().getName() + "---" + order + "执行完成。。。");
	}
}

class MyThread2 extends Thread {
	private int order;

	public MyThread2(int order) {
		super();
		this.order = order;
	}

	@Override
	public void run() {
		System.out.println(Thread.currentThread().getName() + "---" + order + "正在执行。。。");
		try {
			Thread.sleep(1000l);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(Thread.currentThread().getName() + "---" + order + "执行完成。。。");
	}
}
