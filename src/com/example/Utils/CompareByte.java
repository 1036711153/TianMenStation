package com.example.Utils;

public class CompareByte {
	 /**
	  * 比较两个byte数组数据是否相同,相同返回 true
	  * 
	  * @param data1
	  * @param data2
	  * @param len
	  * @return
	  */
	 public static boolean memcmp(byte[] data1, byte[] data2, int len) {
	  if (data1 == null && data2 == null) {
	   return true;
	  }
	  if (data1 == null || data2 == null) {
	   return false;
	  }
	  if (data1 == data2) {
	   return true;
	  }
	  boolean bEquals = true;
	  int i;
	  for (i = 0; i < data1.length && i < data2.length && i < len; i++) {
	   if (data1[i] != data2[i]) {
	    bEquals = false;
	    break;
	   }
	  }
	  return bEquals;
	  }

}
