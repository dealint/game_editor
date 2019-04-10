package id.linkaran.game_editor.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

import id.linkaran.game_editor.Manager;
import id.linkaran.game_editor.core.LevelSetting;
import support.dealint.actor.GroupCollision;

public class Cursor extends GroupCollision {

    private Manager manager;
    private LevelSetting setting;
    public Texture texture;

    public Cursor(Manager manager, Texture texture) {
        super(true);
        this.manager = manager;
        setting = manager.levelSetting;
        this.texture = texture;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setColor(1,1,1,1);
        Color c = batch.getColor();
        batch.setColor(c.r, c.g, c.b, 0.5f);
        batch.draw(texture, getX(), getY(), getWidth(), getHeight());
        batch.setColor(c.r, c.g, c.b, 1f);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }
}
