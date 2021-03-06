package com.yue.ar.coffee.vision;


import com.badlogic.gdx.Gdx;

import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;

public class OrbTask implements Runnable{

    private static final String TAG = "OrbTask";
    private Mat frameGray = null;
    // Features of the scene (the current frame).
    private MatOfKeyPoint frameKeypoints = null;
    // Descriptors of the scene's features.
    private Mat frameDescriptors = null;

    public OrbTask(){

    }


    @Override
    public void run() {

      //  Log.d(TAG,"OrbTask start running !!");

        if(!ARFilter.frameGrayBuffer.isEmpty()) {
            frameGray = ARFilter.frameGrayBuffer.pollFirst();
            frameKeypoints = new MatOfKeyPoint();
            frameDescriptors = new Mat();

            ARFilter.featureDetector.detect(frameGray, frameKeypoints);
            ARFilter.descriptorExtractor.compute(frameGray, frameKeypoints, frameDescriptors);
            Gdx.app.debug(TAG, "frameKeypoints : " + frameKeypoints.toList().size());
            Gdx.app.debug(TAG, "frameDescriptors : " + frameDescriptors.size());

            FrameFeature temp = new FrameFeature(frameKeypoints,frameDescriptors);
            ARFilter.frameFeatureBuffer.addLast(temp);
            Gdx.app.debug(TAG,"frameFeatureBuffer size : "+ ARFilter.frameFeatureBuffer.size());
        }

    }
}
