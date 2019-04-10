package support.dealint.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class SplashScreen extends GameScreen {

    private class ActorAlpha extends Actor {

        Texture texture;

        public ActorAlpha(Texture texture) {
            this.texture = texture;
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            Color color = getColor();
            batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
            batch.draw(texture, getX(), getY(), getWidth() * getScaleX(),
                    getHeight() * getScaleY());
            batch.setColor(color.r, color.g, color.b, 1f);
        }
    }

    private int duration;
    private ActorAlpha mask;
    private Texture backgroundTexture, maskTexture;
    private Texture logo;
    public boolean end = false;
    private Color maskColor;
    private ScreenAdapter nextScreen;
    private GameManager manager;
    private float logoW, logoH;

    public SplashScreen(GameManager manager, float worldWidth, float worldHeight,
                        Texture backgroundTexture,
                        Texture logo, float logoW, float logoH,
                        Color maskColor, int duration) {
        super(worldWidth, worldHeight);
        this.manager = manager;
        this.duration = duration;
        this.maskColor = maskColor;
        this.backgroundTexture = backgroundTexture;
        this.logo = logo;
        this.logoW = logoW;
        this.logoH = logoH;
    }

    public void setNextScreen(ScreenAdapter screen) {
        nextScreen = screen;
    }

    @Override
    public void setStage() {
        Image background = new Image(backgroundTexture);
        background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        bgStage.addActor(background);

        Image image = new Image(logo);
        image.setSize(logoW, logoH);
        image.setPosition(worldWidth/2 - image.getWidth()/2, worldHeight/2 - image.getHeight()/2);
        stage.addActor(image);
    }

    @Override
    public void setGuiStage() {
        maskTexture = GameUtils.getTexture(w, h, maskColor);
        mask = new ActorAlpha(maskTexture);
        mask.setColor(maskColor);

        AlphaAction actionIn = new AlphaAction();
        actionIn.setDuration(2);
        actionIn.setAlpha(0);

        AlphaAction actionScreen = new AlphaAction();
        actionScreen.setDuration(duration);
        actionScreen.setAlpha(0);

        AlphaAction actionOut = new AlphaAction();

        actionOut.setDuration(1);
        actionOut.setAlpha(1);

        mask.addAction(new SequenceAction(actionIn, actionScreen, actionOut, new RunnableAction() {
            @Override
            public void run() {
                manager.game.setScreen(nextScreen);
            }
        }));

        guiStage.addActor(mask);
    }

    @Override
    public void updateStage(float w, float h, float size) {
        mask.setSize(w, h);
        this.w = w;
        this.h = h;
    }

    float w, h;

    @Override
    public void render(float delta) {
        clearScreen(Color.BLACK);

        bgStage.getViewport().apply();
        bgStage.draw();
        bgStage.act();

        stage.getViewport().apply();
        stage.draw();
        stage.act();

        guiStage.getViewport().apply();
        guiStage.draw();
        guiStage.act();
    }


    @Override
    public void dispose() {
        maskTexture.dispose();
    }
}
