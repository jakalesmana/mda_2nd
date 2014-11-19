package com.dyned.mydyned;

import java.io.InputStream;
import java.util.List;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.View;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.dyned.mydyned.composite.MediaMenuAdapter;
import com.dyned.mydyned.constant.URLAddress;
import com.dyned.mydyned.model.MediaMenu;
import com.dyned.mydyned.tools.InternetConnectionListener;
import com.dyned.mydyned.tools.InternetTask;
import com.dyned.mydyned.utils.PreferencesUtil;

/**
author: jakalesmana
 */

public class MediaActivity extends BaseFragmentActivity {
		
	private Handler handler = new Handler();
	private ProgressDialog dialog;
	
	private ListView lvMediaMenu;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_media_fragment);
		setTitle("Media");
		initListView();
		
		if (!PreferencesUtil.getInstance(this).getMediaHint()) {
			showShareGuide();
		} else {
//			retrieveMediaMenuList(Role.getRoleIdByString(PreferencesUtil.getInstance(this).getRoleKey()));
			retrieveMediaMenuList(PreferencesUtil.getInstance(this).getRoleKey());
		}
	};
	
	private void initListView() {
		lvMediaMenu = (ListView) findViewById(R.id.lvMediaMenu);
		InputStream is = getResources().openRawResource(R.drawable.ic_studyguide);
		Bitmap bmp = BitmapFactory.decodeStream(is);
		
		int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, getResources().getDisplayMetrics());
		int width = bmp.getWidth() + (padding * 2);
		bmp.recycle();
		
		lvMediaMenu.getLayoutParams().width = width;
		lvMediaMenu.getLayoutParams().height = LayoutParams.MATCH_PARENT;
		
		lvMediaMenu.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
				MediaMenuAdapter adapter = (MediaMenuAdapter) parent.getAdapter();
				MediaMenu item = adapter.getItem(pos);
				if (!item.isSelected()) {
					switchMediaList(item.getName(), item.getCode());
					adapter.setSelected(pos);
				}
				
			}
		});
	}

	private void showShareGuide() {
		new AlertDialog.Builder(this)
	    .setTitle("Hint")
	    .setIcon(android.R.drawable.ic_dialog_info)
	    .setMessage("Long click to share media.")
	    .setNeutralButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				retrieveMediaMenuList(PreferencesUtil.getInstance(MediaActivity.this).getRoleKey());
			}
		})
	     .show();
		PreferencesUtil.getInstance(this).setMediaHint(true);
	}
	
	private void switchMediaList(String title, String type){
		MediaListActivity fragment = new MediaListActivity();
		Bundle b = new Bundle();
		b.putString("title", title);
		b.putSerializable("type", type);
		fragment.setArguments(b);
		getSupportFragmentManager().beginTransaction().replace(R.id.layoutMedia, fragment).commit();
	}
	
	private void retrieveMediaMenuList(String roleKey){
		InternetTask postTask = new InternetTask(this, new InternetConnectionListener() {
			public void onStart() {
				handler.post(new Runnable() {
					public void run() {
				    	dialog = ProgressDialog.show(MediaActivity.this, "", "Loading..");				
					}
				});
			}
			
			public void onDone(String str) {
				System.out.println("response: " + str);
				List<MediaMenu> list = MediaMenu.parseMediaMenuList(str);
				
				if (list != null && list.size() > 0) {
					lvMediaMenu.setAdapter(new MediaMenuAdapter(MediaActivity.this, list));
					list.get(0).setSelected(true);
					switchMediaList(list.get(0).getName(), list.get(0).getCode());
				}
				
				dialog.dismiss();
			}

			@Override
			public void onConnectionError(String message) {
				dialog.dismiss();
				Toast.makeText(MediaActivity.this, message + ", try again later.", Toast.LENGTH_SHORT).show();
				finish();
			}
		});
		postTask.execute(URLAddress.MEDIA_MENU + "?id=" + roleKey);
	}
}
