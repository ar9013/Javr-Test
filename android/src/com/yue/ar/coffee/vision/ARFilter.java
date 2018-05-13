package com.yue.ar.coffee.vision;

import android.content.Context;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.FeatureDetector;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;
import java.util.ArrayDeque;

public class ARFilter {

    public static ArrayDeque<byte[]> frameBuffer = new ArrayDeque();

    Mat markerSrc = null;
    private final Mat markerCorners = new Mat(4, 1, CvType.CV_32FC2);

    // A feature detector, which finds features in images.
    private final FeatureDetector featureDetector = FeatureDetector.create(FeatureDetector.ORB);

    // A descriptor extractor, which creates descriptors of features.
    private final DescriptorExtractor descriptorExtractor = DescriptorExtractor.create(DescriptorExtractor.ORB);

    // Features of the reference image.
    private final MatOfKeyPoint markerKeypoints = new MatOfKeyPoint();

    // Descriptors of the reference image's features.
    private final Mat markerDescriptors = new Mat();

    public ARFilter(final Context context, final int markerID,String markerName)throws IOException {
        // Load the reference image from the app's resources.
        // It is loaded in BGR (blue, green, red) format.
        markerSrc = Utils.loadResource(context, markerID, Imgcodecs.CV_LOAD_IMAGE_COLOR);

        // Create grayscale and RGBA versions of the reference image.
        final Mat markerGray = new Mat();
        Imgproc.cvtColor(markerSrc, markerGray, Imgproc.COLOR_BGR2GRAY);

        Imgproc.cvtColor(markerSrc, markerSrc, Imgproc.COLOR_BGR2RGBA);

        // Store the reference image's corner coordinates, in pixels.
        markerCorners.put(0, 0, new double[] { 0.0, 0.0 });
        markerCorners.put(1, 0, new double[] { markerGray.cols(), 0.0 });
        markerCorners.put(2, 0, new double[] { markerGray.cols(), markerGray.rows() });
        markerCorners.put(3, 0, new double[] { 0.0, markerGray.rows() });

        // Detect the reference features and compute their descriptors.
        featureDetector.detect(markerGray, markerKeypoints);
        descriptorExtractor.compute(markerGray, markerKeypoints, markerDescriptors);
    }

    public static  void frameGray(){

    }


}
