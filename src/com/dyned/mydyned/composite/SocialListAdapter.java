package com.dyned.mydyned.composite;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dyned.mydyned.R;
import com.dyned.mydyned.model.Social;

public class SocialListAdapter extends BaseAdapter {

	private final int TYPE_HEADER = 0;
	private final int TYPE_ITEM = 1;
	
	private LayoutInflater li;
	private List<?> list;
	
	public SocialListAdapter(Context context, List<?> list) {
		li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.list = list;
	}

	@Override
	public int getItemViewType(int position) {
		return (list.get(position) instanceof Social) ? TYPE_ITEM : TYPE_HEADER;
	}
	
	@Override
	public int getCount() {
		return list.size();
	}
	
	@Override
	public int getViewTypeCount() {
		return 2;
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
		TextView title;
		int position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup viewGroup) {
		ViewHolder holder;
		int type = getItemViewType(position);

		if (convertView == null) {
			holder = new ViewHolder();
			switch (type) {
			case TYPE_HEADER:
				convertView = li.inflate(R.layout.list_header_section, null);
				holder.title = (TextView) convertView.findViewById(R.id.txtTitle);
				break;
			case TYPE_ITEM:
				convertView = li.inflate(R.layout.social_row_item, null);
				holder.title = (TextView) convertView.findViewById(R.id.txtTitle);
				break;
			}
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		switch (type) {
		case TYPE_HEADER:
			holder.title.setText((String)list.get(position));
			break;
		case TYPE_ITEM:
			holder.title.setText(((Social)list.get(position)).getLabel());
			break;
		}
		
		return convertView;
	}
}
