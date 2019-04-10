package id.linkaran.game_editor.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kotcrab.vis.ui.widget.VisDialog;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisTextField;

public class InputDialog extends VisDialog {

    public VisTextField textField;
    public VisTextButton btPositive, btNegative;
    private float percentWidth, percentHeight;
    private Viewport viewport;
    private float w, h;

    public InputDialog(String text, String textPositive, String textNegative, Viewport viewport, float percentWidth, float percentHeight) {
        super(text);
        this.percentWidth = percentWidth;
        this.percentHeight = percentHeight;
        this.viewport = viewport;
        this.btPositive = new VisTextButton(textPositive);
        this.btNegative = new VisTextButton(textNegative);
        this.textField = new VisTextField("");
        textField.focusField();
        setView();
    }

    private void setView() {
        w = Gdx.graphics.getWidth() * percentWidth;
        h = Gdx.graphics.getHeight() * percentHeight;
        float pad = Gdx.graphics.getWidth() * 0.01f;

        clearChildren();

        add(textField).width(w).pad(pad).height(h*0.5f).colspan(2).row();
        add(btPositive).width(w/2 - pad *3).pad(pad).height(h*0.5f);
        add(btNegative).width(w/2 - pad * 3).padRight(pad).height(h*0.5f);
    }
}
