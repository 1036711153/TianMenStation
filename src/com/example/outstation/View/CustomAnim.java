package com.example.outstation.View;


import android.view.animation.Animation;
import android.view.animation.Transformation;

public class CustomAnim extends Animation {

	@Override
	public void initialize(int width, int height, int parentWidth,
			int parentHeight) {
		// TODO Auto-generated method stub
		super.initialize(width, height, parentWidth, parentHeight);
	}
	
	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {
		
		t.getMatrix().setTranslate(0,(float) (Math.sin(interpolatedTime*3.1415926)*380));
//		t.getMatrix().setTranslate(20*interpolatedTime, 0);
//		t.setAlpha(interpolatedTime);
		super.applyTransformation(interpolatedTime, t);
	}
}
