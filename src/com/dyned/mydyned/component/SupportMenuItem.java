package com.dyned.mydyned.component;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.dyned.mydyned.R;
import com.dyned.mydyned.model.SupportItem;

@SuppressLint("ViewConstructor")
public class SupportMenuItem extends FrameLayout {

	private View v;

	public SupportMenuItem(Context context, SupportItem item, int color) {
		super(context);
		
		int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, context.getResources().getDisplayMetrics());
		
		LayoutInflater inflater = LayoutInflater.from(context);
		v = inflater.inflate(R.layout.support_item, null);
		v.setTag(item);
		v.setBackgroundResource(color);
		TextView txtTitle = (TextView) v.findViewById(R.id.txtTitle);
		txtTitle.setText(item.getTitle());
		
		addView(v);
		setPadding(padding, padding, padding, padding);
	}
	
	public void setOnClickHandler(OnClickListener listener){
		v.setOnClickListener(listener);
	}
	
}
