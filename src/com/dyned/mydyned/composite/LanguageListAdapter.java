package com.dyned.mydyned.composite;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dyned.mydyned.R;
import com.dyned.mydyned.model.SerializedNameValuePair;
import com.dyned.mydyned.utils.Setting;

public class LanguageListAdapter extends BaseAdapter {

	private LayoutInflater li;
	private List<SerializedNameValuePair> list;
	private List<SerializedNameValuePair> listSelectedLang;
	private Context ctx;
	
	public LanguageListAdapter(Context context, List<SerializedNameValuePair> list, List<SerializedNameValuePair> listAlreadySelectedFriend) {
		if (listAlreadySelectedFriend == null) {
			listSelectedLang = new ArrayList<SerializedNameValuePair>();
		} else {
			listSelectedLang = new ArrayList<SerializedNameValuePair>();
			for (int i = 0; i < listAlreadySelectedFriend.size(); i++) {
				listSelectedLang.add(listAlreadySelectedFriend.get(i));
			}
		}
		li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.list = list;
		this.ctx = context;
	}
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public void clicked(int position){
		if (contains(listSelectedLang, list.get(position))) {
			removeSelectedLangFromList(list.get(position).getName());
		} else {
			listSelectedLang.add(list.get(position));
		}
		Setting.getInstance(ctx).setLang(listSelectedLang);
	}

	public static class ViewHolder {
		ImageView imgToggle;
		TextView title;
		int position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup viewGroup) {
		ViewHolder holder;

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = li.inflate(R.layout.language_item, null);
			holder.title = (TextView) convertView.findViewById(R.id.txtLang);
			holder.imgToggle = (ImageView) convertView.findViewById(R.id.imgToggle);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		SerializedNameValuePair lang = list.get(position);
		holder.title.setText(lang.getValue());
		if (contains(listSelectedLang, lang)) {
			holder.imgToggle.setVisibility(View.VISIBLE);
		} else holder.imgToggle.setVisibility(View.GONE);
		return convertView;
	}
	
	private boolean contains(List<SerializedNameValuePair> listLang, SerializedNameValuePair lang){
		for (int i = 0; i < listLang.size(); i++) {
			if (listLang.get(i).getName().equals(lang.getName())) {
				return true;
			}
		}
		return false;
	}
	
	private void removeSelectedLangFromList(String name){
		for (int i = 0; i < listSelectedLang.size(); i++) {
			if (listSelectedLang.get(i).getName().equals(name)) {
				listSelectedLang.remove(i);
			}
		}
	}
}
