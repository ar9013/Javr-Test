package com.yue.ar.coffee.vision;

import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;

public class FrameFeature {

    MatOfKeyPoint keyPoints = null;
    Mat descriptors = null;

    public FrameFeature(MatOfKeyPoint keypoints, Mat descriptors){

        this.keyPoints = keypoints;
        this.descriptors =descriptors;


    }
}
