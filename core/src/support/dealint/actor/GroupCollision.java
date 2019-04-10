package support.dealint.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GroupCollision extends Group {
    public Rectangle rect;
    public Circle circle;
    public Polygon polygon;
    public ShapeRenderer renderer;
    public boolean rectCollision = true;
    public boolean punyaGroup = false;
    public float circleRadius;

    public GroupCollision(boolean rectCollision) {
        rect = new Rectangle();
        circle = new Circle();
        renderer = new ShapeRenderer();
        this.rectCollision = rectCollision;
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
