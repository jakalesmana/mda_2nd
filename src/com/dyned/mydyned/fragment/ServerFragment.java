package com.dyned.mydyned.fragment;

import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.dyned.mydyned.R;
import com.dyned.mydyned.composite.ServerListAdapter;
import com.dyned.mydyned.model.Server;
import com.dyned.mydyned.tools.InternetConnectionListener;
import com.dyned.mydyned.tools.InternetTask;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class ServerFragment extends SherlockFragment {

	private ListView lvServer;
	private PullToRefreshListView ptrLvServer;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_server, container, false);
		ptrLvServer = (PullToRefreshListView) view.findViewById(R.id.lvServer);
		lvServer = ptrLvServer.getRefreshableView();
		
		ptrLvServer.setMode(Mode.PULL_FROM_START);
		ptrLvServer.setOnRefreshListener(refreshHandler);
		
		refreshServerStatus();
		
		return view;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		refreshServerStatus();
	}
	
	private OnRefreshListener2<ListView> refreshHandler = new OnRefreshListener2<ListView>() {
		public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
			refreshServerStatus();
		}

		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {}
	};
	
	private void refreshServerStatus() {
		InternetTask task = new InternetTask(getSherlockActivity(), new InternetConnectionListener() {
			public void onStart() {
				ptrLvServer.setRefreshing();
			}
			
			public void onDone(String str) {
				System.out.println("response: " + str);
				List<Server> servers = Server.parseServer(str);
				lvServer.setAdapter(new ServerListAdapter(getSherlockActivity(), servers));
				ptrLvServer.onRefreshComplete();
			}

			@Override
			public void onConnectionError(String message) {
//				Toast.makeText(getSherlockActivity(), message + ", try again later.", Toast.LENGTH_SHORT).show();
				ptrLvServer.onRefreshComplete();
			}
		});
		task.execute("http://www.pistarlabs.net/parser/");
	}

}
