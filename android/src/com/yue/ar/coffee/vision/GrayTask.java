package com.yue.ar.coffee.vision;

import android.graphics.Bitmap;
import android.util.Log;

import com.yue.ar.coffee.AndroidLauncher;
import com.yue.ar.coffee.camera.AndroidCameraController;
import com.yue.ar.coffee.camera.CameraSurface;

import java.util.ArrayDeque;

import boofcv.android.ConvertBitmap;
import boofcv.core.encoding.ConvertNV21;
import boofcv.struct.image.GrayU8;


/**
 * Created by luokangyu on 07/03/2018.
 */

//public class GrayTask implements Runnable {
//
//    private static final String TAG = "GrayTask";
//    private boolean isMarker = false;
//
//    ArrayDeque<GrayU8> grayMarkers  =null;
//    ArrayDeque<GrayU8> grayFrames = null;
//
//    public GrayTask(boolean isMarker){
//        this.isMarker = isMarker;
//
//        if(isMarker){
//           grayMarkers = new ArrayDeque<>();
//        }else{
//            grayFrames = new ArrayDeque<>();
//        }
//    }
//
//    @Override
//    public void run() {
//
//        if(isMarker){
//            cvtMarkerGray();
//        }else{
//            cvtFrameGray();
//        }
//    }
//
//    private void cvtMarkerGray(){
//        Log.d(TAG,"cvtMarkerGray");
//
//        if(!AndroidLauncher.markers.isEmpty()) {
//            Log.d(TAG,"markers :"+AndroidLauncher.markers.size());
//            Bitmap bitmap = AndroidLauncher.markers.pollFirst();
//            GrayU8 markerGray = ConvertBitmap.bitmapToGray(bitmap, (GrayU8) null, null);
//            Log.d(TAG,"markers :"+AndroidLauncher.markers.size());
//
//            grayMarkers.addLast(markerGray);
//        }
//    }
//
//    private void cvtFrameGray(){
//        Log.d(TAG,"cvtFrameGray");

//        if(!AndroidCameraController.frames.isEmpty()){
//
//            Log.d(TAG,"frames :"+AndroidCameraController.frames.size());
//            byte[] frame = AndroidCameraController.frames.pollFirst();
//            Log.d(TAG,"frames :"+AndroidCameraController.frames.size());
//
//            GrayU8 grayFrame = new GrayU8(CameraSurface.previewWidth,CameraSurface.previewHeight);
//            ConvertNV21.nv21ToGray(frame, CameraSurface.previewWidth,CameraSurface.previewHeight,grayFrame);
//
//            grayFrames.addLast(grayFrame);
//        }
//    }
//}
