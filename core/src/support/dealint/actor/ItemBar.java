package support.dealint.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.viewport.Viewport;


public class ItemBar extends Actor {

    private TextureRegion textureBelakang, textureDepan;
    private boolean horizontal, flip;
    private float batas;
    private float itemW, itemH;

    public int value;
    public int maxValue;
    public Viewport viewport;

    public ItemBar(int maxValue, TextureRegion textureBelakang, TextureRegion textureDepan, boolean horizontal, boolean flip) {
        this.textureBelakang = textureBelakang;
        this.textureDepan = textureDepan;
        this.maxValue = maxValue;
        this.horizontal = horizontal;
        this.flip = flip;
        value = maxValue;
    }

    public ItemBar(int maxValue, TextureRegion textureDepan, boolean horizontal, boolean flip) {
        this.textureDepan = textureDepan;
        this.maxValue = maxValue;
        this.horizontal = horizontal;
        this.flip = flip;
        value = maxValue;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        for (int i = 1; i<= maxValue; i++) {
            if (horizontal) {
                float posX;
                if (!flip) {
                    posX = getX() + (itemW * (i - 1)) + (batas * (i - 1));
                } else {
                    posX = getX() + getWidth() - (itemW * (i - 1)) - (batas * (i - 1)) - itemW;
                }
                if (value >= i) {
                    batch.draw(textureDepan, posX, getY(), itemW, itemH);
                } else {
                    if (textureBelakang != null) {
                        batch.draw(textureBelakang, posX, getY(), itemW, itemH);
                    }
                }
            } else {
                float posY;
                if (!flip) {
                    posY = getY() + (itemW * (i - 1)) + (batas * (i - 1));
                } else {
                    posY = getY() + getHeight() - (itemH * (i - 1)) - (batas * (i - 1)) - itemH;
                }
                if (value >= i) {
                    batch.draw(textureDepan, getX(), posY, itemW, itemH);
                } else {
                    if (textureBelakang != null) {
                        batch.draw(textureBelakang, getX(), posY, itemW, itemH);
                    }
                }
            }
        }
    }

    public void set(float itemW, float itemH, float batas) {
        if (horizontal) {
            setSize(itemW * maxValue + batas * (maxValue -1), itemH);
        } else {
            setSize(itemW, itemH  * maxValue + batas * (maxValue -1));
        }
        this.itemW = itemW;
        this.itemH = itemH;
        this.batas = batas;
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

    public void setValue(int value) {
        this.value = value;
    }
}
