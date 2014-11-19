package com.dyned.mydyned;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.actionbarsherlock.app.SherlockActivity;

public class VideoPlayerActivity extends SherlockActivity implements OnPreparedListener, 
	OnBufferingUpdateListener, OnErrorListener {
	
	private ProgressBar progressBar;
	private String url;
	private VideoView videoView;
	private MediaController mc;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.video_player);
		
		getSupportActionBar().hide();
		
		progressBar = (ProgressBar)findViewById(R.id.progressMovie);
		
		this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		progressBar.setVisibility(View.VISIBLE);
		
		url = getIntent().getStringExtra("url").replace(" ", "%20");
		videoView = (VideoView)findViewById(R.id.videoView);
		
		try {
			videoView.setOnPreparedListener(this);
			videoView.setOnErrorListener(this);
			
			mc = new MediaController(this);
			mc.setAnchorView(videoView);
			
			videoView.setMediaController(mc);
			Uri uri = Uri.parse(url);
			videoView.setVideoURI(uri);
			videoView.requestFocus();
			videoView.start();
		} catch (Exception e) {
			Toast.makeText(this, "Error while playing.", Toast.LENGTH_SHORT).show();
			finish();
		}
	}
	
	
	public void onPrepared(MediaPlayer mp) {
		progressBar.setVisibility(View.GONE);
	}

	public void onBufferingUpdate(MediaPlayer mp, int bufferedPercent) {
	}

	public boolean onError(MediaPlayer mp, int what, int extra) {
		Toast.makeText(this, "Error while playing.", Toast.LENGTH_SHORT).show();
		finish();
		return false;
	}
	
	@Override
	protected void onDestroy() {
		videoView.stopPlayback();
		mc = null;
		videoView = null;
		super.onDestroy();
	}
	
}
