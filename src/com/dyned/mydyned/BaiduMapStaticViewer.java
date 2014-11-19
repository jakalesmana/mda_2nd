package com.dyned.mydyned;

import android.os.Bundle;
import android.widget.ImageView;

import com.actionbarsherlock.app.SherlockActivity;
import com.dyned.mydyned.utils.ImageUtil;

public class BaiduMapStaticViewer extends SherlockActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_baidu_map_static_viewer);
		getSupportActionBar().hide();
		
		ImageView map = (ImageView) findViewById(R.id.imageViewer);
		map.setImageBitmap(ImageUtil.getTempBitmap());
	}
}
