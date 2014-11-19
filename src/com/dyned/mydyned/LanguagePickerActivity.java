package com.dyned.mydyned;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.dyned.mydyned.composite.LanguageListAdapter;
import com.dyned.mydyned.model.SerializedNameValuePair;
import com.dyned.mydyned.utils.Setting;

public class LanguagePickerActivity extends BaseActivity {

	private ListView lvLang;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_language_picker);
		lvLang = (ListView)findViewById(R.id.lvLanguage);
		buildLangList();
		lvLang.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
				LanguageListAdapter adapter = (LanguageListAdapter)parent.getAdapter();
				adapter.clicked(pos);
				adapter.notifyDataSetChanged();
			}
		});
	}

	private void buildLangList() {
		List<SerializedNameValuePair> listLang = new ArrayList<SerializedNameValuePair>();
		listLang.add(new SerializedNameValuePair("ENG", "English"));
		listLang.add(new SerializedNameValuePair("CHI", "Chinese"));
		listLang.add(new SerializedNameValuePair("SPA", "Spanish"));
		listLang.add(new SerializedNameValuePair("TUR", "Turkish"));
		listLang.add(new SerializedNameValuePair("POR", "Portuguese"));
		listLang.add(new SerializedNameValuePair("JPN", "Japanese"));
		listLang.add(new SerializedNameValuePair("IND", "Indonesian"));
		listLang.add(new SerializedNameValuePair("CZR", "Czech Repulic"));
		listLang.add(new SerializedNameValuePair("ITA", "Italian"));
		listLang.add(new SerializedNameValuePair("OTH", "Other"));
		lvLang.setAdapter(new LanguageListAdapter(this, listLang, Setting.getInstance(this).getLangList()));
	}
}
