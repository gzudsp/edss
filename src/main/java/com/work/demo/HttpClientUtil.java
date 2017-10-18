package com.work.demo;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.work.beans.User;

import net.sf.json.JSONObject;

public class HttpClientUtil {
	public static void main(String[] args) throws Exception {
		Map map = new HashMap<>();
		map.put("name", "李四");
		map.put("description", "12313");
		map.put("tokenId", "123");
		map.put("url", "http://127.0.0.1:8080/ssm/testUpload");
		String localFile = "C:/Users/lenovo/Desktop/committee_info.zip";
		// HttpClientUtil.PostMethod(localFile, map);
		HttpClientUtil.HttpMethod(localFile, map);
		//HttpClientUtil.httpPost("http://127.0.0.1:8080/ssm/testUpload", localFile, map);
		
	}

	/**
	 * 模拟post请求
	 * 
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public static void PostMethod(String localFile, Map<String, String> map)
			throws ClientProtocolException, IOException {
		File file = new File(localFile);
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet("http://127.0.0.1:8080/ssm/");
		CloseableHttpResponse response1 = httpclient.execute(httpGet);
		// The underlying HTTP connection is still held by the response object
		// to allow the response content to be streamed directly from the
		// network socket.
		// In order to ensure correct deallocation of system resources
		// the user MUST call CloseableHttpResponse#close() from a finally
		// clause.
		// Please note that if response content is not fully consumed the
		// underlying
		// connection cannot be safely re-used and will be shut down and
		// discarded
		// by the connection manager.
		try {
			System.out.println(response1.getStatusLine());
			HttpEntity entity1 = response1.getEntity();
			// do something useful with the response body
			// and ensure it is fully consumed
			EntityUtils.consume(entity1);
		} finally {
			response1.close();
		}

		HttpPost httpPost = new HttpPost("http://127.0.0.1:8080/ssm/");
		// List <NameValuePair> nvps = new ArrayList <NameValuePair>();
		List<User> nvps = new ArrayList<User>();
		// nvps.add(new User(1, "vip"));
		// nvps.add(new User("password", "secret"));
		httpPost.setEntity(new UrlEncodedFormEntity(nvps));
		CloseableHttpResponse response2 = httpclient.execute(httpPost);

		try {
			System.out.println(response2.getStatusLine());
			HttpEntity entity2 = response2.getEntity();
			// do something useful with the response body
			// and ensure it is fully consumed
			EntityUtils.consume(entity2);
		} finally {
			response2.close();
		}
	}

	public static void HttpMethod(String url, Map<String, String> map) {
		HttpClient httpclient = new DefaultHttpClient();
		HttpClient httpclient1 = new DefaultHttpClient();
		try {
			HttpPost httppost = new HttpPost(map.get("url"));
			httppost.setProtocolVersion(new ProtocolVersion("HTTP", 2, 1));
			FileBody bin = new FileBody(new File(url));
			StringBody comment = new StringBody(map.get("name"), Consts.UTF_8);
			StringBody desc = new StringBody(map.get("description"));
			StringBody tokenId = new StringBody(map.get("tokenId"));
			MultipartEntity reqEntity = new MultipartEntity();
			reqEntity.addPart("uploadFile", bin);// file1为请求后台的File upload;属性
			reqEntity.addPart("name", comment);// filename1为请求后台的普通参数;属性
			reqEntity.addPart("description", desc);// filename1为请求后台的普通参数;属性
			reqEntity.addPart("tokenId", tokenId);// filename1为请求后台的普通参数;属性
			httppost.setEntity(reqEntity);

			HttpResponse response = httpclient.execute(httppost);

			int statusCode = response.getStatusLine().getStatusCode();

			if (statusCode == HttpStatus.SC_OK) {

				System.out.println("服务器正常响应.....");

				HttpEntity resEntity = response.getEntity();

				System.out.println(EntityUtils.toString(resEntity));// httpclient自带的工具类读取返回数据

				System.out.println(resEntity.getContent());

				EntityUtils.consume(resEntity);
			}

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				httpclient.getConnectionManager().shutdown();
			} catch (Exception ignore) {

			}
		}
		
	}

	public static final String learnSelUrl = "http://192.168.0.124:8080/site/trdcredit/jxlxxwactivereq";

	/**
	 * post请求
	 * 
	 * @param url
	 * @param param
	 * @return
	 * @throws IOException
	 */
	public static JSONObject postJsonData(String url, Map<String, String> params,String filepath) {
		CloseableHttpClient httpclient = HttpClientUtil.createDefault();
		
		HttpPost httpPost = new HttpPost(url);
		
		// 拼接参数
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			String key = entry.getKey().toString();
			String value = entry.getValue().toString();
			System.out.println("key=" + key + " value=" + value);
			NameValuePair pair = new BasicNameValuePair(key, value);
			list.add(pair);
		}
		FileBody fileBody = new FileBody(new File(filepath));
		CloseableHttpResponse response = null;
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(list));
			response = httpclient.execute(httpPost);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		/** 请求发送成功，并得到响应 **/
		JSONObject jsonObject = null;
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			HttpEntity httpEntity = response.getEntity();
			String result = null;
			try {
				result = EntityUtils.toString(httpEntity);
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} // 返回json格式：
			jsonObject = JSONObject.fromObject(result);
		}
		return jsonObject;
	}

	/**
	 * Creates {@link CloseableHttpClient} instance with default configuration.
	 */
	public static CloseableHttpClient createDefault() {
		return HttpClientBuilder.create().build();
	}

	/**
	 * http发送post请求
	 * 
	 * @param url
	 * @param maps
	 * @return
	 */
	public static JSONObject sendPost(String url, Map<String, String> params) {
		DefaultHttpClient client1 = new DefaultHttpClient();
		CloseableHttpClient client = HttpClientBuilder.create().build();
		/** NameValuePair是传送给服务器的请求参数 param.get("name") **/
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			String key = entry.getKey().toString();
			String value = entry.getValue().toString();
			System.out.println("key=" + key + " value=" + value);
			NameValuePair pair = new BasicNameValuePair(key, value);
			list.add(pair);
		}
		
		UrlEncodedFormEntity entity = null;
		try {
			/** 设置编码 **/
			entity = new UrlEncodedFormEntity(list, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		/** 新建一个post请求 **/
		HttpPost post = new HttpPost(url);
		post.setEntity(entity);
		
		HttpResponse response = null;
		try {
			/** 客服端向服务器发送请求 **/
			response = client.execute(post);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		/** 请求发送成功，并得到响应 **/
		JSONObject jsonObject = null;
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			HttpEntity httpEntity = response.getEntity();
			String result = null;
			try {
				result = EntityUtils.toString(httpEntity);
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} // 返回json格式：
			jsonObject = JSONObject.fromObject(result);
		}
		return jsonObject;
	}
	
	public static void httpPost(String url,String filepath,Map<String, String> map) {
		
		//CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		//CloseableHttpClient httpClient = HttpClients.createDefault();
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
		//添加文件
		entityBuilder.addPart("uploadFile",new FileBody(new File(filepath)));
		//添加字段
		for (String key : map.keySet()) {
			entityBuilder.addPart(key,new StringBody(map.get(key),ContentType.create("http/text", Consts.UTF_8)));
		}
		try {
			CloseableHttpResponse response = httpClient.execute(post);
			StatusLine statusLine = response.getStatusLine();
			HttpEntity entity = response.getEntity();
			System.out.println(statusLine);
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			httpClient.close();
		}
		
		
	}
}
