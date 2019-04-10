package id.linkaran.game_editor.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.kotcrab.vis.ui.widget.VisLabel;

import id.linkaran.game_editor.Manager;
import id.linkaran.game_editor.core.LevelSetting;
import support.dealint.actor.GroupCollision;

public class Benda extends GroupCollision {

    private Manager manager;
    private LevelSetting setting;
    public Texture texture;
    public String nama;
    public int jenis;
    public boolean dipilih = false;

    public float jarakX, jarakY;

    public VisLabel lbNama;

    public Benda(Manager manager, Texture texture) {
        super(true);
        this.manager = manager;
        setting = manager.levelSetting;
        punyaGroup = true;
        this.texture = texture;

        lbNama = new VisLabel("");
        lbNama.setColor(Color.BLACK);
        lbNama.setFontScale(1.5f);
        addActor(lbNama);

    }

    //update ketika beberapa benda dipilih
    public void updateJarak(float x, float y) {
        jarakX = getX() - x;
        jarakY = getY() - y;
    }

    public void gerakBersama(float x, float y) {
        setPosition(x + jarakX, y + jarakY);
    }

    public void drag(float x, float y) {
        float xPos = (getX() + x) - getWidth() / 2;
        float yPos = (getY() + y) - getHeight() / 2;
        setPosition(MathUtils.clamp(xPos, 0, manager.levelScreen.mainLayout.getWidth() - getWidth()),
                MathUtils.clamp(yPos, 0, manager.levelScreen.mainLayout.getHeight() - getHeight()));
    }

    public void update() {
        nama = setting.arrNamaObjek.get(jenis);
        lbNama.setText(nama + " (" + jenis + ")");
        lbNama.setSize(getWidth(), 40);
        lbNama.setPosition(0, -lbNama.getHeight());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(texture, getX(), getY(), getWidth(), getHeight());

        if (dipilih) {
            renderRectangle(batch, manager.levelScreen.viewport, Color.valueOf("ffff0077"));
            lbNama.setColor(Color.RED);
        } else {
            lbNama.setColor(Color.BLACK);
        }
    }

    @Override
    public void act(float delta) {
        updateRect();
    }
}
