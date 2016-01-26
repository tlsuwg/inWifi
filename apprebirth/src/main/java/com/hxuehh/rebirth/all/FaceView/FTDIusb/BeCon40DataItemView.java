/**
 * 
 */
package com.hxuehh.rebirth.all.FaceView.FTDIusb;


import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxuehh.rebirth.R;

/**
 * @author suwg
 * @data 2014-4-26
 */
@SuppressLint("NewApi")
public class BeCon40DataItemView extends LinearLayout {

	/**
	 * @param context
	 */
	public BeCon40DataItemView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView(context);

	}


	public BeCon40DataItemView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		initView(context);
	}


	public BeCon40DataItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	TextView show_loc_item_text;
	private byte number = 0;

	private void initView(Context context) {
		// TODO Auto-generated method stub
		this.inflate(context, R.layout.re315address_item_lin, this);
		show_loc_item_text = (TextView) findViewById(R.id.show_loc_item_text);
		show_loc_item_text.setText(  "？");
		
	}

	public byte getNumber() {
		return number;
	}
	
	public void setNumber(byte number) {
		this.number = number;
		show_loc_item_text.setText(number + "");
	}
	

}
