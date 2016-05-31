/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.authentication.activity;

import java.io.UnsupportedEncodingException;

import com.authentication.asynctask.AsynSmartCard;
import com.authentication.asynctask.AsynSmartCard.OnReadIBANListener;
import com.authentication.asynctask.AsynSmartCard.OnCreateCardListener;
import com.authentication.asynctask.AsynSmartCard.OnDeleteCardListener;
import com.authentication.asynctask.AsynSmartCard.OnExternalAuthListener;
import com.authentication.asynctask.AsynSmartCard.OnInitCardListener;
import com.authentication.asynctask.AsynSmartCard.OnReadBinaryListener;
import com.authentication.asynctask.AsynSmartCard.OnReadRecordListener;
import com.authentication.asynctask.AsynSmartCard.OnWriteBinaryListener;
import com.authentication.asynctask.AsynSmartCard.OnWriteRecordListener;
import com.authentication.utils.DataUtils;
import com.authentication.utils.ToastUtil;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
/**
 * This is the main Activity that displays the current chat session.
 */
public class SmartCardActivity extends BaseActivity {
	public static final int SMARTCARD_INITCARD = 100;
	public static final int SMARTCARD_EXTERNALAUTH = 101;
	public static final int SMARTCARD_PRINTER = 102;
	public static final int SMARTCARD_READCARD = 103;
	public static final int SMARTCARD_WRITECARD = 104;
	public static final int SMARTCARD_CREATECARD = 105;
	public static final int SMARTCARD_DELETECARD = 106;
	public static final int SMARTCARD_READIBAN = 107;

	private EditText mInputEditText;
	private EditText mOutEditText01;
	private EditText mOutEditText02;
	private EditText mOutEditText03;
	private EditText mOutEditText04;
	private Button mSendButton;
	private Button mbuttonrecv;
	private Button mbuttonprinter;
	private Button minitcardButton;
	private Button mcreatefileButton;
	private Button mdeletefileButton;
	private Button mexternalauthbutton;
	private Button mReadIBANbutton;
	//private CheckBox checkBox_sixteen;
	private AsynSmartCard SmartCard = null;
	
	private String printerBuffer = null;
	// 当地的蓝牙适配器
	private BluetoothAdapter mBluetoothAdapter = null;
	
	private Handler mThreadHandler = null;
	
    private int UTF =1;
    
    private ProgressDialog progressDialog;

