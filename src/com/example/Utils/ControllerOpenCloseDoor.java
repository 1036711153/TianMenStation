package com.example.Utils;

/**
 * 
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import com.example.instation.InStationUploadDataActivity;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

/**
 *************************************************************** 
  控制开关的Scoket编程
 * @version
 *************************************************************** 
 */
public class ControllerOpenCloseDoor {
	private Context context;
	private String ip;
	private Handler mHandler;
	
	public ControllerOpenCloseDoor(Context context, String ip,Handler handler) {
		this.context = context;
		this.ip = ip;
		mHandler=handler;
	}

	public void OpenDoor() {
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				Socket socket = null;

				PrintWriter pw = null;
				try {
					// 客户端socket指定服务器的地址和端口号
					socket = new Socket(ip, 1234);
					System.out.println("Socket=" + socket);
					// 同服务器原理一样
					DataInputStream br = new DataInputStream(socket.getInputStream());
					pw = new PrintWriter(socket.getOutputStream(), true);
					pw.println("P1");
					byte[] b = new byte[1024];
					/* int in = socket.getInputStream().read(b); */

					// String str = br.readLine();
					br.read(b);
					System.out.println(new String(b));
					System.out.println("close......");
					br.close();
					pw.close();
					socket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Message msg=new Message();
					msg.obj=1;
					mHandler.sendMessage(msg);
				}
			}
		}).start();
		
		
		
		
	}
	
	
	public void CloseDoor() {
		
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				Socket socket = null;
				PrintWriter pw = null;
				try {
					// 客户端socket指定服务器的地址和端口号
					socket = new Socket(ip, 1234);
					System.out.println("Socket=" + socket);
					// 同服务器原理一样
					DataInputStream br = new DataInputStream(socket.getInputStream());
					pw = new PrintWriter(socket.getOutputStream(), true);
					pw.println("P2");
					byte[] b = new byte[1024];
					/* int in = socket.getInputStream().read(b); */

					// String str = br.readLine();
					br.read(b);
					System.out.println(new String(b));
					System.out.println("close......");
					br.close();
					pw.close();
					socket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Message msg=new Message();
					msg.obj=1;
					mHandler.sendMessage(msg);
				}
			}
		}).start();
	}
	
	
	
	public void Recycle() {
		context=null;
		ip=null;
		mHandler=null;
	}
}
