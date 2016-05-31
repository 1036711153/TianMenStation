package com.example.Utils;


public class ASNItoChart {
	private String bString; 
	private String aString; 
	
	public ASNItoChart(String RFID) {
		 aString=RFID.substring(0, 4).toUpperCase();
		 bString=RFID.substring(4,16).toUpperCase();
	}
	
	public String getChepai() {
		String chepai_1=toConvertString(aString);
		String chepai_2=new String(toByteArray(bString));
		String chepaiString=chepai_1+chepai_2;
		return chepaiString;
	}


	public String toConvertString(String chepai_1) {
		String string = null;
		switch (chepai_1) {
		case "B6F5":
			string = "鄂";
			break;
		case "BEA9":
			string = "京";
			break;
		case "BDF2":
			string = "津";
			break;
		case "BBA6":
			string = "沪";
			break;
		case "D3E5":
			string = "渝";
			break;
		case "C3C9":
			string = "蒙";
			break;
		case "D0C2":
			string = "新";
			break;
		case "B2D8":
			string = "藏";
			break;
		case "C4FE":
			string = "宁";
			break;
		case "B9F0":
			string="桂";
			break;
		case "B8DB":
			string="港";
			break;
		case "B0C4":
			string="澳";
			break;
		case "BADA":
			string="黑";
			break;
		case "BCAA":
			string="吉";
			break;
		case "C1C9":
			string="辽";
			break;
		case "BCBD":
			string="冀";
			break;
		case "BDFA":
			string="晋";
			break;
		case "C7E0":
			string="青";
			break;
		case "C2B3":
			string="鲁";
			break;
		case "D4A5":
			string="豫";
			break;
		case "CBD5":
			string="苏";
			break;
		case "CDEE":
			string="皖";
			break;
		case "D5E3":
			string="浙";
			break;
		case "C3F6":
			string="闽";
			break;
		case "B8D3":
			string="赣";
			break;
		case "CFE6":
			string="湘";
			break;
		case "D4C1":
			string="粤";
			break;
		case "CCA8":
			string="台";
			break;
		case "C7ED":
			string="琼";
			break;
		case "B8CA":
			string="甘";
			break;
		case "C9C2":
			string="陕";
			break;
		case "B4A8":
			string="川";
			break;
		case "B9F3":
			string="贵";
			break;
		case "D4C6":
			string="云";
			break;
		default:
			break;
		}
		return string;
	}
	
	 /**
	  * 16进制的字符串表示转成字节数组
	  * 
	  * @param hexString
	  *            16进制格式的字符串
	  * @return 转换后的字节数组
	  **/
	 public static byte[] toByteArray(String hexString) {
	  if (hexString.equals(""))
	   throw new IllegalArgumentException("this hexString must not be empty");
	 
	  hexString = hexString.toLowerCase();
	  final byte[] byteArray = new byte[hexString.length() / 2];
	  int k = 0;
	  for (int i = 0; i < byteArray.length; i++) {//因为是16进制，最多只会占用4位，转换成字节需要两个16进制的字符，高位在先
	   byte high = (byte) (Character.digit(hexString.charAt(k), 16) & 0xff);
	   byte low = (byte) (Character.digit(hexString.charAt(k + 1), 16) & 0xff);
	   byteArray[i] = (byte) (high << 4 | low);
	   k += 2;
	  }
	  return byteArray;
	 }

}
