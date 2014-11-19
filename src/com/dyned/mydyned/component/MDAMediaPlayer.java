package com.dyned.mydyned.component;

import android.content.Context;
import android.media.MediaPlayer;

import com.dyned.mydyned.R;

public class MDAMediaPlayer {
	
	private static MDAMediaPlayer instance;
	private static MediaPlayer player;
	private static Context ctx;
	
	private MDAMediaPlayer() {}
	
	public static MDAMediaPlayer getInstance(Context cotx){
		if (instance == null) {
			instance = new MDAMediaPlayer();
			player = MediaPlayer.create(cotx, R.raw.asses_audio);
			ctx = cotx;
		}
		return instance;
	}
	
	public MediaPlayer getPlayer(){
		if (player == null) {
			player = MediaPlayer.create(ctx, R.raw.asses_audio);
		}
		return player;
	}
}
