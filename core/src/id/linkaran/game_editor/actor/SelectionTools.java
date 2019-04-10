package id.linkaran.game_editor.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.viewport.Viewport;

import support.dealint.actor.ActorRectangle;

public class SelectionTools extends ActorRectangle {

    private Viewport viewport;
    private float tempX, tempY;

    public SelectionTools(Viewport viewport) {
        this.viewport = viewport;
    }

    public void updateTemp() {
        tempX = getX();
        tempY = getY();
    }

    public void resetTemp() {
        tempX = 0;
        tempY = 0;
    }
    
    public void update(float touchX, float touchY) {
        if (touchX > tempX && touchY > tempY) {
            setPosition(tempX, tempY);
            setWidth(touchX - tempX);
            setHeight(touchY - tempY);
        }

        if (touchX < tempX && touchY > tempY) {
            setPosition(touchX, tempY);
            setWidth(tempX - touchX);
            setHeight(touchY - tempY);
        }

        if (touchX < tempX && touchY < tempY) {
            setPosition(touchX, touchY);
            setWidth(tempX - touchX);
            setHeight(tempY - touchY);
        }

        if (touchX > tempX && touchY < tempY) {
            setPosition(tempX, touchY);
            setWidth(touchX - tempX);
            setHeight(tempY - touchY);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        renderRectangle(batch, viewport,  Color.valueOf("6abafcAA"));
    }

    @Override
    public void act(float delta) {
        updateRect();
    }
}
