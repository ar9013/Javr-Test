package com.yue.ar.coffee.vision;

import android.util.Log;

import com.yue.ar.coffee.AndroidLauncher;

import org.opencv.core.CvType;
import org.opencv.core.DMatch;
import org.opencv.core.KeyPoint;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;

import java.util.ArrayList;
import java.util.List;

public class MatchTask implements Runnable{

    private static final String TAG = "MatchTask";
    private MatOfDMatch matches = new MatOfDMatch();
    private String markerName = null;

    public MatchTask(){

    }


    @Override
    public void run() {
        //Log.d(TAG,"MatchTask start running !!");

        if(!ARFilter.frameFeatureBuffer.isEmpty()) {
            FrameFeature frameFeature = ARFilter.frameFeatureBuffer.pollFirst();


            for (int markerIndex = 0; markerIndex < AndroidLauncher.markerList.size(); markerIndex++) {

                ARFilter markerFilter = AndroidLauncher.markerList.get(markerIndex);

                match(markerIndex,markerFilter, frameFeature);

            }
        }

    }

    private  void match(int markerIndex , ARFilter markerFilter,FrameFeature frameFeature){
        markerName = markerFilter.getMarkerName();
       // Log.d(TAG,"markerName : "+markerName);

        ARFilter.descriptorMatcher.match(frameFeature.descriptors, markerFilter.getMarkerDescriptors(), matches);
        Log.d(TAG,"matches size : "+matches.size());

        final List<DMatch> matchesList = matches.toList();
        if (matchesList.size() < 4) {
            // There are too few matches to find the homography.
            Log.d(TAG,"XX matches < 4 , return ");

            return;
        }

                final List<KeyPoint> markerKeypointsList = markerFilter.getMarkerKeypoints().toList();
                final List<KeyPoint> frameKeypointsList = frameFeature.keyPoints.toList();

                // Calculate the max and min distances between keypoints.
                double maxDist = 0.0;
                double minDist = Double.MAX_VALUE;

                for (final DMatch match : matchesList) {
                    final double dist = match.distance;
                    if (dist < minDist) {
                        minDist = dist;
                    }
                    if (dist > maxDist) {
                        maxDist = dist;
                    }
                }

                   Log.d(TAG,"minDist : "+minDist);
                    Mat frameCorners = new Mat(4, 1, CvType.CV_32FC2);

                if (minDist > 50.0) {
                    // The target is completely lost.
                    // Discard any previously found corners.
                    Log.d(TAG,"Total lost !!");
                    frameCorners.create(0, 0, frameCorners.type());
                    return;
                } else if (minDist > 25.0) {
                    // The target is lost but maybe it is still close.
                    // Keep any previously found corners.
                    Log.d(TAG,"target near by !!");

                    return;
                }

                // Identify "good" keypoints and on match distance.
                final ArrayList<Point> goodMarkerePointsList = new ArrayList<Point>();
                final ArrayList<Point> goodFramePointsList = new ArrayList<Point>();
                final double maxGoodMatchDist = 1.75 * minDist;
                for (final DMatch match : matchesList) {
                    if (match.distance < maxGoodMatchDist) {
                        goodMarkerePointsList.add(markerKeypointsList.get(match.trainIdx).pt);
                        goodFramePointsList.add(frameKeypointsList.get(match.queryIdx).pt);
                    }
                }

                if (goodMarkerePointsList.size() < 4 || goodFramePointsList.size() < 4) {
                    // There are too few good points to find the homography.
                    return;
                }
                // There are enough good points to find the homography.
                // (Otherwise, the method would have already returned.)

                // Convert the matched points to MatOfPoint2f format, as
                // required by the Calib3d.findHomography function.
                final MatOfPoint2f goodMarkerPoints = new MatOfPoint2f();
                goodMarkerPoints.fromList(goodMarkerePointsList);

                final MatOfPoint2f goodFramePoints = new MatOfPoint2f();
                goodFramePoints.fromList(goodFramePointsList);

                Log.d(TAG, "marker Name : "+markerName);
                Log.d(TAG,"goodMarkerPoints size : "+goodMarkerePointsList.size());
                Log.d(TAG,"goodFramePointsList size : "+goodFramePointsList.size());


                MatchResult resultMatch = new MatchResult(markerIndex,goodMarkerPoints,goodFramePoints,frameCorners);
                ARFilter.matchResultBuffer.addLast(resultMatch);
                Log.d(TAG,"matchResultBuffer size : "+ARFilter.matchResultBuffer.size());

    }

}
