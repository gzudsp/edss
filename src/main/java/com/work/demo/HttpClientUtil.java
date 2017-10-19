package com.work.demo;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class HttpClientUtil {
	public static void main(String[] args)  {
		Map<String, String> map = new HashMap<>();
		map.put("name", "李四");
		map.put("description", "12313");
		map.put("tokenId", "123");
		String localFile = "C:/Users/lenovo/Desktop/committee_info.zip";
		HttpClientUtil.httpPost("http://127.0.0.1:8080/ssm/testUpload", localFile, map);
	}

	public static CloseableHttpClient createDefault() {
		return HttpClientBuilder.create().build();
	}
	/**
	 * 
	 * @param url 主机地址
	 * @param filepath 文件地址
	 * @param map form参数
	 */
	public static void httpPost(String url,String filepath,Map<String, String> map) {
		
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		//CloseableHttpClient httpClient1 = HttpClients.createDefault();
		//DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
		//添加文件
		File file = new File(filepath);
		if(file.exists()){
			entityBuilder.addPart("uploadFile",new FileBody(file));
		}
		//添加字段
		if(map!=null){
			for (String key : map.keySet()) {
				StringBody body = new StringBody(map.get(key),ContentType.create("http/text", Consts.UTF_8));
				entityBuilder.addPart(key,body);
			}
		}
		try {
			HttpEntity build = entityBuilder.build();
			post.setEntity(build);
			CloseableHttpResponse response = httpClient.execute(post);
			StatusLine statusLine = response.getStatusLine();
			HttpEntity entity = response.getEntity();
			System.out.println(statusLine);
			System.out.println("-----");
			System.out.println(EntityUtils.toString(entity));
			System.out.println(entity.getContent());
			
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
