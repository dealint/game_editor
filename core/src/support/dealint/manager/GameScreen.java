package support.dealint.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public abstract class GameScreen extends ScreenAdapter {

    protected float worldWidth, worldHeight;
    protected GameSetting setting;
    protected GameTimer timer;
    protected GameInput input;
    protected InputMultiplexer inputMultiplexer;

    public Viewport viewport, guiViewport, bgViewport, gutterViewport;
    public Stage stage, guiStage, bgStage;

    protected SpriteBatch batch;
    public OrthographicCamera camera;
    public OrthographicCamera bgCamera;

    public GameScreen(GameSetting gameSetting, GameTimer gameTimer, GameInput gameInput) {
        setting = gameSetting;
        timer = gameTimer;
        input = gameInput;
        if (gameInput != null) {
            input.gameScreen = this;
        }
        worldWidth = setting.worldWidth;
        worldHeight = setting.worldHeight;
    }

    public GameScreen(float worldWidth, float worldHeight, GameTimer gameTimer, GameInput gameInput) {
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
        timer = gameTimer;
        input = gameInput;
        input.gameScreen = this;
    }

    public GameScreen(float worldWidth, float worldHeight) {
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        bgCamera = new OrthographicCamera();
        camera = new OrthographicCamera();

        bgViewport = new FillViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), bgCamera);
        gutterViewport = new FitViewport(worldWidth, worldHeight, new OrthographicCamera());
        viewport = new ExtendViewport(worldWidth, worldHeight, camera);
        guiViewport = new ScreenViewport();

        bgStage = new Stage(bgViewport);
        stage = new Stage(viewport);
        guiStage = new Stage(guiViewport);

        setStage();
        setGuiStage();
        setInput();
    }

    public void setInput() {
        inputMultiplexer = new InputMultiplexer();
        if (input != null) {
            inputMultiplexer.addProcessor(input);
        }
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    public abstract void setStage();

    public abstract void setGuiStage();

    public abstract void updateStage(float w, float h, float size);

    @Override
    public void resize(int width, int height) {
        bgViewport.update(width, height, true);

        gutterViewport.update(width, height);

        viewport.update(width, height, true);
        viewport.getCamera().position.set(worldWidth/2, worldHeight/2,0);

        guiViewport.update(width, height, true);
        guiStage.getCamera().position.set(width/2, height/2, 0);
        updateStage(width, height, (width + height) / 2);
    }

    @Override
    public void render(float delta) {
        clearScreen(Color.BLACK);
        bgStage.getViewport().apply();
        bgStage.draw();

        stage.getViewport().apply();
        stage.draw();
        stage.act();
        if (timer!=null) {
            timer.update(delta);
        }

        guiStage.getViewport().apply();
        guiStage.draw();
        guiStage.act();
    }

    @Override
    public abstract void dispose();

    protected void clearScreen(Color color) {
        Gdx.gl.glClearColor(color.r, color.g,
                color.b, color.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);
    }
}
