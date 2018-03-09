package com.yue.ar.coffee;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.util.Log;
import android.view.SurfaceView;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.yue.ar.coffee.camera.AndroidCameraController;
import com.yue.ar.coffee.vision.GrayTask;
import com.yue.ar.suite.R;

import java.util.ArrayDeque;

import boofcv.android.ConvertBitmap;
import boofcv.struct.image.GrayU8;


public class AndroidLauncher extends AndroidApplication {

	private int origWidth;
	private int origHeight;
	private String TAG = "AndroidLauncher";
	CameraControl cameraControl = null;  // 為了 取得 callback

    public static ArrayDeque<Bitmap> markers = null;
	GrayTask grayTask = null;
	boolean isMarker = false;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 設定手機螢幕垂直顯示
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		initMarker();
		grayTask = new GrayTask(isMarker);
		grayTask.run();

		// 設定 Android app
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration()
				;
		config.r = 8 ;
		config.g = 8 ;
		config.b = 8 ;
		config.a = 8 ;
		config.hideStatusBar = true;

		cameraControl = new AndroidCameraController(this);

		initialize(new com.yue.ar.coffee.Deck(cameraControl), config);


		if(graphics.getView() instanceof SurfaceView)
		{
			SurfaceView glView = (SurfaceView) graphics.getView();

			glView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
		}

		// 設定 螢幕不會關閉
		graphics.getView().setKeepScreenOn(true);
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG,"onResume");

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.r = 8;
		config.g = 8;
		config.b = 8;
		config.a = 8;

		CameraControl cameraControl = new AndroidCameraController(this);

		initialize(new com.yue.ar.coffee.Deck(cameraControl),config);

		if(graphics.getView() instanceof SurfaceView){
			SurfaceView glView = (SurfaceView) graphics.getView();

			glView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
		}

		graphics.getView().setKeepScreenOn(true);

		origHeight = graphics.getHeight();
		origWidth = graphics.getWidth();

	}


	public void restoreFixedSize() {
		if (graphics.getView() instanceof SurfaceView) {
			SurfaceView glView = (SurfaceView) graphics.getView();
			glView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
			glView.getHolder().setFixedSize(origWidth, origHeight);
		}
	}

	public void post(Runnable r) {
		handler.post(r);
	}

	private void initMarker(){
		isMarker = true;
		markers = new ArrayDeque<>();
		BitmapDrawable drawable = (BitmapDrawable) getResources().getDrawable(R.drawable.coffee);
		Bitmap bitmap = drawable.getBitmap();
		markers.addLast(bitmap);

	}

}
