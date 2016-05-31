package cn.itcast.picture;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.authentication.activity.R;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

public class TakePictureActivity extends Activity {
    private View layout;
    private Camera camera;
    private String file_pathname;
    private String dirname;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        		WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.take_picture_activity);
        initData();
        layout = this.findViewById(R.id.buttonlayout);
        
        SurfaceView surfaceView = (SurfaceView) this.findViewById(R.id.surfaceView);
        surfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceView.getHolder().setFixedSize(176, 144);
        surfaceView.getHolder().setKeepScreenOn(true);
        surfaceView.getHolder().addCallback(new SurfaceCallback());
    }
    
    private void initData() {
    	if (getIntent()!=null) {
    		 dirname=getIntent().getStringExtra("dirname");
		}else {
			dirname="exception_picture";
		}
		
	}

	public void takepicture(View v){
    	if(camera!=null){
    		switch (v.getId()) {
    		case R.id.takepicture:
    			camera.takePicture(null, null, new MyPictureCallback());
    			break;

    		}
    	}
    }
    
    private final class MyPictureCallback implements PictureCallback{
		public void onPictureTaken(byte[] data, Camera camera) {
			FileOutputStream outStream =null;
			try {
				
				File tmpDir = new File(Environment.getExternalStorageDirectory()
						+ "/"+dirname);
				if (!tmpDir.exists()) {
					tmpDir.mkdir();
				 }
		     	String 	photo_pathname=System.currentTimeMillis()+".jpg";
		     	file_pathname = tmpDir.getAbsolutePath() + "/" + photo_pathname;
				File jpgFile = new File(file_pathname);
			    outStream = new FileOutputStream(jpgFile);
				outStream.write(data);
				outStream.close();
				outStream=null;
//				camera.startPreview();
				Intent intent = getIntent();
				intent.putExtra("file_pathname", file_pathname);
				TakePictureActivity.this.setResult(1, intent);
				TakePictureActivity.this.finish();
			} catch (Exception e) {
				e.printStackTrace();
			}
			finally{
				if (outStream!=null) {
					try {
						outStream.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
    	
    }
    
    private final class SurfaceCallback implements Callback{
		public void surfaceCreated(SurfaceHolder holder) {
			try{
				camera = Camera.open();//������ͷ
				Log.e("camera", "camera open");
				Camera.Parameters parameters = camera.getParameters();
				parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
				//Log.i("MainActivity", parameters.flatten());
				parameters.setPreviewSize(800, 480);
				parameters.setPreviewFrameRate(5);
				parameters.setPictureSize(800, 480);
				parameters.setJpegQuality(85);
				camera.setParameters(parameters);
				camera.setDisplayOrientation(90);
				camera.setPreviewDisplay(holder);
				camera.startPreview();//��ʼԤ��
				camera.autoFocus(null);
			}catch (Exception e) {
				e.printStackTrace();
			}
		}

		public void surfaceChanged(SurfaceHolder holder, int format, int width,int height) {
		}

		public void surfaceDestroyed(SurfaceHolder holder) {
			if(camera!=null){
				camera.release();
				camera = null;
				Log.e("camera", "camera release");
			}
		}
    	
    }

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			if (camera!=null) {
				camera.autoFocus(null);//对焦
			}
			return true;
		}
		return super.onTouchEvent(event);
	}
	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(camera!=null){
			camera.release();
			camera = null;
			Log.e("camera", "camera release");
		}
	}
    
    
}