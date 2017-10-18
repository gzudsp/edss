package com.work.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class MyFileReader1 {
	public static void main(String[] args)throws Exception{
		//文件绝对路径改成你自己的文件路径
		BufferedReader br = new BufferedReader( new FileReader(  
                new File("C:\\Users\\Lenovo\\Desktop\\committee_info.txt").getAbsoluteFile())); 
		//可以换成工程目录下的其他文本文件
		//BufferedReader br=new BufferedReader(fr);
		///StringBuffer allText = new StringBuffer();
		int countline = 0;
		//表名
		String tableName = "";
		//列名
		List<String> cloums = new ArrayList<String>();
		int c = 0;
		while(br.readLine()!=null){
			countline +=1;
			c +=1;
			String line=br.readLine();
			//if(line!=null && line.length()>0){
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
					if(line!=null && !"".equals(line)){
						String[] values = line.split("|");
						//下面创建entity，设置值，保存
						/*CommitteeInfo committeeInfo = new CommitteeInfo(
								values[0], values[1], values[2], values[3], 
								values[4], values[5], values[6], values[7]);*/
						String sql = "insert into committee_info values(?,?,?,?,?,?,?,?)";
						
						
					}
				}
				System.out.println(line);
			}
		//}
		System.out.println(c);
		br.close();
	}
}