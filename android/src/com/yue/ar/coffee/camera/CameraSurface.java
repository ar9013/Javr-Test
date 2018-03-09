package com.yue.ar.coffee.camera;

import android.content.Context;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CameraSurface extends SurfaceView implements SurfaceHolder.Callback {

    private Camera androidCamera = null;

      public static int previewWidth = 0;
      public static int previewHeight =0;

    AndroidCameraController cameraController = null;

    public CameraSurface(Context context,AndroidCameraController cameraController) {
        super(context);

        this.cameraController = cameraController;

        getHolder().addCallback(this);
        getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        androidCamera = Camera.open(0);
        androidCamera.setDisplayOrientation(90);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {

        Camera.Parameters params = androidCamera.getParameters();
        androidCamera.setParameters(params);

        Camera.Size size = params.getPictureSize();
        previewHeight = size.height;
        previewHeight = size.width;

        try{
            androidCamera.setPreviewCallback(cameraController);
            androidCamera.setPreviewDisplay(holder);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        androidCamera.setPreviewCallback(null);
        androidCamera.stopPreview();
        androidCamera.release();
        androidCamera = null;
    }

    public Camera getCamera() {
        return androidCamera;
    }

}
