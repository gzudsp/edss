package com.work.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class UnZipFile {
	public static void main(String[] args) throws IOException {
		//String zipFile = "A:/a.zip"; //C:/Users/lenovo/Desktop/committee_info.zip
		String zipFile = "C:/Users/lenovo/Desktop/committee_info.zip";
		String descDir = "A:/测试/";
		List<String> files = UnZipFile.unZipFiles(new File(zipFile), descDir);
		System.out.println(files);
	}
	
	public static List<String> unZipFiles(File zipFile, String descDir) throws IOException{
		ZipFile zip = new ZipFile(zipFile, Charset.forName("GBK"));// 解决中文文件夹乱码
		String name = zip.getName().substring(zip.getName().lastIndexOf('\\') + 1, zip.getName().lastIndexOf('.'));
		List<String> list = new ArrayList<>();
		File pathFile = new File(descDir + name);
		if (!pathFile.exists()) {
			pathFile.mkdirs();
		}
		
		for (Enumeration<? extends ZipEntry> entries = zip.entries(); entries.hasMoreElements();) {
			ZipEntry entry = (ZipEntry) entries.nextElement();
			String zipEntryName = entry.getName();
			InputStream in = zip.getInputStream(entry);
			String outPath  = (descDir + name + "/" + zipEntryName).replaceAll("\\*", "/");
			//System.out.println("zipEntryName="+descDir+zipEntryName);
			// 判断路径是否存在,不存在则创建文件路径
			File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
			if (!file.exists()) {
				file.mkdirs();
			}
			// 判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
			if (new File(outPath).isDirectory()) {
				continue;
			}
			// 输出文件路径信息
			// System.out.println(outPath);

			FileOutputStream out = new FileOutputStream(outPath);
			System.out.println(outPath);
			list.add(outPath);
			byte[] buf1 = new byte[1024];
			int len;
			while ((len = in.read(buf1)) > 0) {
				out.write(buf1, 0, len);
			}
			in.close();
			out.close();
		}
		System.out.println("******************解压完毕********************");
		
		return list;
	}
}
