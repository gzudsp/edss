package com.work.tools;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

public class ReadCustomerFile {
    
    int idx;
    Connection conn = null;
    PreparedStatement pstmt = null;    
    
    // 使用commons-io.jar包的FileUtils的类进行读取
    public void readTxtFileByFileUtils(String fileName) {
        File file = new File(fileName);
        
        dbConnection();
        
        try {
            LineIterator lineIterator = FileUtils.lineIterator(file, "GB2312");
            int flag = 0;
            while (lineIterator.hasNext()) {
            	flag++;
            	String line = lineIterator.nextLine();
            	if(flag>=3){
            		
            		// 行数据转换成数组
            		String[] custArray = line.split("\\|");
            		insertCustInfo(custArray);
            		//Thread.sleep(10);
            	}
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            dbDisConnection();
        }
    }
    
    // 插入到数据库中
    public void insertCustInfo(String[] strArray) {         
        try {
            StringBuffer sqlBf = new StringBuffer();
            sqlBf.setLength(0);
            //INFO_ID|PARTY_ID|INFO_TYPE_ID|FILE_NAME|FILE_SIZE|FILE_TYPE|STATUS_ID
            sqlBf.append("INSERT INTO committee_info(INFO_ID, PARTY_ID, INFO_TYPE_ID,FILE_NAME,"
            		+ "FILE_SIZE,FILE_TYPE,STATUS_ID)\n");
            sqlBf.append("VALUES(?");
            sqlBf.append(", ? \n");
            sqlBf.append(" , ? \n");
            sqlBf.append(" , ? \n");
            sqlBf.append(" , ? \n");
            sqlBf.append(" , ? \n");
            sqlBf.append(" , ?) \n");
            
            pstmt = conn.prepareStatement(sqlBf.toString());
            idx = 1;
            pstmt.clearParameters();
            pstmt.setString(idx++, strArray[0]);
            pstmt.setString(idx++, strArray[1]);
            pstmt.setString(idx++, strArray[2]);
            pstmt.setString(idx++, strArray[3]);
            pstmt.setString(idx++, strArray[4]);
            pstmt.setString(idx++, strArray[5]);
            pstmt.setString(idx++, strArray[6]);
            
            pstmt.executeUpdate();            
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
        }
    }
    
    // 连接数据库
    public Connection dbConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            
            String url = "jdbc:mysql://localhost:3306/yaodou?useUnicode=true&characterEncoding=utf8";  
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
    
    public static void main(String[] args) {
        ReadCustomerFile rcf = new ReadCustomerFile();
        rcf.readTxtFileByFileUtils("C:\\Users\\lenovo\\Desktop\\committee_info.txt");
    }
}