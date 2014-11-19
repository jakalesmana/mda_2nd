package com.dyned.mydyned.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.dyned.mydyned.LanguagePickerActivity;
import com.dyned.mydyned.R;
import com.dyned.mydyned.utils.Setting;

public class SettingFragment extends SherlockFragment {
	
	private TextView txtMap;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.setting_fragment, container, false);
		
		//language setting
		FrameLayout layoutLanguage = (FrameLayout)view.findViewById(R.id.layoutLanguage);
		layoutLanguage.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {
				Intent i = new Intent(getSherlockActivity(), LanguagePickerActivity.class);
				startActivity(i);
			}
		});
		
		//map setting
		LinearLayout layoutMap = (LinearLayout)view.findViewById(R.id.layoutMap);
		layoutMap.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {
				showMapChooser();
			}
			
		});
		
		txtMap = (TextView)view.findViewById(R.id.txtMap);
		
		if (Setting.getInstance(getSherlockActivity()).getMap().equals("")) {
			txtMap.setVisibility(View.GONE);
		} else {
			txtMap.setVisibility(View.VISIBLE);
			txtMap.setText(Setting.getInstance(getSherlockActivity()).getMap().equals("google") ? "Google Map" : "Baidu Map");
		}
		
		return view;
	}
	
	private void showMapChooser() {
		final String[] items = new String[] { "Google Map", "Baidu Map" };
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getSherlockActivity(), android.R.layout.select_dialog_item, items);
		AlertDialog.Builder builder = new AlertDialog.Builder(getSherlockActivity());

		builder.setTitle("Choose default map that work best in your location:");
		builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				if (item == 0) {//google
					Setting.getInstance(getSherlockActivity()).setDefaultMap("google");
					txtMap.setText("Google Map");
				} else if (item == 1) {//baidu
					Setting.getInstance(getSherlockActivity()).setDefaultMap("baidu");
					txtMap.setText("Baidu Map");
				} 
				txtMap.setVisibility(View.VISIBLE);
			}
		});
		builder.show();
	}
}
