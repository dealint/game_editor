package support.dealint.actor;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Bar extends ActorRectangle {

    protected int value, maxValue;
    TextureRegion textureBar, textureBorder, textureEfek, textureFull;
    boolean efek, flip;

    int kecepatanEfek;
    int barX, barY, barWidth, barHeight;
    Camera camera;
    Viewport viewport;

    public Bar(int maxValue) {
        this.maxValue = maxValue;
        value = maxValue;
    }

    public void setEfek(TextureRegion region, int kecepatanEfek) {
        efek = true;
        textureEfek = region;
        this.kecepatanEfek = kecepatanEfek;
    }

    public void setFull(TextureRegion region) {
        textureFull = region;
    }

    public void updateBarPosition(float xPercent, float yPercent) {

    }

    public void kurangiValue(int i) {
        if (value - i >= 0) {
            value -= i;
        } else {
            value = 0;
        }
    }

    public void tambahValue(int i) {
        if (value + i >= maxValue) {
            value = maxValue;
        } else {
            value += i;
        }

    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getMaxValue() {
        return maxValue;
    }
}
