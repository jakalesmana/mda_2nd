package com.dyned.mydyned.component;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dyned.mydyned.R;
import com.dyned.mydyned.model.App;
import com.dyned.mydyned.utils.AnimateFirstDisplayListener;
import com.dyned.mydyned.utils.LocationUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class AppMenuItem extends LinearLayout {

	private DisplayImageOptions optionsIcon = new DisplayImageOptions.Builder()
	.showStubImage(R.drawable.ic_launch_dyned)
	.showImageForEmptyUri(R.drawable.ic_launch_dyned).cacheOnDisc().build();
	
	public AppMenuItem(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public AppMenuItem(final Context context, final App app) {
		super(context);
		setOrientation(LinearLayout.VERTICAL);
		
		LayoutInflater li = LayoutInflater.from(context);
		View v = li.inflate(R.layout.dialog_app_picker_item, null);
		ImageView img = (ImageView)v.findViewById(R.id.imgApp);
		TextView txtTitle = (TextView)v.findViewById(R.id.txtTitle);
		txtTitle.setTextColor(Color.WHITE);
		v.setBackgroundResource(R.drawable.transparent_selectable_bg);
		
//		int resID = getContext().getResources().getIdentifier(app.getAppIcon(), "drawable", context.getPackageName());
//		img.setImageResource(resID);
		
		Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launch_dyned);
		img.getLayoutParams().width = bmp.getWidth();
		img.getLayoutParams().height = bmp.getWidth();
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.displayImage(app.getAppIcon(), img,
		optionsIcon, new AnimateFirstDisplayListener(app.getAppIcon(),img, false, 0, 0));
		
		
		txtTitle.setText(app.getAppName());
	
		FrameLayout divider = new FrameLayout(context);
		divider.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		divider.setBackgroundResource(R.drawable.divider);
		
		v.setOnClickListener(new OnClickListener() {
			@SuppressLint("DefaultLocale")
			public void onClick(View view) {
//				//already installed, just launch
//				PackageManager pack = context.getPackageManager();
//                Intent i = pack.getLaunchIntentForPackage(appPackage);
//                context.startActivity(i);
				
				if(check(app.getPackageName())) {
					PackageManager pack = context.getPackageManager();
					Intent in = pack.getLaunchIntentForPackage(app.getPackageName());
					context.startActivity(in);
				} else {
					
					String globalCode = LocationUtil.getCountryCode();
					String baiduCode = LocationUtil.getBDCountryCode();
					String codeByIP = LocationUtil.getCountryCodeByIP();
					if (globalCode.toLowerCase().equals("cn") || codeByIP.toLowerCase().equals("cn") ||
							baiduCode.toLowerCase().equals("cn") || !LocationUtil.isPingGoogleSucceded()) {
						if (app.getDirectLink().equals("")) {
							Toast.makeText(context, "No application link found.", Toast.LENGTH_SHORT).show();
						} else {
							showConfirmation(app.getAppName(), app.getDirectLink());
						}
					} else {
						Intent marketIntent = new Intent(Intent.ACTION_VIEW);
						marketIntent.setData(Uri.parse(app.getMarketUrl()));
						context.startActivity(marketIntent);
					}
				}
			}
		});
		
		addView(v);
		addView(divider);
	}
	
	private void showConfirmation(String name, final String directLink) {
		final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
		alertDialog.setTitle(name);
		alertDialog.setMessage("Would you like to download " + name + "?");
						
		alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				alertDialog.dismiss();
			}
		});
		
		alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {	
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(directLink));
				getContext().startActivity(browserIntent);
			}
		});
		
		alertDialog.show();
	}
	
	public boolean check(String packageName)
	{
	    try{
	        getContext().getPackageManager().getApplicationInfo(packageName, 0 );
	        return true;
	    } catch( PackageManager.NameNotFoundException e ){
	        return false;
	    }
	}

}
