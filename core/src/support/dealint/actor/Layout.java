package support.dealint.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Layout extends GroupCollision {

    private Viewport viewport;
    private Color color;

    public Table table;

    public Layout(Viewport viewport, Color color) {
        super(true);
        this.viewport = viewport;
        this.color = color;
        table = new Table();
        addActor(table);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (color !=null) {
            renderRectangle(batch, viewport, color);
        }
        super.draw(batch, parentAlpha);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        updateRect();
    }
}
