package swagger;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.template.util.JacksonUtil;
import com.template.util.LogicUtil;

/***
 * Http请求客户端
 * @author zyl
 *
 */
public class HttpUtil {
	
	public static Logger log  = Logger.getLogger(HttpUtil.class);
	
	/**
	 * get方式请求
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public static String get(String url) throws Exception {
		String result = "{}";
		CloseableHttpClient httpClient=null;
		CloseableHttpResponse httpResponse=null;
		try{
			httpClient =HttpClients.createDefault();
			HttpClientContext context = HttpClientContext.create();
			HttpGet httpGet = new HttpGet(url);
			httpResponse = httpClient.execute(httpGet,context);
			if(httpResponse.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = httpResponse.getEntity();
				result = EntityUtils.toString(entity);
				log.debug(result);
				EntityUtils.consume(entity);
			}
		}catch(Exception e){
			throw new RuntimeException("http client error!");
		}finally{
			if(httpResponse!=null){
				httpResponse.close();
			}
		}
		return result;
	}
	/**
	 * get方式请求
	 * @param url
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public static String get(String url,Map<String,Object> params) throws Exception {
		String result = "{}";
		CloseableHttpClient httpClient=null;
		CloseableHttpResponse httpResponse=null;
		try{
			httpClient =HttpClients.createDefault();
			HttpClientContext context = HttpClientContext.create();
			if(LogicUtil.isNotNullAndEmpty(params)){
				if(!url.endsWith("?")){
					url+="?";
				}
				for(String key : params.keySet()){
					url = addParam(url, key,params.get(key));
				}
			}
			HttpGet httpGet = new HttpGet(url);
			httpResponse = httpClient.execute(httpGet,context);
//			if(httpResponse.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = httpResponse.getEntity();
				result = EntityUtils.toString(entity);
				log.debug(result);
				EntityUtils.consume(entity);
//			}
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException("http client error!");
		}finally{
			if(httpResponse!=null){
				httpResponse.close();
			}
		}
		return result;
	}
	/**
	 * get方式请求
	 * @param url
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public static byte[] getByte(String url,Map<String,Object> params) throws Exception {
		byte[] bytes;
		CloseableHttpClient httpClient=null;
		CloseableHttpResponse httpResponse=null;
		try{
			httpClient =HttpClients.createDefault();
			HttpClientContext context = HttpClientContext.create();
			if(LogicUtil.isNotNullAndEmpty(params)){
				if(!url.endsWith("?")){
					url+="?";
				}
				for(String key : params.keySet()){
					url = addParam(url, key,params.get(key));
				}
			}
			HttpGet httpGet = new HttpGet(url);
			httpResponse = httpClient.execute(httpGet,context);
//			if(httpResponse.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = httpResponse.getEntity();
				bytes = EntityUtils.toByteArray(entity);
				log.debug(bytes);
				EntityUtils.consume(entity);
//			}
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException("http client error!");
		}finally{
			if(httpResponse!=null){
				httpResponse.close();
			}
		}
		return bytes;
	}
	/**
	 * get方式请求
	 * @param url
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public static String get(String url,Map<String,Object> params,Map<String,Object> headers) throws Exception {
		String result = "{}";
		CloseableHttpClient httpClient=null;
		CloseableHttpResponse httpResponse=null;
		try{
			httpClient =HttpClients.createDefault();
			HttpClientContext context = HttpClientContext.create();
			if(LogicUtil.isNotNullAndEmpty(params)){
				if(!url.endsWith("?")){
					url+="?";
				}
				for(String key : params.keySet()){
					url = addParam(url, key,params.get(key));
				}
			}
			HttpGet httpGet = new HttpGet(url);
			if(LogicUtil.isNullOrEmpty(headers)){
				setHeader(httpGet,headers);
			}
			httpResponse = httpClient.execute(httpGet,context);
//			if(httpResponse.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = httpResponse.getEntity();
				result = EntityUtils.toString(entity);
				log.debug(result);
				EntityUtils.consume(entity);
//			}
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException("http client error!");
		}finally{
			if(httpResponse!=null){
				httpResponse.close();
			}
		}
		return result;
	}
	/**
	 * post方式请求 没有参数时 params为空的map 不能为null
	 * @param url
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public static String post(String url, Map<String, Object> params) throws Exception{
		String result = "{}";
		
		CloseableHttpClient httpClient=null;
		CloseableHttpResponse httpResponse=null;
		try{
			httpClient =HttpClients.createDefault();
			HttpClientContext context = HttpClientContext.create();
			HttpPost httpPost = new HttpPost(url);
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			
			Set<String> keySet = params.keySet();
			for (String key : keySet) {
				addParam(nvps, key,params.get(key));
			}
			httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
			httpResponse = httpClient.execute(httpPost,context);
//			if(httpResponse.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = httpResponse.getEntity();
				result = EntityUtils.toString(entity);
				log.debug(result);
				EntityUtils.consume(entity);
//			}
		}catch(Exception e){
			throw new RuntimeException("http client error!");
		}finally{
			if(httpResponse!=null){
				httpResponse.close();
			}
		}
		return result;
		
	}
	
	
	/**
	 * post方式请求 没有参数时 params为空的map 不能为null
	 * @param url
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public static String post(String url, Map<String, Object> params,Map<String,Object> headers) throws Exception{
		String result = "{}";
		
		CloseableHttpClient httpClient=null;
		CloseableHttpResponse httpResponse=null;
		try{
			httpClient =HttpClients.createDefault();
			HttpClientContext context = HttpClientContext.create();
			HttpPost httpPost = new HttpPost(url);
			if(LogicUtil.isNullOrEmpty(headers)){
				setHeader(httpPost,headers);
			}
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			
			Set<String> keySet = params.keySet();
			for (String key : keySet) {
				addParam(nvps, key,params.get(key));
			}
			httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
			httpResponse = httpClient.execute(httpPost,context);
//			if(httpResponse.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = httpResponse.getEntity();
				result = EntityUtils.toString(entity);
				log.debug(result);
				EntityUtils.consume(entity);
//			}
		}catch(Exception e){
			throw new RuntimeException("http client error!");
		}finally{
			if(httpResponse!=null){
				httpResponse.close();
			}
		}
		return result;
		
	}
	
	/**
	 * post方式上传文件 
	 * @param url
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public static String postFile(String url, Map<String, Object> params) throws Exception{
		String result = "{}";
		
		CloseableHttpClient httpClient=null;
		CloseableHttpResponse httpResponse=null;
		try{
			httpClient =HttpClients.createDefault();
			HttpClientContext context = HttpClientContext.create();
			HttpPost httpPost = new HttpPost(url);
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			MultipartEntityBuilder bulider=MultipartEntityBuilder.create();
			Set<String> keySet = params.keySet();
			for (String key : keySet) {
				addParam(bulider, key,params.get(key));
			}
			httpPost.setEntity(bulider.seContentType(ContentType.MULTIPART_FORM_DATA).setCharset(Charset.forName("UTF-8")).build());
			httpResponse = httpClient.execute(httpPost,context);
//			if(httpResponse.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = httpResponse.getEntity();
				result = EntityUtils.toString(entity);
				log.debug(result);
				EntityUtils.consume(entity);
//			}
		}catch(Exception e){
			throw new RuntimeException("http client error!");
		}finally{
			if(httpResponse!=null){
				httpResponse.close();
			}
		}
		return result;
		
	}
	/**
	 * post方式请求 没有参数时 params为空的map 不能为null
	 * @param url
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public static String postJson(String url, Object params) throws Exception{
		String result = "{}";
		
		CloseableHttpClient httpClient=null;
		CloseableHttpResponse httpResponse=null;
		try{
			httpClient =HttpClients.createDefault();
			HttpClientContext context = HttpClientContext.create();
			HttpPost httpPost = new HttpPost(url);
			httpPost.setEntity(EntityBuilder.create().setContentType(ContentType.APPLICATION_JSON).setText(JacksonUtil.get().readAsString(params)).build());
			httpResponse = httpClient.execute(httpPost,context);
//			if(httpResponse.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = httpResponse.getEntity();
				result = EntityUtils.toString(entity);
				log.debug(result);
				EntityUtils.consume(entity);
//			}
		}catch(Exception e){
			throw new RuntimeException("http client error!");
		}finally{
			if(httpResponse!=null){
				httpResponse.close();
			}
		}
		return result;
		
	}
	
	
	/**
	 * post方式请求 没有参数时 params为空的map 不能为null
	 * @param url
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public static String postJson(String url, Object params,Map<String,Object> headers) throws Exception{
		String result = "{}";
		
		CloseableHttpClient httpClient=null;
		CloseableHttpResponse httpResponse=null;
		try{
			httpClient =HttpClients.createDefault();
			HttpClientContext context = HttpClientContext.create();
			HttpPost httpPost = new HttpPost(url);
			if(LogicUtil.isNullOrEmpty(headers)){
				setHeader(httpPost,headers);
			}
			httpPost.setEntity(EntityBuilder.create().setContentType(ContentType.APPLICATION_JSON).setText(JacksonUtil.get().readAsString(params)).build());
			httpResponse = httpClient.execute(httpPost,context);
//			if(httpResponse.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = httpResponse.getEntity();
				result = EntityUtils.toString(entity);
				log.debug(result);
				EntityUtils.consume(entity);
//			}
		}catch(Exception e){
			throw new RuntimeException("http client error!");
		}finally{
			if(httpResponse!=null){
				httpResponse.close();
			}
		}
		return result;
		
	}
	
	public static String request(String type,String url,Map<String, Object> params) throws Exception{
		if("get".equals(type)){
			return get(url, params);
		}else if("post".equals(type)){
			return post(url, params);
		}else{
			throw new RuntimeException("The request of the unknown type!");
		}
	}
	
	public static String request(String type,String url,Map<String, Object> params,Map<String,Object> headerParams) throws Exception{
		if("get".equals(type)){
			return get(url, params,headerParams);
		}else if("post".equals(type)){
			return post(url, params,headerParams);
		}else if("postJson".equals(type)){
			return postJson(url, params,headerParams);
		}else{
			throw new RuntimeException("The request of the unknown type!");
		}
	}
	
	private static void setHeader(HttpRequestBase request,Map<String,Object> headerParams){
		if(LogicUtil.isNotNullAndEmpty(headerParams)){
			for(String key:headerParams.keySet()){
				request.setHeader(key, headerParams.get(key).toString());
			}
		}
	}
	
	private static String addParam(String url,String key,Object valueObj) throws Exception{
		if(valueObj==null){
			return url;
		}
		if(valueObj.getClass().isArray()){
			Object[] valueObjArrays= (Object[])valueObj;
			for(Object obj:valueObjArrays){
				url=addParam(url, key, obj);
			}
		}else if(valueObj instanceof Collection){
			Collection collection = (Collection)valueObj;
			for(Object obj:collection){
				url=addParam(url, key, obj);
			}
		}else if(!"".equals(valueObj.toString())){
			url+="&"+key+"="+URLEncoder.encode(valueObj.toString(), "UTF-8");
		}
		return url;
	}
	
	private static void addParam(List<NameValuePair> nvps, String key,
			Object valueObj) {
		if(valueObj==null){
			return;
		}
		if(valueObj.getClass().isArray()){
			Object[] valueObjArrays= (Object[])valueObj;
			for(Object obj:valueObjArrays){
				addParam(nvps, key, obj);
			}
		}else if(valueObj instanceof Collection){
			Collection collection = (Collection)valueObj;
			for(Object obj:collection){
				addParam(nvps, key, obj);
			}
		}else if(!"".equals(valueObj.toString())){
			nvps.add(new BasicNameValuePair(key, valueObj.toString()));
		}
	}
	
	
	private static void addParam(MultipartEntityBuilder bulider, String key,
			Object valueObj) {
		if(valueObj==null){
			return;
		}
		if(valueObj.getClass().isArray()){
			Object[] valueObjArrays= (Object[])valueObj;
			for(Object obj:valueObjArrays){
				addParam(bulider, key, obj);
			}
		}else if(valueObj instanceof Collection){
			Collection collection = (Collection)valueObj;
			for(Object obj:collection){
				addParam(bulider, key, obj);
			}
		}else if(valueObj instanceof File){
			bulider.addBinaryBody(key, (File)valueObj);
		}else if(!"".equals(valueObj.toString())){
			bulider.addTextBody(key,  valueObj.toString());
		}
	}
	
	public static void response(HttpServletResponse response,String json) throws Exception{
		Writer w=null;
		try{
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json; charset=utf-8");
			w = response.getWriter();
			w.write(json);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(w!=null){
				w.close();
			}
		}
	}
	
	public static String uploadFile(String url,Map<String,String> headers, Map<String, Object> params) throws Exception{
		String result = "{}";
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);

		Set<String> headerKeys = headers.keySet();
		for(String key : headerKeys){
			httpPost.addHeader(key, headers.get(key));
		}

		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

		Set<String> keySet = params.keySet();
		for (String key : keySet) {
			Object val = params.get(key);
			if(val instanceof File){
				builder.addPart(key,new FileBody((File)val));
			}else{
				builder.addTextBody(key, String.valueOf(val),ContentType.DEFAULT_BINARY);
			}
		}

		httpPost.setEntity(builder.build());
		HttpResponse httpResponse = httpClient.execute(httpPost);
		if(httpResponse.getStatusLine().getStatusCode() == 200) {
			HttpEntity entity = httpResponse.getEntity();
			result = EntityUtils.toString(entity);
		}
		return result;
	}

	/**
	 * 根据url下载文件，保存到filepath中
	 * 
	 * @param url
	 * @param filepath
	 * @return
	 */
	public static File download(String url, File folder) {
		File file = null;
		CloseableHttpClient httpClient=null;
		CloseableHttpResponse httpResponse=null;
		try {
			httpClient =HttpClients.createDefault();
			HttpClientContext context = HttpClientContext.create();
			HttpGet httpget = new HttpGet(url);
			HttpResponse response = httpClient.execute(httpget,context);

			HttpEntity entity = response.getEntity();
			String fileName = getFileName(response);
			if(null == folder){
				file = new File(fileName);
			}else{
				file = new File(folder,fileName);
			}
			FileOutputStream fileout = new FileOutputStream(file);
			fileout.write(EntityUtils.toByteArray(entity));
			fileout.flush();
			fileout.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return file;
	}

	private static String getFileName(HttpResponse response) {  
        Header contentHeader = response.getFirstHeader("Content-Disposition");  
        String filename = null;  
        if (contentHeader != null) {  
            HeaderElement[] values = contentHeader.getElements();  
            if (values.length == 1) {  
                NameValuePair param = values[0].getParameterByName("filename");  
                if (param != null) {  
                    try {  
                        filename = param.getValue();  
                    } catch (Exception e) {  
                        e.printStackTrace();  
                    }  
                }  
            }  
        }  
        return filename;  
	}
	
}
