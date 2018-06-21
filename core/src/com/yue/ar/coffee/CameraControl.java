package com.yue.ar.coffee;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;

public interface CameraControl {

    // Synchronous interface
    void prepareCamera();
    void startPreview();
    void stopPreview();
    void takePicture();
    byte[] getPictureData();

    // Asynchronous interface - need when called from a non platform thread (GDXOpenGl thread)
    // 非同步的 介面，要從
    void startPreviewAsync();
    void stopPreviewAsync();
    void prepareCameraAsync();

    byte[] takePictureAsync(long timeout);
    void saveAsJpeg(FileHandle jpgfile, Pixmap cameraPixmap);
    boolean isReady();
    boolean isTargetFound();
    String getMarkerName();
    String getMarkerTitle();
    String getMarkerDes();
    int getMarkerListSize();
}
