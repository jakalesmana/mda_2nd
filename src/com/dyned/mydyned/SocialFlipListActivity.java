package com.dyned.mydyned;

import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ViewAnimator;

import com.dyned.mydyned.animation.AnimationFactory;
import com.dyned.mydyned.animation.AnimationFactory.FlipDirection;
import com.dyned.mydyned.component.SocialFlipperItem;
import com.dyned.mydyned.constant.URLAddress;
import com.dyned.mydyned.fragment.HomeFragment;
import com.dyned.mydyned.model.SocialMedia;
import com.dyned.mydyned.tools.InternetConnectionListener;
import com.dyned.mydyned.tools.PostInternetTask;

/**
author: jakalesmana
 */

public class SocialFlipListActivity extends BaseActivity {
	private Handler handler = new Handler();
//	private ViewAnimator flipperTwitter, flipperFacebook, flipperWeibo, flipperWechat;
	private ProgressDialog dialog;
	private LinearLayout layoutParent;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_flip_list_social);
		
		setTitle(HomeFragment.SOCIAL_MEDIA);
		
		layoutParent = (LinearLayout)findViewById(R.id.layoutSocialParent);
//		flipperTwitter = (ViewAnimator)this.findViewById(R.id.flipperTwitter);
//		flipperFacebook = (ViewAnimator)this.findViewById(R.id.flipperFacebook);
//		flipperWeibo = (ViewAnimator)this.findViewById(R.id.flipperWeibo);
//		flipperWechat = (ViewAnimator)this.findViewById(R.id.flipperWechat);
//		flipperTwitter.setOnClickListener(new OnFlipperClick());
//		flipperFacebook.setOnClickListener(new OnFlipperClick());
//		flipperWeibo.setOnClickListener(new OnFlipperClick());
//		flipperWechat.setOnClickListener(new OnFlipperClick());
//		
//		LinearLayout layoutTwitter = (LinearLayout)findViewById(R.id.layoutTwitter);
//		LinearLayout layoutFacebook = (LinearLayout)findViewById(R.id.layoutFacebook);
//		LinearLayout layoutWeibo = (LinearLayout)findViewById(R.id.layoutWeibo);
//		LinearLayout layoutWechat = (LinearLayout)findViewById(R.id.layoutWechat);
//		initTwitter(layoutTwitter);
//		initFacebook(layoutFacebook);
//		initWeibo(layoutWeibo);
//		initWechat(layoutWechat);
		
		retrieveSocialMediaList();
	}
	
	private void retrieveSocialMediaList() {
		PostInternetTask getTask = new PostInternetTask(this, new InternetConnectionListener() {
			public void onStart() {
				handler.post(new Runnable() {
					public void run() {
				    	dialog = ProgressDialog.show(SocialFlipListActivity.this, "", "Loading..");				
					}
				});
			}
			
			public void onDone(String str) {
				System.out.println("response socmed: " + str);
				
				List<SocialMedia> listSocmed = SocialMedia.ParseSocialMediaList(str);
				System.out.println("socmed count: " + listSocmed.size());
				
				for (int i = 0; i < listSocmed.size(); i++) {
					SocialFlipperItem flipper = new SocialFlipperItem(SocialFlipListActivity.this, listSocmed.get(i));
					flipper.setOnClickHandler(new OnFlipperClick());
					layoutParent.addView(flipper);
				}
				
				dialog.dismiss();
			}
			
			@Override
			public void onConnectionError(String message) {
				dialog.dismiss();
				Toast.makeText(SocialFlipListActivity.this, message + ", try again later.", Toast.LENGTH_SHORT).show();
			}
		});
		getTask.execute(URLAddress.SOCIAL_MEDIA_URL);
	}

//	private void initWechat(LinearLayout layoutWechat) {
//		SocialItem item = new SocialItem(this, "DynEd China", "http://weixin.qq.com/r/JHUpJhPEti3JhwJFnyAA");
//		item.setOnClickListener(new OnItemClick());
//		layoutWechat.addView(item);
//	}
//	
//	private void initWeibo(LinearLayout layoutWeibo) {
//		SocialItem item = new SocialItem(this, "DynEd China", "http://e.weibo.com/u/3174203551");
//		SocialItem item2 = new SocialItem(this, "Webi's Weibo", "http://weibo.com/webi1998");
//		item.setOnClickListener(new OnItemClick());
//		item2.setOnClickListener(new OnItemClick());
//		layoutWeibo.addView(item);
//		layoutWeibo.addView(item2);
//	}
//
//	private void initFacebook(LinearLayout layoutFacebook) {        
//		SocialItem item = new SocialItem(this, "DynEd Mobile", "http://www.facebook.com/dynedmobile");
//		SocialItem item2 = new SocialItem(this, "DynEd International Inc.", "http://www.facebook.com/DynEdIntlinc");
//		SocialItem item3 = new SocialItem(this, "DynEd LatAm", "http://www.facebook.com/dynedlatam");
//		SocialItem item4 = new SocialItem(this, "DynEd Europe BV", "http://www.facebook.com/pages/DynEd-Europe-BV/268286295303");
//		SocialItem item5 = new SocialItem(this, "DynEd Turkey", "http://www.facebook.com/pages/DynEd-Turkey/254734834610304");
//		SocialItem item6 = new SocialItem(this, "DynEd Srilanka", "http://www.facebook.com/dynedsl");
//		SocialItem item7 = new SocialItem(this, "PT. DynEd Indonesia", "http://www.facebook.com/dyned.indonesia");
//		item.setOnClickListener(new OnItemClick());
//		item2.setOnClickListener(new OnItemClick());
//		item3.setOnClickListener(new OnItemClick());
//		item4.setOnClickListener(new OnItemClick());
//		item5.setOnClickListener(new OnItemClick());
//		item6.setOnClickListener(new OnItemClick());
//		item7.setOnClickListener(new OnItemClick());
//		layoutFacebook.addView(item);
//		layoutFacebook.addView(item2);
//		layoutFacebook.addView(item3);
//		layoutFacebook.addView(item4);
//		layoutFacebook.addView(item5);
//		layoutFacebook.addView(item6);
//		layoutFacebook.addView(item7);
//	}
//
//	private void initTwitter(LinearLayout layoutTwitter) {
//		SocialItem item = new SocialItem(this, "DynEd Mobile", "https://twitter.com/dyned_mobile");
//		SocialItem item2 = new SocialItem(this, "DynEd Turkey", "https://twitter.com/DynEdTurkey");
//		SocialItem item3 = new SocialItem(this, "DynEd Indonesia", "https://twitter.com/dyned_indonesia");
//		item.setOnClickListener(new OnItemClick());
//		item2.setOnClickListener(new OnItemClick());
//		item3.setOnClickListener(new OnItemClick());
//		layoutTwitter.addView(item);
//		layoutTwitter.addView(item2);
//		layoutTwitter.addView(item3);
//	}

	class OnFlipperClick implements OnClickListener {
		public void onClick(View v) {
			AnimationFactory.flipTransition((ViewAnimator)v, FlipDirection.LEFT_RIGHT);
		}
	}
	
	class OnItemClick implements OnClickListener {
		public void onClick(View v) {
			Intent i = new Intent(SocialFlipListActivity.this, WebViewerActivity.class);
			i.putExtra("url_menu", "" + v.getTag());
			startActivity(i);
		}
	}
}
