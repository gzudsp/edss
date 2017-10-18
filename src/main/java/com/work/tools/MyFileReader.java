package com.work.tools;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class MyFileReader {
	public static void main(String[] args)throws Exception{
		//文件绝对路径改成你自己的文件路径
		FileReader fr=new FileReader("C:\\Users\\lenovo\\Desktop\\committee_info.txt");
		//可以换成工程目录下的其他文本文件
		BufferedReader br=new BufferedReader(fr);
		///StringBuffer allText = new StringBuffer();
		int countline = 0;
		//表名
		String tableName = "";
		//列名
		List<String> cloums = new ArrayList<String>();
		while(br.readLine()!=null){
			countline +=1;
			String line=br.readLine().trim();
			if(countline ==1){
				tableName = line;
			}else if(countline ==2 ){
				String[] cloumstrs = line.split("|");
				for (int i = 0; i < cloumstrs.length; i++) {
					cloums.add(cloumstrs[i]);
				}
				//列名全部取出
			}
			if(countline > 2){
				String[] values = line.split("|");
				//下面创建entity，设置值，保存
				
			}
			System.out.println(line);
		}
		br.close();
	}
}