package com.hxuehh.reuse_Process_Imp.staicUtil.utils;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;


public class Animation3DUtil {
	public  static void createRotateAnimation(View view) {
		Rotate3dAnimation rotate3dAnimation=new Rotate3dAnimation(90, -70, 0, 0, 0, 0);
			rotate3dAnimation.setDuration(600);
			rotate3dAnimation.setFillAfter(true);
			Rotate3dAnimation  rotate3dAnimation2=new Rotate3dAnimation(-70, 50, 0, 0, 0, 0);
			rotate3dAnimation2.setDuration(500);
			rotate3dAnimation2.setFillAfter(true);
			rotate3dAnimation.setAnimationListener(new MyRotateListener(view,rotate3dAnimation2));
			
			Rotate3dAnimation  rotate3dAnimation3=new Rotate3dAnimation(50, -40, 0, 0, 0, 0);
			rotate3dAnimation3.setDuration(450);
			rotate3dAnimation3.setFillAfter(true);
			rotate3dAnimation2.setAnimationListener(new MyRotateListener(view,rotate3dAnimation3));
			
			
			Rotate3dAnimation  rotate3dAnimation4=new Rotate3dAnimation(-40,30, 0, 0, 0, 0);
			rotate3dAnimation4.setDuration(400);
			rotate3dAnimation4.setFillAfter(true);
			rotate3dAnimation3.setAnimationListener(new MyRotateListener(view,rotate3dAnimation4));
			
			
			Rotate3dAnimation  rotate3dAnimation5=new Rotate3dAnimation(30,-20, 0, 0, 0, 0);
			rotate3dAnimation5.setDuration(300);
			rotate3dAnimation5.setFillAfter(true);
			rotate3dAnimation4.setAnimationListener(new MyRotateListener(view,rotate3dAnimation5));
			
			
			Rotate3dAnimation  rotate3dAnimation6=new Rotate3dAnimation(-20,15, 0, 0, 0, 0);
			rotate3dAnimation6.setDuration(200);
			rotate3dAnimation6.setFillAfter(true);
			rotate3dAnimation5.setAnimationListener(new MyRotateListener(view,rotate3dAnimation6));
			
			Rotate3dAnimation  rotate3dAnimation7=new Rotate3dAnimation(15,0, 0, 0, 0, 0);
			rotate3dAnimation7.setDuration(50);
			rotate3dAnimation7.setFillAfter(true);
			rotate3dAnimation6.setAnimationListener(new MyRotateListener(view,rotate3dAnimation7));
			view.startAnimation(rotate3dAnimation);
		}

		private static class  MyRotateListener implements AnimationListener{
			private Rotate3dAnimation animationS;
			private  View view;
			public MyRotateListener(View view,Rotate3dAnimation animation) {
				this.animationS = animation;
				this.view=view;
				}
			@Override
			public void onAnimationEnd(Animation animation) {
				view.startAnimation(animationS);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationStart(Animation animation) {

			}
			
		}
}
