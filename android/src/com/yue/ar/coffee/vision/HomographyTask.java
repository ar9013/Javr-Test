package com.yue.ar.coffee.vision;

import android.util.Log;

import com.yue.ar.coffee.AndroidLauncher;

import org.opencv.calib3d.Calib3d;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.imgproc.Imgproc;

public class HomographyTask implements Runnable {

    private static final String TAG = "HomographyTask";
    private  Mat candidateFrameCorners = new Mat(4, 1, CvType.CV_32FC2);
    private  MatOfPoint mIntSceneCorners = new MatOfPoint();

    public HomographyTask(){

    }

    @Override
    public void run() {
//        if(!ARFilter.matchResultBuffer.isEmpty()) {
//            MatchResult temp = ARFilter.matchResultBuffer.pollFirst();
//            final Mat homography = Calib3d.findHomography(temp.getGoodMarkerPoints(), temp.getGoodFramePoints());
//            int matchedIndex = temp.getMatchedIndex();
//
//            Log.d(TAG,"homography size : "+homography.size());
//            Log.d(TAG,"matchedID : "+AndroidLauncher.markerList.get(matchedIndex).getMarkerID());
//
//            Core.perspectiveTransform(AndroidLauncher.markerList.get(matchedIndex).getMarkerCorners(), candidateFrameCorners, homography);
//            candidateFrameCorners.convertTo(mIntSceneCorners, CvType.CV_32S);
//
//            if (Imgproc.isContourConvex(mIntSceneCorners)) {
//                // The corners form a convex polygon, so record them as
//                // valid scene corners.
//                candidateFrameCorners.copyTo(temp.frameCorners);
//                Log.d(TAG,"frameCorners size : "+temp.getFrameCorners().size());
//               // targetFound = true;
//            }
//
//        }
    }
}