	// 记录当创建服务器套接字
	String mmsg = "";
	String mmsg2 = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.smartcard);
		

		
		mInputEditText = (EditText) findViewById(R.id.editText1);
		mInputEditText.setGravity(Gravity.TOP);
		mInputEditText.setSelection(mInputEditText.getText().length(),
		mInputEditText.getText().length());
		mInputEditText.clearFocus();
		mInputEditText.setFocusable(false);
		
		new MyThread().start();    
	}
	
	private void initData(){
		SmartCard = new AsynSmartCard(handlerThread.getLooper());
		SmartCard.setOnInitCardListener(new OnInitCardListener() {

			@Override
			public void OnInitCardSuccess(byte[] data) {
				// TODO Auto-generated method stub
				String buffer = null;
				try {
					buffer = new String(data,"GBK");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				mHandler.sendMessage(mHandler.obtainMessage(UTF, (String)(buffer+"\r\n")));
			}

			@Override
			public void OnInitCardFail(int code) {
				// TODO Auto-generated method stub
				if(code == 0x0042)
				{
					ToastUtil.showToast(SmartCardActivity.this, "MALLOC FAILURE");
				}
				mHandler.sendMessage(mHandler.obtainMessage(UTF, (String)("Reset Card Fail!\r\n")));
			}
		});
		
		SmartCard.setOnExternalAuthListener(new OnExternalAuthListener() {

			@Override
			public void OnExternalAuthSuccess() {
				// TODO Auto-generated method stub
				mHandler.sendMessage(mHandler.obtainMessage(UTF, (String)("External Auth Success!\r\n")));
			}

			@Override
			public void OnExternalAuthFail(int code) {
				// TODO Auto-generated method stub
				if(code == 0x0042)
				{
					ToastUtil.showToast(SmartCardActivity.this, "MALLOC FAILURE");
				}
				mHandler.sendMessage(mHandler.obtainMessage(UTF, (String)("External Auth Fail!\r\n")));
			}
		});
		
		SmartCard.setOnReadBinaryListener(new OnReadBinaryListener() {

			@Override
			public void OnReadBinarySuccess(byte[] data) {
				// TODO Auto-generated method stub
				String BinaryBuffer = null;
				String strRec = null;
				try {
					strRec = new String(data,"GBK");//UTF-8
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				System.out.println("strRec="+strRec);
				BinaryBuffer  = "    姓名:  "+strRec.substring(0,4)+"\n";
				BinaryBuffer += "身份证号: "+strRec.substring(4,22)+"\n";
				BinaryBuffer += "档案编号: "+strRec.substring(22,34)+"\n";
				BinaryBuffer += "联系方式: "+strRec.substring(34,45)+"\n";
				printerBuffer = BinaryBuffer;
				//mInputEditText.getText().append((String) printerBuffer);
				mHandler.sendMessage(mHandler.obtainMessage(UTF, (String)(BinaryBuffer)));
			}

			@Override
			public void OnReadBinaryFail(int code) {
				// TODO Auto-generated method stub
				if(code == 0x0042)
				{
					ToastUtil.showToast(SmartCardActivity.this, "MALLOC FAILURE");
				}
				mHandler.sendMessage(mHandler.obtainMessage(UTF, (String)("Read Binary File Fail!\r\n")));
			}
		});
		
		SmartCard.setOnReadRecordListener(new OnReadRecordListener() {

			@Override
			public void OnReadRecordSuccess(byte[] data) {
				// TODO Auto-generated method stub
				String RecordBuffer = null;
				String strRec = bcd2Str(data); 
				
				RecordBuffer = "违章记录: "+strRec.substring(0,2)+"\n";
				RecordBuffer += "违章积分: "+strRec.substring(2,4)+"\n";
				RecordBuffer += "违章金额: "+strRec.substring(4,10)+"\n";
				RecordBuffer += "违章时间: "+strRec.substring(10,22)+"\n";
				printerBuffer += RecordBuffer;
				mHandler.sendMessage(mHandler.obtainMessage(UTF, (String)(RecordBuffer)));
			}

			private String bcd2Str(byte[] bytes) {  
		        char temp[] = new char[bytes.length * 2], val;  
		  
		        for (int i = 0; i < bytes.length; i++) {  
		            val = (char) (((bytes[i] & 0xf0) >> 4) & 0x0f);  
		            temp[i * 2] = (char) (val > 9 ? val + 'A' - 10 : val + '0');  
		  
		            val = (char) (bytes[i] & 0x0f);  
		            temp[i * 2 + 1] = (char) (val > 9 ? val + 'A' - 10 : val + '0');  
		        }  
		        return new String(temp);  
		    }

			@Override
			public void OnReadRecordFail(int code) {
				// TODO Auto-generated method stub
				if(code == 0x0042)
				{
					ToastUtil.showToast(SmartCardActivity.this, "MALLOC FAILURE");
				}
				mHandler.sendMessage(mHandler.obtainMessage(UTF, (String)("Read Record File Fail!\r\n")));
			}
		});
		
		SmartCard.setOnWriteBinaryListener(new OnWriteBinaryListener() {

			@Override
			public void OnWriteBinarySuccess() {
				// TODO Auto-generated method stub
				mHandler.sendMessage(mHandler.obtainMessage(UTF, (String)("Update Binary File success!\r\n")));
			}

			@Override
			public void OnWriteBinaryFail(int code) {
				// TODO Auto-generated method stub
				if(code == 0x0042)
				{
					ToastUtil.showToast(SmartCardActivity.this, "MALLOC FAILURE");
				}
				mHandler.sendMessage(mHandler.obtainMessage(UTF, (String)("Update Binary File Fail!\r\n")));
			}
		});
		
		SmartCard.setOnWriteRecordListener(new OnWriteRecordListener() {

			@Override
			public void OnWriteRecordSuccess() {
				// TODO Auto-generated method stub
				mHandler.sendMessage(mHandler.obtainMessage(UTF, (String)("Update Record File success!\r\n")));
			}

			@Override
			public void OnWriteRecordFail(int code) {
				// TODO Auto-generated method stub
				if(code == 0x0042)
				{
					ToastUtil.showToast(SmartCardActivity.this, "MALLOC FAILURE");
				}
				mHandler.sendMessage(mHandler.obtainMessage(UTF, (String)("Update Record File Fail!\r\n")));
			}
		});
		
		SmartCard.setOnCreateCardListener(new OnCreateCardListener() {

			@Override
			public void OnCreateCardSuccess() {
				// TODO Auto-generated method stub
				mHandler.sendMessage(mHandler.obtainMessage(UTF, (String)("Create File Sucess!\r\n")));
			}

			@Override
			public void OnCreateCardFail(int code) {
				// TODO Auto-generated method stub
				if(code == 0x0042)
				{
					ToastUtil.showToast(SmartCardActivity.this, "MALLOC FAILURE");
				}
				mHandler.sendMessage(mHandler.obtainMessage(UTF, (String)("Create File Fail!\r\n")));
			}
		});
		
		SmartCard.setOnDeleteCardListener(new OnDeleteCardListener() {

			@Override
			public void OnDeleteCardSuccess() {
				// TODO Auto-generated method stub
				mHandler.sendMessage(mHandler.obtainMessage(UTF, (String)("Delete File Success!\r\n")));
			}

			@Override
			public void OnDeleteCardFail(int code) {
				// TODO Auto-generated method stub
				if(code == 0x0042)
				{
					ToastUtil.showToast(SmartCardActivity.this, "MALLOC FAILURE");
				}
				mHandler.sendMessage(mHandler.obtainMessage(UTF, (String)("Delete File Fail!\r\n")));
			}
		});

		SmartCard.setOnReadIBANListener(new OnReadIBANListener() {

			@Override
			public void OnReadIBANSuccess(byte[] data) {
				// TODO Auto-generated method stub
				String IBANCode = DataUtils.toHexString(data);
				printerBuffer = "银行账号: "+IBANCode+"\n";
				mHandler.sendMessage(mHandler.obtainMessage(UTF, (String)printerBuffer));
			}

			@Override
			public void OnReadIBANFail(int code) {
				// TODO Auto-generated method stub
				if(code == 0x0042)
				{
					ToastUtil.showToast(SmartCardActivity.this, "MALLOC FAILURE");
				}
				mHandler.sendMessage(mHandler.obtainMessage(UTF, (String)("Read IBAN Fail!\r\n")));
			}
		});
	}
	
		class MyThread extends Thread {

			public void run() {
	            // 其它线程中新建一个handler
	            Looper.prepare();// 创建该线程的Looper对象，用于接收消息,在非主线程中是没有looper的所以在创建handler前一定要使用prepare()创建一个Looper
	            mThreadHandler = new Handler(Looper.myLooper()) {
			            public void handleMessage(Message msg) {
			            	super.handleMessage(msg);
			            	switch (msg.what) {
							case SMARTCARD_INITCARD:
								Init_Card();
								break;
							case SMARTCARD_EXTERNALAUTH:
								External_Authentication();
								break;
							case SMARTCARD_PRINTER:
								Printer_Information();
								break;
							case SMARTCARD_READCARD:
								Read_Basic_Information();
								Read_Violate_Record();
								break;
							case SMARTCARD_WRITECARD:
								Update_Basic_Information();
								Update_Violate_Record();
								break;
							case SMARTCARD_CREATECARD:
								Create_SmartFile();
								break;
							case SMARTCARD_DELETECARD:
								Delete_SmartFile();
								break;
							case SMARTCARD_READIBAN:
								ReadIBAN();
								break;
							default:
								break;
						}
	                }
	            };
	            Looper.myLooper().loop();//建立一个消息循环，该线程不会退出
			}
			private byte asc_to_bcd(byte asc) {  
		        byte bcd;  
		  
		        if ((asc >= '0') && (asc <= '9'))  
		            bcd = (byte) (asc - '0');  
		        else if ((asc >= 'A') && (asc <= 'F'))  
		            bcd = (byte) (asc - 'A' + 10);  
		        else if ((asc >= 'a') && (asc <= 'f'))  
		            bcd = (byte) (asc - 'a' + 10);  
		        else  
		            bcd = (byte) (asc - 48);  
		        return bcd;  
		    }  
		  
		    private byte[] ASCII_To_BCD(byte[] ascii, int asc_len) {  
		        byte[] bcd = new byte[(asc_len+1) / 2];  
		        int j = 0;  
		        for (int i = 0; i < (asc_len + 1) / 2; i++) {  
		            bcd[i] = asc_to_bcd(ascii[j++]);  
		            bcd[i] = (byte) (((j >= asc_len) ? 0x00 : asc_to_bcd(ascii[j++])) + (bcd[i] << 4));  
		        }  
		        return bcd;  
		    }  
		    
		    public void Read_Violate_Record()
			{
		    	int Count = 0x0B;
		    	SmartCard.readrecord(Count);
			}
		    
			public void Update_Violate_Record()
			{
				TextView view01 = (TextView) findViewById(R.id.editText_records);
				TextView view02 = (TextView) findViewById(R.id.editText_credits);
				TextView view03 = (TextView) findViewById(R.id.editText_amount);
				TextView view04 = (TextView) findViewById(R.id.editText_time);
				
				String recordstemp = view01.getText().toString();
				String records = null;
				System.out.println("recordtemp.length()="+recordstemp.length());
				if(recordstemp.length()<2)
				{
					for(int i = recordstemp.length();i<2;i++)
					{
						if(records==null)
							records = "0";
						else
							records += "0";
					}
					records += recordstemp;
			//		System.out.println("records="+records);
			//		System.out.println("recordstemp="+recordstemp);
				}
				else if(recordstemp.length()>2)
				{
					records = recordstemp.substring(recordstemp.length()-2);
				}
				else
					records = recordstemp;
				
				String creditstemp = view02.getText().toString();
				String credits = null;
				if(creditstemp.length()<2)
				{
					for(int i = creditstemp.length();i<2;i++)
					{
						if(credits==null)
							credits = "0";
						else
							credits += "0";
					}
					credits += creditstemp;
				}
				else if(creditstemp.length()>2)
				{
					credits = creditstemp.substring(creditstemp.length()-2);
				}
				else
					credits = creditstemp;
				
				String amounttemp = view03.getText().toString();
				String amount = null;
				if(amounttemp.length()<6)
				{
					for(int i = amounttemp.length();i<6;i++)
					{
						if(amount==null)
							amount = "0";
						else
							amount += "0";
					}
					amount += amounttemp;
				}
				else if(amounttemp.length()>6)
				{
					amount = amounttemp.substring(amounttemp.length()-6);
				}
				else
					amount = amounttemp;
				
				String timetemp = view04.getText().toString();
				String time = null;
				if(timetemp.length()<12)
				{
					for(int i = timetemp.length();i<12;i++)
					{
						if(time==null)
							time = "0";
						else
							time += "0";
					}
					time += timetemp;
				}
				else if(timetemp.length()>12)
				{
					time = timetemp.substring(timetemp.length()-12);
				}
				else
					time = timetemp;
				String message = records+credits+amount+time;
				//String message = recordstemp+creditstemp+amounttemp+timetemp;
				//String message = view01.getText().toString()+view02.getText().toString()+view03.getText().toString()+view04.getText().toString();
			
				byte[] midbytes = null;
				try {
					midbytes = message.getBytes("GBK");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("midbytes.length="+midbytes.length);
				byte[] BCDbytes = ASCII_To_BCD(midbytes, midbytes.length);
				System.out.println("BCDbytes.length="+BCDbytes.length);
				if(BCDbytes.length != 0x0B)
				{
					ToastUtil.showToast(SmartCardActivity.this, "写记录文件长度错误");
				}
				SmartCard.writerecord(BCDbytes,BCDbytes.length);

			}
			
			public void Read_Basic_Information()
			{
				int Count = 0x31;
				SmartCard.readbinary(Count);
			}
			
			public void Update_Basic_Information()
			{
				TextView view01 = (TextView) findViewById(R.id.editText_name);
				TextView view02 = (TextView) findViewById(R.id.editText_ID);
				TextView view03 = (TextView) findViewById(R.id.editText_number);
				TextView view04 = (TextView) findViewById(R.id.editText_contact);
				String name = view01.getText().toString();
				if(name.length()<4)
				{
					for(int i = name.length();i<4;i++)
						name += " ";
				}
				else if(name.length()>4)
				{
					name = name.substring(0, 4);

				}
					
				String ID = view02.getText().toString();
				if(ID.length()<18)
				{
					for(int i = ID.length();i<18;i++)
						ID += " ";
				}
				else if(ID.length()>18)
				{
					ID = ID.substring(0, 18);

				}
				String number = view03.getText().toString();
				if(number.length()<12)
				{
					for(int i = number.length();i<12;i++)
						number += " ";
				}
				else if(number.length()>12)
				{
					number = number.substring(0, 12);

				}
				String contact = view04.getText().toString();
				if(contact.length()<11)
				{
					for(int i = contact.length();i<11;i++)
						contact += " ";
				}
				else if(contact.length()>11)
				{
					contact = contact.substring(0, 11);

				}
				String message = name+ID+number+contact;
				//String message = name+view02.getText().toString()+view03.getText().toString()+view04.getText().toString();
				
				byte[] midbytes = null;
				try {
					midbytes = message.getBytes("GBK");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(midbytes.length > 0x31)
				{
					ToastUtil.showToast(SmartCardActivity.this, "超过二进制文件最大长度");
				}
				SmartCard.writebinary(midbytes,midbytes.length);

			}
			
			public void Init_Card()
			{
				SmartCard.initcard();
			}
			
			public void External_Authentication()
			{
				SmartCard.externalauth();
			}
			
			public void Printer_Information()
			{
				//application.Printf(printerBuffer);
				//PrinterAPI.printPaper(printerBuffer);
			}
			
			public void Create_SmartFile()
			{
				SmartCard.createcard();
			}
			
			public void Delete_SmartFile()
			{
				SmartCard.deletecard();
			}
			
			public void ReadIBAN()
			{
				SmartCard.ReadIBAN(SmartCard.SCARD_LOAN);
				SmartCard.ReadIBAN(SmartCard.SCARD_DEBIT);
			}
			
		}
//    }

	@Override
	public void onStart() {
		super.onStart();
		setupChat();
	}
	
	@Override
	public synchronized void onResume() {
		super.onResume();
		initData();
	}
	

	
	private void setupChat() {
		minitcardButton = (Button) findViewById(R.id.button_initcard);
		minitcardButton.setOnClickListener(new OnClickListener() {
		public void onClick(View v) {
			//发送需要更新imageview视图的消息
			showProgressDialog("正在初始化，请稍候......");
			mThreadHandler.sendMessage(mThreadHandler.obtainMessage(SMARTCARD_INITCARD));
		}
		});
		
		mReadIBANbutton  = (Button) findViewById(R.id.button_ReadIBAN);
		mReadIBANbutton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
			//if (view.getId() == R.id.button_createfile) {
				showProgressDialog("正在读卡，请稍候......");
				mThreadHandler.sendMessage(mThreadHandler.obtainMessage(SMARTCARD_READIBAN));
				}
			});
		/////////////////////////
//		mexternalauthbutton = (Button) findViewById(R.id.button_externalauth);
//		mexternalauthbutton.setOnClickListener(new OnClickListener() {
//			public void onClick(View v) {
//				//showProgressDialog("正在认证，请稍候......");
//				mThreadHandler.sendMessage(mThreadHandler.obtainMessage(SMARTCARD_EXTERNALAUTH));
//			}
//		});
		///////////////////////////
		mcreatefileButton = (Button) findViewById(R.id.button_createfile);
		mcreatefileButton.setOnClickListener(new OnClickListener() {
		public void onClick(View v) {
		//if (view.getId() == R.id.button_createfile) {
			showProgressDialog("正在建立文件，请稍候......");
			mThreadHandler.sendMessage(mThreadHandler.obtainMessage(SMARTCARD_CREATECARD));
			}
		});
		
		mdeletefileButton = (Button) findViewById(R.id.button_deletefile);
		mdeletefileButton.setOnClickListener(new OnClickListener() {
		public void onClick(View v) {
			showProgressDialog("正在删除文件，请稍候......");
			mThreadHandler.sendMessage(mThreadHandler.obtainMessage(SMARTCARD_DELETECARD));
		}
		}	);

		// 初始化发送按钮，单击事件侦听器
		mSendButton = (Button) findViewById(R.id.button_send);
		mSendButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				// 发送消息使用内容的文本编辑控件
				/*TextView view = (TextView) findViewById(R.id.edit_text_out);
				TextView view2 = (TextView) findViewById(R.id.edit_text_out2);
				String message = view.getText().toString();
				String message2 = view2.getText().toString();*/
				showProgressDialog("正在写卡，请稍候......");
				mThreadHandler.sendMessage(mThreadHandler.obtainMessage(SMARTCARD_WRITECARD));
			}
		});
		//////////////button_printer
		mbuttonprinter = (Button) findViewById(R.id.button_printer);
		mbuttonprinter.setOnClickListener(new OnClickListener() {
		public void onClick(View v) {
				//printerBuffer = "姓名:小明 ERY:13016975236";
				mThreadHandler.sendMessage(mThreadHandler.obtainMessage(SMARTCARD_PRINTER));
			}
		});
		
		mbuttonrecv = (Button) findViewById(R.id.button_recv);
		mbuttonrecv.setOnClickListener(new OnClickListener() {
		public void onClick(View v) {
				// 接收消息使用内容的文本编辑控件
			showProgressDialog("正在读卡，请稍候......");
			mThreadHandler.sendMessage(mThreadHandler.obtainMessage(SMARTCARD_READCARD));
			}
		});
		
	}

	public void onMyButtonClick(View view) throws UnsupportedEncodingException {
		if (view.getId() == R.id.button_clean) {
			mInputEditText.setText("");
			printerBuffer = "";
		}
		
	}
	@Override
	public synchronized void onPause() {
		SmartCard.close();
		super.onPause();

	}

	@Override
	public void onStop() {
		super.onStop();

	}

	@Override
	public void onDestroy() {
		super.onDestroy();

	}
	
	private final Handler mHandler = new Handler() {
			
			@Override
			public void handleMessage(Message msg) {
				mInputEditText.getText().append((String) msg.obj);
				cancleProgressDialog();
				}
	};
	private void showProgressDialog(String message) {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(this);
		}
		progressDialog.setMessage(message);
		if (!progressDialog.isShowing()) {
			progressDialog.show();
		}
	}

	private void showProgressDialog(int resId) {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(this);
		}
		progressDialog.setMessage(getResources().getString(resId));
		if (!progressDialog.isShowing()) {
			progressDialog.show();
		}
	}

	private void cancleProgressDialog() {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.cancel();
		}
	}
}
