package com.example.Utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;

public class TopActivity {
	//获取栈顶Activity及其所属进程
		public static String getTopActivityNameAndProcessName(Context context){
			String processName=null;
			String topActivityName=null;
			 ActivityManager activityManager =
			(ActivityManager)(context.getSystemService(android.content.Context.ACTIVITY_SERVICE )) ;
		     List<RunningTaskInfo> runningTaskInfos = activityManager.getRunningTasks(1) ;
		     if(runningTaskInfos != null){
		    	 ComponentName f=runningTaskInfos.get(0).topActivity;
		    	 String topActivityClassName=f.getClassName();
		    	 String temp[]=topActivityClassName.split("\\.");
		    	 //栈顶Activity的名称
		    	 topActivityName=temp[temp.length-1];
		    	 int index=topActivityClassName.lastIndexOf(".");
		    	//栈顶Activity所属进程的名称
		    	 processName=topActivityClassName.substring(0, index);
		    	 System.out.println("---->topActivityName="+topActivityName+",processName="+processName);
		    	 
		     }
		     return topActivityName;
		}

}
