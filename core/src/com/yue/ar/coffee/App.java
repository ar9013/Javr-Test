package com.yue.ar.coffee;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import java.text.SimpleDateFormat;

/**
 * App
 */


public class App extends ApplicationAdapter {

    private String TAG = "App";
    private Stage stage;

    //（StretchViewport） to view stage
    public static final float WORLD_WIDTH = 480;
    public static final float WORLD_HEIGHT = 800;

    public SpriteBatch batch;
    private SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private String date = sDateFormat.format(new java.util.Date());


    // Camera state
    public enum Mode {
        normal, prepare, preview, takePicture, waitForPictureReady,
    }


    private PerspectiveCamera gmaeCamera;

    // set mode neomal
    private Mode mode ;

    private final CameraControl cameraControl;

    // pass object AndroidCameraController witch implement interface CameraControl
    public App(CameraControl cameraControl) {
        this.cameraControl = cameraControl;

    }

    @Override
    public void create() {

		// set Log LEVEL
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		// use StretchViewport create stage

		stage = new Stage(new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT));
		// set stage catch input
		Gdx.input.setInputProcessor(stage);

		    // set mode to normal
			mode = Mode.normal;

			// control camera in normal mode
		if (mode == Mode.normal) {
			mode = Mode.prepare;
			if (cameraControl != null) {
                cameraControl.prepareCameraAsync();
			}
		}

		batch = new SpriteBatch();

		// Config Game camera
        gmaeCamera = new PerspectiveCamera(67.0f, 2.0f * Gdx.graphics.getWidth()
				/ Gdx.graphics.getHeight(), 2.0f);
        gmaeCamera.far = 100.0f;
        gmaeCamera.near = 0.1f;
        gmaeCamera.position.set(2.0f, 2.0f, 2.0f);
        gmaeCamera.lookAt(0.0f, 0.0f, 0.0f);

    }

    @Override
    public void render() {

        Gdx.gl20.glHint(GL20.GL_GENERATE_MIPMAP_HINT, GL20.GL_NICEST);

        // switch mode to control camera
        switch (mode)
        {
            case normal:
            {
                // clear screen to blue.
                Gdx.gl20.glClearColor(0.0f, 0.0f, 0.6f, 1.0f);
            }
            break;
            case prepare:
            {
                // clear screen to half translucent （半透明）.
                Gdx.gl20.glClearColor(0.0f, 0.0f, 0f, 0.6f);
                if (cameraControl != null) {
                    if (cameraControl.isReady()) {

                        // Async control camera to preview
                        cameraControl.startPreviewAsync();

                        // set mode to preview
                        mode = Mode.preview;
                    }
                }
            }
            break;
            case preview:
            {
                // 完全透明，可以看到 攝影機的影像
                Gdx.gl20.glClearColor(0.0f, 0.0f, 0.0f, 0f);
            }
            break;
            case takePicture:
            {
                // clear screen to trasnparent
                Gdx.gl20.glClearColor(0f, 0.0f, 0.0f, 0.0f);
                if (cameraControl != null) {
                    cameraControl.takePicture();
                }
                // set to waitForPictureReady mode
                mode = Mode.waitForPictureReady;
            }
            break;
            case waitForPictureReady:
            {
                Gdx.gl20.glClearColor(0.0f, 0f, 0.0f, 0.0f);

            }
            break;
        }

        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        batch.begin();
        stage.act(); // 更新舞台逻辑
        stage.draw();// 绘制舞台
        batch.end();

        Gdx.gl20.glEnable(GL20.GL_DEPTH_TEST);
        Gdx.gl20.glEnable(GL20.GL_TEXTURE);
        Gdx.gl20.glEnable(GL20.GL_TEXTURE_2D);
        Gdx.gl20.glEnable(GL20.GL_LINE_LOOP);
        Gdx.gl20.glDepthFunc(GL20.GL_LEQUAL);
        Gdx.gl20.glClearDepthf(1.0F);
        gmaeCamera.update(true);

    }

    @Override
    public void dispose() {
        if (stage != null) {
            stage.dispose();
        }


        batch.dispose();
    }

}
