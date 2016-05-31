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

import android.content.Context;

/**
 *************************************************************** 
  控制开关的Scoket编程
 * @version
 *************************************************************** 
 */
public class ControllerOpenCloseDoor1 {
	private Context context;
	private String ip;
	
	public ControllerOpenCloseDoor1(Context context, String ip) {
		this.context = context;
		this.ip = ip;
	}

	public void OpenDoor() {
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
			T.showShort(context, "连接开关门失败！");
		}
	}
	
	
	public void CloseDoor() {

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
			T.showShort(context, "连接开关门失败！");
		}
	}

}
