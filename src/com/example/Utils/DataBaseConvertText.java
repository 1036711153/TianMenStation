package com.example.Utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import android.os.Environment;

public class DataBaseConvertText {
	private String content;
	private String name;
	private String FileName;


	public DataBaseConvertText(String content, String name, String fileName) {
		this.content = content;
		this.name = name;
		FileName = fileName;
	}

	public void SaveText() {
		File tmpDir=new File(Environment.getExternalStorageDirectory()+"/"+FileName);
		if(!tmpDir.exists()){
			tmpDir.mkdir();
		}
		String nametxt=name+".txt";
	    String  anjian_TEXT = tmpDir.getAbsolutePath()+"/"+nametxt;
		File img=new File(anjian_TEXT);
		FileOutputStream outputStream=null;
		BufferedWriter writer=null;
		try {
			outputStream=new FileOutputStream(img);
			writer=new BufferedWriter(new OutputStreamWriter(outputStream));
			writer.write(content);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(writer!=null)
				try {
					writer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		  }
	}

}
