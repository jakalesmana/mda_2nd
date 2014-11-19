/*
 * Copyright (C) 2007-2010 Geometer Plus <contact@geometerplus.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301, USA.
 */

package com.dyned.mydyned.dev;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.dyned.mydyned.R;

public class BugReportActivity extends SherlockActivity {
	public static final String STACKTRACE = "mydyned.stacktrace";
	public static final String LOG = "mydyned.log";
	
	public static String tempLog = "";
	
//	private String getVersionName() {
//		try {
//			return getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
//		} catch (Exception e) {
//			return "";
//		}
//	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bug_report_view);
		final String stackTrace = getIntent().getStringExtra(STACKTRACE) + "\n\n" + tempLog;
		final TextView reportTextView = (TextView)findViewById(R.id.report_text);
		reportTextView.setMovementMethod(ScrollingMovementMethod.getInstance());
		reportTextView.setClickable(false);
		reportTextView.setLongClickable(false);

//		final String versionName = getVersionName();
		reportTextView.append("Unexpected error occurred. Please click the Send button below to inform us. " +
				"\n\nNOTE: This is for development purpose only. Please restart app manually after clicking Send button since the app has crashed and may cause abnormal behavior. Thank you.\n\n");
		reportTextView.append("Oops, unexpected error. Please click the Send button below to inform us. This is for development purpose only. Thank you.\n\n");
//		reportTextView.append(stackTrace);

		findViewById(R.id.send_report).setOnClickListener(
			new View.OnClickListener() {
				public void onClick(View view) {
					Intent sendIntent = new Intent(Intent.ACTION_SEND);
					sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { "jaka@pistarlabs.com", "christian@pistarlabs.com" });
					sendIntent.putExtra(Intent.EXTRA_TEXT, stackTrace);
					sendIntent.putExtra(Intent.EXTRA_SUBJECT, "MyDynEd Exception Captured");
					sendIntent.setType("message/rfc822");
					startActivity(sendIntent);
					finish();
				}
			}
		);

//		findViewById(R.id.cancel_report).setOnClickListener(
//			new View.OnClickListener() {
//				public void onClick(View view) {
//					finish();
//				}
//			}
//		);
	}
}
