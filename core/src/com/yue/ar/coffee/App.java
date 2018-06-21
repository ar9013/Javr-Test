package com.yue.ar.coffee;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import java.text.SimpleDateFormat;

/**
 * App
 */


public class App extends ApplicationAdapter {

    private String TAG = "GDXApp";
    private Stage stage;

    //（StretchViewport） to view stage
    public static final float WORLD_WIDTH = 480;
    public static final float WORLD_HEIGHT = 800;

    public SpriteBatch batch;
    private SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private String date = sDateFormat.format(new java.util.Date());
    public static ShapeRenderer shapeRenderer = null;

    // Camera state
    public enum Mode {
        normal, prepare, preview, takePicture, waitForPictureReady,wrapTarget
    }


    private PerspectiveCamera gmaeCamera;

    // set mode neomal
    private Mode mode ;

    private final CameraControl cameraControl;
    private String matchedMarkerName = null;
    private String matchedMarkerTitle = null;
    private String matchedMarkerDes = null;

    private Label markerTitle = null;
    private Label markerDes = null;
    FreeTypeFontGenerator freeTypeFontGenerator;
    FreeTypeFontGenerator.FreeTypeFontParameter titleFontParameter;
    FreeTypeFontGenerator.FreeTypeFontParameter desFontParameter;
    BitmapFont titleFont;
    BitmapFont desFont;
    String title = "Test 標題";
    String des = "Test 內容";
    Label.LabelStyle titleStyle = null;
    Label.LabelStyle desStyle = null;

    int falseCount = 0;
    boolean isDraw = false;
    private static final float MIN_FRAME_LENGTH = 1f/30f;
    private float timeSinceLastRender;


    // pass object AndroidCameraController witch implement interface CameraControl
    public App(CameraControl cameraControl) {
        this.cameraControl = cameraControl;
    }

    @Override
    public void create() {

		// set Log LEVEL
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		// use StretchViewport create stage
        shapeRenderer = new ShapeRenderer();
		stage = new Stage(new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT));

        init();
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

    private void init(){

        // 读取 bitmapfont.fnt 文件创建位图字体
        freeTypeFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("font/DroidSansFallback.ttf"));

        titleFontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        titleFontParameter.size=24;

        desFontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        desFontParameter.size = 18;


        titleStyle = new Label.LabelStyle();
        desStyle = new Label.LabelStyle();

