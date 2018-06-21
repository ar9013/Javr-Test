package com.yue.ar.coffee.vision;



import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;

public class MatchResult {

    private int markerIndex  = 0;
    private MatOfPoint2f goodMarkerPoints = null;
    private MatOfPoint2f goodFramePoints = null;
    Mat frameCorners = null;


    public MatchResult(int markerIndex , MatOfPoint2f goodMarkerPoints , MatOfPoint2f goodFramePoints, Mat frameCorners){

        this.markerIndex = markerIndex;
        this.goodMarkerPoints = goodMarkerPoints;
        this.goodFramePoints = goodFramePoints;
        this.frameCorners = frameCorners;
    }

    int getMatchedIndex(){
        return markerIndex;
    }

    MatOfPoint2f getGoodMarkerPoints(){
        return  goodMarkerPoints;
    }

    MatOfPoint2f getGoodFramePoints(){
        return  goodFramePoints;
    }

    Mat getFrameCorners(){
        return  frameCorners;
    }


}
