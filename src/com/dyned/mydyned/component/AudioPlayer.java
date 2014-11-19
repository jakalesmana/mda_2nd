package com.dyned.mydyned.component;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.dyned.mydyned.R;

public class AudioPlayer extends FrameLayout {

	private View v;
	private MediaPlayer player;
	private Handler seekHandler = new Handler();
	private SeekBar bar;
	private boolean playing, shouldUpdate;
	private ImageView btnMedia;
	private TextView txtCurrent, txtTotal;
//	private Context ctx;
	private int durationTotalSecond;
	
	public AudioPlayer(Context context) {
		super(context);
		setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
//		this.ctx = context;
		
		LayoutInflater i = LayoutInflater.from(context);
		v = i.inflate(R.layout.layout_audio, null);
		
		init();
		seekUpdation();
		addView(v);
	}
	
	public void init() {
		System.out.println("audio player init");
		txtCurrent = (TextView)v.findViewById(R.id.txtCurrent);
		txtTotal = (TextView)v.findViewById(R.id.txtTotal);
		
		btnMedia = (ImageView)v.findViewById(R.id.imgPlay);
		bar = (SeekBar)v.findViewById(R.id.barAudio);
//		player = MDAMediaPlayer.getInstance(ctx).getPlayer();
		player = MediaPlayer.create(getContext(), R.raw.asses_audio);
		try {
			player.prepare();
		} catch (Exception e) {
			e.printStackTrace();
//			player.stop();
//			player.release();
//			player = MediaPlayer.create(getContext(), R.raw.asses_audio);
		}
		durationTotalSecond = Integer.parseInt(getTimeString(player.getDuration()).split(":")[1]);
		player.setOnCompletionListener(new OnCompletionListener() {			
			public void onCompletion(MediaPlayer mp) {
				playing = false;
				btnMedia.setImageResource(android.R.drawable.ic_media_play);
				shouldUpdate = false;
				bar.setProgress(0);
			}
		});
		
		bar.setMax(player.getDuration());
		bar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			public void onStopTrackingTouch(SeekBar bar) {
				player.seekTo(bar.getProgress());
				if (playing) {
					player.start();
				} else {
					player.pause();
				}
				shouldUpdate = true;
				seekUpdation();
			}
			
			public void onStartTrackingTouch(SeekBar bar) {
				player.pause();
				shouldUpdate = false;
			}
			
			public void onProgressChanged(SeekBar seekbar, int progress, boolean fromUser) {
				System.out.println("progress: " + progress + " and " + (player.getDuration() - progress));
				txtCurrent.setText(getTimeString(progress));
				txtTotal.setText("-" + getTimeString(player.getDuration() - progress));
			}
		});
		
		btnMedia.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {
				if (playing) {
					System.out.println("audio player pause");
					player.pause();
					playing = false;
					btnMedia.setImageResource(android.R.drawable.ic_media_play);
				} else {
					System.out.println("audio player play");
					shouldUpdate = true;
					player.start();
					playing = true;
					btnMedia.setImageResource(android.R.drawable.ic_media_pause);
					seekUpdation();
				}
			}
		});
	}
	
	Runnable run = new Runnable() {
		public void run() {
			if (shouldUpdate) {
				seekUpdation();
			}
		}
	};

	public void seekUpdation() {
		int pos = 0;
//		int dur = 0;
		try {
			pos = player.getCurrentPosition();
//			dur = player.getDuration();
		} catch (Exception e) {
			pos = 0;
//			dur = 0;
		}
		bar.setProgress(pos);
		txtCurrent.setText(getTimeString(pos));
		try {
			txtTotal.setText("-" + getTimeString(player.getDuration() - pos).split(":")[0] + ":" + getToGoSecondString(txtCurrent.getText().toString()));
		} catch (Exception e) {
			txtTotal.setText("");
		}
		seekHandler.postDelayed(run, 100);
	}

	private String getToGoSecondString(String current) {
		int togo = durationTotalSecond - ((Integer.parseInt(current.split(":")[1])));
		String res = "" + togo;
		if (("" + togo).length() < 2) {
			res = "0" + togo;
		}
		return res;
	}

	public void forceStop(){
		seekHandler.removeCallbacksAndMessages(run);
		bar.setProgress(0);
		playing = false;
		btnMedia.setImageResource(android.R.drawable.ic_media_play);
		if (player != null) {
			player.stop();
			player.reset();
			player.release();
			player = null;
		}
	}
	
	private String getTimeString(long millis) {
	    StringBuffer buf = new StringBuffer();

//	    int hours = (int) (millis / (1000 * 60 * 60));
	    int minutes = (int) ((millis % (1000 * 60 * 60)) / (1000 * 60));
	    int seconds = (int) (((millis % (1000 * 60 * 60)) % (1000 * 60)) / 1000);

	    buf
//	        .append(String.format("%02d", hours))
//	        .append(":")
	        .append(String.format("%02d", minutes))
	        .append(":")
	        .append(String.format("%02d", seconds));

	    return buf.toString();
	}

	public void reset() {
		init();
	}
}
