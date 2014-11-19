package com.dyned.mydyned.component;

import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewAnimator;

import com.dyned.mydyned.R;
import com.dyned.mydyned.WebViewerActivity;
import com.dyned.mydyned.composite.SocialItem;
import com.dyned.mydyned.model.SocialMedia;

@SuppressLint("ViewConstructor")
public class SocialFlipperItem extends FrameLayout {

	private ViewAnimator animator;
	private TextView txtTitle;
	private ImageView imgIcon;
	private RelativeLayout layoutItems;
	private SocialMedia socmed;
	private FrameLayout layoutIcon;
	
	public SocialFlipperItem(Context context, SocialMedia socmed) {
		super(context);
		
		this.socmed = socmed;
		
		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.social_flip_item, null);
		
		animator = (ViewAnimator)v.findViewById(R.id.flipperSocial);
		txtTitle = (TextView)v.findViewById(R.id.txtSocialTitle);
		imgIcon = (ImageView)v.findViewById(R.id.imgIcon);
		layoutItems = (RelativeLayout)v.findViewById(R.id.layoutItem);
		layoutIcon = (FrameLayout)v.findViewById(R.id.layoutIcon);
		
		txtTitle.setText(socmed.getTitle());
		setColorAndIcon();
		
		//set items
		LinearLayout layoutSocial = (LinearLayout) v.findViewById(R.id.layoutSocial);
		for (int i = 0; i < socmed.getListItem().size(); i++) {
			SocialItem item = new SocialItem(context, socmed.getListItem().get(i).getShortcut(), socmed.getListItem().get(i).getUrl());
			item.setTag(socmed.getListItem().get(i).getUrl());
			item.setOnClickListener(new OnItemClick());
			layoutSocial.addView(item);
		}
		
		addView(v);
	}
	
	private void setColorAndIcon() {
		int color = 0xffff355b;
		int imgres = R.drawable.ic_social;
		
		if (socmed.getTitle().toLowerCase(Locale.getDefault()).contains("facebook")) {
			color = 0xff3b5998;
			imgres = R.drawable.ic_facebook;
		} else if(socmed.getTitle().toLowerCase(Locale.getDefault()).contains("twitter")){
			color = 0xff00aced;
			imgres = R.drawable.ic_twitter;
		} else if(socmed.getTitle().toLowerCase(Locale.getDefault()).contains("weibo")){
			color = 0xffdd4b39;
			imgres = R.drawable.ic_weibo;
		} else if(socmed.getTitle().toLowerCase(Locale.getDefault()).contains("wechat")){
			color = 0xff027f01;
			imgres = R.drawable.wechat;
		}
		
		txtTitle.setBackgroundColor(color);
		layoutItems.setBackgroundColor(color);
		layoutIcon.setBackgroundColor(color);
		imgIcon.setImageResource(imgres);
	}

	private class OnItemClick implements OnClickListener {
		public void onClick(View v) {
			Intent i = new Intent(getContext(), WebViewerActivity.class);
			i.putExtra("url_menu", "" + v.getTag());
			getContext().startActivity(i);
		}
	}
	
	public void setOnClickHandler(OnClickListener listener){
		animator.setOnClickListener(listener);
	}

}
