package com.example.Utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;

public class SaveImage {
	private Bitmap bitmap;
	private String FileName;
	private String StorageDirectoryName;
	//安检图片路径
	private String imagePath;
	
	
	
	public SaveImage(Bitmap bitmap, String fileName, String storageDirectoryName) {
		this.bitmap = bitmap;
		FileName = fileName;
		StorageDirectoryName = storageDirectoryName;
		File tmpDir = new File(Environment.getExternalStorageDirectory()
				+ "/"+StorageDirectoryName);
		if (!tmpDir.exists()) {
			tmpDir.mkdir();
		}
		String photo_name1 = FileName + System.currentTimeMillis() + ".png";
		imagePath = tmpDir.getAbsolutePath() + "/" + photo_name1;
	}
	
	
	public String getImagePath(){
		return  imagePath;
	}


	public Uri SaveBitmap() {
		File img = new File(imagePath);
		try {
			FileOutputStream fos = new FileOutputStream(img);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 85, fos);
			fos.flush();
			fos.close();
			return Uri.fromFile(img);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}finally{
			bitmap.recycle();
		}
	}
	
	
	public Uri SaveBigBitmap(){
		Bitmap map = Bitmap.createScaledBitmap(bitmap, 480, 800, false);
		File img = new File(imagePath);
		try {
			FileOutputStream fos = new FileOutputStream(img);
			map.compress(Bitmap.CompressFormat.PNG, 95, fos);
			fos.flush();
			fos.close();
			return Uri.fromFile(img);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}finally{
			map.recycle();
			bitmap.recycle();
		}
	}
	
	
	
	public  Uri compressBmpToFile(){ 
		Bitmap map = Bitmap.createScaledBitmap(bitmap, 480, 800, true);
		File img = new File(imagePath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
        int options = 100;//个人喜欢从80开始,  
        map.compress(Bitmap.CompressFormat.PNG, options, baos);  
        while (baos.toByteArray().length / 1024 > 400) {   
            baos.reset();  
            options -= 10;  
            map.compress(Bitmap.CompressFormat.PNG, options, baos);  
        }  
        try {  
            FileOutputStream fos = new FileOutputStream(img);  
            fos.write(baos.toByteArray());  
            fos.flush();  
            fos.close();  
            return Uri.fromFile(img);
        } catch (Exception e) {  
            e.printStackTrace(); 
        	return null; 
        }
        finally{
        	map.recycle();
        	bitmap.recycle();
        }
    }

}
