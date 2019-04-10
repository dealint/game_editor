package support.dealint.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.badlogic.gdx.utils.viewport.Viewport;

import support.dealint.manager.GameUtils;

public class VerticalBar extends Bar {

    public VerticalBar(Viewport viewport, int maxValue, TextureRegion textureBar, TextureRegion textureBorder, boolean flip) {
        super(maxValue);
        this.camera = viewport.getCamera();
        this.viewport = viewport;
        this.textureBar = textureBar;
        this.textureBorder = textureBorder;
        this.flip = flip;

        this.barX = 0;
        this.barY = 0;

    }

    public void updateBarPosition(float xPercent, float yPercent) {
        barX = (int) (getHeight() * xPercent);
        barY = (int) (getWidth() * yPercent);

        widthEfek = (int) GameUtils.banding(textureBar.getRegionHeight(),0, textureBorder.getRegionHeight(), 0, getWidth());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (!flip) {
            batch.draw(textureBorder, getX(), getY()-getWidth(), 0, getWidth(), getHeight(), getWidth(), 1, 1, 90);
        } else {
            batch.draw(textureBorder, getX(), getY()-getWidth()+getHeight(), 0, getWidth(), getHeight(), getWidth(), -1, 1, 90);
        }

        if (efek) {
            renderEfek(batch);
        }
        renderBar(batch);
        renderRectangle(batch, viewport, Color.YELLOW);
    }

    private void renderBar(Batch batch) {
        batch.flush();
        barWidth = (int) GameUtils.banding(textureBar.getRegionWidth(),0, textureBorder.getRegionWidth(), 0, getHeight());
        barHeight = (int) GameUtils.banding(textureBar.getRegionHeight(), 0, textureBorder.getRegionHeight(), 0, getWidth());
        int width = 0;
        if (value!=0) {
            width = (int) GameUtils.banding(value, 0, maxValue, 0, barWidth);
        } else {
            width = 1;
        }

        Rectangle scissors = new Rectangle();
        Rectangle clipBounds;
        if (!flip) {
            clipBounds = new Rectangle(getX() + barY, getY() + barX, barHeight, width);

            ScissorStack.calculateScissors(camera, viewport.getScreenX(), viewport.getScreenY(), viewport.getScreenWidth(), viewport.getScreenHeight(),
                    batch.getTransformMatrix(), clipBounds, scissors);
            ScissorStack.pushScissors(scissors);

            if (textureFull == null) {
                batch.draw(textureBar, getX() + barY, getY() - barHeight + barX, 0, barHeight, barWidth, barHeight  , 1, 1, 90);
            } else {
                if (value == maxValue) {
                    batch.draw(textureFull, getX() + barY, getY()-barHeight + barX, 0, barHeight, barWidth, barHeight  , 1, 1, 90);
                } else {
                    batch.draw(textureBar, getX() + barY, getY()-barHeight + barX, 0, barHeight, barWidth, barHeight  , 1, 1, 90);
                }
            }
        } else {
            clipBounds = new Rectangle(getX() + barY, getY() + (getHeight() - barWidth - barX) + (barWidth - width), barHeight, width);
            ScissorStack.calculateScissors(camera, viewport.getScreenX(), viewport.getScreenY(), viewport.getScreenWidth(), viewport.getScreenHeight(),
                    batch.getTransformMatrix(), clipBounds, scissors);
            ScissorStack.pushScissors(scissors);
            if (textureFull == null) {
                batch.draw(textureBar, getX() + barY, getY() - barHeight + (getHeight() - barX), 0, barHeight, barWidth, barHeight  , -1, 1, 90);
            } else {
                if (value == maxValue) {
                    batch.draw(textureFull, getX() + barY, getY() - barHeight + (getHeight() - barX), 0, barHeight, barWidth, barHeight  , -1, 1, 90);
                } else {
                    batch.draw(textureBar, getX() + barY, getY() - barHeight + (getHeight() - barX), 0, barHeight, barWidth, barHeight  , -1, 1, 90);
                }
            }
        }
        batch.flush();
        ScissorStack.popScissors();
    }

    private int widthEfek;
    private void renderEfek(Batch batch) {
        batch.flush();
        barWidth = (int) GameUtils.banding(textureBar.getRegionWidth(),0, textureBorder.getRegionWidth(), 0, getHeight());
        barHeight = (int) GameUtils.banding(textureBar.getRegionHeight(), 0, textureBorder.getRegionHeight(), 0, getWidth());
        int width = 0;
        if (value!=0) {
            width = (int) GameUtils.banding(value, 0, maxValue, 0, barWidth);
        } else {
            width = 1;
        }

        if (widthEfek > width) {
            widthEfek-=kecepatanEfek;
        } else {
            widthEfek = width;
        }
        if (widthEfek < 1) {
            widthEfek = 1;
        }

        Rectangle scissors = new Rectangle();
        Rectangle clipBounds;
        if (!flip) {

            clipBounds = new Rectangle(getX() + barY, getY() + barX, barHeight, widthEfek);
            ScissorStack.calculateScissors(camera, viewport.getScreenX(), viewport.getScreenY(), viewport.getScreenWidth(), viewport.getScreenHeight(),
                    batch.getTransformMatrix(), clipBounds, scissors);
            ScissorStack.pushScissors(scissors);
            batch.draw(textureEfek, getX() + barY, getY() - barHeight + barX, 0, barHeight, barWidth, barHeight  , 1, 1, 90);
        } else {

            clipBounds = new Rectangle(getX() + barY, getY() + (getHeight() - barWidth - barX) + (barWidth - widthEfek), barHeight, widthEfek);
            ScissorStack.calculateScissors(camera, viewport.getScreenX(), viewport.getScreenY(), viewport.getScreenWidth(), viewport.getScreenHeight(),
                    batch.getTransformMatrix(), clipBounds, scissors);
            ScissorStack.pushScissors(scissors);
            batch.draw(textureEfek, getX() + barY, getY() - barHeight + (getHeight() - barX), 0, barHeight, barWidth, barHeight  , -1, 1, 90);
        }
        batch.flush();
        ScissorStack.popScissors();
    }
}