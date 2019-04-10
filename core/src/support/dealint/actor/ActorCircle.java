package support.dealint.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.viewport.Viewport;

public class ActorCircle extends Actor {
    public Circle circle;
    public ShapeRenderer renderer;
    public boolean punyaGroup = false;
    public float circleRadius;

    public ActorCircle() {
        circle = new Circle();
        renderer = new ShapeRenderer();
    }

    public void renderCircle(Batch batch, Viewport viewport, Color color) {
        batch.end();
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        renderer.setProjectionMatrix(viewport.getCamera().projection);
        renderer.setTransformMatrix(viewport.getCamera().view);
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(color);
        renderer.circle(circle.x, circle.y, circle.radius);
        renderer.end();
        batch.begin();
    }

    protected void updateCircle() {
        if (punyaGroup) {
            circle.set(getStagePos().x  + getWidth()/2, getStagePos().y + getHeight()/2, getWidth()/2);
        } else {
            circle.set(getX() + getWidth()/2, getY() + getHeight()/2, circleRadius);
        }
    }

    public Vector2 getStagePos() {
        return localToStageCoordinates(new Vector2());
    }
}
