package com.picture.compress;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

public class PictureUtils {
	private BitmapFactory.Options opts ;  
	
	public PictureUtils() {
		super();
		opts = new BitmapFactory.Options();
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public   Uri compressImageFromFile(String srcPath) { 
		File file = new File(srcPath);
		FileInputStream in=null;
		try {
			in = new FileInputStream(file);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Bitmap bitmap = null;
		opts.inDither=false;
		opts.inPurgeable=true;//。当系统内存不够时候图片自动被回收  
		opts.inInputShareable = true;
		opts.inTempStorage=new byte[16*1024];
		opts.inPreferredConfig = Bitmap.Config.RGB_565;
		opts.inSampleSize = 7;
		opts.inJustDecodeBounds = false; //读内容  
		try {
			bitmap = BitmapFactory.decodeFileDescriptor(in.getFD(), null, opts);
			return SaveBitmap(bitmap,srcPath,file);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		finally{
			if (in!=null) {
				try {
					in.close();
					in=null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(bitmap != null && !bitmap.isRecycled()){    
		        bitmap.recycle();    
		        bitmap = null;    
		    }   
		    System.gc(); 
		 }
      }  
	
	
	
	public   Uri compressBigImageFromFile(String srcPath) { 
		File file = new File(srcPath);
		FileInputStream in=null;
		try {
			in = new FileInputStream(file);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Bitmap bitmap = null;
		opts.inDither=false;
		opts.inPurgeable=true;//。当系统内存不够时候图片自动被回收  
		opts.inInputShareable = true;
		opts.inTempStorage=new byte[16*1024];
		opts.inPreferredConfig = Bitmap.Config.RGB_565;
		opts.inSampleSize =7;
		opts.inJustDecodeBounds = false; //读内容  
		try {
			bitmap = BitmapFactory.decodeFileDescriptor(in.getFD(), null, opts);
			return SaveBitmap(bitmap,srcPath,file);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		finally{
			if (in!=null) {
				try {
					in.close();
					in=null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(bitmap != null && !bitmap.isRecycled()){    
		        bitmap.recycle();    
		        bitmap = null;    
		    }   
		    System.gc(); 
		 }
      }  
	
	public   Uri SaveBitmap(Bitmap bitmap,String imgPath,File file) {
		FileOutputStream fos=null;
		try {
			fos= new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
			fos.flush();
			return Uri.fromFile(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}finally{
			if (fos!=null) {
				try {
					fos.close();
					fos=null;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		   if(bitmap != null && !bitmap.isRecycled()){    
		        bitmap.recycle();    
		        bitmap = null;    
		    }    
		}
	}
	
	
	public  static void SaveByteBitmap(Bitmap bitmap,String imagepath) {
		File file = new File(imagepath);
		try {
			FileOutputStream fos = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 85, fos);
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
		   if(bitmap != null && !bitmap.isRecycled()){    
		        bitmap.recycle();    
		        bitmap = null;    
		    }    
		   System.gc(); 
		}
	}
	
}
