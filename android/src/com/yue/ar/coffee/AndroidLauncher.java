package com.yue.ar.coffee;

import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.os.Bundle;

import android.util.Log;
import android.view.SurfaceView;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.yue.ar.coffee.camera.AndroidCameraController;
import com.yue.ar.coffee.vision.ARFilter;

import com.yue.ar.suite.R;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import java.io.IOException;
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

					final ARFilter chiayi ;
					try {
						chiayi = new ARFilter(AndroidLauncher.this,R.drawable.chiayi,"chiayi",getContext().getString(R.string.chiayi_title),getContext().getString(R.string.chiayi_des));
						markerList.addLast(chiayi);
					}catch (IOException ex) {
						ex.printStackTrace();
					}

					final ARFilter chiayi_park;
					try {
						chiayi_park = new ARFilter(AndroidLauncher.this,R.drawable.chiayi_park,"chiayi_park",getContext().getString(R.string.chiayi_park_title),getContext().getString(R.string.chiayi_park_des));
						markerList.addLast(chiayi_park);
					}catch (IOException ex) {
						ex.printStackTrace();
					}

					final ARFilter clean;
					try {
						clean = new ARFilter(AndroidLauncher.this,R.drawable.clean,"clean",getContext().getString(R.string.clean_title),getContext().getString(R.string.clean_des));
						markerList.addLast(clean);
					}catch (IOException ex) {
						ex.printStackTrace();
					}

					final  ARFilter school;
					try {
						school = new ARFilter(AndroidLauncher.this,R.drawable.school,"school",getContext().getString(R.string.school_title),getContext().getString(R.string.school_des));
						markerList.addLast(school);
					}catch (IOException ex) {
					ex.printStackTrace();
					}

					final ARFilter shanghai;
					try{
					    shanghai = new ARFilter(AndroidLauncher.this,R.drawable.shanghai,"shanghai",getContext().getString(R.string.shanghai_title),getContext().getString(R.string.shanghai_des));
					    markerList.addLast(shanghai);
                    }catch (IOException ex){
					    ex.printStackTrace();
                    }

                    final ARFilter summer_street;
					try{
						summer_street = new ARFilter(AndroidLauncher.this,R.drawable.summer_street,"summer_street",getContext().getString(R.string.summer_street_title),getContext().getString(R.string.shanghai_des));
                        markerList.addLast(summer_street);
                    }catch (IOException ex){
					    ex.printStackTrace();
                    }

                    final  ARFilter water_pool;
					try{
					    water_pool = new ARFilter(AndroidLauncher.this,R.drawable.water_pool,"water_pool",getContext().getString(R.string.water_pool_title),getContext().getString(R.string.water_pool_des));
                        markerList.addLast(water_pool);
					}catch (IOException ex){
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