        titleStyle.fontColor = new Color(0, 1, 1, 1);
        desStyle.fontColor = new Color(1,1,0,1);

    }

    private void makeInfoLabel(String matchedMarkerTitle,String matchedMarkerDes,int xTitle, int yTitle,int xDes, int yDes){
        titleFontParameter.characters = matchedMarkerTitle;
        titleFont = freeTypeFontGenerator.generateFont(titleFontParameter);

        desFontParameter.characters = matchedMarkerDes;
        desFont = freeTypeFontGenerator.generateFont(desFontParameter);

        titleStyle.font = titleFont;
        desStyle.font = desFont;

        markerTitle = new Label(matchedMarkerTitle, titleStyle);
        markerDes = new Label(matchedMarkerDes, desStyle);

        markerTitle.setPosition(xTitle, yTitle);
        markerDes.setPosition(xDes, yDes);

        markerTitle.setFontScale(1.0f);
        markerDes.setFontScale(1.0f);

        stage.addActor(markerTitle);
        stage.addActor(markerDes);
    }

    private void clearInfoLabel(){
        for (Actor actor : stage.getActors()) {
            actor.remove();
        }
    }




    @Override
    public void render() {

        timeSinceLastRender += Gdx.graphics.getDeltaTime();
        if (timeSinceLastRender >= MIN_FRAME_LENGTH) {
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
                            Gdx.app.debug(TAG,"GDX PREVIEW");

                        }
                    }
                }
                break;
                case preview:
                {
                    // 完全透明，可以看到 攝影機的影像
                    Gdx.gl20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

                    if(cameraControl.isTargetFound()) {

                        falseCount = 0;
                        Gdx.app.debug(TAG, "GDX Target Found !!");

                        matchedMarkerName = cameraControl.getMarkerName();
                        matchedMarkerTitle = cameraControl.getMarkerTitle();
                        matchedMarkerDes = cameraControl.getMarkerDes();


                        if (matchedMarkerName == "chiayi" && isDraw == false) {
                            clearInfoLabel();

                            Gdx.app.debug(TAG, "Gdx " + matchedMarkerName);
                            Gdx.app.debug(TAG, "Gdx " + matchedMarkerTitle);
                            Gdx.app.debug(TAG, "Gdx " + matchedMarkerDes);

                            makeInfoLabel(matchedMarkerTitle,matchedMarkerDes,40,200,40,120);

                            isDraw =true;


                        } else if (matchedMarkerName == "chiayi_park" && isDraw == false) {

                            clearInfoLabel();

                            Gdx.app.debug(TAG, "Gdx " + matchedMarkerName);
                            Gdx.app.debug(TAG, "Gdx " + matchedMarkerTitle);
                            Gdx.app.debug(TAG, "Gdx " + matchedMarkerDes);

                            makeInfoLabel(matchedMarkerTitle,matchedMarkerDes,40,200,40,100);

                            isDraw =true;

                        } else if (matchedMarkerName == "clean") {
                            clearInfoLabel();

                            Gdx.app.debug(TAG, "Gdx " + matchedMarkerName);
                            Gdx.app.debug(TAG, "Gdx " + matchedMarkerTitle);
                            Gdx.app.debug(TAG, "Gdx " + matchedMarkerDes);

                            makeInfoLabel(matchedMarkerTitle,matchedMarkerDes,40,200,40,150);
                            isDraw =true;

                        } else if (matchedMarkerName == "school") {
                            clearInfoLabel();

                            Gdx.app.debug(TAG, "Gdx " + matchedMarkerName);
                            Gdx.app.debug(TAG, "Gdx " + matchedMarkerTitle);
                            Gdx.app.debug(TAG, "Gdx " + matchedMarkerDes);

                            makeInfoLabel(matchedMarkerTitle,matchedMarkerDes,40,200,40,60);
                            isDraw =true;
                        }else if(matchedMarkerName == "shanghai"){
                            clearInfoLabel();

                            Gdx.app.debug(TAG, "Gdx " + matchedMarkerName);
                            Gdx.app.debug(TAG, "Gdx " + matchedMarkerTitle);
                            Gdx.app.debug(TAG, "Gdx " + matchedMarkerDes);

                            makeInfoLabel(matchedMarkerTitle,matchedMarkerDes,40,200,40,130);
                            isDraw =true;
                        }else if(matchedMarkerName == "summer_street"){
                            clearInfoLabel();

                            Gdx.app.debug(TAG, "Gdx " + matchedMarkerName);
                            Gdx.app.debug(TAG, "Gdx " + matchedMarkerTitle);
                            Gdx.app.debug(TAG, "Gdx " + matchedMarkerDes);

                            makeInfoLabel(matchedMarkerTitle,matchedMarkerDes,40,200,40,130);
                            isDraw =true;
                        }else if(matchedMarkerName == "water_pool"){
                            clearInfoLabel();

                            Gdx.app.debug(TAG, "Gdx " + matchedMarkerName);
                            Gdx.app.debug(TAG, "Gdx " + matchedMarkerTitle);
                            Gdx.app.debug(TAG, "Gdx " + matchedMarkerDes);

                            makeInfoLabel(matchedMarkerTitle,matchedMarkerDes,40,200,40,110);
                            isDraw =true;
                        }
                    }
                    else{
                        falseCount = falseCount +1;
                        Gdx.app.debug(TAG,"falseCount : "+falseCount);

                        if( falseCount > cameraControl.getMarkerListSize()) {
                            if (stage.getActors().size > 0) {
                               clearInfoLabel();
                                falseCount = 0;
                                isDraw =false;
                            }
                        }
                    }
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

            timeSinceLastRender = 0f;
        }
    }

    @Override
    public void dispose() {
        if (stage != null) {
            freeTypeFontGenerator.dispose();
            stage.dispose();
        }
        batch.dispose();
    }

}
