package com.dyned.mydyned.component;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.dyned.mydyned.R;
import com.dyned.mydyned.composite.AppAdapter;
import com.dyned.mydyned.manager.AppManager;
import com.dyned.mydyned.model.App;

public class AppPickerDialog extends Dialog {

	private List<App> listApp;
	private Context ctx;
	private PackageManager pm;
	
	public AppPickerDialog(final Context context, boolean fromInstalled) {
		super(context);
		this.ctx = context;
		pm = ctx.getPackageManager();
		
		requestWindowFeature((int) Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_app_picker);
		getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		
		ListView lvApp = (ListView)findViewById(R.id.lvApp);
		if (fromInstalled) {
			listApp = getAppList(ctx);
		} else {
			listApp = AppManager.getInstance().getApps();
		}
		
		lvApp.setAdapter(new AppAdapter(context, listApp));
		
		lvApp.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
				App app = listApp.get(pos);
//				if (app.getPackageName().equals("com.dyned.engine")) {
					//check if the app installed
					if(check(app.getPackageName())) {
						PackageManager pack = context.getPackageManager();
						Intent in = pack.getLaunchIntentForPackage(app.getPackageName());
						context.startActivity(in);
					} else {
						Intent marketIntent = new Intent(Intent.ACTION_VIEW);
						marketIntent.setData(Uri.parse(app.getMarketUrl()));
						context.startActivity(marketIntent);
					}
//				} else {
//					//already installed, just launch
//					PackageManager pack = context.getPackageManager();
//	                Intent i = pack.getLaunchIntentForPackage(app.getPackageName());
//	                context.startActivity(i);
//				}
				dismiss();
			}
		});
		
		if (listApp.size() == 0) {
			Toast.makeText(ctx, "No DynEd application installed.", Toast.LENGTH_SHORT).show();
			Handler h = new Handler();
			h.postDelayed(new Runnable() {
				public void run() {
					dismiss();
				}
			}, 1);
		}
	}
	
	private List<App> getAppList(Context ctx){
		List<App> apps = new ArrayList<App>();
//		apps.add(new App("com.dyned.engine", "DynEd Pro", "ic_launch_dyned"));
		for(App app : AppManager.getInstance().getApps()) {
//		    Log.d("PackageList", "package: " + app.packageName + ", name: " + pm.getApplicationLabel(app).toString());
		    if (isExist(app.getPackageName())) {
//				try {
					apps.add(getApp(app.getPackageName()));
//					apps.add(new App(app.packageName, pm.getApplicationLabel(app).toString(), pm.getApplicationIcon(app.packageName)));
//				} catch (NameNotFoundException e) {
//					apps.add(new App(app.packageName, pm.getApplicationLabel(app).toString(), "ic_ge", ""));
//				}
			}
		}
		
//		Collections.sort(apps, new Comparator<App>() {
//	        public int compare(App app1, App app2) {
//	            return app1.getAppName().compareToIgnoreCase(app2.getAppName());
//	        }
//	    });
		
		
//		apps.add(new App("qs.dn.ns", "General English 1", "ic_ge"));
//		apps.add(new App("qs.dn.ns2", "General English 2", "ic_ge"));
//		apps.add(new App("qs.dn.ns3", "General English 3", "ic_ge"));
//		apps.add(new App("qs.dn.ns4", "General English 4", "ic_ge"));
//		apps.add(new App("qs.dn.ns5", "General English 5", "ic_ge"));
//		apps.add(new App("qs.dn.ns6", "General English 6", "ic_ge"));
		
		return apps;
	}
	
	private boolean isExist(String pckg){
//		List<App> apps = AppManager.getInstance().getApps();
//		for (int i = 0; i < apps.size(); i++) {
//			if (apps.get(i).getPackageName().equals(pckg)) {
//				return true;
//			}
//		}
		for(ApplicationInfo app : pm.getInstalledApplications(0)) {
			if (app.packageName.equals(pckg)) {
				return true;
			}
		}
		return false;
	}
	
	private App getApp(String pckg){
		List<App> apps = AppManager.getInstance().getApps();
		for (int i = 0; i < apps.size(); i++) {
			if (apps.get(i).getPackageName().equals(pckg)) {
				return apps.get(i);
			}
		}
		return null;
	}
	
	public boolean check(String packageName)
	{
	    try{
	        ctx.getPackageManager().getApplicationInfo(packageName, 0 );
	        return true;
	    } catch( PackageManager.NameNotFoundException e ){
	        return false;
	    }
	}
	
	@Override
	public void show() {
		setCanceledOnTouchOutside(false);
		super.show();
	}
	

}
