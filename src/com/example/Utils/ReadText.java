package com.example.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import android.os.Environment;

public class ReadText {
	public static String SaveText(String name, String fileName) {
		File tmpDir=new File(Environment.getExternalStorageDirectory()+"/"+fileName);
		if(!tmpDir.exists()){
			tmpDir.mkdir();
		}
		String nametxt=name+".txt";
	    String  anjian_TEXT = tmpDir.getAbsolutePath()+"/"+nametxt;
		File img=new File(anjian_TEXT);
		FileInputStream in=null;
		BufferedReader reader=null;
		StringBuilder content=new StringBuilder();
		try {
			in=new FileInputStream(img);
			reader=new BufferedReader(new InputStreamReader(in));
			String lineString="";
			while((lineString=reader.readLine())!=null){
				content.append(lineString);
			}
			return content.toString();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}finally{
			if(reader!=null)
				try {
					reader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		  }
	}
}
