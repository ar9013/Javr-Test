package com.yue.ar.coffee;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import android.util.Log;
import android.view.SurfaceView;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.yue.ar.coffee.camera.AndroidCameraController;
import com.yue.ar.coffee.vision.ARFilter;
import com.yue.ar.coffee.vision.ImageDetectionFilter;
import com.yue.ar.suite.R;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.LinkedList;


public class AndroidLauncher extends AndroidApplication {

	private int origWidth;
	private int origHeight;
	private String TAG = "AndroidLauncher";
	CameraControl cameraControl = null;  // 為了 取得 callback

    public static LinkedList<ARFilter> markerList = null;

	// opencv BaseLoader
	BaseLoaderCallback loaderCallback = new BaseLoaderCallback(this) {
		@Override
		public void onManagerConnected(int status) {
			switch(status){
				case LoaderCallbackInterface.SUCCESS:
				{
					Log.i(TAG,"OpenCV loaded successfully");
					markerList = new LinkedList<>();

					final ARFilter bird ;
					try {
						bird = new ARFilter(AndroidLauncher.this,R.drawable.bird,"bird");
						markerList.addLast(bird);
					}catch (IOException ex) {
						ex.printStackTrace();
					}

					final ARFilter butterflies;
					try {
						butterflies = new ARFilter(AndroidLauncher.this,R.drawable.fireworks,"butterfly");
						markerList.addLast(butterflies);
					}catch (IOException ex) {
						ex.printStackTrace();
					}

					final ARFilter coffee;
					try {
						coffee = new ARFilter(AndroidLauncher.this,R.drawable.coffee,"coffee");
						markerList.addLast(coffee);
					}catch (IOException ex) {
						ex.printStackTrace();
					}

					final  ARFilter dinosaur;
					try {
						dinosaur = new ARFilter(AndroidLauncher.this,R.drawable.rocket,"dinosaur");
						markerList.addLast(dinosaur);
					}catch (IOException ex) {
					ex.printStackTrace();
					}

					Log.d(TAG,"markers size : "+markerList.size());
				}
				break;

				default:
					super.onManagerConnected(status);
					break;
			}
		}
	};


	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 設定手機螢幕垂直顯示
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		// 設定 Android app
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration()
				;
		config.r = 8 ;
		config.g = 8 ;
		config.b = 8 ;
		config.a = 8 ;
		config.hideStatusBar = true;

		cameraControl = new AndroidCameraController(this);

		initialize(new com.yue.ar.coffee.App(cameraControl), config);


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

		if(!OpenCVLoader.initDebug()){
			Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
			OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_1_0, this, loaderCallback);
		}else{
			Log.d(TAG, "OpenCV library found inside package. Using it!");
			loaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
		}

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.r = 8;
		config.g = 8;
		config.b = 8;
		config.a = 8;

		CameraControl cameraControl = new AndroidCameraController(this);

		initialize(new com.yue.ar.coffee.App(cameraControl),config);

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


}
