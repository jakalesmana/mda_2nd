package com.dyned.mydyned.composite;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dyned.mydyned.R;
import com.dyned.mydyned.model.Server;

public class ServerListAdapter extends BaseAdapter {
	
	private LayoutInflater li;
	private List<Server> list;
	
	public ServerListAdapter(Context context, List<Server> list) {
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
		TextView title, message;
		ImageView status;
		int position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup viewGroup) {
		ViewHolder holder;

		if (convertView == null) {
			holder = new ViewHolder();

			convertView = li.inflate(R.layout.server_item, null);
			holder.title = (TextView) convertView.findViewById(R.id.txtServerName);
			holder.message = (TextView) convertView.findViewById(R.id.txtServerMessage);
			holder.status = (ImageView) convertView.findViewById(R.id.imgStatus);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.title.setText(((Server)list.get(position)).getName());
		holder.message.setText(((Server)list.get(position)).getMessage());
		if (((Server)list.get(position)).getStatus().equalsIgnoreCase("UP")) {
			holder.status.setImageResource(R.drawable.green_square);
		} else {
			holder.status.setImageResource(R.drawable.red_square);
		}
		
		return convertView;
	}
}
