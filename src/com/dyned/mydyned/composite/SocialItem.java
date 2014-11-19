package com.dyned.mydyned.composite;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dyned.mydyned.R;

public class SocialItem extends LinearLayout {
	
	private View v;
	
	public SocialItem(Context context, String title, String url) {
		super(context);
		this.setOrientation(VERTICAL);
		
		v = inflate(context, R.layout.social_item, null);
		TextView txtTitle = (TextView)v.findViewById(R.id.txtTitle);
		txtTitle.setText(title);
		
		v.setTag(url);
		
		addView(v);
	}
	
	@Override
	public void setOnClickListener(OnClickListener l) {
		v.setOnClickListener(l);
	}
}
