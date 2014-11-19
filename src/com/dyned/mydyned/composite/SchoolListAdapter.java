package com.dyned.mydyned.composite;

import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dyned.mydyned.R;
import com.dyned.mydyned.model.School;

public class SchoolListAdapter extends BaseAdapter implements StickyListHeadersAdapter {
	
	private LayoutInflater li;
	private List<School> list;
	
	public SchoolListAdapter(Context context, List<School> list) {
		li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.list = list;
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

	public static class ViewHolder {
		TextView title, subtitle;
		int position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup viewGroup) {
		ViewHolder holder;

		if (convertView == null) {
			holder = new ViewHolder();

			convertView = li.inflate(R.layout.school_row_item, null);
			holder.title = (TextView) convertView.findViewById(R.id.txtTitle);
			holder.subtitle = (TextView) convertView.findViewById(R.id.txtSubtitle);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.title.setText(((School)list.get(position)).getName());
		if (((School)list.get(position)).getCity().equals("")) {
			holder.subtitle.setVisibility(View.GONE);
		} else {
			holder.subtitle.setText("Branch: " + ((School)list.get(position)).getCity());
			holder.subtitle.setVisibility(View.VISIBLE);
		}
		
		return convertView;
	}

	public static class HeaderViewHolder {
		TextView title;
		int position;
	}
	
	@Override
	public View getHeaderView(int position, View convertView, ViewGroup parent) {
		HeaderViewHolder holder;

		if (convertView == null) {
			holder = new HeaderViewHolder();
			convertView = li.inflate(R.layout.list_header_section, null);
			holder.title = (TextView) convertView.findViewById(R.id.txtTitle);
			convertView.setTag(holder);
		} else {
			holder = (HeaderViewHolder) convertView.getTag();
		}
		
		holder.title.setText(((School) list.get(position)).getCountryName());
				
		return convertView;
	}

	@Override
	public long getHeaderId(int position) {
		long id = 0;
		School school = (School) list.get(position);
		for (int i = 0; i < school.getCountryCode().length(); i++) {
			id += school.getCountryCode().charAt(i);
		}
		
		return id;
	}
}
