package com.yue.ar.coffee.vision;

import android.util.Log;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

import boofcv.struct.image.ImageDataType;
import boofcv.struct.image.ImageType;


public class TrackTask implements Runnable {

    private String TAG = "TrackTask";




    public TrackTask(){

    }

    @Override
    public void run() {
        TLD();
    }

    private void TLD(){

        Log.d(TAG,"TLD");





    }
}
