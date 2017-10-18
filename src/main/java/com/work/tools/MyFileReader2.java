package com.work.tools;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.lang.StringUtils;

public class MyFileReader2 {
	int idx;
	Connection conn = null;
	PreparedStatement pstmt = null;

	public static void main(String[] args) throws Exception {
		/**
		 * 入参:userId,date
		 */
		String src = "‪C:/Users/lenovo/Desktop/committee_info.zip";
		String des = "D:/file3/";
		/*MyFileReader2 rcf = new MyFileReader2();
		//解压文件
		String unZipFile = unZipFiles(new File(src), des);
		//读取解压文件获取表明,列名 D:/user/committee_info.txt
		Map map = rcf.readFile("C:/Users/lenovo/Desktop/user.txt");
		String tabName = (String) map.get("tabName");
		String[] clumes = (String[]) map.get("clumes");
		List<String[]> list = (List<String[]>) map.get("list");
		//删除,创建临时表
		String newTab = rcf.checkDBTable(tabName,clumes,"userId");
		System.out.println(newTab);
		//向新表插入新数据
		rcf.insertCustInfo(list,clumes,newTab);*/
		MyFileReader2 rcf = new MyFileReader2();
		UnZipFile.unZipFiles(new File(src),des);
	}
	
	/**
	 * 读文件并插入数据
	 * @param path
	 */
	public static Map readFile(File file) {
		String read = TextFile.read(file);
		String[] lines = read.split("\n");
		String tabName = lines[0];
		String[] clumes = lines[1].split("\\|");
		Map resultMap = new HashMap();
		List<String[]> list = new ArrayList();
		for (int i = 0; i < lines.length; i++) {
			if (i > 1) {
				String[] values = lines[i].split("\\|");
				// System.out.println(line);
				list.add(values);
				/*String newTab = checkDBTable(tabName,clumes,userId);
				insertCustInfo(values, clumes, newTab);*/
			}
		}
		resultMap.put("tabName", tabName);
		resultMap.put("clumes", clumes);
		resultMap.put("list", list);
		return resultMap;
	}
	  

