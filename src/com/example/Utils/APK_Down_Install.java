package com.example.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

public class APK_Down_Install {
	private Context context;
	private String httpUrl;
	private File file;
	
	
	public APK_Down_Install(Context context, String httpUrl) {
		this.context = context;
		this.httpUrl = httpUrl;
	}
	
	
	public void Down_Install() {
		downLoadFile();
		openFile(file);
		
	}

	//下载apk程序代码
	 private File downLoadFile() {
	                 // TODO Auto-generated method stub
	                 final String fileName = "Tianmen.apk";
	                 File tmpFile = new File("/sdcard/Tianmen");
	                 if (!tmpFile.exists()) {
	                         tmpFile.mkdir();
	                 }
	                file = new File("/sdcard/Tianmen/" + fileName);
	                try {
	                         URL url = new URL(httpUrl);
	                         try {
	                                 HttpURLConnection conn = (HttpURLConnection) url
	                                                 .openConnection();
	                                 InputStream is = conn.getInputStream();
	                                 FileOutputStream fos = new FileOutputStream(file);
	                                 byte[] buf = new byte[256];
	                                 conn.connect();
	                                 double count = 0;
	                                 if (conn.getResponseCode() >= 400) {
	                                         Toast.makeText(context, "连接超时", Toast.LENGTH_SHORT)
	                                                         .show();
	                                 } else {
	                                         while (count <= 100) {
	                                                 if (is != null) {
	                                                         int numRead = is.read(buf);
	                                                         if (numRead <= 0) {
	                                                                 break;
	                                                         } else {
	                                                                 fos.write(buf, 0, numRead);
	                                                         }
	                                                } else {
	                                                         break;
	                                                 }
	                                        }
	                                 }
	                                conn.disconnect();
	                                 fos.close();
	                                 is.close();
	                         } catch (IOException e) {
	                                e.printStackTrace();
	                         }
	                 } catch (MalformedURLException e) {
	                        e.printStackTrace();
	                 }
	                return file;
	         }
	 
	       //打开APK程序代码
	       private void openFile(File file) {
	                 // TODO Auto-generated method stub
	                 Log.e("OpenFile", file.getName());
	                 Intent intent = new Intent();
	                 intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	                 intent.setAction(android.content.Intent.ACTION_VIEW);
	                 intent.setDataAndType(Uri.fromFile(file),
	                                 "application/vnd.android.package-archive");
	                 context.startActivity(intent);
	         }

}
