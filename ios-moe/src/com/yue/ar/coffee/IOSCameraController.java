package com.yue.ar.coffee;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;

public class IOSCameraController implements CameraControl {
    @Override
    public void prepareCamera() {
        
    }

    @Override
    public void startPreview() {

    }

    @Override
    public void stopPreview() {

    }

    @Override
    public void takePicture() {

    }

    @Override
    public byte[] getPictureData() {
        return new byte[0];
    }

    @Override
    public void startPreviewAsync() {

    }

    @Override
    public void stopPreviewAsync() {

    }

    @Override
    public void prepareCameraAsync() {

    }


    @Override
    public byte[] takePictureAsync(long timeout) {
        return new byte[0];
    }

    @Override
    public void saveAsJpeg(FileHandle jpgfile, Pixmap cameraPixmap) {

    }

    @Override
    public boolean isReady() {
        return false;
    }

    @Override
    public boolean isTargetFound() {
        return false;
    }

    @Override
    public String getMarkerName() {
        return null;
    }

    @Override
    public String getMarkerTitle() {
        return null;
    }

    @Override
    public String getMarkerDes() {
        return null;
    }

    @Override
    public int getMarkerListSize() {
        return 0;
    }

}
