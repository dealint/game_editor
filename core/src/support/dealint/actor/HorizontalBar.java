package support.dealint.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.badlogic.gdx.utils.viewport.Viewport;

import support.dealint.manager.GameUtils;

public class HorizontalBar extends Bar {

    public HorizontalBar(Viewport viewport, int maxValue, TextureRegion textureBar, TextureRegion textureBorder, boolean flip) {
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
        barX = (int) (getWidth() * xPercent);
        barY = (int) (getHeight() * yPercent);

        widthEfek = (int) GameUtils.banding(textureBar.getRegionWidth(),0, textureBorder.getRegionWidth(), 0, getWidth());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (!flip) {
            batch.draw(textureBorder, getX(), getY(), getWidth(), getHeight());
        } else {
            batch.draw(textureBorder, getX()+getWidth(), getY(), 0,0, getWidth(), getHeight(), -1, 1, 0);
        }
        if (efek) {
            renderEfek(batch);
        }
        renderBar(batch);


    }

    private void renderBar(Batch batch) {
        batch.flush();
        barWidth = (int) GameUtils.banding(textureBar.getRegionWidth(), 0, textureBorder.getRegionWidth(), 0, getWidth());
        barHeight = (int) GameUtils.banding(textureBar.getRegionHeight(), 0, textureBorder.getRegionHeight(), 0, getHeight());
        int width = 0;
        if (value != 0) {
            width = (int) GameUtils.banding(value, 0, maxValue, getX(), barWidth);
        } else {
            width = 1;
        }

        Rectangle scissors = new Rectangle();
        Rectangle clipBounds = null;
        if (!flip) {
            clipBounds = new Rectangle(getX() + barX, getY() + barY, width, getHeight());
            ScissorStack.calculateScissors(camera, viewport.getScreenX(), viewport.getScreenY(), viewport.getScreenWidth(), viewport.getScreenHeight(),
                    batch.getTransformMatrix(), clipBounds, scissors);
            ScissorStack.pushScissors(scissors);

            if (textureFull == null) {
                batch.draw(textureBar, getX() + barX, getY() + barY, barWidth, barHeight);
            } else {
                if (value == maxValue) {
                    batch.draw(textureFull, getX() + barX, getY() + barY, barWidth, barHeight);
                } else {
                    batch.draw(textureBar, getX() + barX, getY() + barY, barWidth, barHeight);
                }
            }
        } else {
            clipBounds = new Rectangle(getX() + (getWidth() - barWidth - barX) + (barWidth - width), getY() + barY, width, getHeight());
            ScissorStack.calculateScissors(camera, viewport.getScreenX(), viewport.getScreenY(), viewport.getScreenWidth(), viewport.getScreenHeight(),
                    batch.getTransformMatrix(), clipBounds, scissors);
            ScissorStack.pushScissors(scissors);

            if (textureFull == null) {
                batch.draw(textureBar, getX() + getWidth() - barX, getY() + barY, 0, 0, barWidth, barHeight, -1, 1, 0);
            } else {
                if (value == maxValue) {
                    batch.draw(textureFull, getX() + getWidth() - barX, getY() + barY, 0, 0, barWidth, barHeight, -1, 1, 0);
                } else {
                    batch.draw(textureBar, getX()+getWidth()-barX, getY() + barY, 0,0, barWidth, barHeight, -1, 1, 0);
                }
            }
        }
        batch.flush();
        ScissorStack.popScissors();
    }

    private int widthEfek;
    private void renderEfek(Batch batch) {
        batch.flush();
        barWidth = (int) GameUtils.banding(textureEfek.getRegionWidth(),0, textureBorder.getRegionWidth(), 0, getWidth());
        barHeight = (int) GameUtils.banding(textureEfek.getRegionHeight(), 0, textureBorder.getRegionHeight(), 0, getHeight());
        int width = 0;
        if (value != 0) {
            width = (int) GameUtils.banding(value, 0, maxValue, getX(), barWidth);
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
        Rectangle clipBounds = null;
        if (!flip) {
            clipBounds = new Rectangle(getX() + barX, getY() + barY, widthEfek, getHeight());
            ScissorStack.calculateScissors(camera, viewport.getScreenX(), viewport.getScreenY(), viewport.getScreenWidth(), viewport.getScreenHeight(),
                    batch.getTransformMatrix(), clipBounds, scissors);
            ScissorStack.pushScissors(scissors);
            batch.draw(textureEfek, getX() + barX, getY() + barY, barWidth, barHeight);
        } else {
            clipBounds = new Rectangle(getX() + (getWidth() - barWidth - barX) + (barWidth - widthEfek), getY() + barY, widthEfek, getHeight());
            ScissorStack.calculateScissors(camera, viewport.getScreenX(), viewport.getScreenY(), viewport.getScreenWidth(), viewport.getScreenHeight(),
                    batch.getTransformMatrix(), clipBounds, scissors);
            ScissorStack.pushScissors(scissors);
            batch.draw(textureEfek, getX()+getWidth()-barX, getY() + barY, 0,0, barWidth, barHeight, -1, 1, 0);
        }
        batch.flush();
        ScissorStack.popScissors();
    }
}
