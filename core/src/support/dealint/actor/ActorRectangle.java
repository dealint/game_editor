package support.dealint.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.viewport.Viewport;

public class ActorRectangle extends Actor {
    public Rectangle rect;
    public ShapeRenderer renderer;
    public boolean punyaGroup = false;

    public ActorRectangle() {
        rect = new Rectangle();
        renderer = new ShapeRenderer();
    }

    public void renderRectangle(Batch batch, Viewport viewport, Color color) {
        batch.end();
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        renderer.setProjectionMatrix(viewport.getCamera().projection);
        renderer.setTransformMatrix(viewport.getCamera().view);
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(color);
        renderer.rect(rect.x, rect.y, rect.width, rect.height);
        renderer.end();
        batch.begin();
    }

    protected void updateRect() {
        if (punyaGroup) {
            rect.set(getStagePos().x, getStagePos().y, getWidth(), getHeight());
        } else {
            rect.set(getX(), getY(), getWidth(), getHeight());
        }
    }

    public Vector2 getStagePos() {
        return localToStageCoordinates(new Vector2());
    }
}
