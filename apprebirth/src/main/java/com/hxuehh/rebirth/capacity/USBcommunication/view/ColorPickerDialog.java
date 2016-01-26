package com.hxuehh.rebirth.capacity.USBcommunication.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

public class ColorPickerDialog extends Dialog {
	private final boolean debug = true;
	private final String TAG = "ColorPicker";
	
	Context context;
	private String title;//标题
	private int mInitialColor;//初始颜色
    private OnColorChangedListener mListener;

	/**
     * 初始颜色黑色
     * @param context
     * @param title 对话框标题
     * @param listener 回调
     */
    public ColorPickerDialog(Context context, String title, 
    		OnColorChangedListener listener) {
    	this(context, Color.BLACK, title, listener);
    }
    
    /**
     * 
     * @param context
     * @param initialColor 初始颜色
     * @param title 标题
     * @param listener 回调
     */
    public ColorPickerDialog(Context context, int initialColor, 
    		String title, OnColorChangedListener listener) {
        super(context);
        this.context = context;
        mListener = listener;
        mInitialColor = initialColor;
        this.title = title;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager manager = getWindow().getWindowManager();
		int height = (int) (manager.getDefaultDisplay().getHeight() * 0.5f);
		int width = (int) (manager.getDefaultDisplay().getWidth() * 0.7f);
		ColorPickerView myView = new ColorPickerView(context, height, width,null);
        setContentView(myView);
        setTitle(title);
    }
    

    /**
     * 回调接口
     * @author <a href="clarkamx@gmail.com">LynK</a>
     * 
     * Create on 2012-1-6 上午8:21:05
     *
     */

    
    public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getmInitialColor() {
		return mInitialColor;
	}

	public void setmInitialColor(int mInitialColor) {
		this.mInitialColor = mInitialColor;
	}

	public OnColorChangedListener getmListener() {
		return mListener;
	}

	public void setmListener(OnColorChangedListener mListener) {
		this.mListener = mListener;
	}
}