	public void dropTab(String oldTab,String newTab){
		conn = dbConnection();
		String sql1 = "DROP TABLE IF EXISTS "+oldTab+";";
		String sql2 = "DROP TABLE IF EXISTS "+newTab+";";
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			stmt.execute(sql1);
			stmt.execute(sql2);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			dbDisConnection();
		}
	}

	// 插入到数据库中
	public void insertCustInfo(List<String[]> list, String[] clumes, String newtab) {
		conn = dbConnection();
		if(clumes.length>0&&newtab.length()>0){
			try {
				StringBuffer sqlBf = new StringBuffer();
				sqlBf.setLength(0);
				String temp0 = "INSERT INTO " + newtab+"(";
				String temp1 = "";
				String temp2 = "";
				for (int j = 0; j < clumes.length; j++) {
					if(j==clumes.length-1){
						temp1 += clumes[j]+") VALUES(";
						temp2 += " ?)";
					}else{
						temp1 += clumes[j]+",";
						temp2 +="? ,";
					}
				}
				sqlBf.append(temp0 + temp1 +temp2);
				//System.out.println(sqlBf);
				pstmt = conn.prepareStatement(sqlBf.toString());
				idx = 1;
				pstmt.clearParameters();
				for (String[] values : list) {
					for (int k = 0; k < values.length; k++) {
						pstmt.setObject(idx++, values[k]);
					}
					idx = 1;
					pstmt.addBatch();
					pstmt.executeBatch();
				}
				//pstmt.executeUpdate();
				
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if (pstmt != null) {
					try {
						pstmt.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				dbDisConnection();
			}
		}
	}
	/*
	 * srcDir:C:\\Users\\lenovo\\Desktop\\aa.zip
	 * descDir:目标文件,如目标文件为空则解压到当前文件件
	 * zipFile:源文件
	 */
    public static String unZipFiles(File zipFile,String descDir)throws IOException{ 
        File pathFile = new File(descDir);  
        if(!pathFile.exists()){  
            pathFile.mkdirs();  
        }
        String outPath = "";
        ZipFile zip = new ZipFile(zipFile);  
        for(Enumeration entries = zip.entries();entries.hasMoreElements();){  
            ZipEntry entry = (ZipEntry)entries.nextElement();  
            String zipEntryName = entry.getName();  
            InputStream in = zip.getInputStream(entry);
            //String a = descDir+zipEntryName;
            outPath = (descDir+zipEntryName).replaceAll("\\*", "/");;  
            //判断路径是否存在,不存在则创建文件路径  
            File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));  
            if(!file.exists()){  
                file.mkdirs();  
            }  
            //判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压  
            if(new File(outPath).isDirectory()){  
                continue;  
            }  
            //输出文件路径信息  
            System.out.println(outPath);  
            
            OutputStream out = new FileOutputStream(outPath);  
            byte[] buf1 = new byte[1024];  
            int len;  
            while((len=in.read(buf1))>0){  
                out.write(buf1,0,len);  
            }  
            in.close();  
            out.close();  
            }  
        	System.out.println("解压完成");
        	return outPath;
    }
    
    /*
     * 删除临时表,创建临时表,插入数据
     */
    public String checkDBTable(String tabName,String[] clumes,String userId){
    	//删除临时表,创建临时表
    	conn = dbConnection();
    	String deleTempTable = "";
    	String newTab = userId+"_"+System.currentTimeMillis();
    	if(tabName!=null&&clumes!=null){
    		//String dropTab = "DROP TABLE IF EXISTS "+tabName+";DROP TABLE IF EXISTS "+newTab;
    		String temp0 = "CREATE TABLE "+newTab+"(";
    		String temp1 = "";
    		String temp2 = "PRIMARY KEY (`id`)) ";
    		for (int i = 0; i < clumes.length; i++) {
    			if(i==clumes.length-1){
    				temp1 += clumes[i] + " varchar(255) DEFAULT NULL)";
    			}else{
    				temp1 += clumes[i] + " varchar(255) DEFAULT NULL ,";
    			}
    		}
    		deleTempTable = temp0 + temp1 ;//+ temp2
    		PreparedStatement ps = null;
    		try {
    			ps = conn.prepareStatement(deleTempTable);
    			System.out.println(deleTempTable);
    			ps.execute();
			} catch (SQLException e) {
				e.printStackTrace();
			}finally{
				if (ps != null) {
					try {
						ps.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				dbDisConnection();
			}
    	}
    	return newTab;
    }
    
    public static void unzip(String zipFilePath, String unzipFilePath, boolean includeZipFileName) throws Exception    
    {    
        if (StringUtils.isEmpty(zipFilePath) || StringUtils.isEmpty(unzipFilePath))    
        {    
            //throw new ParameterException(ICommonResultCode.PARAMETER_IS_NULL);              
        	System.out.println("未上传文件");
        }    
        File zipFile = new File(zipFilePath);    
        //如果解压后的文件保存路径包含压缩文件的文件名，则追加该文件名到解压路径    
        if (includeZipFileName)    
        {    
            String fileName = zipFile.getName();    
            if (StringUtils.isNotEmpty(fileName))    
            {    
                fileName = fileName.substring(0, fileName.lastIndexOf("."));    
            }    
            unzipFilePath = unzipFilePath + File.separator + fileName;    
        }    
        //创建解压缩文件保存的路径    
        File unzipFileDir = new File(unzipFilePath);    
        if (!unzipFileDir.exists() || !unzipFileDir.isDirectory())    
        {    
            unzipFileDir.mkdirs();    
        }    
            
        //开始解压    
        ZipEntry entry = null;    
        String entryFilePath = null, entryDirPath = null;    
        File entryFile = null, entryDir = null;    
        int index = 0, count = 0, bufferSize = 1024;    
        byte[] buffer = new byte[bufferSize];    
        BufferedInputStream bis = null;    
        BufferedOutputStream bos = null;    
        ZipFile zip = new ZipFile(zipFile);    
        Enumeration<ZipEntry> entries = (Enumeration<ZipEntry>)zip.entries();    
        //循环对压缩包里的每一个文件进行解压         
        while(entries.hasMoreElements())    
        {    
            entry = entries.nextElement();    
            //构建压缩包中一个文件解压后保存的文件全路径    
            entryFilePath = unzipFilePath + File.separator + entry.getName();    
            //构建解压后保存的文件夹路径    
            index = entryFilePath.lastIndexOf(File.separator);    
            if (index != -1)    
            {    
                entryDirPath = entryFilePath.substring(0, index);    
            }    
            else    
            {    
                entryDirPath = "";    
            }               
            entryDir = new File(entryDirPath);    
            //如果文件夹路径不存在，则创建文件夹    
            if (!entryDir.exists() || !entryDir.isDirectory())    
            {    
                entryDir.mkdirs();    
            }    
                
            //创建解压文件    
            entryFile = new File(entryFilePath);    
            if (entryFile.exists())    
            {    
                //检测文件是否允许删除，如果不允许删除，将会抛出SecurityException    
                SecurityManager securityManager = new SecurityManager();    
                securityManager.checkDelete(entryFilePath);    
                //删除已存在的目标文件    
                entryFile.delete();     
            }    
                
            //写入文件    
            bos = new BufferedOutputStream(new FileOutputStream(entryFile));    
            bis = new BufferedInputStream(zip.getInputStream(entry));    
            while ((count = bis.read(buffer, 0, bufferSize)) != -1)    
            {    
                bos.write(buffer, 0, count);    
            }    
            bos.flush();    
            bos.close();                
        }    
    } 
	
	// 连接数据库
	public Connection dbConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/yaodou?characterEncoding=utf8";
			String user = "root";
			String password = "root";

			conn = DriverManager.getConnection(url, user, password);
			System.out.println("Connection 开启！");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}

	// 关闭数据库
	public void dbDisConnection() {
		if (conn != null) {
			try {
				conn.close();
				System.out.println("Connection 关闭！");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}