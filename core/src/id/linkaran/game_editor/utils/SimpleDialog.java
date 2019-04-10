package id.linkaran.game_editor.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kotcrab.vis.ui.widget.VisDialog;
import com.kotcrab.vis.ui.widget.VisTextButton;

public class SimpleDialog extends VisDialog {

    public VisTextButton btPositive, btNegative;
    private float percentWidth, percentHeight;
    private Viewport viewport;
    private float w, h;

    public SimpleDialog(String text, String textPositive, String textNegative, Viewport viewport, float percentWidth, float percentHeight) {
        super(text);
        this.percentWidth = percentWidth;
        this.percentHeight = percentHeight;
        this.viewport = viewport;
        this.btPositive = new VisTextButton(textPositive);
        this.btNegative = new VisTextButton(textNegative);
        setView();
    }

    private void setView() {
        w = Gdx.graphics.getWidth() * percentWidth;
        h = Gdx.graphics.getHeight() * percentHeight;
        float pad = Gdx.graphics.getWidth() * 0.01f;

        add(btPositive).width(w/2 - pad *3).pad(pad).height(h*0.5f);
        add(btNegative).width(w/2 - pad * 3).padRight(pad).height(h*0.5f);
    }

    public void tampil(Stage stage) {
        fadeIn();
        centerWindow();
        setMovable(false);
        show(stage);
    }
}
