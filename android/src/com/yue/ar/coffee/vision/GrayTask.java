package com.yue.ar.coffee.vision;

import android.util.Log;

import com.yue.ar.coffee.camera.CameraSurface;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;


public class GrayTask implements Runnable {

    private static final String TAG = "GrayTask";
    private byte[] frameData = null;
    private Mat frameSrc = null;
    private Mat frameGray = null;

    public GrayTask() {

    }

    @Override
    public void run() {
        // Log.d(TAG,"gray task start !!");
        if (!ARFilter.frameBuffer.isEmpty()) {
            frameData = ARFilter.frameBuffer.pollFirst();
            Log.d(TAG, "size frameBuffer : " + ARFilter.frameBuffer.size());

            frameSrc = new Mat(CameraSurface.previewHeight + CameraSurface.previewHeight / 2, CameraSurface.previewWidth, CvType.CV_8UC1);
            frameSrc.put(0, 0, frameData);
            Log.d(TAG, "frameSrc size : " + frameSrc.size());

            frameGray = new Mat();
            Imgproc.cvtColor(frameSrc, frameGray, Imgproc.COLOR_YUV2GRAY_NV21);
            Log.d(TAG, "frameGray size : " + frameGray.size());

            ARFilter.frameGrayBuffer.addLast(frameGray);
            Log.d(TAG, " frameGrayBuffer size : " + ARFilter.frameGrayBuffer.size());


        }
    }
}
