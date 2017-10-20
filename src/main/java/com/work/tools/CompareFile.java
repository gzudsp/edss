package com.work.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CompareFile {
	public static void main(String[] args) throws Exception {
		new CompareFile().method();
	}

	public void method() throws IOException {
		BufferedReader src = new BufferedReader(new FileReader(new File("A:/a.txt")));
		BufferedReader desc = new BufferedReader(new FileReader(new File("A:/b.txt")));
		List<String> list1 = new ArrayList<String>();
		List<String> list2 = new ArrayList<String>();
		List<String> list3 = new ArrayList<String>();
		String srcStr;
		String descStr;
		while ((srcStr = src.readLine()) != null) {
			list1.add(srcStr.trim());
			list3.add(srcStr.trim());
		}
		while ((descStr = desc.readLine()) != null) {
			list2.add(descStr.trim());
		}
		// 交集
		list3.retainAll(list2);
		//差集
		list1.removeAll(list3);
		list2.removeAll(list3);
		
		list1.addAll(list2);
		for (String string : list1) {
			System.out.println(string);
		}
	}
}
