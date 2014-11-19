package com.dyned.mydyned.utils;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

		private static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

		private final String imageUri;
		private final ImageView imageView;
		private final boolean rounded;
		private final int pixel;
		private final int borderWidth;

		public AnimateFirstDisplayListener(String imageUri, ImageView imageView, boolean rounded, int pixel, int borderWidth) {
			this.imageUri = imageUri;
			this.imageView = imageView;
			this.rounded = rounded;
			this.pixel = pixel;
			this.borderWidth = borderWidth;
		}

		@Override
		public void onLoadingComplete(Bitmap loadedImage) {
			if (loadedImage != null) {
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if(rounded)
					loadedImage = ImageUtil.getRoundedCornerBitmap(loadedImage, pixel, borderWidth);
				if (firstDisplay) {
					new FadeInBitmapDisplayer(500).display(loadedImage, imageView);
				} else {
					imageView.setImageBitmap(loadedImage);
				}
				displayedImages.add(imageUri);
			}
		}
	}