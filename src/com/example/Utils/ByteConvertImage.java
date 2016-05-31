package com.example.Utils;

import java.io.File;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ByteConvertImage {
	 //byte数组到图片
	public static Bitmap Bytes2Bimap(byte[] b){   
	    if(b.length!=0){
	        return BitmapFactory.decodeByteArray(b, 0, b.length);
	    } else {
	        return null;
	    }
	}
}
