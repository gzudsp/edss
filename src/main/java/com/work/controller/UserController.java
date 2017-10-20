package com.work.controller;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.work.beans.Message;
import com.work.beans.User;
import com.work.service.UserService;
import com.work.tools.MyFileReader2;
import com.work.tools.UnZipFile;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

	@RequestMapping("findUserByTokenId")
	@ResponseBody
	public User findUserByTokenId(User user) {
		User users = userService.findUserByToKenId(user.getTokenId());
		if (users != null) {

		} else {
			System.out.println("不存在该用户!");
		}
		System.out.println("findUserByTokenId");
		return users;
	}

	@RequestMapping(value="testUpload")
	@ResponseBody
	public Message testUpload(User user, MultipartFile uploadFile,Model model,HttpServletRequest request) throws Exception {
		User users = userService.findUserByToKenId(user.getTokenId());
		//InputStream inputStream = uploadFile.getInputStream();
		Message ms = new Message();
		/*String description = user.getDescription();
		String name = user.getName();
		String tokenId = user.getTokenId();*/
		String name = request.getParameter("name");
		String description = request.getParameter("description");
		String tokenId = request.getParameter("tokenId");
		
		Map<String,String> map = new HashMap<String,String>();
		if(uploadFile.isEmpty()){
			System.out.println("文件未上传");
			map.put("status", "文件未上传");
		}else{
			/*System.out.println("文件长度: " + uploadFile.getSize());  
            System.out.println("文件类型: " + uploadFile.getContentType());  
            System.out.println("文件名称: " + uploadFile.getName());  
            System.out.println("文件原名: " + uploadFile.getOriginalFilename()); */
			String filename = uploadFile.getOriginalFilename();
			System.out.println("desc="+description+",name="+name+",tokenId="+tokenId);
			//${sss}/user/服务器系统日期/zip
			String path = "D:\\"+tokenId+"\\"+filename;
			System.out.println(path);
			File file = new File(path);
			System.out.println(file.getAbsolutePath());
			if (users != null) {
				if(!file.exists()){
					file.mkdirs();
				}
				try {
					uploadFile.transferTo(file);
					//insertData(file,name);
					ms.setInfo("success");
					ms.setStatus("true");
				} catch (IllegalStateException | IOException  e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				System.out.println("不存在该用户!");
				ms.setInfo("不存在该用户!");
				ms.setStatus("false");
			}
		}
		return ms;
	}
	
	@RequestMapping(value="writeDataToDB",method=RequestMethod.GET)
	@ResponseBody
	public Message writeDataToDB(String path) {
		Message ms = new Message();
		MyFileReader2 rcf = new MyFileReader2();
		path = "C:/Users/lenovo/Desktop/committee_info.zip";
		String descDir = "A:/测试/";
		try {
			//解压文件
			List<String> files = UnZipFile.unZipFiles(new File(path), descDir);
			//获取解压的第一个文件
			String file = files.get(0);
			System.out.println(files);
			//读取txt文件,返回文件信息
			Map map = rcf.readFile(new File(file));
			//表名
			String tabName = (String) map.get("tabName");
			//列集合
			String[] clumes = (String[]) map.get("clumes");
			//数据值
			List<String[]> list = (List<String[]>) map.get("list");
			//删除,创建临时表
			//String newTab = rcf.checkDBTable(tabName,clumes,"userId");
			//System.out.println(newTab);
			//向新表插入新数据
			//rcf.insertCustInfo(list,clumes,newTab);
			ms.setInfo("success");
			ms.setStatus("true");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ms;
	}
	
	public Message insertData(File zipFile,String userName) throws SQLException{
		MyFileReader2 rcf = new MyFileReader2();
		Message ms = new Message();
		//path = "C:/Users/lenovo/Desktop/committee_info.zip";
		String descDir = "A:/测试/";
		try {
			//解压文件
			List<String> files = UnZipFile.unZipFiles(zipFile, descDir);
			//获取解压的第一个文件
			String file = files.get(0);
			System.out.println(files);
			//读取txt文件,返回文件信息
			Map map = rcf.readFile(new File(file));
			//表名
			String tabName = (String) map.get("tabName");
			//列集合
			List<String> clumes = (List<String>) map.get("clumes");
			//数据值
			List<String[]> list = (List<String[]>) map.get("list");
			//列长度
			List<Integer> length = (List<Integer>) map.get("lengths");
			//删除,创建临时表
			rcf.checkDBTable(tabName,clumes,userName,length);
			//插入新数据
			rcf.insertCustInfo(list,clumes,tabName);
			ms.setInfo("success");
			ms.setStatus("true");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ms;
	}
	
	@RequestMapping("getFormFile")
	@ResponseBody
	public Message getFormFile(@RequestParam("files") MultipartFile[] files){
		Message ms = new Message();
		if(files!=null){
			for (MultipartFile multipartFile : files) {
				String filename = multipartFile.getOriginalFilename();
				System.out.println(filename);
			}
			ms.setInfo("file not empty");
		}
		
		return ms;
	}
    
    public static void main(String[] args) {
    	String srcFile = "C:/Users/lenovo/Desktop/committee_info.zip";
    	String descFile = "D:/user/";
    	/*try {
			new MyFileReader2().unZipFiles(new File(srcFile),descFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
    	try {
			MyFileReader2 rcf = new MyFileReader2();
			//解压文件
			String unZipFile = rcf.unZipFiles(new File(srcFile), descFile);
			//读取解压文件获取表明,列名
			Map map = rcf.readFile(new File(unZipFile));
			String tabName = (String) map.get("tabName");
			System.out.println(tabName);
			String[] clumes = (String[]) map.get("clumes");
			List<String[]> list = (List<String[]>) map.get("list");
			//删除,创建临时表
//			String newTab = rcf.checkDBTable(tabName,clumes,"userId");
//			System.out.println(newTab);
			//向新表插入新数据
			//rcf.insertCustInfo(list,clumes,newTab);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
