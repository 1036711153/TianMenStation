package com.picture.compress;

import java.io.File;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;

public class CreatePhoto {
	private String FileName;
	private String StorageDirectoryName;
	//安检图片路径
	private String imagePath;
	private File file;
	private File tmpDir;
	
	public CreatePhoto( String fileName, String storageDirectoryName) {
		FileName = fileName;
		StorageDirectoryName = storageDirectoryName;
		tmpDir = new File(Environment.getExternalStorageDirectory()
				+ "/"+StorageDirectoryName);
		if (!tmpDir.exists()) {
			tmpDir.mkdir();
		 }
	}
	
	public File createFile() {
		String photo_name1 = FileName + System.currentTimeMillis() + ".png";
		imagePath = tmpDir.getAbsolutePath() + "/" + photo_name1;
	    file =new File(imagePath);
	    return file;
	}
	
	public Uri getFileUri() {
		return Uri.fromFile(file);
	}
	
	public String getImagePath(){
		return  imagePath;
	}
}
