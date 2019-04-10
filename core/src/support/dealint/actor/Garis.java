package support.dealint.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.viewport.Viewport;

import support.dealint.manager.GameUtils;

public class Garis extends ActorRectangle {

    protected Viewport viewport;
    private TextureRegion texture, texture2;
    public Actor actor;

    public boolean ready = false;

    public Garis(Viewport viewport, TextureRegion texture, TextureRegion texture2) {
        this.viewport = viewport;
        this.texture = texture;
        this.texture2 = texture2;
        setWidth(10);
        setVisible(false);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (ready) {
            batch.draw(texture2, getX(), getY(), getWidth() / 2, 0, getWidth(), getHeight(),
                    1, 1, getRotation());
        } else {
            batch.draw(texture, getX(), getY(), getWidth() / 2, 0, getWidth(), getHeight(),
                    1, 1, getRotation());
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    public void updateGaris(float startX, float startY) {
        Vector3 touch = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        viewport.unproject(touch);

        float speedX = touch.x - getWidth()/2 - startX;
        float speedY = touch.y - startY;

        setPosition(startX, startY);
        setHeight(GameUtils.jarak(startX, startY, touch.x, touch.y));
        setRotation(-(float) Math.toDegrees(Math.atan2(speedX, speedY)));
    }

    public void updateGaris(float x, float y, float endX, float endY) {

        float speedX = x - getWidth()/2 - endX;
        float speedY = y - endY;

        setPosition(endX-getWidth()/2, endY);
        setHeight(GameUtils.jarak(x, y, endX, endY));
        setRotation(-(float) Math.toDegrees(Math.atan2(speedX, speedY)));
    }
}
