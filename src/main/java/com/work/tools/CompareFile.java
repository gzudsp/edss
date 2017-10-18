package com.work.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CompareFile {
	public static void main(String[] args) throws Exception {
		new CompareFile().compare();
	}

	public void compare() throws Exception {
		BufferedReader src = new BufferedReader(new FileReader(new File("A:/a.txt")));
		BufferedReader desc = new BufferedReader(new FileReader(new File("A:/b.txt")));
		List<String> list1 = new ArrayList<String>();
		List<String> list2 = new ArrayList<String>();
		List<String> list3 = new ArrayList<String>();
		String srcStr;
		String descStr;
		while ((srcStr = src.readLine()) != null) {
			list1.add(srcStr.trim());
		}
		while ((descStr = desc.readLine()) != null) {
			list2.add(descStr.trim());
		}
		int srcSize = list1.size();
		int descSize = list2.size();

		if (srcSize > descSize) { // 目标文件物理删除
			for (int i = 0; i < list1.size(); i++) {
				for (int j = 0; j < list2.size(); j++) {
					String value = list2.get(j);
					if (!list1.contains(value)) {
						list3.add(value);
					}
				}
			}
		} else if (srcSize < descSize) {// 目标文件增加,包含更新
			Iterator<String> iterator = list2.iterator();
			while (iterator.hasNext()) {
				String next = iterator.next();
				if(!list1.contains(next)){
					list3.add(next);
				}
			}
		}else{ //前后数目不变
			for (int i = 0; i < list2.size(); i++) {
				if(!list1.contains(list2.get(i))){
					list3.add(list2.get(i));
				}
			}
		}
		for (String string : list3) {
			System.out.println(string);
		}
	}
}
