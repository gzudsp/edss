package com.work.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class TextFile extends ArrayList<String>{  
	private static final long serialVersionUID = -8611548278759606004L;

	//read a file as a single string  
    public static String read(File file){  
        StringBuilder sb = new StringBuilder();  
        try{  
            BufferedReader in = new BufferedReader( new FileReader(file.getAbsoluteFile()));  
            try{  
                String s;  
                while((s=in.readLine()) != null){  
                    sb.append(s);  
                    sb.append("\n");  
                }  
            }finally{  
                in.close();  
            }  
        }catch(IOException e){  
            throw new RuntimeException(e);  
        }  
        return sb.toString();  
    }  
    public static void main(String[] args) {
		MyFileReader2 myFileReader2 = new MyFileReader2();
		/*try {
			String unZipFiles = myFileReader2.unZipFiles(new File("C:\\Users\\lenovo\\Desktop\\committee_info.zip"), "D:/file1/");
			System.out.println(unZipFiles);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		String a = "C:\\Users\\lenovo\\Desktop\\committee_info.zip";
		String descDir = "D:/qq/";
		try {  
            ZipFile zf = new ZipFile(new File(a));  
            for (Enumeration entries = zf.entries(); entries.hasMoreElements();) {  
                ZipEntry entry = (ZipEntry) entries.nextElement();  
                String zipEntryName = entry.getName();  
                InputStream in = zf.getInputStream(entry); 
                File file = new File(descDir + zipEntryName);
                if(!file.exists()){
                	file.getParentFile().mkdir();
                	file.createNewFile();
                }
                OutputStream out = new FileOutputStream(file);  
                byte[] buf1 = new byte[1024];  
                int len;  
                while ((len = in.read(buf1)) > 0) {  
                    out.write(buf1, 0, len);  
                }  
                in.close();  
                out.close();  
                System.out.println("解压缩完成.");  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
		
	}
}
