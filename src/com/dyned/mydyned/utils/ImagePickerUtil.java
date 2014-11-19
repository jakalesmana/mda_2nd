package com.dyned.mydyned.utils;

import java.io.File;
import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;

public class ImagePickerUtil {
	
	public static final int PICK_FROM_CAMERA = 1;
	public static final int PICK_FROM_FILE = 2;
	public static final int CROP_IMAGE = 3;

	private static ImagePickerUtil instance;
	private static Fragment fragment;
	private static Activity act;
	private Uri mImageCaptureUri;
	private static String temp_pick_image;
	private static String temp_crop_image;

	private ImagePickerUtil() {
	}

	public static ImagePickerUtil GetInstance(Activity activity, Fragment fr) {
		if (instance == null) {
			instance = new ImagePickerUtil();
		}
		fragment = fr;
		act = activity;
		temp_pick_image = "vout_temp_pick_image" + System.currentTimeMillis() + ".jpg";
		temp_crop_image = "vout_temp_crop_image" + System.currentTimeMillis() + ".jpg";
		return instance;
	}
	
	public void startCameraPickerActivity(){
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		File file = new File(Environment.getExternalStorageDirectory(), temp_pick_image);
		mImageCaptureUri = Uri.fromFile(file);
		try {
			intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
			intent.putExtra("return-data", true);
			if (fragment != null) {
				fragment.startActivityForResult(intent, PICK_FROM_CAMERA);
			} else {
				act.startActivityForResult(intent, PICK_FROM_CAMERA);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void startSDCardPickerActivity() {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		if (fragment != null) {
			fragment.startActivityForResult(Intent.createChooser(intent, "Complete action using"), PICK_FROM_FILE);
		} else {
			act.startActivityForResult(Intent.createChooser(intent, "Complete action using"), PICK_FROM_FILE);
		}
	}
	
	public Bitmap getBitmapResult(int requestCode, int resultCode, Intent data, int imageWidth){
		System.out.println("request code: " + requestCode);
		if (resultCode != Activity.RESULT_OK)
			return null;

		Bitmap bitmap = null;
		
		if (requestCode == PICK_FROM_CAMERA || requestCode == PICK_FROM_FILE) {
			if (requestCode == PICK_FROM_FILE) {
				mImageCaptureUri = data.getData();
			}
			System.out.println("capture uri: " + mImageCaptureUri.getPath());
			startCropperActivity(mImageCaptureUri, imageWidth);
		    
		} else if (requestCode == CROP_IMAGE) {
			System.out.println("crop image result");
			bitmap = BitmapFactory.decodeFile(getCropTempUri().getPath());
						
			//delete temp file
			File f = new File(mImageCaptureUri.getPath());
	        if (f != null && f.exists()){
	        	f.delete();
	        }
	        
	        //delete temp crop file
	        File file = getCropTempFile();
	        if (file != null && file.exists()) {
				file.delete();
			}
		}

		if (bitmap != null) {
			return bitmap;
		}
		return null;
	}

	public void startCropperActivity(Uri imageUri, int imageWidth) {
		if(imageUri == null) return;
		
		mImageCaptureUri = imageUri;
		Intent intent = new Intent("com.android.camera.action.CROP");
		
	    intent.setType("image/*");
	    List<ResolveInfo> list;
	    if (fragment != null) {
	    	list = fragment.getActivity().getPackageManager().queryIntentActivities(intent, 0);
		} else {
			list = act.getPackageManager().queryIntentActivities(intent, 0);
		}
	    
	    int size = list.size();
	    if (size == 0) {      
	        return;
	    } else {
	        intent.setData(mImageCaptureUri);        
	        intent.putExtra("outputX", imageWidth);
	        intent.putExtra("outputY", imageWidth);
	        intent.putExtra("aspectX", 1);
	        intent.putExtra("aspectY", 1);
	        intent.putExtra("scale", true);
	        intent.putExtra(MediaStore.EXTRA_OUTPUT, getCropTempUri());
//	        intent.putExtra("return-data", true);
            Intent i = new Intent(intent);
            ResolveInfo res = list.get(0);
            i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            if (fragment != null) {
            	fragment.startActivityForResult(i, CROP_IMAGE);
			} else {
				act.startActivityForResult(i, CROP_IMAGE);
			}
	    }
	}
	
	private Uri getCropTempUri(){
		return Uri.fromFile(getCropTempFile());
	}
	
	private File getCropTempFile() {
	    if (isSDCARDMounted()) {
		    File f = new File(Environment.getExternalStorageDirectory(), temp_crop_image);
		    try {
		    	f.createNewFile();
		    } catch (IOException e) {
		    }
	    	return f;
	    } else {
	    	return null;
	    }
	}
	
	private boolean isSDCARDMounted(){
	   String status = Environment.getExternalStorageState();
	    if (status.equals(Environment.MEDIA_MOUNTED))
	    	return true;
	    return false;
	}
}
