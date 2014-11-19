package com.dyned.mydyned;
//package com.pistarlabs.android.mydyned;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.ListView;
//
//import com.pistarlabs.android.mydyned.composite.SocialListAdapter;
//import com.pistarlabs.android.mydyned.model.Social;
//
///**
//author: jakalesmana
// */
//
//public class SocialListActivity extends BaseActivity {
//
//	private List<Object> listSocial;
//	
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_list_social);
//		
//		initListSocial();
//		
//		ListView lvSocial = (ListView)findViewById(R.id.lvSocial);
//		lvSocial.setAdapter(new SocialListAdapter(this, listSocial));
//		lvSocial.setOnItemClickListener(new OnItemClickListener() {
//			public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
//				if (parent.getAdapter().getItem(pos) instanceof Social) {
//					Social social = (Social)parent.getAdapter().getItem(pos);
//					Intent i = new Intent(SocialListActivity.this, WebViewerActivity.class);
//					i.putExtra("url_menu", social.getUrl());
//					startActivity(i);
//				}
//			}
//		});
//	}
//
//	private void initListSocial() {
//		listSocial = new ArrayList<Object>();
//		listSocial.add("Twitter");
//		listSocial.add(new Social(Social.TYPE_TWITTER, "DynEd Turkey", "https://twitter.com/DynEdTurkey"));
//		listSocial.add(new Social(Social.TYPE_TWITTER, "DynEd Indonesia", "https://twitter.com/dyned_indonesia"));
//		listSocial.add(new Social(Social.TYPE_TWITTER, "DynEd Mobile", "https://twitter.com/dyned_mobile"));
//		listSocial.add("Facebook");
//		listSocial.add(new Social(Social.TYPE_FACEBOOK, "DynEd International Inc.", "http://www.facebook.com/DynEdIntlinc"));
//		listSocial.add(new Social(Social.TYPE_FACEBOOK, "DynEd LatAm", "http://www.facebook.com/dynedlatam"));
//		listSocial.add(new Social(Social.TYPE_FACEBOOK, "DynEd Mobile", "http://www.facebook.com/dynedmobile"));
//		listSocial.add(new Social(Social.TYPE_FACEBOOK, "DynEd User in Srilanka", "http://www.facebook.com/dynedsl"));
//		listSocial.add(new Social(Social.TYPE_FACEBOOK, "DynEd Turkey", "http://www.facebook.com/pages/DynEd-Turkey/254734834610304"));
//		listSocial.add(new Social(Social.TYPE_FACEBOOK, "DynEd Europe BV", "http://www.facebook.com/pages/DynEd-Europe-BV/268286295303"));
//		listSocial.add(new Social(Social.TYPE_FACEBOOK, "PT. DynEd Indonesia", "http://www.facebook.com/dyned.indonesia"));
//		listSocial.add("Weibo");
//		listSocial.add(new Social(Social.TYPE_WEIBO, "DynEd China", "http://e.weibo.com/u/3174203551"));
//	}
//}
