package com.dyned.mydyned.component;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dyned.mydyned.R;
import com.dyned.mydyned.component.PinnedHeaderExpListView.PinnedHeaderAdapter;
import com.dyned.mydyned.model.School;
import com.dyned.mydyned.model.SchoolCountry;

public class MyExpandableListAdapter extends BaseExpandableListAdapter implements PinnedHeaderAdapter, OnScrollListener {
	
	private Context context;
	private int mPinnedHeaderBackgroundColor;
	private int mPinnedHeaderTextColor;
	private List<SchoolCountry> listSchool;
	private LayoutInflater li;
	
	public MyExpandableListAdapter(Context context, List<SchoolCountry> listSchool) {
		li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.context = context;
		this.listSchool = listSchool;
//		mPinnedHeaderBackgroundColor = 0xff66656b; //grey
		mPinnedHeaderBackgroundColor = 0xff00853e; //green
        mPinnedHeaderTextColor = context.getResources().getColor(android.R.color.white);
	}

    public School getChild(int groupPosition, int childPosition) {
//        return children[groupPosition][childPosition];
		return listSchool.get(groupPosition).getListSchool().get(childPosition);
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    public int getChildrenCount(int groupPosition) {
//        return children[groupPosition].length;
    	return listSchool.get(groupPosition).getListSchool().size();
    }

    public View getGenericView() {
        // Layout parameters for the ExpandableListView
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 64);

        TextView textView = new TextView(context);
        textView.setLayoutParams(lp);
        // Center the text vertically
        textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        // Set the text starting position
        textView.setPadding(36, 0, 0, 0);
        return textView;
    	
//    	View view = LayoutInflater.from(context).inflate(R.layout.school_row_item, null);
//    	return view;
    	
    }

    public static class ViewHolder {
		TextView title, subtitle;
	}
    
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        
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

		holder.title.setText(getChild(groupPosition, childPosition).getName());
		
		if (!getChild(groupPosition, childPosition).isEurope()) {
			if ((getChild(groupPosition, childPosition).getCity().equals(""))) {
				holder.subtitle.setVisibility(View.GONE);
			} else {
				holder.subtitle.setText("Branch: " + getChild(groupPosition, childPosition).getCity());
				holder.subtitle.setVisibility(View.VISIBLE);
			}
		} else {
			holder.subtitle.setText(getChild(groupPosition, childPosition).getWebsite());
			holder.subtitle.setVisibility(View.VISIBLE);
		}
		
		return convertView;
    }


    public Object getGroup(int groupPosition) {
//        return groups[groupPosition];
    	return listSchool.get(groupPosition).getCountry();
    }

    public int getGroupCount() {
//        return groups.length;
    	return listSchool.size();
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        LinearLayout layout = (LinearLayout) (LayoutInflater.from(context).inflate(R.layout.list_header_section, parent, false));
        TextView tv = (TextView)layout.findViewById(R.id.txtTitle);
        tv.setText(getGroup(groupPosition).toString());
        return layout;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public boolean hasStableIds() {
        return true;
    }

    public void configurePinnedHeader(View v, int position, int alpha) {
        TextView header = (TextView) v;
        final String title = (String) getGroup(position);

        header.setText(title);
        if (alpha == 255) {
            header.setBackgroundColor(mPinnedHeaderBackgroundColor);
            header.setTextColor(mPinnedHeaderTextColor);
        } else {
            header.setBackgroundColor(Color.argb(alpha, 
                    Color.red(mPinnedHeaderBackgroundColor),
                    Color.green(mPinnedHeaderBackgroundColor),
                    Color.blue(mPinnedHeaderBackgroundColor)));
            header.setTextColor(Color.argb(alpha, 
                    Color.red(mPinnedHeaderTextColor),
                    Color.green(mPinnedHeaderTextColor),
                    Color.blue(mPinnedHeaderTextColor)));
        }
    }

    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (view instanceof PinnedHeaderExpListView) {
            ((PinnedHeaderExpListView) view).configureHeaderView(firstVisibleItem);
        }

    }

    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }
}
